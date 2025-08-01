package com.angelorobson.buildergen.processor

import com.angelorobson.buildergen.annotation.GenerateBuilder
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.writeTo
import com.squareup.kotlinpoet.ksp.toTypeName

class BuilderProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(GenerateBuilder::class.qualifiedName!!)
        val ret = symbols.filter { !it.validate() }.toList()
        
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it ->
                generateBuilder(it as KSClassDeclaration)
            }
        
        return ret
    }

    private fun generateBuilder(classDeclaration: KSClassDeclaration) {
        val className = classDeclaration.simpleName.asString()
        val packageName = classDeclaration.packageName.asString()
        val builderClassName = "${className}_Builder"

        try {
            val constructorParams = getConstructorParameters(classDeclaration)
            
            if (constructorParams.isEmpty()) {
                logger.warn("No constructor parameters found for $className. Builder may not be useful.")
                return
            }

            val prefix = getAnnotationPrefix(classDeclaration)
            val builderClass = generateBuilderClass(className, builderClassName, packageName, constructorParams, prefix)
            
            val file = FileSpec.builder(packageName, builderClassName)
                .addType(builderClass)
                .indent("  ") // Use 2 spaces for indentation
                .build()

            // Write with custom formatting to avoid line breaks in strings
            val fileWithCustomFormatting = file.toBuilder()
                .build()

            fileWithCustomFormatting.writeTo(codeGenerator, Dependencies(false, classDeclaration.containingFile!!))

        } catch (e: Exception) {
            logger.error("Error generating builder for $className: ${e.message}")
        }
    }

    private fun getConstructorParameters(classDeclaration: KSClassDeclaration): List<KSValueParameter> {
        return classDeclaration.primaryConstructor?.parameters ?: emptyList()
    }

    private fun getAnnotationPrefix(classDeclaration: KSClassDeclaration): String {
        val annotation = classDeclaration.annotations.find { 
            it.annotationType.resolve().declaration.qualifiedName?.asString() == GenerateBuilder::class.qualifiedName
        }
        
        return annotation?.arguments?.find { it.name?.asString() == "prefix" }?.value?.toString() ?: ""
    }

    private fun generateBuilderClass(
        originalClassName: String,
        builderClassName: String,
        packageName: String,
        parameters: List<KSValueParameter>,
        prefix: String
    ): TypeSpec {
        val builderClass = TypeSpec.classBuilder(builderClassName)
            .addModifiers(KModifier.PUBLIC)

        parameters.forEach { param ->
            val paramName = param.name!!.asString()
            val paramType = param.type.resolve().toTypeName()
            val methodName = if (prefix.isNotEmpty()) {
                "$prefix${paramName.replaceFirstChar { it.uppercase() }}"
            } else {
                paramName
            }
            
            builderClass.addProperty(
                PropertySpec.builder(paramName, paramType.copy(nullable = true))
                    .mutable(true)
                    .initializer("null")
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
            
            builderClass.addFunction(
                FunSpec.builder(methodName)
                    .addParameter(paramName, paramType)
                    .returns(ClassName(packageName, builderClassName))
                    .addStatement("this.%N = %N", paramName, paramName)
                    .addStatement("return this")
                    .build()
            )
        }

        val buildFunction = FunSpec.builder("build")
            .returns(ClassName(packageName, originalClassName))
            .addCode(generateBuildFunctionCode(originalClassName, parameters))
            .build()

        builderClass.addFunction(buildFunction)

        return builderClass.build()
    }

    private fun generateBuildFunctionCode(originalClassName: String, parameters: List<KSValueParameter>): CodeBlock {
        val codeBuilder = CodeBlock.builder()
        
        parameters.forEach { param ->
            val paramName = param.name!!.asString()
            val hasDefault = param.hasDefault
            val resolvedType = param.type.resolve()
            val isNullable = resolvedType.isMarkedNullable
            
            when {
                // Campo nullable -> sempre opcional, pode ser null
                isNullable -> {
                    codeBuilder.add("val %N = this.%N\n", paramName, paramName)
                }
                // Campo não-nullable com valor padrão -> opcional, usa valor padrão
                hasDefault -> {
                    val paramType = resolvedType.toTypeName()
                    codeBuilder.add(
                        "val %N = this.%N ?: %L\n",
                        paramName, paramName, getDefaultValue(paramType, resolvedType)
                    )
                }
                // Campo não-nullable sem valor padrão -> obrigatório
                else -> {
                    codeBuilder.add(
                        "val %N = this.%N ?: error(%S)\n",
                        paramName, paramName, "$paramName required"
                    )
                }
            }
        }
        
        val constructorCall = CodeBlock.builder()
        constructorCall.add("return %N(", originalClassName)
        
        parameters.forEachIndexed { index, param ->
            val paramName = param.name!!.asString()
            if (index > 0) constructorCall.add(", ")
            
            constructorCall.add("%N", paramName)
        }
        
        constructorCall.add(")")
        codeBuilder.add(constructorCall.build())
        
        return codeBuilder.build()
    }

    private fun getDefaultValue(typeName: TypeName, resolvedType: KSType): String {
        val typeString = typeName.toString()
        
        return when {
            typeString.startsWith("kotlin.collections.List") || 
            typeString.startsWith("java.util.List") ||
            typeString.contains("List<") -> "emptyList()"
            typeString == "kotlin.String" -> "\"\""
            typeString == "kotlin.Int" -> "0"
            typeString == "kotlin.Long" -> "0L"
            typeString == "kotlin.Float" -> "0.0f"
            typeString == "kotlin.Double" -> "0.0"
            typeString == "kotlin.Boolean" -> "true"
            else -> {
                // Para objetos personalizados, tenta criar uma instância com construtor padrão
                val declaration = resolvedType.declaration
                if (declaration is KSClassDeclaration) {
                    val className = declaration.simpleName.asString()
                    "${className}()"
                } else {
                    "null"
                }
            }
        }
    }
}

class BuilderProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return BuilderProcessor(environment.codeGenerator, environment.logger)
    }
}