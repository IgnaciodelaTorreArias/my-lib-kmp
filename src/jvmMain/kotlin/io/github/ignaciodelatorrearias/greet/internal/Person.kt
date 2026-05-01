package io.github.ignaciodelatorrearias.greet.internal

import io.github.ignaciodelatorrearias.greet.types.PersonParams
import io.github.ignaciodelatorrearias.greet.types.Response
import com.fizzed.jne.JNE
import java.lang.foreign.MemorySegment

val ensureLoaded by lazy {
    JNE.loadLibrary("my_rust_protos")
}

internal actual fun greet(p: PersonParams): Response {
    ensureLoaded
    return functionArgsResult<PersonParams, Response>(
        p,
        Response.ADAPTER,
        my_rust_protos_h::lib_greet_greet
    )
}

internal actual fun createNewPerson(p: PersonParams): NativePointer {
    ensureLoaded
    return NativePointer(createNewArgs(
        p,
        my_rust_protos_h::lib_greet_create_new_person
    ))
}

internal actual fun personGreet(ptr: NativePointer, other: PersonParams): Response {
    ensureLoaded
    return methodArgsResult(
        ptr.ptr as MemorySegment,
        other,
        Response.ADAPTER,
        my_rust_protos_h::lib_greet_person_greet
    )
}

internal actual fun freePerson(ptr: NativePointer) {
    ensureLoaded
    my_rust_protos_h.lib_greet_free_person(ptr.ptr as MemorySegment)
}