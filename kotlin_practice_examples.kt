// Kotlin Practice Examples
// This file contains examples and exercises to help you learn Kotlin

package com.singkhmer.transliterator

fun main() {
    println("=== Kotlin Learning Examples ===\n")
    
    // Basic Syntax Examples
    basicSyntaxExamples()
    
    // Variables and Data Types
    variableExamples()
    
    // Control Flow Examples
    controlFlowExamples()
    
    // Function Examples
    functionExamples()
    
    // Class Examples
    classExamples()
    
    // Collection Examples
    collectionExamples()
    
    // Null Safety Examples
    nullSafetyExamples()
    
    // Extension Examples
    extensionExamples()
    
    // Practice Exercises
    practiceExercises()
}

fun basicSyntaxExamples() {
    println("1. Basic Syntax Examples:")
    println("Hello, Kotlin!")
    
    // String templates
    val name = "Kotlin"
    val version = 1.9
    println("Welcome to $name version $version")
    println("Result: ${2 + 3}")
    
    println()
}

fun variableExamples() {
    println("2. Variable Examples:")
    
    // Mutable vs Immutable
    var mutableVar = "I can change"
    val immutableVar = "I cannot change"
    
    println("Mutable: $mutableVar")
    mutableVar = "Changed!"
    println("After change: $mutableVar")
    println("Immutable: $immutableVar")
    
    // Type inference
    val number = 42 // Int
    val text = "Hello" // String
    val decimal = 3.14 // Double
    val boolean = true // Boolean
    
    println("Number: $number (${number::class.simpleName})")
    println("Text: $text (${text::class.simpleName})")
    println("Decimal: $decimal (${decimal::class.simpleName})")
    println("Boolean: $boolean (${boolean::class.simpleName})")
    
    println()
}

fun controlFlowExamples() {
    println("3. Control Flow Examples:")
    
    // If expression
    val number = 10
    val result = if (number > 0) "Positive" else "Negative"
    println("Number $number is $result")
    
    // When expression
    val day = 3
    val dayName = when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        else -> "Weekend"
    }
    println("Day $day is $dayName")
    
    // For loop
    println("Counting from 1 to 5:")
    for (i in 1..5) {
        print("$i ")
    }
    println()
    
    // For loop with step
    println("Even numbers from 0 to 10:")
    for (i in 0..10 step 2) {
        print("$i ")
    }
    println()
    
    println()
}

fun functionExamples() {
    println("4. Function Examples:")
    
    // Basic function
    fun greet(name: String) {
        println("Hello, $name!")
    }
    greet("Kotlin")
    
    // Function with return type
    fun add(a: Int, b: Int): Int {
        return a + b
    }
    println("5 + 3 = ${add(5, 3)}")
    
    // Single expression function
    fun multiply(a: Int, b: Int) = a * b
    println("4 * 6 = ${multiply(4, 6)}")
    
    // Function with default parameters
    fun greetWithDefault(name: String = "World") {
        println("Hello, $name!")
    }
    greetWithDefault()
    greetWithDefault("Kotlin")
    
    // Higher-order function
    fun operateOnNumbers(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
        return operation(a, b)
    }
    
    val sum = operateOnNumbers(10, 5) { x, y -> x + y }
    val product = operateOnNumbers(10, 5) { x, y -> x * y }
    println("Sum: $sum, Product: $product")
    
    println()
}

fun classExamples() {
    println("5. Class Examples:")
    
    // Basic class
    class Person(val name: String, var age: Int) {
        init {
            println("Person $name created")
        }
        
        fun introduce() {
            println("Hi, I'm $name and I'm $age years old")
        }
    }
    
    val person = Person("Alice", 25)
    person.introduce()
    
    // Data class
    data class Book(val title: String, val author: String, val year: Int) {
        fun isClassic(): Boolean = (2024 - year) > 50
    }
    
    val book = Book("1984", "George Orwell", 1949)
    println("Book: ${book.title} by ${book.author}")
    println("Is classic: ${book.isClassic()}")
    
    // Object (Singleton)
    object Database {
        fun connect() {
            println("Connected to database")
        }
    }
    Database.connect()
    
    println()
}

fun collectionExamples() {
    println("6. Collection Examples:")
    
    // Lists
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println("Original list: $numbers")
    
    val doubled = numbers.map { it * 2 }
    println("Doubled: $doubled")
    
    val evenNumbers = numbers.filter { it % 2 == 0 }
    println("Even numbers: $evenNumbers")
    
    val sum = numbers.reduce { acc, num -> acc + num }
    println("Sum: $sum")
    
    // Maps
    val fruits = mapOf("apple" to "red", "banana" to "yellow", "grape" to "purple")
    println("Fruits: $fruits")
    
    fruits.forEach { (fruit, color) ->
        println("$fruit is $color")
    }
    
    println()
}

fun nullSafetyExamples() {
    println("7. Null Safety Examples:")
    
    // Nullable types
    var nullableString: String? = "Hello"
    println("Nullable string: $nullableString")
    
    nullableString = null
    println("After setting to null: $nullableString")
    
    // Safe calls
    val length = nullableString?.length
    println("Length (safe call): $length")
    
    // Elvis operator
    val displayName = nullableString ?: "Unknown"
    println("Display name: $displayName")
    
    // Safe call with default
    val lengthOrDefault = nullableString?.length ?: 0
    println("Length or default: $lengthOrDefault")
    
    println()
}

fun extensionExamples() {
    println("8. Extension Examples:")
    
    // Extension function
    fun String.addExclamation() = "$this!"
    
    val greeting = "Hello".addExclamation()
    println("Extended greeting: $greeting")
    
    // Extension property
    val String.isPalindrome: Boolean
        get() = this == this.reversed()
    
    println("'racecar' is palindrome: ${"racecar".isPalindrome}")
    println("'hello' is palindrome: ${"hello".isPalindrome}")
    
    println()
}

fun practiceExercises() {
    println("9. Practice Exercises:")
    
    // Exercise 1: Sum even numbers
    fun sumEvenNumbers(numbers: List<Int>): Int {
        return numbers.filter { it % 2 == 0 }.sum()
    }
    
    val testNumbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println("Sum of even numbers in $testNumbers: ${sumEvenNumbers(testNumbers)}")
    
    // Exercise 2: Book class
    data class Book(val title: String, val author: String, val year: Int) {
        fun isClassic(): Boolean = (2024 - year) > 50
    }
    
    val books = listOf(
        Book("1984", "George Orwell", 1949),
        Book("The Great Gatsby", "F. Scott Fitzgerald", 1925),
        Book("Harry Potter", "J.K. Rowling", 1997)
    )
    
    books.forEach { book ->
        println("${book.title} (${book.year}) - Classic: ${book.isClassic()}")
    }
    
    // Exercise 3: Null safety
    fun getFirstChar(str: String?): Char? {
        return str?.firstOrNull()
    }
    
    println("First char of 'Hello': ${getFirstChar("Hello")}")
    println("First char of null: ${getFirstChar(null)}")
    
    // Exercise 4: Group words by first letter
    fun groupWordsByFirstLetter(words: List<String>): Map<Char, List<String>> {
        return words.groupBy { it.firstOrNull() ?: ' ' }
    }
    
    val words = listOf("apple", "banana", "cherry", "date", "elderberry")
    val grouped = groupWordsByFirstLetter(words)
    println("Words grouped by first letter: $grouped")
    
    println("\n=== End of Examples ===")
}

// Additional practice functions you can experiment with:

/**
 * Exercise: Create a function that finds the longest word in a list
 */
fun findLongestWord(words: List<String>): String? {
    return words.maxByOrNull { it.length }
}

/**
 * Exercise: Create a function that checks if a number is prime
 */
fun isPrime(number: Int): Boolean {
    if (number < 2) return false
    for (i in 2 until number) {
        if (number % i == 0) return false
    }
    return true
}

/**
 * Exercise: Create a function that reverses a string
 */
fun reverseString(str: String): String {
    return str.reversed()
}

/**
 * Exercise: Create a function that counts vowels in a string
 */
fun countVowels(str: String): Int {
    val vowels = setOf('a', 'e', 'i', 'o', 'u')
    return str.lowercase().count { it in vowels }
}