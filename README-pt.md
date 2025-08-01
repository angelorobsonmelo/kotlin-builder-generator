# Kotlin Builder Generator

[English](README.md) | **Português**

Uma biblioteca Kotlin que gera automaticamente builders para data classes usando anotações, especialmente útil para testes.

## Como usar

### 1. Adicione a dependência

#### Para projetos Android

No seu `build.gradle.kts` (app level):

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
}

dependencies {
    // Adicione estas dependências
    implementation("com.angelorobson.buildergen:annotation:1.0.0")
    ksp("com.angelorobson.buildergen:processor:1.0.0")
    
    // Suas outras dependências...
}
```

#### Para Maven Local (desenvolvimento)

Se você clonou este repositório e publicou localmente:

```kotlin
repositories {
    mavenLocal() // Adicione esta linha
    mavenCentral()
    google()
}

dependencies {
    implementation("com.angelorobson.buildergen:annotation:1.0.0")
    ksp("com.angelorobson.buildergen:processor:1.0.0")
}
```

#### Para projetos Kotlin JVM

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

### 2. Anote sua data class

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

#### Personalização com Prefix

Você pode personalizar o prefixo dos métodos do builder usando o parâmetro `prefix`:

```kotlin
@GenerateBuilder(prefix = "with")
data class TestPrefixUser(
    val id: String,
    val name: String,
    val age: Int,
    val email: String
)
```

### 3. Use o builder gerado

#### Sem prefix (padrão)
```kotlin
// Em seus testes
val user = User_Builder()
    .name("João Silva")
    .age(30)
    .email("joao@example.com")
    .isActive(true)
    .build()

// Ou crie múltiplos objetos facilmente
val testUsers = listOf(
    User_Builder().name("User1").age(20).email("user1@test.com").build(),
    User_Builder().name("User2").age(30).email("user2@test.com").build()
)
```

#### Com prefix = "with"
```kotlin
// Quando você usa @GenerateBuilder(prefix = "with")
val user = TestPrefixUser_Builder()
    .withId("123")
    .withName("João Silva")
    .withAge(30)
    .withEmail("joao@example.com")
    .build()

// Múltiplos objetos com prefix
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

## 🔨 Exemplos Práticos para Android

### Exemplo com Customer (campos obrigatórios e opcionais)

```kotlin
@GenerateBuilder
data class Customer(
    val id: Long,                    // Obrigatório
    val name: String,                // Obrigatório  
    val email: String,               // Obrigatório
    val phone: String?,              // Opcional (nullable)
    val address: String?,            // Opcional (nullable)
    val birthDate: String? = null,   // Opcional com valor padrão
    val isActive: Boolean = true     // Com valor padrão
)

// Uso nos testes:
val customer = Customer_Builder()
    .id(1L)
    .name("João Silva")
    .email("joao@example.com")
    .phone("(11) 99999-9999")
    .address("Rua das Flores, 123")
    .isActive(true)
    .build()

// Apenas campos obrigatórios (campos opcionais ficam null ou usam valor padrão)
val minimalCustomer = Customer_Builder()
    .id(2L)
    .name("Maria Santos")
    .email("maria@example.com")
    .build()
```

### Exemplo com Product (BigDecimal e múltiplos tipos)

```kotlin
@GenerateBuilder
data class Product(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val category: String,
    val inStock: Boolean
)

// Uso nos testes:
val product = Product_Builder()
    .id(1L)
    .name("Smartphone Samsung")
    .price(BigDecimal("1299.99"))
    .category("Eletrônicos")
    .inStock(true)
    .build()

// Criando múltiplos produtos para testes
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

### Exemplo com Listas e Objetos Complexos

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

// Uso nos testes:
val entity = EntityWithHistory_Builder()
    .id("entity-123")
    .name("Entidade de Teste")
    .historyEntities(listOf(
        HistoryEventEntity("h1", "created", System.currentTimeMillis()),
        HistoryEventEntity("h2", "updated", System.currentTimeMillis())
    ))
    .tags(listOf("teste", "android", "builder"))
    .isActive(true)
    .build()

// Com valores padrão (listas vazias)
val simpleEntity = EntityWithHistory_Builder()
    .id("simple-456")
    .name("Entidade Simples")
    .build() // historyEntities e tags ficam vazias
```

### Exemplo em Testes de Integração Android

```kotlin
class UserRepositoryTest {
    
    @Test
    fun `should save and retrieve user`() {
        // Arrange - usando builder para criar dados de teste
        val testUser = User_Builder()
            .name("João da Silva")
            .email("joao.silva@example.com")
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
        // Criando vários usuários de teste facilmente
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

### Exemplo usando prefix = "with"

```kotlin
@GenerateBuilder(prefix = "with")
data class OrderItem(
    val id: String,
    val productName: String,
    val quantity: Int,
    val price: BigDecimal,
    val discount: BigDecimal = BigDecimal.ZERO
)

// Uso com prefix "with":
val orderItem = OrderItem_Builder()
    .withId("item-001")
    .withProductName("MacBook Pro")
    .withQuantity(1)
    .withPrice(BigDecimal("8999.00"))
    .withDiscount(BigDecimal("500.00"))
    .build()

// Lista de itens para teste
val orderItems = listOf(
    OrderItem_Builder()
        .withId("item-001")
        .withProductName("iPhone 15")
        .withQuantity(2)
        .withPrice(BigDecimal("4999.00"))
        .build(), // discount usa valor padrão (ZERO)
        
    OrderItem_Builder()
        .withId("item-002")
        .withProductName("AirPods Pro")
        .withQuantity(1)
        .withPrice(BigDecimal("1299.00"))
        .withDiscount(BigDecimal("100.00"))
        .build()
)
```

## Características

- ✅ Geração automática de builders usando KSP
- ✅ Interface fluente  
- ✅ Validação de campos obrigatórios
- ✅ Suporte a parâmetros com valores padrão
- ✅ Ideal para criação de dados de teste
- ✅ Suporte a todos os tipos Kotlin
- ✅ Mais rápido que KAPT
- ✅ **Kotlin 2.0.0** compatível
- ✅ **Version Catalog** para gerenciamento de dependências

## 📦 Instalação e Uso

### Opção 1: Usar localmente (recomendado para testes)

1. **Clone o repositório:**
```bash
git clone https://github.com/your-username/kotlin-builder-generator.git
cd kotlin-builder-generator
```

2. **Publique no Maven Local:**
```bash
./gradlew publishToMavenLocal
```

3. **Use no seu projeto Android:**
```kotlin
// No build.gradle.kts do seu app
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

### Opção 2: JitPack (futuramente)

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.your-username:kotlin-builder-generator:annotation:1.0.0")
    ksp("com.github.your-username:kotlin-builder-generator:processor:1.0.0")
}
```

## 🧪 Executar o exemplo

```bash
cd kotlin-builder-generator
./gradlew :sample:test
```

## 📁 Estrutura do projeto

- `annotation/` - A anotação @GenerateBuilder
- `processor/` - O processador KSP que gera o código
- `sample/` - Exemplos de uso e testes
- `gradle/libs.versions.toml` - Catálogo de versões