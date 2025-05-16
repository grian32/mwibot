package me.grian.slashcommands

import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import me.grian.api.slashcommands.SlashCommand

class PingCommand : SlashCommand {
    override val name: String
        get() = "ping"
    override val description: String
        get() = "Ping!"

    override suspend fun execute(event: ChatInputCommandInteractionCreateEvent) {
        event.interaction.respondEphemeral {
            content = "Pong!"
        }
    }
}