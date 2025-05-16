package me.grian.slashcommands.market

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.embed
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import me.grian.Constants
import me.grian.api.slashcommands.SlashCommand
import me.grian.capitalizeAllLetters
import me.grian.slashcommands.market.data.MilkyAPIData
import java.text.NumberFormat

class MarketCommand : SlashCommand {
    override val name: String
        get() = "market"
    override val description: String
        get() = "Gets the currrent ask/bid/vendor prices for the specified item."

    override suspend fun arguments(builder: ChatInputCreateBuilder) = builder.apply {
        string("item_name", "Must be correct, can be any case, does not need to include apostrophes") {
            required = true
        }
    }

    override suspend fun execute(event: ChatInputCommandInteractionCreateEvent, client: HttpClient) {
        val interaction = event.interaction

        val itemName = event.interaction.command.strings["item_name"]?.lowercase() ?: return
        // raw gh is text/plain
        val data: MilkyAPIData = Json.decodeFromString(
            client
                .get("https://raw.githubusercontent.com/holychikenz/MWIApi/refs/heads/main/milkyapi.json")
                .bodyAsText()
        )
        val processedData = data.market.map {
            it.key.replace("'", "").lowercase() to it.value
        }

        val item = processedData.find { it.first == itemName }

        if (item == null) {
            interaction.respondEphemeral {
                content = "Couldn't find the item you specified, please try again."
            }
            return
        }


        val nf = NumberFormat.getInstance()
        interaction.respondEphemeral {
            embed {
                title = "Market Information for ${item.first.capitalizeAllLetters()}"

                description = """
                    Ask: ${nf.format(item.second.ask)} Coins
                    Bid: ${nf.format(item.second.ask)} Coins
                    Vendor: ${nf.format(item.second.ask)} Coins
                """.trimIndent()

                color = Constants.EMBED_COLOR
                author {
                    name = "MWI Bot"
                    icon = "https://github.com/grian32/mwibot/blob/master/image/favicon.png?raw=true"
                }
            }
        }

    }
}