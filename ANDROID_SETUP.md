# 📱 Como usar no Android

Este guia mostra como importar e usar a biblioteca Kotlin Builder Generator em projetos Android.

## 🚀 Passo a Passo

### 1. Publique a biblioteca localmente

```bash
# Clone o repositório (se ainda não fez)
git clone https://github.com/your-username/kotlin-builder-generator.git
cd kotlin-builder-generator

# Publique no Maven Local
./gradlew publishToMavenLocal
```

### 2. Configure seu projeto Android

#### No `build.gradle.kts` (Project level):

```kotlin
// Top-level build file
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
}
```

#### No `build.gradle.kts` (App level):

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") // Adicione esta linha
}

android {
    compileSdk 34
    
    defaultConfig {
        // sua configuração...
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

repositories {
    mavenLocal() // IMPORTANTE: Adicione esta linha
    mavenCentral()
    google()
}

dependencies {
    // Kotlin Builder Generator
    implementation("com.angelorobson.buildergen:annotation:1.0.0")
    ksp("com.angelorobson.buildergen:processor:1.0.0")
    
    // Suas outras dependências...
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    // Para testes (opcional)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
}
```

### 3. Use no seu código Android

#### Crie suas data classes:

```kotlin
// User.kt
import com.angelorobson.buildergen.annotation.GenerateBuilder

@GenerateBuilder
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val isActive: Boolean = true
)

@GenerateBuilder
data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val category: String
)
```

#### Use os builders nos seus testes:

```kotlin
// UserTest.kt
import org.junit.Test
import org.junit.Assert.*

class UserTest {
    
    @Test
    fun `should create user with builder`() {
        val user = UserBuilder()
            .id(1L)
            .name("João Silva")
            .email("joao@example.com")
            .isActive(true)
            .build()
        
        assertEquals("João Silva", user.name)
        assertEquals("joao@example.com", user.email)
        assertTrue(user.isActive)
    }
    
    @Test
    fun `should create test data easily`() {
        val users = listOf(
            UserBuilder().id(1L).name("User 1").email("user1@test.com").build(),
            UserBuilder().id(2L).name("User 2").email("user2@test.com").build(),
            UserBuilder().id(3L).name("User 3").email("user3@test.com").build()
        )
        
        assertEquals(3, users.size)
    }
}
```

#### Use em ViewModels ou Repository (para dados de teste):

```kotlin
// UserRepository.kt
class UserRepository {
    
    fun createMockUsers(): List<User> {
        return listOf(
            UserBuilder()
                .id(1L)
                .name("Admin User")
                .email("admin@company.com")
                .isActive(true)
                .build(),
                
            UserBuilder()
                .id(2L)
                .name("Test User")
                .email("test@company.com")
                .isActive(false)
                .build()
        )
    }
}
```

## 🔧 Configuração Avançada

### Para projetos com múltiplos módulos:

```kotlin
// No build.gradle.kts de cada módulo que usar a biblioteca
dependencies {
    implementation("com.angelorobson.buildergen:annotation:1.0.0")
    ksp("com.angelorobson.buildergen:processor:1.0.0")
}
```

### Para usar com Compose:

```kotlin
@Composable
fun UserProfile(user: User = UserBuilder()
    .id(1L)
    .name("Preview User")
    .email("preview@test.com")
    .build()
) {
    // Sua UI...
}

@Preview
@Composable
fun UserProfilePreview() {
    UserProfile(
        user = UserBuilder()
            .id(999L)
            .name("Preview User")
            .email("preview@example.com")
            .isActive(true)
            .build()
    )
}
```

## ⚠️ Troubleshooting

### Build não encontra as classes geradas?

1. Faça um Clean + Rebuild:
```bash
./gradlew clean build
```

2. Verifique se o KSP está configurado corretamente
3. Confirme que `mavenLocal()` está nos repositories

### KSP não está funcionando?

- Certifique-se que a versão do KSP é compatível com seu Kotlin
- Verifique se não há conflitos com KAPT

### Performance?

- KSP é muito mais rápido que KAPT
- Use Incremental Compilation para builds mais rápidos

## 🎯 Próximos Passos

1. Teste a biblioteca no seu projeto
2. Crie builders para suas data classes
3. Use nos seus testes unitários
4. Aproveite para criar dados mock facilmente!