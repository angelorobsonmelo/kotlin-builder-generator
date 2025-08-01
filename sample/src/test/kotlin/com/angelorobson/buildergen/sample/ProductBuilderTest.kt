package com.angelorobson.buildergen.sample

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ProductBuilderTest {

    @Test
    fun `should create product using builder`() {
        val product = Product_Builder()
            .id(1L)
            .name("Smartphone")
            .price(BigDecimal("999.99"))
            .category("Electronics")
            .inStock(true)
            .build()

        assertThat(product.id).isEqualTo(1L)
        assertThat(product.name).isEqualTo("Smartphone")
        assertThat(product.price).isEqualByComparingTo(BigDecimal("999.99"))
        assertThat(product.category).isEqualTo("Electronics")
        assertThat(product.inStock).isTrue()
    }

    @Test
    fun `should create multiple test products easily`() {
        val products = listOf(
            Product_Builder()
                .id(1L)
                .name("Product A")
                .price(BigDecimal("10.00"))
                .category("Category 1")
                .inStock(true)
                .build(),
            
            Product_Builder()
                .id(2L)
                .name("Product B")
                .price(BigDecimal("20.00"))
                .category("Category 2")
                .inStock(false)
                .build()
        )

        assertThat(products).hasSize(2)
        assertThat(products[0].inStock).isTrue()
        assertThat(products[1].inStock).isFalse()
    }
}