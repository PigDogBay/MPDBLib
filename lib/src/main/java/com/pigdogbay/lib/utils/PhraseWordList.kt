package com.pigdogbay.lib.utils

import java.util.*
import java.util.regex.Pattern

class PhraseWordList {
    @Volatile
    private var stop = false
    private var wordList : List<Pair<String,String>> = listOf(Pair("",""))
    private val inBuffer = CharArray(64)
    private val outBuffer = CharArray(64)

    /**
     * Signal to stop any searches
     */
    fun stop() {
        stop = true
    }

    fun setWordList(wordList: List<String>) {
        this.wordList = wordList.map{Pair(strip(it),it)}
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
            if (word.first.length == len) {
                if (set.isAnagram(word.first)) {
                    callback.Update(word.second)
                }
            }
        }
    }

    fun findPartialWords(partialWord: String, callback: WordListCallback) {
        val length = partialWord.length
        val pattern = createPattern(partialWord)
        for (word in wordList) {
            if (stop) {
                break
            }
            if (word.first.length == length) {
                if (pattern.matcher(word.first).matches()) {
                    callback.Update(word.second)
                }
            }
        }
    }

    private fun createPattern(s: String): Pattern {
        val converted = s.toLowerCase(Locale.US)
                .replace(".", "[a-z]")
                .replace("#", "[a-z]+")
        return Pattern.compile(converted, Pattern.CASE_INSENSITIVE)
    }
}