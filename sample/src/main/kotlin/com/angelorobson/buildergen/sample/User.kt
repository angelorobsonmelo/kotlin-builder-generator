package com.angelorobson.buildergen.sample

import com.angelorobson.buildergen.annotation.GenerateBuilder

@GenerateBuilder
data class User(
    val name: String,
    val age: Int,
    val email: String,
    val isActive: Boolean = true
)