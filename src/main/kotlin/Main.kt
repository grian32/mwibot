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
    /**
     * ClassGraph().enableAllInfo().scan().use { result ->
     *         val commands = result.getClassesImplementing(ChatCommand::class.java)
     *
     *         commands.forEach { command ->
     *             val commandClass = command.loadClass(ChatCommand::class.java)
     *             val instance = commandClass.getDeclaredConstructor().newInstance()
     *
     *             commandMap["${config.prefix}${instance.name}"] = instance
     *
     *             // this dupes the commands but it's probably more efficient for the user.
     *             instance.aliases.forEach {
     *                 commandMap["${config.prefix}$it"] = instance
     *             }
     *
     *             println("Command |${instance.name}| has been successfully initialized and loaded.")
     *         }
     *
     *         val behaviourClasses = result.getClassesImplementing(Behaviour::class.java)
     *
     *         behaviourClasses.forEach { behaviour ->
     *             val behaviourClass = behaviour.loadClass(Behaviour::class.java)
     *             val instance = behaviourClass.getDeclaredConstructor().newInstance()
     *
     *             behaviours.add(instance)
     *
     *             println("Behaviour |${instance.name}| has been successfully initialized and loaded.")
     *         }
     *     }
     */

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