package me.grian

import dev.kord.core.Kord
import org.slf4j.LoggerFactory
import kotlin.io.path.Path
import kotlin.io.path.readText

suspend fun main() {
    val token = Path(".token").readText()
    val kord = Kord(token)
    val logger = LoggerFactory.getLogger("Main")

    kord.login {
        logger.info("Successfully logged in.")
    }
}