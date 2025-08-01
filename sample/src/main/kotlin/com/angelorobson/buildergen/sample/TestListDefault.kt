package com.angelorobson.buildergen.sample

fun main() {
    // Teste com propriedades List que têm valores padrão
    val entity = EntityWithHistory_Builder()
        .id("123")
        .name("Test Entity")
        .build()
    
    println("Entity: $entity")
    println("HistoryEntities size: ${entity.historyEntities.size}")
    println("Tags size: ${entity.tags.size}")
    println("IsActive: ${entity.isActive}")
    
    // Teste com valores específicos para as listas
    val entityWithData = EntityWithHistory_Builder()
        .id("456")
        .name("Entity with Data")
        .historyEntities(listOf(
            HistoryEventEntity("1", "created", System.currentTimeMillis()),
            HistoryEventEntity("2", "updated", System.currentTimeMillis())
        ))
        .tags(listOf("tag1", "tag2"))
        .isActive(false)
        .build()
    
    println("\nEntity with data: $entityWithData")
    println("HistoryEntities size: ${entityWithData.historyEntities.size}")
    println("Tags size: ${entityWithData.tags.size}")
}