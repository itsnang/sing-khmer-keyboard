# FlorisBoard Integration Module

This module integrates selected components from the [FlorisBoard](https://github.com/florisboard/florisboard) open-source keyboard project into the SingKhmer Keyboard, providing a modular and extensible keyboard architecture.

## Overview

The FlorisBoard integration module provides:

- **Modular Keyboard Architecture**: Clean separation of concerns with dedicated managers for keyboard layouts, input processing, and event handling
- **Khmer Language Support**: Specialized key data structures for Khmer characters with transliteration support
- **Extensible Layout System**: JSON-serializable keyboard layouts that can be easily modified or extended
- **Advanced Input Processing**: Sophisticated input event handling with haptic feedback, gestures, and popup support
- **Multi-language Support**: Framework for supporting multiple keyboard languages and layouts

## Architecture

### Core Components

#### 1. KeyData System (`KeyData.kt`)
- **AbstractKeyData**: Base interface for all key types
- **KeyData**: Interface for keyboard keys with type, code, label, and popup support
- **TextKeyData**: Basic text character keys
- **KhmerKeyData**: Khmer-specific keys with transliteration support
- **FunctionKeyData**: Special function keys (backspace, enter, shift, etc.)

#### 2. Keyboard Layout System (`KeyboardLayout.kt`)
- **KeyboardLayout**: Complete keyboard layout with rows and metadata
- **KeyboardRow**: Row of keys with configurable height
- **KeyboardKey**: Individual key with width/height properties
- **KhmerKeyboardLayouts**: Factory for creating Khmer keyboard layouts

#### 3. Keyboard Manager (`KeyboardManager.kt`)
- **KeyboardManager**: Central manager for layout switching and key processing
- **ComputingEvaluator**: Interface for evaluating keyboard state
- Handles shift states, caps lock, input modes, and language switching
- Processes key presses and manages keyboard state

#### 4. Input Event System (`InputEventDispatcher.kt`)
- **InputEventDispatcher**: Handles input events, feedback, and gestures
- Provides haptic and audio feedback
- Supports long press actions and key popups
- Handles gesture input (swipes, pinches)

#### 5. Supporting Classes (`KeyboardSupport.kt`)
- **InputMode**: Text, numeric, symbols, phone, email, URL modes
- **KeyboardSubtype**: Language/locale definitions
- **PopupSet**: Key popup alternatives
- **KeyCode**: Constants for special keys
- **InputShiftState**: Shift state management

## Integration with SingKhmer Keyboard

The FlorisBoard integration is seamlessly integrated into the existing SingKhmer keyboard:

### 1. Dependency Integration
The `android-keyboard` module includes the `florisboard-integration` module as a dependency:

```kotlin
dependencies {
    implementation(project(":transliterator-core"))
    implementation(project(":florisboard-integration"))
    // ... other dependencies
}
```

### 2. Service Integration
The `SingKhmerInputMethodService` initializes and uses FlorisBoard components:

```kotlin
private lateinit var keyboardManager: KeyboardManager
private lateinit var inputEventDispatcher: InputEventDispatcher

private fun initializeFlorisBoard() {
    keyboardManager = KeyboardManager(this)
    inputEventDispatcher = InputEventDispatcher(this)
    
    inputEventDispatcher.setKeyboardManager(keyboardManager)
    inputEventDispatcher.setInputConnection(currentInputConnection)
}
```

### 3. Key Processing
FlorisBoard handles key processing while maintaining compatibility with the existing transliteration system:

```kotlin
private fun handleFlorisKeyPress(keyData: KeyData) {
    when (keyData.type) {
        KeyType.CHARACTER -> {
            if (keyData is KhmerKeyData) {
                handleKhmerInput(keyData)
            } else {
                handleLetterInput(keyData.asString(false))
            }
        }
        KeyType.FUNCTION -> {
            handleFunctionKey(keyData)
        }
    }
}
```

## Features

### Khmer Language Support
- **Native Khmer Characters**: Full support for Khmer Unicode characters
- **Transliteration Integration**: Roman-to-Khmer transliteration using the existing transliterator-core
- **Khmer-specific Layouts**: Optimized keyboard layouts for Khmer input

### Advanced Input Features
- **Haptic Feedback**: Configurable vibration feedback for key presses
- **Long Press Actions**: Support for key alternatives and special actions
- **Gesture Support**: Swipe gestures for cursor movement and shortcuts
- **Multi-layout Support**: Easy switching between character, numeric, and symbol layouts

### Extensibility
- **Modular Design**: Clean separation allows easy extension and modification
- **Serializable Layouts**: Keyboard layouts can be defined in JSON and loaded dynamically
- **Plugin Architecture**: Framework supports adding new input methods and languages

## Usage Examples

### Creating a Custom Layout

```kotlin
val customLayout = KeyboardLayout(
    type = LayoutType.CHARACTERS,
    name = "Custom Khmer",
    rows = listOf(
        KeyboardRow(
            keys = listOf(
                KeyboardKey(KhmerKeyData("ក", "k", 0x1780, "ក")),
                KeyboardKey(KhmerKeyData("ខ", "kh", 0x1781, "ខ"))
            )
        )
    )
)
```

### Handling Custom Key Actions

```kotlin
keyboardManager.setKeyPressListener { keyData ->
    when (keyData) {
        is KhmerKeyData -> {
            // Handle Khmer input with transliteration
            processKhmerInput(keyData)
        }
        is FunctionKeyData -> {
            // Handle function keys
            processFunctionKey(keyData)
        }
    }
}
```

### Configuring Input Feedback

```kotlin
inputEventDispatcher.setHapticFeedbackEnabled(true)
inputEventDispatcher.setVibrationDuration(50L)
inputEventDispatcher.setSoundFeedbackEnabled(true)
```

## Benefits of Integration

1. **Maintainability**: Modular architecture makes the codebase easier to maintain and extend
2. **Reusability**: Components can be reused across different keyboard implementations
3. **Testability**: Clean interfaces make unit testing straightforward
4. **Performance**: Efficient key processing and layout management
5. **User Experience**: Advanced features like haptic feedback and gestures improve usability
6. **Internationalization**: Framework supports easy addition of new languages

## Future Enhancements

The modular architecture enables future enhancements such as:

- **Dynamic Layout Loading**: Load keyboard layouts from external files or servers
- **Theme Support**: Customizable keyboard themes and styling
- **Advanced Gestures**: More sophisticated gesture recognition
- **Predictive Text**: Integration with advanced text prediction engines
- **Voice Input**: Voice-to-text input support
- **Accessibility**: Enhanced accessibility features

## Contributing

When contributing to the FlorisBoard integration:

1. Follow the existing architectural patterns
2. Maintain compatibility with the existing transliterator-core
3. Add appropriate unit tests for new functionality
4. Update documentation for new features
5. Consider performance implications of changes

## License

This integration module adapts components from FlorisBoard, which is licensed under the Apache License 2.0. The adaptations maintain compatibility with the original license terms.