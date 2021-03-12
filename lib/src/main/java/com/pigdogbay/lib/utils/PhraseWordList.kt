package com.pigdogbay.lib.utils

import java.util.*
import java.util.regex.Pattern

class PhraseWordList {
    @Volatile
    private var stop = false
    private var wordList : List<String> = emptyList()
    private val inBuffer = CharArray(64)
    private val outBuffer = CharArray(64)

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

    /**
     * Optimised code to remove spaces, hyphens and any punctuation from a string
     */
    private fun strip(raw: String) : String {
        val l = raw.length
        raw.toCharArray(inBuffer, 0, 0, l)
        var j = 0
        for (i in 0 until l){
            val c = inBuffer[i]
            if (c in 'a'..'z' || c in 'A'..'Z'){
                outBuffer[j++] = c
            }
        }
        return if (j==l) raw else outBuffer.concatToString(0,j)
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