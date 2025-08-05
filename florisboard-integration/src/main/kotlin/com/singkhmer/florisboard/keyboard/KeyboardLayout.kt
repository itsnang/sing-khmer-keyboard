/*
 * Keyboard layout management for SingKhmer Keyboard
 * Adapted from FlorisBoard architecture
 */

package com.singkhmer.florisboard.keyboard

import kotlinx.serialization.Serializable

/**
 * Represents a complete keyboard layout
 */
@Serializable
data class KeyboardLayout(
    val type: LayoutType,
    val name: String,
    val rows: List<KeyboardRow>,
    val meta: LayoutMeta = LayoutMeta()
) {
    fun getAllKeys(): List<AbstractKeyData> {
        return rows.flatMap { row ->
            row.keys.flatMap { it.getAllKeys() }
        }
    }
}

/**
 * Represents a row of keys in a keyboard layout
 */
@Serializable
data class KeyboardRow(
    val keys: List<KeyboardKey>,
    val rowHeight: Float = 1.0f
)

/**
 * Represents a single key or key group in a keyboard
 */
@Serializable
data class KeyboardKey(
    val data: AbstractKeyData,
    val width: Float = 1.0f,
    val height: Float = 1.0f
) {
    fun getAllKeys(): List<AbstractKeyData> {
        return listOf(data)
    }
}

/**
 * Metadata for keyboard layouts
 */
@Serializable
data class LayoutMeta(
    val author: String = "SingKhmer",
    val version: String = "1.0",
    val description: String = "",
    val supportsShift: Boolean = true,
    val supportsCapsLock: Boolean = true
)

/**
 * Khmer keyboard layout factory
 */
object KhmerKeyboardLayouts {
    
    /**
     * Creates the main Khmer character layout
     */
    fun createKhmerLayout(): KeyboardLayout {
        return KeyboardLayout(
            type = LayoutType.CHARACTERS,
            name = "Khmer",
            rows = listOf(
                // First row: ១២៣៤៥៦៧៨៩០ឥឲ
                KeyboardRow(
                    keys = listOf(
                        KeyboardKey(KhmerKeyData("១", "1", 0x17E1, "១")),
                        KeyboardKey(KhmerKeyData("២", "2", 0x17E2, "២")),
                        KeyboardKey(KhmerKeyData("៣", "3", 0x17E3, "៣")),
                        KeyboardKey(KhmerKeyData("៤", "4", 0x17E4, "៤")),
                        KeyboardKey(KhmerKeyData("៥", "5", 0x17E5, "៥")),
                        KeyboardKey(KhmerKeyData("៦", "6", 0x17E6, "៦")),
                        KeyboardKey(KhmerKeyData("៧", "7", 0x17E7, "៧")),
                        KeyboardKey(KhmerKeyData("៨", "8", 0x17E8, "៨")),
                        KeyboardKey(KhmerKeyData("៩", "9", 0x17E9, "៩")),
                        KeyboardKey(KhmerKeyData("០", "0", 0x17E0, "០")),
                        KeyboardKey(KhmerKeyData("ឥ", "i", 0x17A5, "ឥ")),
                        KeyboardKey(KhmerKeyData("ឲ", "o", 0x17B2, "ឲ"))
                    )
                ),
                // Second row: ឆតយុិោះ
                KeyboardRow(
                    keys = listOf(
                        KeyboardKey(KhmerKeyData("ឆ", "ch", 0x1786, "ឆ")),
                        KeyboardKey(KhmerKeyData("ត", "t", 0x178F, "ត")),
                        KeyboardKey(KhmerKeyData("យ", "y", 0x1799, "យ")),
                        KeyboardKey(KhmerKeyData("ុ", "u", 0x17BB, "ុ")),
                        KeyboardKey(KhmerKeyData("ិ", "i", 0x17B7, "ិ")),
                        KeyboardKey(KhmerKeyData("ោ", "o", 0x17C4, "ោ")),
                        KeyboardKey(KhmerKeyData("ះ", "ah", 0x17C7, "ះ")),
                        KeyboardKey(FunctionKeyData(KeyCode.BACKSPACE, "⌫", KeyboardAction.BACKSPACE), width = 1.5f)
                    )
                ),
                // Third row: អសដផគហជកល
                KeyboardRow(
                    keys = listOf(
                        KeyboardKey(KhmerKeyData("អ", "a", 0x17A2, "អ")),
                        KeyboardKey(KhmerKeyData("ស", "s", 0x179F, "ស")),
                        KeyboardKey(KhmerKeyData("ដ", "d", 0x178A, "ដ")),
                        KeyboardKey(KhmerKeyData("ផ", "ph", 0x1795, "ផ")),
                        KeyboardKey(KhmerKeyData("គ", "k", 0x1782, "គ")),
                        KeyboardKey(KhmerKeyData("ហ", "h", 0x17A0, "ហ")),
                        KeyboardKey(KhmerKeyData("ជ", "ch", 0x1787, "ជ")),
                        KeyboardKey(KhmerKeyData("ក", "k", 0x1780, "ក")),
                        KeyboardKey(KhmerKeyData("ល", "l", 0x179B, "ល")),
                        KeyboardKey(FunctionKeyData(KeyCode.ENTER, "↵", KeyboardAction.ENTER), width = 1.5f)
                    )
                ),
                // Fourth row: ឋខចវបនមេាំ
                KeyboardRow(
                    keys = listOf(
                        KeyboardKey(FunctionKeyData(KeyCode.SHIFT, "⇧", KeyboardAction.SHIFT), width = 1.5f),
                        KeyboardKey(KhmerKeyData("ឋ", "th", 0x178B, "ឋ")),
                        KeyboardKey(KhmerKeyData("ខ", "kh", 0x1781, "ខ")),
                        KeyboardKey(KhmerKeyData("ច", "ch", 0x1785, "ច")),
                        KeyboardKey(KhmerKeyData("វ", "v", 0x179C, "វ")),
                        KeyboardKey(KhmerKeyData("ប", "b", 0x1794, "ប")),
                        KeyboardKey(KhmerKeyData("ន", "n", 0x1793, "ន")),
                        KeyboardKey(KhmerKeyData("ម", "m", 0x1798, "ម")),
                        KeyboardKey(KhmerKeyData("េ", "e", 0x17C1, "េ")),
                        KeyboardKey(KhmerKeyData("ា", "aa", 0x17B6, "ា")),
                        KeyboardKey(KhmerKeyData("ំ", "am", 0x17C6, "ំ"))
                    )
                ),
                // Fifth row: Space bar and function keys
                KeyboardRow(
                    keys = listOf(
                        KeyboardKey(FunctionKeyData(KeyCode.SWITCH_TO_TEXT_CONTEXT, "123", KeyboardAction.SWITCH_TO_NUMBERS), width = 1.5f),
                        KeyboardKey(FunctionKeyData(KeyCode.LANGUAGE_SWITCH, "🌐", KeyboardAction.LANGUAGE_SWITCH)),
                        KeyboardKey(FunctionKeyData(KeyCode.SPACE, " ", KeyboardAction.SPACE), width = 4.0f),
                        KeyboardKey(FunctionKeyData(KeyCode.HIDE_KEYBOARD, "⌨", KeyboardAction.HIDE_KEYBOARD)),
                        KeyboardKey(FunctionKeyData(KeyCode.SETTINGS, "⚙", KeyboardAction.SETTINGS))
                    )
                )
            )
        )
    }
    
    /**
     * Creates the numeric layout
     */
    fun createNumericLayout(): KeyboardLayout {
        return KeyboardLayout(
            type = LayoutType.NUMERIC,
            name = "Numeric",
            rows = listOf(
                KeyboardRow(
                    keys = listOf(
                        KeyboardKey(TextKeyData(49, "1")),
                        KeyboardKey(TextKeyData(50, "2")),
                        KeyboardKey(TextKeyData(51, "3"))
                    )
                ),
                KeyboardRow(
                    keys = listOf(
                        KeyboardKey(TextKeyData(52, "4")),
                        KeyboardKey(TextKeyData(53, "5")),
                        KeyboardKey(TextKeyData(54, "6"))
                    )
                ),
                KeyboardRow(
                    keys = listOf(
                        KeyboardKey(TextKeyData(55, "7")),
                        KeyboardKey(TextKeyData(56, "8")),
                        KeyboardKey(TextKeyData(57, "9"))
                    )
                ),
                KeyboardRow(
                    keys = listOf(
                        KeyboardKey(FunctionKeyData(KeyCode.SWITCH_TO_TEXT_CONTEXT, "ABC", KeyboardAction.SWITCH_TO_LETTERS)),
                        KeyboardKey(TextKeyData(48, "0")),
                        KeyboardKey(FunctionKeyData(KeyCode.BACKSPACE, "⌫", KeyboardAction.BACKSPACE))
                    )
                )
            )
        )
    }
}