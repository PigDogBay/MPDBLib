package com.pigdogbay.lib.utils

import java.util.*
import java.util.regex.Pattern

class WordList {
    @Volatile
    private var stop = false
    private var wordList : List<String> = listOf("")

    /**
     * Signal to stop any searches
     */
    fun stop() {
        stop = true
    }

    fun reset() {
        stop = false
    }

    /*
	 * Word list must be sorted and all lower case
	 */
    fun setWordList(wordList: List<String>) {
        this.wordList = wordList
    }

    fun findSupergrams(anagram: String, callback: WordListCallback, length: Int) {
        val anagramLength = anagram.length
        val set = LetterSet(anagram)
        for (word in wordList) {
            if (stop) {
                break
            }
            if (length == 0 && word.length > anagramLength
                    || word.length == length) {
                if (set.isSupergram(word)) {
                    callback.Update(word)
                }
            }
        }
    }

    fun findAnagrams(anagram: String, numberOfBlanks: Int, callback: WordListCallback) {
        val len = anagram.length
        val set = LetterSet(anagram)
        val tooBig = len + numberOfBlanks + 1
        for (word in wordList) {
            if (stop) {
                break
            }
            if (word.length < tooBig) {
                if (set.isAnagram(word, numberOfBlanks)) {
                    callback.Update(word)
                }
            }
        }
    }

    fun findAnagramsExactLength(anagram: String, numberOfBlanks: Int, callback: WordListCallback) {
        val len = anagram.length + numberOfBlanks
        val set = LetterSet(anagram)
        for (word in wordList) {
            if (stop) {
                break
            }
            if (word.length == len) {
                if (set.isAnagram(word, numberOfBlanks)) {
                    callback.Update(word)
                }
            }
        }
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

    fun findSubAnagrams(anagram: String, callback: WordListCallback) {
        val len = anagram.length
        if (len == 1) {
            return
        }
        val set = LetterSet(anagram)
        for (word in wordList) {
            if (stop) {
                break
            }
            val wordLen = word.length
            if (wordLen < len) {
                if (set.isSubgram(word)) {
                    callback.Update(word)
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
            if (word.length == length) {
                if (pattern.matcher(word).matches()) {
                    callback.Update(word)
                }
            }
        }
    }

    fun findWildcardWords(wildcard: String, callback: WordListCallback) {
        val pattern = createPattern(wildcard)
        for (word in wordList) {
            if (stop) {
                break
            }
            if (pattern.matcher(word).matches()) {
                callback.Update(word)
            }
        }
    }

    private fun createPattern(s: String) = Pattern.compile(s.toCrosswordRegex(),Pattern.CASE_INSENSITIVE)

    /**
     * Filter the word list so that it contains letters that are same size as word1
     * and is a subset of the letters contained in word1+word2
     *
     * Create another filtered list if word2 is different length to word1
     *
     * swap lists, we want the smallest to cut down on some processing
     *
     * for each word in the first list find the unused letters
     *
     * Then for each word in the second list see if it is an anagram of the unused letters,
     * if it is the first and second words make a two word anagram
     */
    fun findMultiwordAnagrams(word1: String, word2: String,
                              callback: WordListCallback) {
        val superset = LetterSet(word1 + word2)
        val listA = getFilteredList(superset, word1.length)
        var listB = listA
        if (word1.length != word2.length) {
            listB = getFilteredList(superset, word2.length)
        }
        for (first in listA) {
            superset.clear()
            superset.add(word1)
            superset.add(word2)
            superset.delete(first)
            for (second in listB) {
                if (stop) {
                    break
                }
                if (superset.isAnagram(second)) {
                    callback.Update("$first $second")
                }
            }
        }
    }

    fun findMultiwordAnagrams(word1: String, word2: String, word3: String,
                              callback: WordListCallback) {
        val superset = LetterSet(word1 + word2 + word3)
        val listA = getFilteredList(superset, word1.length)
        var listB = listA
        if (word1.length != word2.length) {
            listB = getFilteredList(superset, word2.length)
        }
        val listC = when (word3.length) {
            word1.length -> listA
            word2.length -> listB
            else -> getFilteredList(superset, word3.length)
        }
        val sublistB = ArrayList<String>()
        val are2And3SameLength = word2.length == word3.length
        val sublistC = if (are2And3SameLength) sublistB else ArrayList()
        for (first in listA) {
            //Prune lists B and C of any words that are impossible with first
            superset.clear()
            superset.add(word1)
            superset.add(word2)
            superset.add(word3)
            superset.delete(first)
            sublistB.clear()
            filterList(superset, word2.length, sublistB, listB)
            if (!are2And3SameLength) {
                sublistC.clear()
                filterList(superset, word3.length, sublistC, listC)
            }
            for (second in sublistB) {
                superset.clear()
                superset.add(word1)
                superset.add(word2)
                superset.add(word3)
                superset.delete(first)
                superset.delete(second)
                for (third in sublistC) {
                    if (stop) {
                        return
                    }
                    if (superset.isAnagram(third)) {
                        callback.Update("$first $second $third")
                    }
                }
            }
        }
    }

    private fun filterList(set: LetterSet, length: Int, matches: ArrayList<String>, wordList: List<String>) {
        for (word in wordList) {
            if (stop) {
                break
            }
            if (word.length == length && set.isSubgram(word)) {
                matches.add(word)
            }
        }
    }

    /*
		Tries to find all word size combinations
	 */
    fun findMultiwordAnagrams(letters: String, startLen: Int, callback: WordListCallback) {
        val len = letters.length
        val middleWordSize = len / 2

        //First show the users requested word sizes
        findOtherMultiwordAnagrams(letters, callback, startLen)

        //Show other words sizes, but skip the ones already shown
        val skipLen = if (startLen > middleWordSize) {len - startLen} else {startLen}

        for (i in middleWordSize downTo 1) {
            if (i == skipLen) continue
            if (stop) {
                break
            }
            findOtherMultiwordAnagrams(letters, callback, i)
        }
    }

    private fun findOtherMultiwordAnagrams(letters: String, callback: WordListCallback, i: Int) {
        val word1 = letters.substring(0, i)
        val word2 = letters.substring(i)
        findMultiwordAnagrams(word1, word2, callback)
    }

    private fun getFilteredList(set: LetterSet, length: Int): List<String> {
        val matches = ArrayList<String>()
        for (word in wordList) {
            if (stop) {
                break
            }
            //skip phrases by checking display word is same length
            if (word.length == length && set.isSubgram(word)) {
                matches.add(word)
            }
        }
        return matches
    }

    fun findCodewords(codewordSolver: CodewordSolver, callback: WordListCallback) {
        val expectedLength = codewordSolver.wordLength
        for (word in wordList) {
            if (stop) {
                break
            }
            if (word.length == expectedLength && codewordSolver.isMatch(word)) {
                callback.Update(word)
            }
        }
    }
}