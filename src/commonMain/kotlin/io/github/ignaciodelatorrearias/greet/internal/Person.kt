package io.github.ignaciodelatorrearias.greet.internal

import io.github.ignaciodelatorrearias.greet.types.PersonParams
import io.github.ignaciodelatorrearias.greet.types.Response

internal expect fun greet(p: PersonParams): Response;
internal expect fun createNewPerson(p: PersonParams): NativePointer;
internal expect fun personGreet(ptr: NativePointer, other: PersonParams): Response;
internal expect fun freePerson(ptr: NativePointer);