package com.angelorobson.buildergen.sample

import com.angelorobson.buildergen.annotation.GenerateBuilder

fun main() {
    // Teste do builder sem prefix
    val user = User_Builder()
        .name("John")
        .age(30)
        .email("john@example.com")
        .build()
    
    println("User: $user")
    
    // Teste do builder com prefix
    val testUser = TestPrefixUser_Builder()
        .withId("123")
        .withName("Jane")
        .withAge(25)
        .withEmail("jane@example.com")
        .build()
    
    println("TestPrefixUser: $testUser")
}