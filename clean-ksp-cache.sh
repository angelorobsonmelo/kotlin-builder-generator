#!/bin/bash

echo "Limpando caches do KSP e Gradle..."

# Parar o daemon do Gradle
./gradlew --stop

# Limpar build directories
echo "Removendo diretórios build..."
find . -name "build" -type d -exec rm -rf {} + 2>/dev/null || true

# Limpar caches específicos do KSP
echo "Removendo caches do KSP..."
find . -name "kspCaches" -type d -exec rm -rf {} + 2>/dev/null || true

# Limpar cache global do Gradle
echo "Limpando cache global do Gradle..."
rm -rf ~/.gradle/caches/

# Limpar daemon locks
echo "Removendo locks do daemon..."
rm -rf ~/.gradle/daemon/

echo "Cache limpo! Execute './gradlew build' para reconstruir."