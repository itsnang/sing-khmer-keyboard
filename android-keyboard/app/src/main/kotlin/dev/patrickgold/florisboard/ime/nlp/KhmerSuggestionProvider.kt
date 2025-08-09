/*
 * Copyright (C) 2025 The SingKhmer Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.patrickgold.florisboard.ime.nlp

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import com.singkhmer.transliterator.Transliterator
import dev.patrickgold.florisboard.ime.core.Subtype
import dev.patrickgold.florisboard.ime.editor.EditorContent

import dev.patrickgold.florisboard.lib.devtools.flogDebug
import java.util.concurrent.ConcurrentHashMap

/**
 * Custom suggestion candidate for Khmer transliteration.
 * This candidate is designed to work with continuous Khmer text input
 * without automatic space insertion and smart text replacement.
 */
data class KhmerSuggestionCandidate(
    override val text: CharSequence,
    override val secondaryText: CharSequence? = null,
    override val confidence: Double = 0.0,
    override val isEligibleForAutoCommit: Boolean = false,
    override val isEligibleForUserRemoval: Boolean = false,
    override val sourceProvider: SuggestionProvider? = null,
    val originalLatinText: String = "", // Store the original Latin text this suggestion replaces
    val textBeforeCursor: String = "", // Store text before the Latin word
) : SuggestionCandidate {
    override val icon: ImageVector? = null
}

/**
 * Khmer suggestion provider that uses the SingKhmer transliteration engine
 * to provide Roman-to-Khmer suggestions with 3-layer search system
 * (exact match → prefix search → fuzzy search)
 */
class KhmerSuggestionProvider(private val context: Context) : SuggestionProvider {

    companion object {
        const val PROVIDER_ID = "khmer_transliterator"
        private const val KHMER_LANGUAGE_TAG = "km"
    }

    // Initialize the transliterator once
    private val transliterator = Transliterator()

    // Cache for frequently accessed suggestions to improve performance
    private val suggestionCache = ConcurrentHashMap<String, List<SuggestionCandidate>>()

    override val providerId: String = PROVIDER_ID

    override suspend fun suggest(
        subtype: Subtype,
        content: EditorContent,
        maxCandidateCount: Int,
        allowPossiblyOffensive: Boolean,
        isPrivateSession: Boolean,
    ): List<SuggestionCandidate> {
        flogDebug { "KhmerSuggestionProvider.suggest() called! Subtype: ${subtype.primaryLocale.languageTag()}, maxCandidateCount=$maxCandidateCount, Content: '${content.text}'" }

        // For testing: Accept any subtype to test the transliterator
        // TODO: Later, change this back to only accept Khmer subtypes
        flogDebug { "Testing mode: Accepting subtype ${subtype.primaryLocale.languageTag()} for Khmer transliteration" }

        // Get the current word being typed - handle Khmer continuous text
        var currentWord = content.composingText.toString().trim()

        // Extract only the Latin characters from the composing text or full text
        val fullText = content.text.toString()
        val selection = content.selection

        flogDebug { "Raw content: composing='$currentWord', fullText='$fullText', selection=${selection.start}-${selection.end}" }

        // Always extract the last Latin word being typed, regardless of composing text state
        if (selection.isValid && selection.start == selection.end) {
            val cursorPos = selection.start
            // Add bounds checking to prevent StringIndexOutOfBoundsException
            val safeTextBeforeCursor = if (cursorPos > 0 && cursorPos <= fullText.length) {
                fullText.substring(0, cursorPos)
            } else if (cursorPos > fullText.length) {
                fullText // Use full text if cursor is beyond text length
            } else {
                ""
            }

            // Extract the last sequence of Latin characters before the cursor
            val latinWordRegex = Regex("[a-zA-Z]+$")
            val match = latinWordRegex.find(safeTextBeforeCursor)

            if (match != null) {
                currentWord = match.value
                flogDebug { "Extracted Latin word from position $cursorPos: '$currentWord'" }
            }
        }

        // If we still have mixed content (Khmer + Latin), extract just the Latin part
        if (currentWord.isNotBlank()) {
            val latinOnlyRegex = Regex("[a-zA-Z]+")
            val latinMatches = latinOnlyRegex.findAll(currentWord)
            val lastLatinWord = latinMatches.lastOrNull()?.value

            if (lastLatinWord != null && lastLatinWord != currentWord) {
                currentWord = lastLatinWord
                flogDebug { "Extracted final Latin word: '$currentWord'" }
            }
        }

        if (currentWord.isBlank()) {
            flogDebug { "No word to transliterate, returning empty suggestions" }
            return emptyList()
        }

        // Check cache first for performance
        suggestionCache[currentWord]?.let { cachedSuggestions ->
            flogDebug { "Returning cached suggestions for '$currentWord'" }
            return cachedSuggestions.take(maxCandidateCount)
        }

        // Use transliterator to get top suggestions
        val khmerSuggestions = transliterator.suggestTop3(currentWord)

        flogDebug { "Transliterator returned ${khmerSuggestions.size} suggestions for '$currentWord': $khmerSuggestions" }

        // Calculate text before the current Latin word for smart replacement
        val textBeforeLatinWord = if (selection.isValid && selection.start == selection.end) {
            val cursorPos = selection.start
            // Add bounds checking to prevent StringIndexOutOfBoundsException
            val safeTextBeforeCursor = if (cursorPos > 0 && cursorPos <= fullText.length) {
                fullText.substring(0, cursorPos)
            } else if (cursorPos > fullText.length) {
                fullText // Use full text if cursor is beyond text length
            } else {
                ""
            }

            // Find where the current Latin word starts
            val latinWordRegex = Regex("[a-zA-Z]+$")
            val match = latinWordRegex.find(safeTextBeforeCursor)

            if (match != null) {
                val startPos = match.range.first
                if (startPos >= 0 && startPos <= safeTextBeforeCursor.length) {
                    safeTextBeforeCursor.substring(0, startPos)
                } else {
                    safeTextBeforeCursor
                }
            } else {
                safeTextBeforeCursor
            }
        } else {
            fullText
        }

        // Convert to SuggestionCandidate objects
        val candidates = khmerSuggestions.mapIndexed { index, khmerText ->
            // Higher confidence for earlier suggestions (exact matches come first)
            val confidence = when (index) {
                0 -> 0.9 // First suggestion gets highest confidence
                1 -> 0.7 // Second suggestion
                2 -> 0.5 // Third suggestion
                else -> 0.3 // Fallback
            }

            KhmerSuggestionCandidate(
                text = khmerText,
                secondaryText = currentWord, // Show romanization as secondary text
                confidence = confidence,
                isEligibleForAutoCommit = index == 0 && confidence > 0.8, // Auto-commit only highest confidence
                isEligibleForUserRemoval = false, // Don't allow removal of transliteration suggestions
                sourceProvider = this,
                originalLatinText = currentWord, // Store the Latin text this replaces
                textBeforeCursor = textBeforeLatinWord // Store text before the Latin word
            )
        }

        // Cache the results for performance
        suggestionCache[currentWord] = candidates

        // Limit cache size to prevent memory issues
        if (suggestionCache.size > 100) {
            // Remove oldest entries (simple LRU-like behavior)
            val keysToRemove = suggestionCache.keys.take(20)
            keysToRemove.forEach { suggestionCache.remove(it) }
        }

        flogDebug { "Returning ${candidates.size} Khmer suggestions for '$currentWord'" }
        return candidates.take(maxCandidateCount)
    }

    override suspend fun notifySuggestionAccepted(subtype: Subtype, candidate: SuggestionCandidate) {
        flogDebug { "KhmerSuggestionProvider.notifySuggestionAccepted() called with candidate: ${candidate.text}" }

        // Extract the romanization from secondary text and the selected Khmer word
        val khmerText = candidate.text.toString()
        val romanText = candidate.secondaryText?.toString()

        if (romanText != null && romanText.isNotBlank()) {
            // Use transliterator's learning capability to increase frequency
            // This helps the system learn user preferences over time
            transliterator.incrementFrequency(romanText, khmerText)

            // Clear cache for this input to get updated frequencies on next request
            suggestionCache.remove(romanText)

            flogDebug { "Incremented frequency for '$romanText' -> '$khmerText'" }
        }
    }

    override suspend fun notifySuggestionReverted(subtype: Subtype, candidate: SuggestionCandidate) {
        flogDebug { "KhmerSuggestionProvider.notifySuggestionReverted() called with candidate: ${candidate.text}" }

        // For now, we don't implement negative learning (decreasing frequency)
        // This could be added in the future if needed

        val romanText = candidate.secondaryText?.toString()
        if (romanText != null) {
            // Clear cache to ensure fresh suggestions
            suggestionCache.remove(romanText)
        }
    }

    override suspend fun removeSuggestion(subtype: Subtype, candidate: SuggestionCandidate): Boolean {
        // We don't support removing transliteration suggestions since they're based on rules
        // rather than learned data
        return false
    }

    override suspend fun getListOfWords(subtype: Subtype): List<String> {
        // This is used for glide typing - return empty list as we don't support this feature
        return emptyList()
    }

    override suspend fun getFrequencyForWord(subtype: Subtype, word: String): Double {
        // This is used for glide typing - return 0.0 as we don't have frequency data in the expected format
        return 0.0
    }

    // Base NlpProvider methods
    override suspend fun create() {
        flogDebug { "KhmerSuggestionProvider.create() called - initializing transliterator" }
        // The transliterator is already initialized in the constructor
        // We could add additional initialization logic here if needed
    }

    override suspend fun preload(subtype: Subtype) {
        flogDebug { "KhmerSuggestionProvider.preload() called for subtype: ${subtype.primaryLocale.languageTag()}" }
        // The transliterator data is already loaded
        // We could add subtype-specific preloading logic here if needed
    }

    override suspend fun destroy() {
        flogDebug { "KhmerSuggestionProvider.destroy() called" }
        // Clean up resources
        suggestionCache.clear()
    }
}
