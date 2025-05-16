package me.grian

import dev.kord.core.Kord
import kotlin.io.path.Path
import kotlin.io.path.readText

suspend fun main() {
    val token = Path(".token").readText()
    val kord = Kord(token)

    kord.login()
}