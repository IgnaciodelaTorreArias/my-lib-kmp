package io.github.ignaciodelatorrearias.greet.internal

import io.github.ignaciodelatorrearias.greet.internal.cinterop.*
import io.github.ignaciodelatorrearias.lib.greet.v1.PersonParams
import io.github.ignaciodelatorrearias.lib.greet.v1.Response

import kotlinx.cinterop.*

@OptIn(ExperimentalForeignApi::class)
internal actual fun greet(p: PersonParams): Response {
    return functionArgsResult(
        p,
        Response.ADAPTER,
        ::lib_greet_greet
    )
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun createNewPerson(p: PersonParams): NativePointer {
    return NativePointer(createNewArgs(
        p,
        ::lib_greet_create_new_person as CreateNewArgsType
    ))
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun personGreet(ptr: NativePointer, other: PersonParams): Response {
    return methodArgsResult(
        ptr.ptr as COpaquePointer,
        other,
        Response.ADAPTER,
        ::lib_greet_person_greet as MethodArgsResultType
    )
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun freePerson(ptr: NativePointer) {
    lib_greet_free_person((ptr.ptr as COpaquePointer).reinterpret())
}