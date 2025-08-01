package com.angelorobson.buildergen.sample

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestPrefixUserBuilderTest {

    @Test
    fun `test builder with prefix creates object correctly`() {
        val user = TestPrefixUser_Builder()
            .withId("123")
            .withName("John Doe")
            .withAge(30)
            .withEmail("john@example.com")
            .build()

        assertEquals("123", user.id)
        assertEquals("John Doe", user.name)
        assertEquals(30, user.age)
        assertEquals("john@example.com", user.email)
    }

    @Test
    fun `test builder without prefix creates object correctly`() {
        val user = User_Builder()
            .name("Jane Doe")
            .age(25)
            .email("jane@example.com")
            .isActive(false)
            .build()

        assertEquals("Jane Doe", user.name)
        assertEquals(25, user.age)
        assertEquals("jane@example.com", user.email)
        assertEquals(false, user.isActive)
    }
}