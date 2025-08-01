# Kotlin Builder Generator

**English** | [Português](README-pt.md)

A Kotlin library that automatically generates builders for data classes using annotations, especially useful for testing.

## How to use

### 1. Add the dependency

#### For Android projects

In your `build.gradle.kts` (app level):

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
}

dependencies {
    // Add these dependencies
    implementation("com.angelorobson.buildergen:annotation:1.0.0")
    ksp("com.angelorobson.buildergen:processor:1.0.0")
    
    // Your other dependencies...
}
```

#### For Maven Local (development)

If you cloned this repository and published locally:

```kotlin
repositories {
    mavenLocal() // Add this line
    mavenCentral()
    google()
}

dependencies {
    implementation("com.angelorobson.buildergen:annotation:1.0.0")
    ksp("com.angelorobson.buildergen:processor:1.0.0")
}
```

#### For Kotlin JVM projects

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":annotation"))
    ksp(project(":processor"))
}
```

### 2. Annotate your data class

```kotlin
import com.angelorobson.buildergen.annotation.GenerateBuilder

@GenerateBuilder
data class User(
    val name: String,
    val age: Int,
    val email: String,
    val isActive: Boolean = true
)
```

#### Customization with Prefix

You can customize the prefix of builder methods using the `prefix` parameter:

```kotlin
@GenerateBuilder(prefix = "with")
data class TestPrefixUser(
    val id: String,
    val name: String,
    val age: Int,
    val email: String
)
```

### 3. Use the generated builder

#### Without prefix (default)
```kotlin
// In your tests
val user = User_Builder()
    .name("John Silva")
    .age(30)
    .email("john@example.com")
    .isActive(true)
    .build()

// Or create multiple objects easily
val testUsers = listOf(
    User_Builder().name("User1").age(20).email("user1@test.com").build(),
    User_Builder().name("User2").age(30).email("user2@test.com").build()
)
```

#### With prefix = "with"
```kotlin
// When you use @GenerateBuilder(prefix = "with")
val user = TestPrefixUser_Builder()
    .withId("123")
    .withName("John Silva")
    .withAge(30)
    .withEmail("john@example.com")
    .build()

// Multiple objects with prefix
val testUsers = listOf(
    TestPrefixUser_Builder()
        .withId("1")
        .withName("User1")
        .withAge(20)
        .withEmail("user1@test.com")
        .build(),
    TestPrefixUser_Builder()
        .withId("2")
        .withName("User2")  
        .withAge(30)
        .withEmail("user2@test.com")
        .build()
)
```

## 🔨 Practical Examples for Android

### Example with Customer (required and optional fields)

```kotlin
@GenerateBuilder
data class Customer(
    val id: Long,                    // Required
    val name: String,                // Required  
    val email: String,               // Required
    val phone: String?,              // Optional (nullable)
    val address: String?,            // Optional (nullable)
    val birthDate: String? = null,   // Optional with default value
    val isActive: Boolean = true     // With default value
)

// Usage in tests:
val customer = Customer_Builder()
    .id(1L)
    .name("John Silva")
    .email("john@example.com")
    .phone("(11) 99999-9999")
    .address("Flower Street, 123")
    .isActive(true)
    .build()

// Only required fields (optional fields become null or use default value)
val minimalCustomer = Customer_Builder()
    .id(2L)
    .name("Maria Santos")
    .email("maria@example.com")
    .build()
```

### Example with Product (BigDecimal and multiple types)

```kotlin
@GenerateBuilder
data class Product(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val category: String,
    val inStock: Boolean
)

// Usage in tests:
val product = Product_Builder()
    .id(1L)
    .name("Samsung Smartphone")
    .price(BigDecimal("1299.99"))
    .category("Electronics")
    .inStock(true)
    .build()

// Creating multiple products for tests
val products = listOf(
    Product_Builder()
        .id(1L)
        .name("iPhone 15")
        .price(BigDecimal("4999.00"))
        .category("Smartphones")
        .inStock(true)
        .build(),
        
    Product_Builder()
        .id(2L)
        .name("Galaxy S24")
        .price(BigDecimal("3499.00"))
        .category("Smartphones") 
        .inStock(false)
        .build()
)
```

### Example with Lists and Complex Objects

```kotlin
data class HistoryEventEntity(
    val id: String,
    val event: String,
    val timestamp: Long
)

@GenerateBuilder
data class EntityWithHistory(
    val id: String,
    val name: String,
    val historyEntities: List<HistoryEventEntity> = emptyList(),
    val tags: List<String> = emptyList(),
    val isActive: Boolean = true
)

// Usage in tests:
val entity = EntityWithHistory_Builder()
    .id("entity-123")
    .name("Test Entity")
    .historyEntities(listOf(
        HistoryEventEntity("h1", "created", System.currentTimeMillis()),
        HistoryEventEntity("h2", "updated", System.currentTimeMillis())
    ))
    .tags(listOf("test", "android", "builder"))
    .isActive(true)
    .build()

// With default values (empty lists)
val simpleEntity = EntityWithHistory_Builder()
    .id("simple-456")
    .name("Simple Entity")
    .build() // historyEntities and tags remain empty
```

### Example in Android Integration Tests

```kotlin
class UserRepositoryTest {
    
    @Test
    fun `should save and retrieve user`() {
        // Arrange - using builder to create test data
        val testUser = User_Builder()
            .name("John da Silva")
            .email("john.silva@example.com")
            .age(30)
            .isActive(true)
            .build()
            
        // Act
        userRepository.save(testUser)
        val retrievedUser = userRepository.findById(1L)
        
        // Assert
        assertThat(retrievedUser).isEqualTo(testUser)
    }
    
    @Test
    fun `should handle multiple users`() {
        // Creating multiple test users easily
        val testUsers = listOf(
            User_Builder().name("User 1").email("user1@test.com").age(25).build(),
            User_Builder().name("User 2").email("user2@test.com").age(30).build(),
            User_Builder().name("User 3").email("user3@test.com").age(35).build()
        )
        
        testUsers.forEach { userRepository.save(it) }
        
        val allUsers = userRepository.findAll()
        assertThat(allUsers).hasSize(3)
    }
}
```

### Example using prefix = "with"

```kotlin
@GenerateBuilder(prefix = "with")
data class OrderItem(
    val id: String,
    val productName: String,
    val quantity: Int,
    val price: BigDecimal,
    val discount: BigDecimal = BigDecimal.ZERO
)

// Usage with "with" prefix:
val orderItem = OrderItem_Builder()
    .withId("item-001")
    .withProductName("MacBook Pro")
    .withQuantity(1)
    .withPrice(BigDecimal("8999.00"))
    .withDiscount(BigDecimal("500.00"))
    .build()

// List of items for testing
val orderItems = listOf(
    OrderItem_Builder()
        .withId("item-001")
        .withProductName("iPhone 15")
        .withQuantity(2)
        .withPrice(BigDecimal("4999.00"))
        .build(), // discount uses default value (ZERO)
        
    OrderItem_Builder()
        .withId("item-002")
        .withProductName("AirPods Pro")
        .withQuantity(1)
        .withPrice(BigDecimal("1299.00"))
        .withDiscount(BigDecimal("100.00"))
        .build()
)
```

## Features

- ✅ Automatic builder generation using KSP
- ✅ Fluent interface  
- ✅ Required field validation
- ✅ Support for parameters with default values
- ✅ Ideal for test data creation
- ✅ Support for all Kotlin types
- ✅ Faster than KAPT
- ✅ **Kotlin 2.0.0** compatible
- ✅ **Version Catalog** for dependency management

## 📦 Installation and Usage

### Option 1: Use locally (recommended for testing)

1. **Clone the repository:**
```bash
git clone https://github.com/your-username/kotlin-builder-generator.git
cd kotlin-builder-generator
```

2. **Publish to Maven Local:**
```bash
./gradlew publishToMavenLocal
```

3. **Use in your Android project:**
```kotlin
// In your app's build.gradle.kts
repositories {
    mavenLocal()
    mavenCentral()
    google()
}

dependencies {
    implementation("com.angelorobson.buildergen:annotation:1.0.0")
    ksp("com.angelorobson.buildergen:processor:1.0.0")
}
```

### Option 2: JitPack (future)

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.your-username:kotlin-builder-generator:annotation:1.0.0")
    ksp("com.github.your-username:kotlin-builder-generator:processor:1.0.0")
}
```

## 🧪 Run the example

```bash
cd kotlin-builder-generator
./gradlew :sample:test
```

## 📁 Project structure

- `annotation/` - The @GenerateBuilder annotation
- `processor/` - The KSP processor that generates the code
- `sample/` - Usage examples and tests
- `gradle/libs.versions.toml` - Version catalog