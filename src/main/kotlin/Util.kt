package me.grian

fun String.capitalizeAllLetters() =
    split(" ").joinToString(" ") {
        (it[0].uppercase() + it.substring(1))
    }