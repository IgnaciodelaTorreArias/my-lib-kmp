package io.github.ignaciodelatorrearias.greet

import io.github.ignaciodelatorrearias.lib.greet.v1.PersonParams

fun main() {
    println(greet(name = "Juan", age = 10))
    val jaime = Person("Jaime", 25)
    val john = Person("John", 22)
    println(jaime.greet(john))
    jaime.close()
    john.close()
}