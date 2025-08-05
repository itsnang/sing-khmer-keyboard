/*
 * Input Event Dispatcher for SingKhmer Keyboard
 * Handles input events, feedback, and gesture processing
 * Adapted from FlorisBoard architecture
 */

package com.singkhmer.florisboard.input

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.inputmethod.InputConnection
import com.singkhmer.florisboard.keyboard.KeyData
import com.singkhmer.florisboard.keyboard.KeyboardManager

/**
 * Dispatches input events and manages feedback
 */
class InputEventDispatcher(private val context: Context) {
    
    private var keyboardManager: KeyboardManager? = null
    private var inputConnection: InputConnection? = null
    private var hapticFeedbackEnabled: Boolean = true
    private var soundFeedbackEnabled: Boolean = true
    private var vibrationDuration: Long = 50L
    
    private val vibrator: Vibrator? by lazy {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }
    
    /**
     * Sets the keyboard manager
     */
    fun setKeyboardManager(manager: KeyboardManager) {
        keyboardManager = manager
    }
    
    /**
     * Sets the input connection
     */
    fun setInputConnection(connection: InputConnection?) {
        inputConnection = connection
    }
    
    /**
     * Dispatches a key press event
     */
    fun dispatchKeyPress(keyData: KeyData, view: View? = null) {
        // Provide haptic feedback
        if (hapticFeedbackEnabled) {
            provideHapticFeedback(view)
        }
        
        // Process the key through the keyboard manager
        keyboardManager?.processKeyPress(keyData, inputConnection)
    }
    
    /**
     * Dispatches a key long press event
     */
    fun dispatchKeyLongPress(keyData: KeyData, view: View? = null): Boolean {
        // Provide stronger haptic feedback for long press
        if (hapticFeedbackEnabled) {
            provideHapticFeedback(view, HapticFeedbackConstants.LONG_PRESS)
        }
        
        // Handle long press actions
        return handleLongPressAction(keyData)
    }
    
    /**
     * Dispatches a key repeat event
     */
    fun dispatchKeyRepeat(keyData: KeyData) {
        // Process repeated key presses (e.g., for backspace)
        keyboardManager?.processKeyPress(keyData, inputConnection)
    }
    
    /**
     * Provides haptic feedback
     */
    private fun provideHapticFeedback(
        view: View?,
        feedbackConstant: Int = HapticFeedbackConstants.KEYBOARD_TAP
    ) {
        if (!hapticFeedbackEnabled) return
        
        // Try to use view's haptic feedback first
        if (view?.performHapticFeedback(feedbackConstant) == true) {
            return
        }
        
        // Fallback to vibrator
        vibrator?.let { vib ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createOneShot(
                    vibrationDuration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(vibrationDuration)
            }
        }
    }
    
    /**
     * Handles long press actions
     */
    private fun handleLongPressAction(keyData: KeyData): Boolean {
        // Check if the key has popup alternatives
        keyData.popup?.let { popupSet ->
            if (popupSet.isNotEmpty()) {
                // Show popup with alternatives
                showKeyPopup(keyData, popupSet.getAllPopups())
                return true
            }
        }
        
        // Handle special long press actions
        when (keyData.code) {
            // Backspace long press - delete word
            -1 -> {
                deleteWord()
                return true
            }
            // Space long press - show language switcher
            32 -> {
                showLanguageSwitcher()
                return true
            }
            // Period long press - show punctuation
            46 -> {
                showPunctuationPopup()
                return true
            }
        }
        
        return false
    }
    
    /**
     * Shows key popup with alternatives
     */
    private fun showKeyPopup(keyData: KeyData, alternatives: List<com.singkhmer.florisboard.keyboard.AbstractKeyData>) {
        // This would typically show a popup view with alternative characters
        // For now, we'll just commit the first alternative
        if (alternatives.isNotEmpty()) {
            val firstAlternative = alternatives.first()
            if (firstAlternative is KeyData) {
                keyboardManager?.processKeyPress(firstAlternative, inputConnection)
            }
        }
    }
    
    /**
     * Deletes the current word
     */
    private fun deleteWord() {
        inputConnection?.let { ic ->
            val textBeforeCursor = ic.getTextBeforeCursor(100, 0)
            if (textBeforeCursor != null) {
                val lastSpaceIndex = textBeforeCursor.lastIndexOf(' ')
                val deleteCount = if (lastSpaceIndex >= 0) {
                    textBeforeCursor.length - lastSpaceIndex - 1
                } else {
                    textBeforeCursor.length
                }
                ic.deleteSurroundingText(deleteCount, 0)
            }
        }
    }
    
    /**
     * Shows language switcher
     */
    private fun showLanguageSwitcher() {
        // This would typically show a language selection popup
        // For now, we'll just trigger a language switch
        // This should be handled by the IME service
    }
    
    /**
     * Shows punctuation popup
     */
    private fun showPunctuationPopup() {
        // This would show common punctuation marks
        // For now, we'll just insert a period
        inputConnection?.commitText(".", 1)
    }
    
    /**
     * Enables or disables haptic feedback
     */
    fun setHapticFeedbackEnabled(enabled: Boolean) {
        hapticFeedbackEnabled = enabled
    }
    
    /**
     * Enables or disables sound feedback
     */
    fun setSoundFeedbackEnabled(enabled: Boolean) {
        soundFeedbackEnabled = enabled
    }
    
    /**
     * Sets the vibration duration for haptic feedback
     */
    fun setVibrationDuration(duration: Long) {
        vibrationDuration = duration
    }
    
    /**
     * Handles gesture input (swipe, etc.)
     */
    fun handleGesture(gesture: InputGesture, startX: Float, startY: Float, endX: Float, endY: Float) {
        when (gesture) {
            InputGesture.SWIPE_LEFT -> {
                // Move cursor left
                inputConnection?.commitText("", -1)
            }
            InputGesture.SWIPE_RIGHT -> {
                // Move cursor right
                inputConnection?.commitText("", 1)
            }
            InputGesture.SWIPE_UP -> {
                // Shift or caps
                // This should be handled by the keyboard manager
            }
            InputGesture.SWIPE_DOWN -> {
                // Hide keyboard
                // This should be handled by the IME service
            }
            InputGesture.PINCH_IN -> {
                // Zoom in or increase text size
                // This could be used for accessibility features
            }
            InputGesture.PINCH_OUT -> {
                // Zoom out or decrease text size
                // This could be used for accessibility features
            }
        }
    }
}

/**
 * Input gesture types
 */
enum class InputGesture {
    SWIPE_LEFT,
    SWIPE_RIGHT,
    SWIPE_UP,
    SWIPE_DOWN,
    PINCH_IN,
    PINCH_OUT
}

/**
 * Input feedback modes
 */
enum class InputFeedbackMode {
    NONE,
    HAPTIC_ONLY,
    SOUND_ONLY,
    HAPTIC_AND_SOUND
}