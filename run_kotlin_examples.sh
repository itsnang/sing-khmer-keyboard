#!/bin/bash

# Kotlin Learning Examples Runner
# This script helps you run the Kotlin practice examples

echo "🚀 Kotlin Learning Examples Runner"
echo "=================================="

# Check if we're in the right directory
if [ ! -f "build.gradle.kts" ]; then
    echo "❌ Error: Please run this script from the project root directory"
    exit 1
fi

# Check if gradlew exists
if [ ! -f "gradlew" ]; then
    echo "❌ Error: gradlew not found. Please make sure you're in the correct directory"
    exit 1
fi

echo "📚 Available options:"
echo "1. Run practice examples (kotlin_practice_examples.kt)"
echo "2. Run your existing transliterator project (Main.kt)"
echo "3. Show project structure"
echo "4. Exit"

read -p "Choose an option (1-4): " choice

case $choice in
    1)
        echo "🎯 Running Kotlin practice examples..."
        echo "This will demonstrate various Kotlin concepts and features."
        echo ""
        ./gradlew :transliterator-core:run --args="kotlin_practice_examples"
        ;;
    2)
        echo "🔤 Running your transliterator project..."
        echo "This will run your existing Main.kt file."
        echo ""
        ./gradlew :transliterator-core:run
        ;;
    3)
        echo "📁 Project structure:"
        echo ""
        echo "transliterator-core/src/main/kotlin/com/singkhmer/transliterator/"
        echo "├── Main.kt                    # Your existing main file"
        echo "├── Transliterator.kt          # Main transliterator class"
        echo "├── Trie.kt                    # Trie data structure"
        echo "└── kotlin_practice_examples.kt # Practice examples (new)"
        echo ""
        echo "📚 Learning materials:"
        echo "├── KOTLIN_LEARNING_GUIDE.md   # Comprehensive guide"
        echo "├── README_KOTLIN_LEARNING.md  # Getting started guide"
        echo "└── run_kotlin_examples.sh     # This script"
        ;;
    4)
        echo "👋 Goodbye! Happy coding with Kotlin!"
        exit 0
        ;;
    *)
        echo "❌ Invalid option. Please choose 1-4."
        exit 1
        ;;
esac