package io.github.ignaciodelatorrearias.greet

import io.github.ignaciodelatorrearias.greet.internal.NativePointer
import io.github.ignaciodelatorrearias.greet.internal.createNewPerson
import io.github.ignaciodelatorrearias.greet.internal.freePerson
import io.github.ignaciodelatorrearias.greet.internal.personGreet
import io.github.ignaciodelatorrearias.lib.greet.v1.PersonParams

//import io.github.ignaciodelatorrearias.greet.internal.personGreet

class Person(val name: String, val age: Int): AutoCloseable {
    internal val ptr: NativePointer = createNewPerson(PersonParams(name, age))
    fun greet(other: Person): String {
        return personGreet(ptr, PersonParams(other.name, other.age)).text
    }
    fun greet(name: String, age: Int): String {
        return personGreet(ptr, PersonParams(name, age)).text
    }
    override fun close() {
        freePerson(ptr)
    }
}