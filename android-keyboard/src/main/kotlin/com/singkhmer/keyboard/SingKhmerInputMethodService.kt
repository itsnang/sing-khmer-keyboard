package com.singkhmer.keyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import com.singkhmer.transliterator.Transliterator
import com.singkhmer.florisboard.keyboard.KeyboardManager
import com.singkhmer.florisboard.keyboard.KeyData
import com.singkhmer.florisboard.keyboard.KhmerKeyboardLayouts
import com.singkhmer.florisboard.input.InputEventDispatcher

class SingKhmerInputMethodService : InputMethodService() {
    
    private lateinit var keyboardView: View
    private lateinit var suggestionStrip: LinearLayout
    private lateinit var suggestion1: TextView
    private lateinit var suggestion2: TextView
    private lateinit var suggestion3: TextView
    
    private val transliterator = Transliterator()
    private var currentWord = StringBuilder()
    private var isCapitalized = false
    
    // FlorisBoard integration components
    private lateinit var keyboardManager: KeyboardManager
    private lateinit var inputEventDispatcher: InputEventDispatcher
    
    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null)
        initializeFlorisBoard()
        initializeViews()
        setupKeyListeners()
        return keyboardView
    }
    
    private fun initializeFlorisBoard() {
        try {
            // Initialize FlorisBoard components
            keyboardManager = KeyboardManager(this)
            inputEventDispatcher = InputEventDispatcher(this)
            
            // Connect components
            inputEventDispatcher.setKeyboardManager(keyboardManager)
            // Set input connection only if available
            currentInputConnection?.let {
                inputEventDispatcher.setInputConnection(it)
            }
            
            // Set up listeners
            keyboardManager.setKeyPressListener { keyData ->
                handleFlorisKeyPress(keyData)
            }
            
            keyboardManager.setLayoutChangeListener { layout ->
                // Handle layout changes if needed
                updateKeyboardLayout(layout)
            }
        } catch (e: Exception) {
            // Log error but don't crash
            android.util.Log.e("SingKhmer", "Error initializing FlorisBoard", e)
        }
    }
    
    private fun initializeViews() {
        try {
            suggestionStrip = keyboardView.findViewById(R.id.suggestion_strip)
            suggestion1 = keyboardView.findViewById(R.id.suggestion_1)
            suggestion2 = keyboardView.findViewById(R.id.suggestion_2)
            suggestion3 = keyboardView.findViewById(R.id.suggestion_3)
            
            // Setup suggestion click listeners with null checks
            suggestion1?.setOnClickListener { commitSuggestion(suggestion1.text.toString()) }
            suggestion2?.setOnClickListener { commitSuggestion(suggestion2.text.toString()) }
            suggestion3?.setOnClickListener { commitSuggestion(suggestion3.text.toString()) }
        } catch (e: Exception) {
            android.util.Log.e("SingKhmer", "Error initializing views", e)
        }
    }
    
    private fun setupKeyListeners() {
        // Number row
        setupKeyListener(R.id.key_1, "1")
        setupKeyListener(R.id.key_2, "2")
        setupKeyListener(R.id.key_3, "3")
        setupKeyListener(R.id.key_4, "4")
        setupKeyListener(R.id.key_5, "5")
        setupKeyListener(R.id.key_6, "6")
        setupKeyListener(R.id.key_7, "7")
        setupKeyListener(R.id.key_8, "8")
        setupKeyListener(R.id.key_9, "9")
        setupKeyListener(R.id.key_0, "0")
        
        // Setup letter keys by finding all TextViews and matching their text
        val mainKeyboard = keyboardView.findViewById<LinearLayout>(R.id.main_keyboard)
        setupLetterKeysFromLayout(mainKeyboard)
        
        // Special keys
        keyboardView.findViewById<View>(R.id.key_space)?.setOnClickListener {
            handleSpaceKey()
        }
        
        keyboardView.findViewById<View>(R.id.key_backspace)?.setOnClickListener {
            handleBackspace()
        }
        
        keyboardView.findViewById<View>(R.id.key_enter)?.setOnClickListener {
            handleEnterKey()
        }
        
        keyboardView.findViewById<View>(R.id.key_shift)?.setOnClickListener {
            handleShiftKey()
        }
    }
    
    private fun setupKeyListener(keyId: Int, character: String) {
        keyboardView.findViewById<View>(keyId)?.setOnClickListener {
            commitCurrentWord()
            currentInputConnection?.commitText(character, 1)
        }
    }
    
    private fun setupLetterKeysFromLayout(layout: LinearLayout) {
        val letters = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
                            "a", "s", "d", "f", "g", "h", "j", "k", "l",
                            "z", "x", "c", "v", "b", "n", "m")
        
        val allTextViews = layout.getAllTextViews()
        
        letters.forEach { letter ->
            val textView = allTextViews.find { 
                it.text.toString().equals(letter, ignoreCase = true) &&
                it.id != R.id.key_shift &&
                it.id != R.id.key_backspace &&
                it.id != R.id.key_space &&
                it.id != R.id.key_enter &&
                it.id != R.id.key_symbols &&
                it.id != R.id.key_language
            }
            textView?.setOnClickListener {
                handleLetterInput(letter)
            }
        }
    }
    
    private fun LinearLayout.getAllTextViews(): List<TextView> {
        val textViews = mutableListOf<TextView>()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is TextView) {
                textViews.add(child)
            } else if (child is LinearLayout) {
                textViews.addAll(child.getAllTextViews())
            }
        }
        return textViews
    }
    
    private fun handleLetterInput(letter: String) {
        val actualLetter = if (isCapitalized) letter.uppercase() else letter.lowercase()
        currentWord.append(actualLetter)
        
        // Show the letter being typed
        currentInputConnection?.setComposingText(currentWord.toString(), 1)
        
        // Update suggestions
        updateSuggestions()
        
        // Reset capitalization after first letter
        if (isCapitalized) {
            isCapitalized = false
        }
    }
    
    private fun handleSpaceKey() {
        if (currentWord.isNotEmpty()) {
            // If there are suggestions, commit the first one
            if (suggestion1.text.isNotEmpty()) {
                commitSuggestion(suggestion1.text.toString())
            } else {
                commitCurrentWord()
            }
        }
        currentInputConnection?.commitText(" ", 1)
    }
    
    private fun handleBackspace() {
        if (currentWord.isNotEmpty()) {
            currentWord.deleteCharAt(currentWord.length - 1)
            if (currentWord.isEmpty()) {
                currentInputConnection?.finishComposingText()
                clearSuggestions()
            } else {
                currentInputConnection?.setComposingText(currentWord.toString(), 1)
                updateSuggestions()
            }
        } else {
            currentInputConnection?.deleteSurroundingText(1, 0)
        }
    }
    
    private fun handleEnterKey() {
        commitCurrentWord()
        currentInputConnection?.sendKeyEvent(
            android.view.KeyEvent(
                android.view.KeyEvent.ACTION_DOWN,
                android.view.KeyEvent.KEYCODE_ENTER
            )
        )
        currentInputConnection?.sendKeyEvent(
            android.view.KeyEvent(
                android.view.KeyEvent.ACTION_UP,
                android.view.KeyEvent.KEYCODE_ENTER
            )
        )
    }
    
    private fun handleShiftKey() {
        isCapitalized = !isCapitalized
        // Update shift key appearance if needed
    }
    
    private fun updateSuggestions() {
        if (currentWord.isEmpty()) {
            clearSuggestions()
            return
        }
        
        try {
            val suggestions = transliterator.suggestTop3(currentWord.toString())
            
            suggestion1.text = suggestions.getOrNull(0) ?: ""
            suggestion2.text = suggestions.getOrNull(1) ?: ""
            suggestion3.text = suggestions.getOrNull(2) ?: ""
            
            // Show suggestion strip if we have suggestions
            suggestionStrip.visibility = if (suggestions.isNotEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        } catch (e: Exception) {
            // Handle any transliteration errors
            clearSuggestions()
        }
    }
    
    private fun clearSuggestions() {
        suggestion1.text = ""
        suggestion2.text = ""
        suggestion3.text = ""
        suggestionStrip.visibility = View.GONE
    }
    
    private fun commitSuggestion(suggestion: String) {
        if (suggestion.isNotEmpty()) {
            currentInputConnection?.finishComposingText()
            currentInputConnection?.commitText(suggestion, 1)
            
            // Learn from user selection
            try {
                transliterator.incrementFrequency(currentWord.toString(), suggestion)
            } catch (e: Exception) {
                // Handle learning errors silently
            }
            
            currentWord.clear()
            clearSuggestions()
        }
    }
    
    private fun commitCurrentWord() {
        if (currentWord.isNotEmpty()) {
            currentInputConnection?.finishComposingText()
            currentInputConnection?.commitText(currentWord.toString(), 1)
            currentWord.clear()
            clearSuggestions()
        }
    }
    
    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        currentWord.clear()
        clearSuggestions()
        isCapitalized = false
    }
    
    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        commitCurrentWord()
    }
    
    /**
     * Handles key presses from the FlorisBoard integration
     */
    private fun handleFlorisKeyPress(keyData: KeyData) {
        when (keyData.type) {
            com.singkhmer.florisboard.keyboard.KeyType.CHARACTER -> {
                if (keyData is com.singkhmer.florisboard.keyboard.KhmerKeyData) {
                    handleKhmerInput(keyData)
                } else {
                    handleLetterInput(keyData.asString(false))
                }
            }
            com.singkhmer.florisboard.keyboard.KeyType.FUNCTION -> {
                handleFunctionKey(keyData)
            }
            else -> {
                // Handle other key types as needed
            }
        }
    }
    
    /**
     * Handles Khmer character input with transliteration support
     */
    private fun handleKhmerInput(khmerKeyData: com.singkhmer.florisboard.keyboard.KhmerKeyData) {
        // Add the roman equivalent to current word for transliteration
        currentWord.append(khmerKeyData.getRomanEquivalent())
        
        // Update suggestions based on current word
        updateSuggestions()
        
        // The actual character commitment is handled by the KeyboardManager
    }
    
    /**
     * Handles function key presses
     */
    private fun handleFunctionKey(keyData: KeyData) {
        when ((keyData as com.singkhmer.florisboard.keyboard.FunctionKeyData).action) {
            com.singkhmer.florisboard.keyboard.KeyboardAction.SPACE -> {
                commitCurrentWord()
                handleSpaceKey()
            }
            com.singkhmer.florisboard.keyboard.KeyboardAction.BACKSPACE -> {
                handleBackspace()
            }
            com.singkhmer.florisboard.keyboard.KeyboardAction.ENTER -> {
                commitCurrentWord()
                handleEnterKey()
            }
            com.singkhmer.florisboard.keyboard.KeyboardAction.SHIFT -> {
                handleShiftKey()
            }
            else -> {
                // Other function keys are handled by the KeyboardManager
            }
        }
    }
    
    /**
     * Updates the keyboard layout when FlorisBoard changes layouts
     */
    private fun updateKeyboardLayout(layout: com.singkhmer.florisboard.keyboard.KeyboardLayout) {
        // This method can be used to update the UI when layout changes
        // For now, we'll keep the existing XML-based layout
        // but this provides a hook for future dynamic layout updates
    }
    
    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        // Update input connection for FlorisBoard components
        if (::inputEventDispatcher.isInitialized) {
            inputEventDispatcher.setInputConnection(currentInputConnection)
        }
    }
}