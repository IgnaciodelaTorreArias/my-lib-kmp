package io.github.ignaciodelatorrearias.greet.internal

import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import io.github.ignaciodelatorrearias.greet.internal.my_rust_protos_h.lib_greet_free_buffer
import io.github.ignaciodelatorrearias.lib.greet.v1.*
import java.lang.foreign.*

internal fun throwErrors(status: Int, outPtr: MemorySegment, outLen: Long) {
    var callStatus = CallStatus.fromValue(status)
    var details: String = ""
    if (status > 0) {
        val resultBytes = outPtr.reinterpret(outLen)
            .toArray(ValueLayout.JAVA_BYTE)
        details = Error.ADAPTER.decode(resultBytes).details
        lib_greet_free_buffer(outPtr, outLen)
    }
    when (callStatus) {
        CallStatus.OK -> return
        CallStatus.DECODE_ERROR -> throw IllegalStateException("Decode error")
        CallStatus.INVALID_ARGUMENTS_DETAILS,
        CallStatus.INVALID_ARGUMENTS -> throw IllegalArgumentException(details)
        CallStatus.UNKNOWN_ENUM_VALUE -> throw IndexOutOfBoundsException()
        CallStatus.EMPTY_PARAMS -> throw IllegalArgumentException("A required field is not present")
        null -> throw IndexOutOfBoundsException("Unknown Status: $status, Details?: $details")
    }
}

internal typealias FunctionArgsResultType = (MemorySegment, Long, MemorySegment, MemorySegment) -> Int

internal fun <I : Message<I, Nothing>, O : Message<O, Nothing>> functionArgsResult(
    input: I,
    outputAdapter: ProtoAdapter<O>,
    func: FunctionArgsResultType,
): O {
    val buf = input.encode()
    lateinit var result: O
    Arena.ofConfined().use { arena ->
        val nativeBuf = arena.allocate(buf.size.toLong())
        nativeBuf.copyFrom(MemorySegment.ofArray(buf))
        val outPtr = arena.allocate(ValueLayout.ADDRESS)
        val outLen = arena.allocate(ValueLayout.JAVA_LONG)
        val status = func(
            nativeBuf,
            buf.size.toLong(),
            outPtr,
            outLen
        )
        val resultAddress = outPtr.get(ValueLayout.ADDRESS, 0)
        val resultLen = outLen.get(ValueLayout.JAVA_LONG, 0)
        throwErrors(status,resultAddress,resultLen)
        val resultBytes = resultAddress.reinterpret(resultLen)
            .toArray(ValueLayout.JAVA_BYTE)
        result = outputAdapter.decode(resultBytes)
        lib_greet_free_buffer(resultAddress, resultLen)
    }
    return result
}

internal typealias CreateNewArgs = (MemorySegment, MemorySegment, Long) -> Int

internal fun <I : Message<I, Nothing>> createNewArgs(
    input: I,
    func: CreateNewArgs,
): MemorySegment {
    val buf = input.encode()
    lateinit var result: MemorySegment
    Arena.ofConfined().use { arena ->
        val nativeBuf = arena.allocate(buf.size.toLong())
        nativeBuf.copyFrom(MemorySegment.ofArray(buf))
        val outInstance = arena.allocate(ValueLayout.ADDRESS)
        val status = func(
            outInstance,
            nativeBuf,
            buf.size.toLong()
        )
        result = outInstance.get(ValueLayout.ADDRESS, 0)
        throwErrors(status, MemorySegment.NULL,0)
    }
    return result
}

internal typealias MethodArgsResultType = (MemorySegment, MemorySegment, Long, MemorySegment, MemorySegment) -> Int

internal fun <I : Message<I, Nothing>, O : Message<O, Nothing>> methodArgsResult(
    ptr: MemorySegment,
    input: I,
    outputAdapter: ProtoAdapter<O>,
    func: MethodArgsResultType,
): O {
    val buf = input.encode()
    lateinit var result: O
    Arena.ofConfined().use { arena ->
        val nativeBuf = arena.allocate(buf.size.toLong())
        nativeBuf.copyFrom(MemorySegment.ofArray(buf))
        val outPtr = arena.allocate(ValueLayout.ADDRESS)
        val outLen = arena.allocate(ValueLayout.JAVA_LONG)
        val status = func(
            ptr,
            nativeBuf,
            buf.size.toLong(),
            outPtr,
            outLen
        )
        val resultAddress = outPtr.get(ValueLayout.ADDRESS, 0)
        val resultLen = outLen.get(ValueLayout.JAVA_LONG, 0)
        throwErrors(status,resultAddress,resultLen)
        val resultBytes = resultAddress.reinterpret(resultLen)
            .toArray(ValueLayout.JAVA_BYTE)
        result = outputAdapter.decode(resultBytes)
        lib_greet_free_buffer(resultAddress, resultLen)
    }
    return result
}