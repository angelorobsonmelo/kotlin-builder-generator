package com.angelorobson.buildergen.sample

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserBuilderTest {

    @Test
    fun `should create user with builder pattern`() {
        val user = User_Builder()
            .name("João Silva")
            .age(30)
            .email("joao@example.com")
            .isActive(true)
            .build()

        assertThat(user.name).isEqualTo("João Silva")
        assertThat(user.age).isEqualTo(30)
        assertThat(user.email).isEqualTo("joao@example.com")
        assertThat(user.isActive).isTrue()
    }

    @Test
    fun `should create user with fluent interface`() {
        val user = User_Builder()
            .name("Maria")
            .age(25)
            .email("maria@test.com")
            .isActive(false)
            .build()

        assertThat(user).isEqualTo(
            User(
                name = "Maria",
                age = 25,
                email = "maria@test.com",
                isActive = false
            )
        )
    }

    @Test
    fun `should throw exception when required field is missing`() {
        val builder = User_Builder()
            .name("Test User")
            .age(20)
            // email is missing

        assertThrows<IllegalStateException> {
            builder.build()
        }
    }

    @Test
    fun `should be useful for test data creation`() {
        val testUsers = listOf(
            User_Builder().name("User1").age(20).email("user1@test.com").isActive(true).build(),
            User_Builder().name("User2").age(30).email("user2@test.com").isActive(false).build(),
            User_Builder().name("User3").age(40).email("user3@test.com").isActive(true).build()
        )

        assertThat(testUsers).hasSize(3)
        assertThat(testUsers.map { it.name }).containsExactly("User1", "User2", "User3")
    }
}