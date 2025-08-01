package com.angelorobson.buildergen.sample

import com.angelorobson.buildergen.annotation.GenerateBuilder

@GenerateBuilder(prefix = "with")
data class TestPrefixUser(
    val id: String,
    val name: String,
    val age: Int,
    val email: String
)