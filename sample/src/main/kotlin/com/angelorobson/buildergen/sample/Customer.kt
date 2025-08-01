package com.angelorobson.buildergen.sample

import com.angelorobson.buildergen.annotation.GenerateBuilder

@GenerateBuilder
data class Customer(
    val id: Long,                    // Obrigatório (não-nullable)
    val name: String,                // Obrigatório (não-nullable)
    val email: String,               // Obrigatório (não-nullable)
    val phone: String?,              // Opcional (nullable)
    val address: String?,            // Opcional (nullable)
    val birthDate: String? = null,   // Opcional (nullable com default)
    val isActive: Boolean = true     // Opcional (tem valor padrão)
)