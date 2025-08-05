# Kotlin Learning Resources

Welcome to your Kotlin learning journey! This repository contains comprehensive materials to help you learn Kotlin programming language.

## 📚 Learning Materials

### 1. [KOTLIN_LEARNING_GUIDE.md](KOTLIN_LEARNING_GUIDE.md)
A comprehensive guide covering:
- Basic syntax and concepts
- Variables and data types
- Control flow
- Functions
- Classes and objects
- Collections
- Null safety
- Extensions
- Coroutines
- Analysis of your existing project code

### 2. [kotlin_practice_examples.kt](kotlin_practice_examples.kt)
Interactive examples and exercises that you can run to practice Kotlin concepts.

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- Gradle (included in the project)

### Running the Examples

1. **Navigate to your project directory:**
   ```bash
   cd /workspace
   ```

2. **Run the practice examples:**
   ```bash
   ./gradlew :transliterator-core:run
   ```

3. **Or run a specific Kotlin file:**
   ```bash
   ./gradlew :transliterator-core:run --args="kotlin_practice_examples"
   ```

### Running Your Existing Project

Your existing transliterator project is already set up with Kotlin. To run it:

```bash
./gradlew :transliterator-core:run
```

This will execute the `Main.kt` file in your transliterator project.

## 📖 Learning Path

### Week 1: Fundamentals
1. **Basic Syntax** - Variables, data types, basic operations
2. **Control Flow** - If statements, when expressions, loops
3. **Functions** - Function declaration, parameters, return types

### Week 2: Object-Oriented Programming
1. **Classes and Objects** - Class definition, properties, methods
2. **Data Classes** - Automatic equals, hashCode, toString
3. **Inheritance** - Extending classes, interfaces

### Week 3: Advanced Concepts
1. **Collections** - Lists, sets, maps, functional operations
2. **Null Safety** - Nullable types, safe calls, Elvis operator
3. **Extensions** - Extension functions and properties

### Week 4: Real-World Applications
1. **Coroutines** - Asynchronous programming
2. **Project Analysis** - Understanding your existing code
3. **Practice Projects** - Building small applications

## 🎯 Practice Exercises

The `kotlin_practice_examples.kt` file includes several exercises:

1. **Basic Syntax**: String manipulation, variable declaration
2. **Control Flow**: Conditional statements, loops
3. **Functions**: Function definition, higher-order functions
4. **Classes**: Class creation, data classes
5. **Collections**: List operations, functional programming
6. **Null Safety**: Handling nullable types
7. **Extensions**: Creating extension functions

## 🔍 Understanding Your Project

Your existing transliterator project demonstrates many Kotlin concepts:

### Key Kotlin Features in Your Code:

1. **Package Declaration**
   ```kotlin
   package com.singkhmer.transliterator
   ```

2. **Class Definition with Properties**
   ```kotlin
   class Transliterator {
       private val trie = Trie()
   }
   ```

3. **Function Definitions**
   ```kotlin
   fun searchExact(input: String) = trie.searchExact(input)
   ```

4. **Collections and Functional Programming**
   ```kotlin
   val exactResults = trie.searchExact(input)
   if (exactResults.size >= 3) {
       return exactResults.take(3)
   }
   ```

5. **Null Safety**
   ```kotlin
   currentNode = currentNode.children[char] ?: return emptyList()
   ```

## 📝 Tips for Learning

1. **Start Small**: Begin with basic syntax and gradually move to complex concepts
2. **Practice Regularly**: Use the practice examples and create your own
3. **Read Code**: Analyze your existing project to understand real-world usage
4. **Experiment**: Don't be afraid to modify and test code
5. **Use Documentation**: Refer to the official Kotlin documentation

## 🛠️ Development Environment

### Recommended IDE
- **IntelliJ IDEA** - Best Kotlin support
- **VS Code** - With Kotlin extension
- **Android Studio** - For Android development

### Useful Extensions
- Kotlin Language Support
- Kotlin Debugger
- Kotlin Formatter

## 📚 Additional Resources

- [Official Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Kotlin Playground](https://play.kotlinlang.org/)
- [Kotlin by Example](https://play.kotlinlang.org/byExample/overview)
- [Kotlin Koans](https://play.kotlinlang.org/koans/overview)

## 🤝 Getting Help

If you encounter issues or have questions:

1. **Check the Documentation**: Start with the official Kotlin docs
2. **Search Online**: Stack Overflow, Kotlin forums
3. **Experiment**: Try different approaches in the practice examples
4. **Ask Questions**: Don't hesitate to ask for clarification

## 🎉 Next Steps

After completing the learning materials:

1. **Build Something**: Create a small project using Kotlin
2. **Contribute**: Help improve your existing transliterator project
3. **Explore**: Learn about Kotlin coroutines, multiplatform, etc.
4. **Share**: Teach others what you've learned

Happy coding with Kotlin! 🚀