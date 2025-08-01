package com.angelorobson.buildergen.sample

import com.angelorobson.buildergen.annotation.GenerateBuilder
import java.math.BigDecimal

@GenerateBuilder
data class Product(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val category: String,
    val inStock: Boolean
)