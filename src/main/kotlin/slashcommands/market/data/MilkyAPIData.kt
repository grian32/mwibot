package me.grian.slashcommands.market.data

import kotlinx.serialization.Serializable

@Serializable
data class MilkyAPIData(
    val market: Map<String, MarketData>,
    val time: Double
)

@Serializable
data class MarketData(
    val ask: Int,
    val bid: Int,
    val vendor: Int,
)
