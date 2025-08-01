package com.angelorobson.buildergen.sample

fun main() {
    // Teste com objeto personalizado que tem valor padrão
    val historyEvent = HistoryEventResponse_Builder()
        .id("123")
        .eventType("GPS_UPDATE")
        .build()
    
    println("HistoryEvent: $historyEvent")
    println("Position: ${historyEvent.position}")
    println("Position latitude: ${historyEvent.position.latitude}")
    println("Position longitude: ${historyEvent.position.longitude}")
    println("Timestamp: ${historyEvent.timestamp}")
    
    // Teste com valor específico para position
    val customPosition = PositionResponse(latitude = 40.7128, longitude = -74.0060)
    val historyEventWithCustomPosition = HistoryEventResponse_Builder()
        .id("456")
        .eventType("LOCATION_UPDATE")
        .position(customPosition)
        .timestamp(System.currentTimeMillis())
        .build()
    
    println("\nHistoryEvent with custom position: $historyEventWithCustomPosition")
    println("Custom position: ${historyEventWithCustomPosition.position}")
}