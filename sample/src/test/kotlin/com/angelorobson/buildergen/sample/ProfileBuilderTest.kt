package com.angelorobson.buildergen.sample

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProfileBuilderTest {

    @Test
    fun `should create profile with long parameter names`() {
        val profile = Profile_Builder()
            .id(1L)
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
            .recoveryEmail("recovery@example.com")
            .driverSummary("Clean driving record with 10 years experience")
            .build()

        assertThat(profile.id).isEqualTo(1L)
        assertThat(profile.email).isEqualTo("test@example.com")
        assertThat(profile.firstName).isEqualTo("John")
        assertThat(profile.lastName).isEqualTo("Doe")
        assertThat(profile.recoveryEmail).isEqualTo("recovery@example.com")
        assertThat(profile.driverSummary).isEqualTo("Clean driving record with 10 years experience")
    }

    @Test
    fun `should throw error when required field is missing`() {
        val builder = Profile_Builder()
            .id(1L)
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
            // recoveryEmail is missing

        val exception = assertThrows<IllegalStateException> {
            builder.build()
        }
        
        assertThat(exception.message).isEqualTo("recoveryEmail required")
    }

    @Test
    fun `should work with all long parameter names without line breaks`() {
        // This test ensures that even with long parameter names,
        // the generated code compiles correctly without line break issues
        val profile = Profile_Builder()
            .id(999L)
            .email("very.long.email.address@example.com")
            .firstName("VeryLongFirstName")
            .lastName("VeryLongLastName")
            .recoveryEmail("very.long.recovery.email.address@example.com")
            .driverSummary("This is a very long driver summary that would normally cause line wrapping issues in the generated code but should work fine now")
            .build()

        assertThat(profile.driverSummary).contains("very long driver summary")
        assertThat(profile.recoveryEmail).contains("very.long.recovery")
    }
}