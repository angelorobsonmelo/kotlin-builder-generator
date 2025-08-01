package com.angelorobson.buildergen.sample

import com.angelorobson.buildergen.annotation.GenerateBuilder

data class PositionResponse(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@GenerateBuilder
data class HistoryEventResponse(
    val id: String,
    val eventType: String,
    val position: PositionResponse = PositionResponse(),
    val timestamp: Long = 0L
)