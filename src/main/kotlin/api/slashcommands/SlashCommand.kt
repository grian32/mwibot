package me.grian.api.slashcommands

import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder

interface SlashCommand {
    val name: String
    val description: String

    suspend fun arguments(builder: ChatInputCreateBuilder) {}
    suspend fun execute(event: ChatInputCommandInteractionCreateEvent)
}