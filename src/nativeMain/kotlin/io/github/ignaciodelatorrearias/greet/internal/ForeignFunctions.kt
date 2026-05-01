package io.github.ignaciodelatorrearias.greet.internal

import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import io.github.ignaciodelatorrearias.greet.internal.cinterop.*
import io.github.ignaciodelatorrearias.greet.types.CallStatus
import io.github.ignaciodelatorrearias.greet.types.Error
import kotlinx.cinterop.*
import platform.posix.*

@OptIn(ExperimentalForeignApi::class)
internal fun throwErrors(status: Int, outPtr: CPointer<UByteVar>?, outLen: size_t){
    var callStatus = CallStatus.fromValue(status)
    var details: String = ""
    if (status > 0) {
        val resultBytes = outPtr!!.readBytes(outLen.toInt())
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

@OptIn(ExperimentalForeignApi::class)
internal typealias FunctionArgsResultType = (CValuesRef<UByteVar>?,
                                             ULong,
                                             CValuesRef<CPointerVarOf<CPointer<UByteVar>>>?,
                                             CValuesRef<ULongVar>?) -> Int

@OptIn(ExperimentalForeignApi::class)
internal fun <I : Message<I, Nothing>, O : Message<O, Nothing>> functionArgsResult(
    input: I,
    outputAdapter: ProtoAdapter<O>,
    func: FunctionArgsResultType,
): O {
    val buffer = input.encode()
    lateinit var result: O
    buffer.usePinned { buf ->
        memScoped {
            val outPtr = alloc<CPointerVar<UByteVar>>()
            val outLen = alloc<size_tVar>()
            val status = func(
                buf.addressOf(0).reinterpret(),
                buffer.size.convert(),
                outPtr.ptr,
                outLen.ptr
            )
            val resultPtr = outPtr.value
            val resultLen = outLen.value
            throwErrors(status, resultPtr, resultLen)
            result = outputAdapter.decode(resultPtr!!.readBytes(resultLen.toInt()))
            lib_greet_free_buffer(resultPtr, resultLen)
        }
    }
    return result
}

@OptIn(ExperimentalForeignApi::class)
internal typealias CreateNewArgsType = (CValuesRef<CPointerVarOf<COpaquePointer>>?,
                                        CValuesRef<UByteVar>?,
                                        ULong) -> Int

@OptIn(ExperimentalForeignApi::class)
internal fun <I : Message<I, Nothing>> createNewArgs(
    input: I,
    func: CreateNewArgsType,
): COpaquePointer {
    val buffer = input.encode()
    lateinit var result: COpaquePointer
    buffer.usePinned { buf ->
        memScoped {
            val outInstance = alloc<COpaquePointerVar>()
            val status = func(
                outInstance.ptr.reinterpret(),
                buf.addressOf(0).reinterpret(),
                buffer.size.convert()
            )
            result = outInstance.value!!
            throwErrors(status, null, 0UL)
        }
    }
    return result
}

@OptIn(ExperimentalForeignApi::class)
internal typealias MethodArgsResultType = (CValuesRef<COpaque>?,
                                           CValuesRef<UByteVar>?,
                                           ULong,
                                           CValuesRef<CPointerVarOf<CPointer<UByteVar>>>?,
                                           CValuesRef<ULongVar>?) -> Int

@OptIn(ExperimentalForeignApi::class)
internal fun <I : Message<I, Nothing>, O : Message<O, Nothing>> methodArgsResult(
    ptr: COpaquePointer,
    input: I,
    outputAdapter: ProtoAdapter<O>,
    func: MethodArgsResultType,
): O {
    val buffer = input.encode()
    lateinit var result: O
    buffer.usePinned { buf ->
        memScoped {
            val outPtr = alloc<CPointerVar<UByteVar>>()
            val outLen = alloc<size_tVar>()
            val status = func(
                ptr.reinterpret(),
                buf.addressOf(0).reinterpret(),
                buffer.size.convert(),
                outPtr.ptr,
                outLen.ptr
            )
            val resultPtr = outPtr.value
            val resultLen = outLen.value
            throwErrors(status, resultPtr, resultLen)
            result = outputAdapter.decode(resultPtr!!.readBytes(resultLen.toInt()))
            lib_greet_free_buffer(resultPtr, resultLen)
        }
    }
    return result
}