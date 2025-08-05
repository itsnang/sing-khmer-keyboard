/*
 * Supporting classes for keyboard functionality
 * Adapted from FlorisBoard for SingKhmer Keyboard
 */

package com.singkhmer.florisboard.keyboard

import kotlinx.serialization.Serializable

/**
 * Computing evaluator interface for key computation
 */
interface ComputingEvaluator {
    fun isShiftPressed(): Boolean
    fun isCapsLockEnabled(): Boolean
    fun getCurrentInputMode(): InputMode
    fun getActiveSubtype(): KeyboardSubtype?
}

/**
 * Input modes for the keyboard
 */
enum class InputMode {
    TEXT,
    NUMERIC,
    SYMBOLS,
    PHONE,
    EMAIL,
    URL
}

/**
 * Keyboard subtype definition
 */
@Serializable
data class KeyboardSubtype(
    val id: String,
    val displayName: String,
    val locale: String,
    val script: String,
    val layoutType: LayoutType = LayoutType.CHARACTERS
)

/**
 * Layout types
 */
enum class LayoutType {
    CHARACTERS,
    SYMBOLS,
    SYMBOLS2,
    NUMERIC,
    NUMERIC_ADVANCED,
    PHONE,
    PHONE2
}

/**
 * Popup set for key popups
 */
@Serializable
data class PopupSet<T : AbstractKeyData>(
    val main: T? = null,
    val relevant: List<T> = emptyList()
) {
    fun isEmpty(): Boolean = main == null && relevant.isEmpty()
    fun isNotEmpty(): Boolean = !isEmpty()
    
    fun getAllPopups(): List<T> {
        val result = mutableListOf<T>()
        main?.let { result.add(it) }
        result.addAll(relevant)
        return result
    }
}

/**
 * Key codes for special keys (negative values to avoid conflicts with Unicode)
 */
object KeyCode {
    const val UNSPECIFIED = 0
    const val SPACE = 32
    const val ENTER = 10
    const val TAB = 9
    
    // Special function keys (negative values)
    const val BACKSPACE = -1
    const val DELETE = -2
    const val SHIFT = -3
    const val CAPS_LOCK = -4
    const val CTRL = -5
    const val ALT = -6
    const val META = -7
    const val SYM = -8
    const val NUM = -9
    const val SWITCH_TO_TEXT_CONTEXT = -10
    const val SWITCH_TO_MEDIA_CONTEXT = -11
    const val SWITCH_TO_CLIPBOARD_CONTEXT = -12
    const val TOGGLE_INCOGNITO_MODE = -13
    const val TOGGLE_ONE_HANDED_MODE = -14
    const val HIDE_KEYBOARD = -15
    const val SETTINGS = -16
    const val LANGUAGE_SWITCH = -17
    
    // Khmer-specific keys
    const val KHMER_COENG = -100
    const val KHMER_ROBAT = -101
    const val KHMER_TRIISAP = -102
    const val KHMER_MUUSIKATOAN = -103
}

/**
 * Key variation for different key states
 */
enum class KeyVariation {
    NORMAL,
    SHIFTED,
    CAPS_LOCK,
    ALT,
    ALT_SHIFTED
}

/**
 * Input shift state
 */
enum class InputShiftState {
    UNSHIFTED,
    SHIFTED_MANUAL,
    SHIFTED_AUTOMATIC,
    CAPS_LOCK
}

/**
 * Capitalization behavior
 */
enum class CapitalizationBehavior {
    NONE,
    WORDS,
    SENTENCES
}