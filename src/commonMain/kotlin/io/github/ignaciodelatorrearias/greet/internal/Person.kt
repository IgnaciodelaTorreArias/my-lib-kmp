package io.github.ignaciodelatorrearias.greet.internal

//import io.github.ignaciodelatorrearias.greet.internal.NativePointer

import io.github.ignaciodelatorrearias.lib.greet.v1.PersonParams
import io.github.ignaciodelatorrearias.lib.greet.v1.Response
//
expect fun greet(p: PersonParams): Response;
//expect fun createNewPerson(p: PersonParams): NativePointer;
//expect fun personGreet(ptr: NativePointer, other: PersonParams): Response;
//expect fun freePerson(ptr: NativePointer);
//expect fun freeBuffer(ptr: NativePointer);