# Kotlin Learning Guide

Welcome to Kotlin! This comprehensive guide will teach you the fundamentals and advanced concepts of Kotlin programming language, with examples from your existing transliterator project.

## Table of Contents
1. [Introduction to Kotlin](#introduction-to-kotlin)
2. [Basic Syntax](#basic-syntax)
3. [Variables and Data Types](#variables-and-data-types)
4. [Control Flow](#control-flow)
5. [Functions](#functions)
6. [Classes and Objects](#classes-and-objects)
7. [Collections](#collections)
8. [Null Safety](#null-safety)
9. [Extensions](#extensions)
10. [Coroutines](#coroutines)
11. [Your Project Analysis](#your-project-analysis)

## Introduction to Kotlin

Kotlin is a modern, statically-typed programming language that runs on the Java Virtual Machine (JVM). It was developed by JetBrains and is now officially supported by Google for Android development.

### Key Features:
- **Interoperable with Java**: Can use Java libraries and frameworks
- **Concise syntax**: Reduces boilerplate code
- **Null safety**: Built-in null safety features
- **Functional programming**: Support for functional programming paradigms
- **Coroutines**: Built-in support for asynchronous programming

## Basic Syntax

### Hello World
```kotlin
fun main() {
    println("Hello, Kotlin!")
}
```

### Comments
```kotlin
// Single line comment

/*
 * Multi-line comment
 */

/**
 * Documentation comment
 */
```

## Variables and Data Types

### Variable Declaration
```kotlin
// Mutable variable (can be changed)
var mutableVar = "I can change"
mutableVar = "Changed!"

// Immutable variable (cannot be changed)
val immutableVar = "I cannot change"
// immutableVar = "This will cause an error"

// Explicit type declaration
val explicitType: String = "Explicitly typed"
val number: Int = 42
val decimal: Double = 3.14
val boolean: Boolean = true
```

### Basic Data Types
```kotlin
// Numbers
val byte: Byte = 127
val short: Short = 32767
val int: Int = 2147483647
val long: Long = 9223372036854775807L
val float: Float = 3.14f
val double: Double = 3.14

// Characters and Strings
val char: Char = 'A'
val string: String = "Hello, Kotlin!"

// Booleans
val boolean: Boolean = true

// Arrays
val array: Array<Int> = arrayOf(1, 2, 3, 4, 5)
```

## Control Flow

### If Expression
```kotlin
// Traditional if-else
val number = 10
if (number > 0) {
    println("Positive")
} else if (number < 0) {
    println("Negative")
} else {
    println("Zero")
}

// If as expression (returns a value)
val result = if (number > 0) "Positive" else "Negative"
```

### When Expression (Switch-like)
```kotlin
val day = 3
when (day) {
    1 -> println("Monday")
    2 -> println("Tuesday")
    3 -> println("Wednesday")
    4 -> println("Thursday")
    5 -> println("Friday")
    else -> println("Weekend")
}

// When as expression
val dayName = when (day) {
    1 -> "Monday"
    2 -> "Tuesday"
    3 -> "Wednesday"
    4 -> "Thursday"
    5 -> "Friday"
    else -> "Weekend"
}

// When with ranges
val number = 5
when (number) {
    in 1..5 -> println("Small")
    in 6..10 -> println("Medium")
    else -> println("Large")
}
```

### Loops
```kotlin
// For loop
for (i in 1..5) {
    println(i) // Prints 1, 2, 3, 4, 5
}

// For loop with step
for (i in 0..10 step 2) {
    println(i) // Prints 0, 2, 4, 6, 8, 10
}

// For loop with downTo
for (i in 5 downTo 1) {
    println(i) // Prints 5, 4, 3, 2, 1
}

// For loop with indices
val fruits = listOf("Apple", "Banana", "Orange")
for (index in fruits.indices) {
    println("$index: ${fruits[index]}")
}

// For loop with values
for (fruit in fruits) {
    println(fruit)
}

// While loop
var counter = 0
while (counter < 5) {
    println(counter)
    counter++
}

// Do-while loop
var count = 0
do {
    println(count)
    count++
} while (count < 5)
```

## Functions

### Basic Functions
```kotlin
// Simple function
fun greet(name: String) {
    println("Hello, $name!")
}

// Function with return type
fun add(a: Int, b: Int): Int {
    return a + b
}

// Single expression function
fun multiply(a: Int, b: Int) = a * b

// Function with default parameters
fun greetWithDefault(name: String = "World") {
    println("Hello, $name!")
}

// Function with named parameters
fun createPerson(name: String, age: Int, city: String) {
    println("Name: $name, Age: $age, City: $city")
}

// Usage
createPerson(name = "John", age = 30, city = "New York")
```

### Higher-Order Functions
```kotlin
// Function that takes another function as parameter
fun operateOnNumbers(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

// Usage
val sum = operateOnNumbers(5, 3) { x, y -> x + y }
val product = operateOnNumbers(5, 3) { x, y -> x * y }

// Lambda expressions
val numbers = listOf(1, 2, 3, 4, 5)
val doubled = numbers.map { it * 2 }
val evenNumbers = numbers.filter { it % 2 == 0 }
```

## Classes and Objects

### Basic Class
```kotlin
class Person(val name: String, var age: Int) {
    // Primary constructor parameters are automatically properties
    
    // Secondary constructor
    constructor(name: String) : this(name, 0)
    
    // Init block (runs when object is created)
    init {
        println("Person $name created")
    }
    
    // Method
    fun introduce() {
        println("Hi, I'm $name and I'm $age years old")
    }
}

// Usage
val person = Person("Alice", 25)
person.introduce()
```

### Data Classes
```kotlin
// Data class automatically provides equals(), hashCode(), toString(), copy()
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// Usage
val user1 = User(1, "John", "john@example.com")
val user2 = user1.copy(name = "Jane") // Creates a copy with different name
```

### Object Declaration (Singleton)
```kotlin
object Database {
    fun connect() {
        println("Connected to database")
    }
}

// Usage
Database.connect()
```

### Companion Objects
```kotlin
class MyClass {
    companion object {
        fun create(): MyClass = MyClass()
    }
}

// Usage
val instance = MyClass.create()
```

## Collections

### Lists
```kotlin
// Immutable list
val immutableList = listOf(1, 2, 3, 4, 5)

// Mutable list
val mutableList = mutableListOf(1, 2, 3, 4, 5)
mutableList.add(6)

// List operations
val numbers = listOf(1, 2, 3, 4, 5)
val doubled = numbers.map { it * 2 }
val evenNumbers = numbers.filter { it % 2 == 0 }
val sum = numbers.reduce { acc, num -> acc + num }
```

### Sets
```kotlin
// Immutable set
val immutableSet = setOf(1, 2, 3, 4, 5)

// Mutable set
val mutableSet = mutableSetOf(1, 2, 3, 4, 5)
mutableSet.add(6)
```

### Maps
```kotlin
// Immutable map
val immutableMap = mapOf("a" to 1, "b" to 2, "c" to 3)

// Mutable map
val mutableMap = mutableMapOf("a" to 1, "b" to 2)
mutableMap["c"] = 3

// Map operations
val map = mapOf("a" to 1, "b" to 2, "c" to 3)
val keys = map.keys
val values = map.values
val entry = map["a"] // Returns 1
```

## Null Safety

### Nullable Types
```kotlin
// Nullable type (can be null)
var nullableString: String? = "Hello"
nullableString = null

// Non-nullable type (cannot be null)
var nonNullableString: String = "Hello"
// nonNullableString = null // This would cause a compilation error
```

### Safe Calls
```kotlin
val name: String? = "John"
val length = name?.length // Returns null if name is null, otherwise returns length

// Safe call with default value
val lengthOrDefault = name?.length ?: 0
```

### Elvis Operator
```kotlin
val name: String? = null
val displayName = name ?: "Unknown"
```

### Not-null Assertion
```kotlin
val name: String? = "John"
val length = name!!.length // Throws exception if name is null
```

## Extensions

### Extension Functions
```kotlin
// Extending String class
fun String.addExclamation() = "$this!"

// Usage
val greeting = "Hello".addExclamation() // Returns "Hello!"

// Extending Int class
fun Int.isEven() = this % 2 == 0

// Usage
val isEven = 4.isEven() // Returns true
```

## Coroutines

### Basic Coroutines
```kotlin
import kotlinx.coroutines.*

// Launch a coroutine
fun main() = runBlocking {
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
}

// Async coroutine
suspend fun fetchData(): String {
    delay(1000L)
    return "Data"
}

// Usage
val data = async { fetchData() }
val result = data.await()
```

## Your Project Analysis

Looking at your existing transliterator project, here are the key Kotlin concepts being used:

### 1. Package Declaration
```kotlin
package com.singkhmer.transliterator
```
This declares the package namespace for your classes.

### 2. Class Definition with Properties
From your `Transliterator.kt`:
```kotlin
class Transliterator {
    private val trie = Trie()
    
    init {
        // Load dataset and populate trie on initialization
        val dataset = loadDataset()
        dataset.forEach { (romanization, khmerWords) ->
            khmerWords.forEach { khmerWord ->
                trie.insert(romanization, khmerWord) // Uses default freq = 1
            }
        }
    }
}
```

**Key Concepts:**
- `private val trie = Trie()` - Private immutable property
- `init` block - Initialization block that runs when the class is instantiated
- `forEach` - Higher-order function for iterating over collections

### 3. Function Definitions
```kotlin
fun hello(): String {
    return "Hello from Sing Khmer Transliteration Engine"
}

fun searchExact(input: String) = trie.searchExact(input)
```

**Key Concepts:**
- Function with explicit return type
- Single-expression function (shorthand syntax)

### 4. Data Classes and Mutable Collections
From your `Trie.kt`:
```kotlin
class TrieNode {
    val children: MutableMap<Char, TrieNode> = mutableMapOf()
    val words: MutableMap<String, Int> = mutableMapOf() // Khmer word -> frequency count
}
```

**Key Concepts:**
- `MutableMap` - Mutable collection type
- Generic types `<Char, TrieNode>` and `<String, Int>`

### 5. Null Safety and Safe Calls
```kotlin
// From your Trie.kt searchExact function
for (char in lowerInput) {
    currentNode = currentNode.children[char] ?: return emptyList()
}
```

**Key Concepts:**
- Elvis operator `?:` - Returns `emptyList()` if `children[char]` is null
- Safe navigation with `?.` (implicit in this case)

### 6. String Templates and Interpolation
From your `Main.kt`:
```kotlin
println("jg -> ${results}")
```

**Key Concepts:**
- String templates using `$` for simple variables or `${}` for expressions

### 7. Collections and Functional Programming
```kotlin
// From your suggestTop3 function
val exactResults = trie.searchExact(input)
if (exactResults.size >= 3) {
    return exactResults.take(3)
}
```

**Key Concepts:**
- `take(3)` - Extension function to get first 3 elements
- `size` property for collections

### 8. Control Flow with When and If
```kotlin
// From your suggestTop3 function
if (input.isBlank()) {
    return emptyList()
}
```

**Key Concepts:**
- `isBlank()` - Extension function to check if string is blank
- Early return pattern

## Practice Exercises

### Exercise 1: Basic Syntax
Create a function that takes a list of numbers and returns the sum of all even numbers.

```kotlin
fun sumEvenNumbers(numbers: List<Int>): Int {
    return numbers.filter { it % 2 == 0 }.sum()
}
```

### Exercise 2: Classes and Objects
Create a `Book` class with properties for title, author, and year, and a method to check if it's a classic (older than 50 years).

```kotlin
data class Book(val title: String, val author: String, val year: Int) {
    fun isClassic(): Boolean = (2024 - year) > 50
}
```

### Exercise 3: Null Safety
Create a function that safely extracts the first character of a nullable string.

```kotlin
fun getFirstChar(str: String?): Char? {
    return str?.firstOrNull()
}
```

### Exercise 4: Collections
Create a function that groups a list of words by their first letter.

```kotlin
fun groupWordsByFirstLetter(words: List<String>): Map<Char, List<String>> {
    return words.groupBy { it.firstOrNull() ?: ' ' }
}
```

## Next Steps

1. **Practice**: Try implementing the exercises above
2. **Explore**: Look at more of your existing code to understand patterns
3. **Build**: Create a small project using the concepts you've learned
4. **Resources**: Check out the official Kotlin documentation and tutorials

## Useful Resources

- [Official Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Kotlin Playground](https://play.kotlinlang.org/)
- [Kotlin by Example](https://play.kotlinlang.org/byExample/overview)

Happy coding with Kotlin! 🚀