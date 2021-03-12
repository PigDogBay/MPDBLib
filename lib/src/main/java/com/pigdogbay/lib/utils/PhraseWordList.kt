package com.pigdogbay.lib.utils

import java.util.*
import java.util.regex.Pattern

class PhraseWordList {
    @Volatile
    private var stop = false
    private var wordList : List<String> = emptyList()

    val isWordListEmpty : Boolean get() = wordList.isEmpty()

    /**
     * Signal to stop any searches
     */
    fun stop() {
        stop = true
    }

    fun setWordList(wordList: List<String>) {
        this.wordList = wordList
    }

    fun findAnagrams(anagram: String, callback: WordListCallback) {
        val len = anagram.length
        val set = LetterSet(anagram)
        for (word in wordList) {
            if (stop) {
                break
            }
            if (word.length == len) {
                if (set.isAnagram(word)) {
                    callback.Update(word)
                }
            }
        }
    }

    fun findPartialWords(partialWord: String, callback: WordListCallback) {
        val length = partialWord.length
        val pattern = Pattern.compile(partialWord.toCrosswordRegex(), Pattern.CASE_INSENSITIVE)
        for (word in wordList) {
            if (stop) {
                break
            }
            if (word.length == length) {
                if (pattern.matcher(word).matches()) {
                    callback.Update(word)
                }
            }
        }
    }
}