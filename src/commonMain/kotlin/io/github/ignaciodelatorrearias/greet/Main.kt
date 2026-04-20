package io.github.ignaciodelatorrearias.greet

import io.github.ignaciodelatorrearias.lib.greet.v1.PersonParams
import io.github.ignaciodelatorrearias.greet.internal.greet

fun main() {
    print(greet(PersonParams(name = "Juan", age = 10)).text)
}