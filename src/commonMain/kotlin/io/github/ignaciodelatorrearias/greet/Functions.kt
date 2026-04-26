package io.github.ignaciodelatorrearias.greet

import io.github.ignaciodelatorrearias.greet.internal.greet
import io.github.ignaciodelatorrearias.lib.greet.v1.PersonParams

fun greet(name: String, age: Int): String {
    return greet(PersonParams(name, age)).text
}