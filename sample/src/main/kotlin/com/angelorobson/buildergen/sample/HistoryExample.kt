package com.angelorobson.buildergen.sample

import com.angelorobson.buildergen.annotation.GenerateBuilder

data class HistoryEventEntity(
    val id: String,
    val event: String,
    val timestamp: Long
)

@GenerateBuilder
data class EntityWithHistory(
    val id: String,
    val name: String,
    val historyEntities: List<HistoryEventEntity> = listOf(),
    val tags: List<String> = emptyList(),
    val isActive: Boolean = true
)