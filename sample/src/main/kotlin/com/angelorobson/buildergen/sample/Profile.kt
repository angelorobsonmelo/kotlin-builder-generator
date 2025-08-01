package com.angelorobson.buildergen.sample

import com.angelorobson.buildergen.annotation.GenerateBuilder

@GenerateBuilder
data class Profile(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val recoveryEmail: String,
    val driverSummary: String
)