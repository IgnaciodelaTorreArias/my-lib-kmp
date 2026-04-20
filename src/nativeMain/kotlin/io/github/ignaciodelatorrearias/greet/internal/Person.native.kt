package io.github.ignaciodelatorrearias.greet.internal

import io.github.ignaciodelatorrearias.lib.greet.v1.PersonParams
import io.github.ignaciodelatorrearias.lib.greet.v1.Response
import io.github.ignaciodelatorrearias.greet.internal.cinterop.lib_greet_greet

import kotlinx.cinterop.*
import platform.posix.*

@OptIn(ExperimentalForeignApi::class)
actual fun greet(p: PersonParams): Response {
    val buffer = p.encode()
    lateinit var result: Response
    buffer.usePinned { buf ->
        memScoped {
            val outLen = alloc<size_tVar>()                 // size_t*
            val outPtr = alloc<CPointerVar<UByteVar>>()     // uint8_t**

            lib_greet_greet(
                buf.addressOf(0).reinterpret<UByteVar>(),
                buffer.size.convert(),
                outPtr.ptr,
                outLen.ptr
            )
            val resultPtr = outPtr.value
            val resultLen = outLen.value
            if (resultPtr != null) {
                result = Response.ADAPTER.decode(resultPtr.readBytes(resultLen.toInt()))
            }
        }
    }
    return result
}
