#!/bin/bash
cd /Users/angelomelo/AndroidStudioProjects/kotlin-builder-generator
echo "Starting build test..."
./gradlew clean build --no-daemon --stacktrace
echo "Build test completed"