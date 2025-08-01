package com.angelorobson.buildergen.annotation

/**
 * Annotation that generates a builder pattern class for the annotated data class.
 * 
 * When applied to a data class, this annotation will generate a builder class
 * that allows for fluent construction of instances, particularly useful in tests.
 * 
 * @param prefix Optional prefix for setter method names. Default is empty string.
 * 
 * Example usage:
 * ```
 * @GenerateBuilder
 * data class User(val name: String, val age: Int, val email: String)
 * 
 * // Generated builder can be used as:
 * val user = UserBuilder()
 *     .name("John")
 *     .age(25)
 *     .email("john@example.com")
 *     .build()
 * 
 * @GenerateBuilder(prefix = "with")
 * data class Product(val id: String, val price: Double)
 * 
 * // Generated builder with prefix:
 * val product = ProductBuilder()
 *     .withId("123")
 *     .withPrice(99.99)
 *     .build()
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateBuilder(val prefix: String = "")