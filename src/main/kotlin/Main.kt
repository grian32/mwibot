package me.grian

import dev.kord.core.Kord
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import io.github.classgraph.ClassGraph
import me.grian.api.slashcommands.SlashCommand
import org.slf4j.LoggerFactory
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.log

suspend fun main() {
    val token = Path(".token").readText()
    val kord = Kord(token)
    val logger = LoggerFactory.getLogger("Main")

    val commands: MutableList<SlashCommand> = mutableListOf()

    ClassGraph().enableAllInfo().scan().use { result ->
        val foundCommands = result.getClassesImplementing(SlashCommand::class.java)

        for (i in foundCommands) {
            val cmdClass = i.loadClass(SlashCommand::class.java)
            val instance = cmdClass.getDeclaredConstructor().newInstance()

            commands.add(instance)

            logger.info("Succesfully loaded and initialized slash command [${instance.name}]")
        }
    }

    for (i in commands) {
        kord.createGlobalChatInputCommand(i.name, i.description) {
            i.arguments(this)
        }
    }

    kord.on<ChatInputCommandInteractionCreateEvent> {
        val commandName = interaction.invokedCommandName

        for (i in commands) {
            if (i.name == commandName) {
                i.execute(this)
                logger.info("Command [${commandName}] ran by user [${interaction.user.username}|${interaction.user.id.value}]]")
                return@on
            }
        }
    }

    kord.login {
        logger.info("Successfully logged in.")
    }
}