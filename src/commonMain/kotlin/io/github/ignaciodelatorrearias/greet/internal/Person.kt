package io.github.ignaciodelatorrearias.greet.internal

//import io.github.ignaciodelatorrearias.greet.internal.NativePointer

import io.github.ignaciodelatorrearias.lib.greet.v1.PersonParams
import io.github.ignaciodelatorrearias.lib.greet.v1.Response

internal expect fun greet(p: PersonParams): Response;
internal expect fun createNewPerson(p: PersonParams): NativePointer;
internal expect fun personGreet(ptr: NativePointer, other: PersonParams): Response;
internal expect fun freePerson(ptr: NativePointer);