/*
 * Keyboard Manager for SingKhmer Keyboard
 * Handles layout management and key processing
 * Adapted from FlorisBoard architecture
 */

package com.singkhmer.florisboard.keyboard

import android.content.Context
import android.view.inputmethod.InputConnection

/**
 * Main keyboard manager that handles layout switching and key processing
 */
class KeyboardManager(private val context: Context) : ComputingEvaluator {
    
    private var currentLayout: KeyboardLayout = KhmerKeyboardLayouts.createKhmerLayout()
    private var currentInputMode: InputMode = InputMode.TEXT
    private var shiftState: InputShiftState = InputShiftState.UNSHIFTED
    private var capsLockEnabled: Boolean = false
    private var activeSubtype: KeyboardSubtype? = null
    
    // Listeners
    private var keyPressListener: ((KeyData) -> Unit)? = null
    private var layoutChangeListener: ((KeyboardLayout) -> Unit)? = null
    
    init {
        // Initialize with default Khmer subtype
        activeSubtype = KeyboardSubtype(
            id = "khmer_kh",
            displayName = "ខ្មែរ",
            locale = "km_KH",
            script = "Khmr"
        )
    }
    
    /**
     * Sets the key press listener
     */
    fun setKeyPressListener(listener: (KeyData) -> Unit) {
        keyPressListener = listener
    }
    
    /**
     * Sets the layout change listener
     */
    fun setLayoutChangeListener(listener: (KeyboardLayout) -> Unit) {
        layoutChangeListener = listener
    }
    
    /**
     * Gets the current keyboard layout
     */
    fun getCurrentLayout(): KeyboardLayout = currentLayout
    
    /**
     * Processes a key press
     */
    fun processKeyPress(keyData: KeyData, inputConnection: InputConnection?) {
        when (keyData) {
            is KhmerKeyData -> {
                handleKhmerKey(keyData, inputConnection)
            }
            is TextKeyData -> {
                handleTextKey(keyData, inputConnection)
            }
            is FunctionKeyData -> {
                handleFunctionKey(keyData, inputConnection)
            }
        }
        
        // Notify listener
        keyPressListener?.invoke(keyData)
    }
    
    /**
     * Handles Khmer character input
     */
    private fun handleKhmerKey(keyData: KhmerKeyData, inputConnection: InputConnection?) {
        val textToCommit = when (shiftState) {
            InputShiftState.SHIFTED_MANUAL,
            InputShiftState.SHIFTED_AUTOMATIC -> {
                // Apply shift transformation if needed
                keyData.khmerChar.uppercase()
            }
            InputShiftState.CAPS_LOCK -> {
                keyData.khmerChar.uppercase()
            }
            else -> keyData.khmerChar
        }
        
        inputConnection?.commitText(textToCommit, 1)
        
        // Reset shift state if it was manual
        if (shiftState == InputShiftState.SHIFTED_MANUAL) {
            shiftState = InputShiftState.UNSHIFTED
        }
    }
    
    /**
     * Handles regular text input
     */
    private fun handleTextKey(keyData: TextKeyData, inputConnection: InputConnection?) {
        val textToCommit = keyData.asString(false)
        inputConnection?.commitText(textToCommit, 1)
    }
    
    /**
     * Handles function key actions
     */
    private fun handleFunctionKey(keyData: FunctionKeyData, inputConnection: InputConnection?) {
        when (keyData.action) {
            KeyboardAction.BACKSPACE -> {
                inputConnection?.deleteSurroundingText(1, 0)
            }
            KeyboardAction.DELETE -> {
                inputConnection?.deleteSurroundingText(0, 1)
            }
            KeyboardAction.ENTER -> {
                inputConnection?.commitText("\n", 1)
            }
            KeyboardAction.SPACE -> {
                inputConnection?.commitText(" ", 1)
            }
            KeyboardAction.SHIFT -> {
                toggleShift()
            }
            KeyboardAction.CAPS_LOCK -> {
                toggleCapsLock()
            }
            KeyboardAction.SWITCH_TO_NUMBERS -> {
                switchToLayout(LayoutType.NUMERIC)
            }
            KeyboardAction.SWITCH_TO_LETTERS -> {
                switchToLayout(LayoutType.CHARACTERS)
            }
            KeyboardAction.SWITCH_TO_SYMBOLS -> {
                switchToLayout(LayoutType.SYMBOLS)
            }
            KeyboardAction.LANGUAGE_SWITCH -> {
                // Handle language switching
                switchLanguage()
            }
            KeyboardAction.HIDE_KEYBOARD -> {
                // This should be handled by the IME service
            }
            KeyboardAction.SETTINGS -> {
                // This should be handled by the IME service
            }
        }
    }
    
    /**
     * Toggles shift state
     */
    private fun toggleShift() {
        shiftState = when (shiftState) {
            InputShiftState.UNSHIFTED -> InputShiftState.SHIFTED_MANUAL
            InputShiftState.SHIFTED_MANUAL -> InputShiftState.UNSHIFTED
            InputShiftState.SHIFTED_AUTOMATIC -> InputShiftState.UNSHIFTED
            InputShiftState.CAPS_LOCK -> InputShiftState.UNSHIFTED
        }
        capsLockEnabled = false
    }
    
    /**
     * Toggles caps lock
     */
    private fun toggleCapsLock() {
        capsLockEnabled = !capsLockEnabled
        shiftState = if (capsLockEnabled) {
            InputShiftState.CAPS_LOCK
        } else {
            InputShiftState.UNSHIFTED
        }
    }
    
    /**
     * Switches to a different layout type
     */
    private fun switchToLayout(layoutType: LayoutType) {
        currentLayout = when (layoutType) {
            LayoutType.CHARACTERS -> KhmerKeyboardLayouts.createKhmerLayout()
            LayoutType.NUMERIC -> KhmerKeyboardLayouts.createNumericLayout()
            else -> currentLayout // Keep current layout for unsupported types
        }
        currentInputMode = when (layoutType) {
            LayoutType.CHARACTERS -> InputMode.TEXT
            LayoutType.NUMERIC -> InputMode.NUMERIC
            LayoutType.SYMBOLS -> InputMode.SYMBOLS
            else -> currentInputMode
        }
        
        layoutChangeListener?.invoke(currentLayout)
    }
    
    /**
     * Switches between available languages/subtypes
     */
    private fun switchLanguage() {
        // For now, just toggle between Khmer and English
        // This can be expanded to support multiple languages
        activeSubtype = if (activeSubtype?.id == "khmer_kh") {
            KeyboardSubtype(
                id = "english_us",
                displayName = "English",
                locale = "en_US",
                script = "Latn"
            )
        } else {
            KeyboardSubtype(
                id = "khmer_kh",
                displayName = "ខ្មែរ",
                locale = "km_KH",
                script = "Khmr"
            )
        }
        
        // Switch layout based on active subtype
        switchToLayout(LayoutType.CHARACTERS)
    }
    
    // ComputingEvaluator implementation
    override fun isShiftPressed(): Boolean {
        return shiftState == InputShiftState.SHIFTED_MANUAL || 
               shiftState == InputShiftState.SHIFTED_AUTOMATIC
    }
    
    override fun isCapsLockEnabled(): Boolean {
        return capsLockEnabled
    }
    
    override fun getCurrentInputMode(): InputMode {
        return currentInputMode
    }
    
    override fun getActiveSubtype(): KeyboardSubtype? {
        return activeSubtype
    }
    
    /**
     * Gets the current shift state
     */
    fun getShiftState(): InputShiftState = shiftState
    
    /**
     * Sets automatic shift for sentence beginnings
     */
    fun setAutomaticShift(enabled: Boolean) {
        if (enabled && shiftState == InputShiftState.UNSHIFTED) {
            shiftState = InputShiftState.SHIFTED_AUTOMATIC
        }
    }
    
    /**
     * Resets the keyboard state
     */
    fun reset() {
        shiftState = InputShiftState.UNSHIFTED
        capsLockEnabled = false
        currentInputMode = InputMode.TEXT
    }
}