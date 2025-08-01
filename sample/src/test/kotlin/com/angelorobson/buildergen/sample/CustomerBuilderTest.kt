package com.angelorobson.buildergen.sample

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CustomerBuilderTest {

    @Test
    fun `should create customer with only required fields`() {
        // Required properties only (non-nullable)
        val customer = Customer_Builder()
            .id(1L)
            .name("João Silva")
            .email("joao@example.com")
            .build()

        assertThat(customer.id).isEqualTo(1L)
        assertThat(customer.name).isEqualTo("João Silva")
        assertThat(customer.email).isEqualTo("joao@example.com")
        
        // nullable properties must be null
        assertThat(customer.phone).isNull()
        assertThat(customer.address).isNull()
        assertThat(customer.birthDate).isNull()
        
        // With default property value
        assertThat(customer.isActive).isTrue()
    }

    @Test
    fun `should create customer with all fields`() {
        val customer = Customer_Builder()
            .id(2L)
            .name("Maria Santos")
            .email("maria@example.com")
            .phone("(11) 99999-9999")
            .address("Rua das Flores, 123")
            .birthDate("1990-01-01")
            .isActive(false)
            .build()

        assertThat(customer.id).isEqualTo(2L)
        assertThat(customer.name).isEqualTo("Maria Santos")
        assertThat(customer.email).isEqualTo("maria@example.com")
        assertThat(customer.phone).isEqualTo("(11) 99999-9999")
        assertThat(customer.address).isEqualTo("Rua das Flores, 123")
        assertThat(customer.birthDate).isEqualTo("1990-01-01")
        assertThat(customer.isActive).isFalse()
    }

    @Test
    fun `should allow nullable fields to be explicitly set to null`() {
        val customer = Customer_Builder()
            .id(3L)
            .name("Pedro")
            .email("pedro@example.com")
            .phone(null)        // Explicitamente null
            .address(null)      // Explicitamente null
            .birthDate(null)    // Explicitamente null
            .build()

        assertThat(customer.phone).isNull()
        assertThat(customer.address).isNull()  
        assertThat(customer.birthDate).isNull()
    }

    @Test
    fun `should throw error when required field is missing - id`() {
        val builder = Customer_Builder()
            .name("Test")
            .email("test@example.com")
            // id is missing

        val exception = assertThrows<IllegalStateException> {
            builder.build()
        }
        
        assertThat(exception.message).isEqualTo("id required")
    }

    @Test
    fun `should throw error when required field is missing - name`() {
        val builder = Customer_Builder()
            .id(1L)
            .email("test@example.com")
            // name is missing

        val exception = assertThrows<IllegalStateException> {
            builder.build()
        }
        
        assertThat(exception.message).isEqualTo("name required")
    }

    @Test
    fun `should throw error when required field is missing - email`() {
        val builder = Customer_Builder()
            .id(1L)
            .name("Test")
            // email is missing

        val exception = assertThrows<IllegalStateException> {
            builder.build()
        }
        
        assertThat(exception.message).isEqualTo("email required")
    }

    @Test
    fun `should work perfectly for test data creation`() {
        val customers = listOf(
            // Minimum required
            Customer_Builder()
                .id(1L)
                .name("Customer 1")
                .email("customer1@test.com")
                .build(),
                
            // With some optional extras
            Customer_Builder()
                .id(2L)
                .name("Customer 2") 
                .email("customer2@test.com")
                .phone("123456789")
                .build(),
                
            // Full
            Customer_Builder()
                .id(3L)
                .name("Customer 3")
                .email("customer3@test.com")
                .phone("987654321")
                .address("Test Address")
                .birthDate("1985-01-01")
                .isActive(false)
                .build()
        )

        assertThat(customers).hasSize(3)
        assertThat(customers[0].phone).isNull()      // Not defined
        assertThat(customers[1].phone).isNotNull()   // defined
        assertThat(customers[2].isActive).isFalse()  // It was set to false
    }
}