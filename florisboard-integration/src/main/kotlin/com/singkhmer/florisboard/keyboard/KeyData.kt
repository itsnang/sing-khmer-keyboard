/*
 * Adapted from FlorisBoard for SingKhmer Keyboard
 * Core keyboard data structures for key definitions
 */

package com.singkhmer.florisboard.keyboard

import kotlinx.serialization.Serializable

/**
 * Basic interface for a key data object
 */
interface AbstractKeyData {
    /**
     * Computes a KeyData object for this key data
     */
    fun compute(evaluator: ComputingEvaluator): KeyData?

    /**
     * Returns the data described by this key as a string
     */
    fun asString(isForDisplay: Boolean): String
}

/**
 * Interface describing a basic key which can carry a character, emoji, or special function
 */
interface KeyData : AbstractKeyData {
    val type: KeyType
    val code: Int
    val label: String
    val groupId: Int
    val popup: PopupSet<AbstractKeyData>?

    companion object {
        const val GROUP_DEFAULT: Int = 0
        const val GROUP_LEFT: Int = 1
        const val GROUP_RIGHT: Int = 2
        const val GROUP_ENTER: Int = 3
        const val GROUP_KANA: Int = 4
    }
}

/**
 * Enum class for different key types
 */
enum class KeyType {
    CHARACTER,
    ENTER_EDITING,
    FUNCTION,
    LOCK,
    MODIFIER,
    NAVIGATION,
    NUMERIC,
    PLACEHOLDER,
    SYSTEM_GUI,
    TOGGLE,
    UNSPECIFIED
}

/**
 * Basic text key data implementation
 */
@Serializable
data class TextKeyData(
    override val code: Int,
    override val label: String,
    override val type: KeyType = KeyType.CHARACTER,
    override val groupId: Int = KeyData.GROUP_DEFAULT,
    override val popup: PopupSet<AbstractKeyData>? = null
) : KeyData {
    
    override fun compute(evaluator: ComputingEvaluator): KeyData? {
        return this
    }
    
    override fun asString(isForDisplay: Boolean): String {
        return if (code > 0) {
            String(Character.toChars(code))
        } else {
            label
        }
    }
}

/**
 * Khmer-specific key data with transliteration support
 */
@Serializable
data class KhmerKeyData(
    val khmerChar: String,
    val romanChar: String,
    override val code: Int,
    override val label: String,
    override val type: KeyType = KeyType.CHARACTER,
    override val groupId: Int = KeyData.GROUP_DEFAULT,
    override val popup: PopupSet<AbstractKeyData>? = null
) : KeyData {
    
    override fun compute(evaluator: ComputingEvaluator): KeyData? {
        return this
    }
    
    override fun asString(isForDisplay: Boolean): String {
        return if (isForDisplay) {
            khmerChar
        } else {
            khmerChar
        }
    }
    
    fun getRomanEquivalent(): String = romanChar
}

/**
 * Function key data for special keyboard functions
 */
@Serializable
data class FunctionKeyData(
    override val code: Int,
    override val label: String,
    val action: KeyboardAction,
    override val type: KeyType = KeyType.FUNCTION,
    override val groupId: Int = KeyData.GROUP_DEFAULT,
    override val popup: PopupSet<AbstractKeyData>? = null
) : KeyData {
    
    override fun compute(evaluator: ComputingEvaluator): KeyData? {
        return this
    }
    
    override fun asString(isForDisplay: Boolean): String {
        return label
    }
}

/**
 * Enum for keyboard actions
 */
enum class KeyboardAction {
    BACKSPACE,
    DELETE,
    ENTER,
    SPACE,
    SHIFT,
    CAPS_LOCK,
    SWITCH_TO_SYMBOLS,
    SWITCH_TO_LETTERS,
    SWITCH_TO_NUMBERS,
    LANGUAGE_SWITCH,
    HIDE_KEYBOARD,
    SETTINGS
}