# SingKhmer Keyboard

A modern Android keyboard application with advanced Khmer language support, featuring Roman-to-Khmer transliteration and a modular architecture based on FlorisBoard components.

## Features

### 🇰🇭 Khmer Language Support
- **Native Khmer Input**: Full Unicode Khmer character support
- **Roman-to-Khmer Transliteration**: Type in Roman characters and get Khmer output
- **Intelligent Character Mapping**: Advanced transliteration engine with context-aware conversion
- **Khmer-optimized Layouts**: Keyboard layouts designed specifically for Khmer input

### ⌨️ Advanced Keyboard Features
- **Modular Architecture**: Clean, maintainable codebase based on FlorisBoard
- **Haptic Feedback**: Configurable vibration feedback for enhanced typing experience
- **Multi-layout Support**: Character, numeric, and symbol layouts
- **Long Press Actions**: Access alternative characters and special functions
- **Gesture Support**: Swipe gestures for cursor movement and shortcuts

### 🔧 Developer-Friendly
- **Extensible Design**: Easy to add new languages and input methods
- **Comprehensive Testing**: Unit tests for core functionality
- **Clean Architecture**: SOLID principles and separation of concerns
- **Well-Documented**: Extensive documentation and code comments

## Architecture

The project follows a modular architecture with clear separation of concerns:

```
sing-khmer-keyboard/
├── android-keyboard/          # Main Android application
├── transliterator-core/       # Core transliteration engine
├── transliterator-cli/        # Command-line transliteration tool
└── florisboard-integration/   # Keyboard framework components
```

### Module Overview

#### 1. `android-keyboard`
The main Android application module containing:
- **SingKhmerInputMethodService**: Core keyboard service
- **UI Components**: Keyboard views and suggestion strips
- **Android Integration**: Input method framework integration
- **User Interface**: Settings and configuration screens

#### 2. `transliterator-core`
The transliteration engine providing:
- **Character Mapping**: Roman-to-Khmer character conversion rules
- **Context Analysis**: Intelligent transliteration based on context
- **Rule Engine**: Configurable transliteration rules
- **Performance Optimization**: Efficient string processing

#### 3. `transliterator-cli`
Command-line interface for:
- **Testing Transliteration**: Validate transliteration rules
- **Batch Processing**: Convert text files
- **Development Tools**: Debug and test transliteration logic

#### 4. `florisboard-integration`
Keyboard framework components adapted from FlorisBoard:
- **Keyboard Management**: Layout switching and key processing
- **Input Processing**: Event handling and feedback
- **Extensible Architecture**: Support for multiple languages and layouts
- **Advanced Features**: Gestures, popups, and haptic feedback

## Getting Started

### Prerequisites

- **Android Studio**: Arctic Fox (2020.3.1) or later
- **JDK**: Java 17 or later
- **Android SDK**: API level 21 (Android 5.0) or later
- **Kotlin**: 1.9.0 or later

### Building the Project

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/sing-khmer-keyboard.git
   cd sing-khmer-keyboard
   ```

2. **Open in Android Studio**:
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Build the project**:
   ```bash
   ./gradlew build
   ```

4. **Run tests**:
   ```bash
   ./gradlew test
   ```

### Installation

1. **Build the APK**:
   ```bash
   ./gradlew assembleDebug
   ```

2. **Install on device**:
   ```bash
   ./gradlew installDebug
   ```

3. **Enable the keyboard**:
   - Go to Settings > System > Languages & input > Virtual keyboard
   - Add "SingKhmer Keyboard"
   - Set as default keyboard

## Usage

### Basic Typing

1. **Switch to SingKhmer Keyboard**: Tap the keyboard switcher icon
2. **Type in Roman**: Type Roman characters (e.g., "chomreab suor")
3. **Get Khmer Output**: The keyboard automatically converts to Khmer (ជំរាបសួរ)

### Advanced Features

#### Transliteration Examples

| Roman Input | Khmer Output | Meaning |
|-------------|--------------|----------|
| `chomreab suor` | ជំរាបសួរ | Hello |
| `arkun` | អរគុណ | Thank you |
| `kampuchea` | កម្ពុជា | Cambodia |
| `phnom penh` | ភ្នំពេញ | Phnom Penh |

#### Keyboard Shortcuts

- **Long press keys**: Access alternative characters
- **Swipe left**: Delete word
- **Swipe right**: Move cursor
- **Long press space**: Switch languages
- **Long press enter**: Access emoji

### Configuration

Access keyboard settings through:
1. Settings > System > Languages & input > Virtual keyboard > SingKhmer Keyboard
2. Or long press the settings key on the keyboard

Available options:
- **Haptic Feedback**: Enable/disable vibration
- **Sound Feedback**: Enable/disable key sounds
- **Auto-capitalization**: Automatic sentence capitalization
- **Transliteration Mode**: Toggle Roman-to-Khmer conversion

## Development

### Project Structure

```
android-keyboard/
├── src/main/java/com/singkhmer/keyboard/
│   ├── SingKhmerInputMethodService.kt    # Main keyboard service
│   ├── KeyboardView.kt                   # Custom keyboard view
│   ├── SuggestionStripView.kt           # Suggestion display
│   └── SettingsActivity.kt              # Keyboard settings
├── src/main/res/
│   ├── layout/                          # UI layouts
│   ├── values/                          # Strings and styles
│   └── xml/                             # Input method configuration
└── src/test/java/                       # Unit tests

transliterator-core/
├── src/main/kotlin/com/singkhmer/transliterator/
│   ├── Transliterator.kt                # Main transliteration engine
│   ├── CharacterMap.kt                  # Character mapping rules
│   ├── RuleEngine.kt                    # Transliteration rule processing
│   └── ContextAnalyzer.kt               # Context-aware conversion
└── src/test/kotlin/                     # Unit tests

florisboard-integration/
├── src/main/kotlin/com/singkhmer/florisboard/
│   ├── KeyData.kt                       # Key data structures
│   ├── KeyboardLayout.kt                # Layout definitions
│   ├── KeyboardManager.kt               # Layout management
│   ├── InputEventDispatcher.kt          # Input event handling
│   └── KeyboardSupport.kt               # Supporting classes
└── src/test/kotlin/                     # Unit tests
```

### Adding New Features

#### Adding a New Language

1. **Create character mappings** in `transliterator-core`:
   ```kotlin
   object NewLanguageMap {
       val characterMap = mapOf(
           "a" to "ា",
           "i" to "ិ",
           // ... more mappings
       )
   }
   ```

2. **Create keyboard layout** in `florisboard-integration`:
   ```kotlin
   object NewLanguageLayouts {
       fun createLayout(): KeyboardLayout {
           return KeyboardLayout(
               type = LayoutType.CHARACTERS,
               name = "New Language",
               rows = listOf(/* keyboard rows */)
           )
       }
   }
   ```

3. **Update keyboard manager** to support the new language

#### Adding New Input Methods

1. **Extend KeyData** for custom key types:
   ```kotlin
   data class CustomKeyData(
       override val type: KeyType,
       override val code: Int,
       override val label: String,
       val customProperty: String
   ) : KeyData
   ```

2. **Update InputEventDispatcher** to handle custom keys

3. **Add processing logic** in the main service

### Testing

The project includes comprehensive tests:

#### Unit Tests
```bash
# Run all tests
./gradlew test

# Run specific module tests
./gradlew :transliterator-core:test
./gradlew :florisboard-integration:test
./gradlew :android-keyboard:testDebugUnitTest
```

#### Integration Tests
```bash
# Run Android instrumentation tests
./gradlew connectedAndroidTest
```

#### Manual Testing
1. Install the keyboard on a test device
2. Test basic typing functionality
3. Verify transliteration accuracy
4. Test special keys and gestures
5. Validate settings and configuration

### Code Style

The project follows Kotlin coding conventions:

- **Naming**: Use camelCase for functions and variables, PascalCase for classes
- **Formatting**: Use 4-space indentation, 120-character line limit
- **Documentation**: Document public APIs with KDoc
- **Architecture**: Follow SOLID principles and clean architecture

## Contributing

We welcome contributions! Please follow these guidelines:

### Getting Started

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make your changes**
4. **Add tests** for new functionality
5. **Ensure tests pass**: `./gradlew test`
6. **Commit changes**: `git commit -m 'Add amazing feature'`
7. **Push to branch**: `git push origin feature/amazing-feature`
8. **Open a Pull Request**

### Contribution Guidelines

- **Code Quality**: Follow existing code style and architecture
- **Testing**: Add unit tests for new features
- **Documentation**: Update documentation for API changes
- **Compatibility**: Ensure backward compatibility
- **Performance**: Consider performance implications

### Areas for Contribution

- **Language Support**: Add support for new languages
- **UI/UX**: Improve keyboard design and user experience
- **Performance**: Optimize transliteration and input processing
- **Accessibility**: Enhance accessibility features
- **Testing**: Improve test coverage and quality
- **Documentation**: Enhance documentation and examples

## Roadmap

### Version 1.1
- [ ] Improved Khmer transliteration accuracy
- [ ] Additional keyboard themes
- [ ] Voice input support
- [ ] Enhanced gesture recognition

### Version 1.2
- [ ] Predictive text suggestions
- [ ] Multi-language support (Thai, Lao)
- [ ] Cloud sync for user preferences
- [ ] Advanced customization options

### Version 2.0
- [ ] AI-powered text prediction
- [ ] Handwriting recognition
- [ ] Advanced accessibility features
- [ ] Plugin architecture for third-party extensions

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **FlorisBoard**: This project adapts components from the excellent [FlorisBoard](https://github.com/florisboard/florisboard) open-source keyboard
- **Khmer Unicode**: Thanks to the Unicode Consortium for Khmer character standardization
- **Android Open Source Project**: For the input method framework
- **Contributors**: All the developers who have contributed to this project

## Support

- **Issues**: Report bugs and request features on [GitHub Issues](https://github.com/yourusername/sing-khmer-keyboard/issues)
- **Discussions**: Join discussions on [GitHub Discussions](https://github.com/yourusername/sing-khmer-keyboard/discussions)
- **Email**: Contact us at support@singkhmer.com

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed history of changes.