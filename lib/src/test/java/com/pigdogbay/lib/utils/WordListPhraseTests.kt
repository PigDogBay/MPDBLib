package com.pigdogbay.lib.utils
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class WordListPhraseTests : WordListCallback {
    private val wordList = listOf("jack-o-lanterns","jack-o-lantern","narrow escape","holly bailey","henry bailey", "close shave","close","shave")
    private val matches = ArrayList<String>()

    @Before
    fun setup(){
        matches.clear()
    }

    private fun testAnagram(query : String, results : Array<String>){
        matches.clear()
        val target = PhraseWordList()
        target.setWordList(wordList)
        target.findAnagrams(query, this)
        assertArrayEquals(results,matches.toArray())
    }

    private fun testCrossword(query : String, results : Array<String>){
        matches.clear()
        val target = PhraseWordList()
        target.setWordList(wordList)
        target.findPartialWords(query, this)
        assertArrayEquals(results,matches.toArray())
    }

    @Test
    fun anagrams1() {
        testAnagram("vasec-hesol", arrayOf("close shave"))
        testAnagram("yelia bhyllo", arrayOf("holly bailey"))
    }


    @Test
    fun crossword1() {
        testCrossword("j..k-o-....e..", arrayOf("jack-o-lantern"))
        testCrossword("h.... bailey", arrayOf("holly bailey","henry bailey"))
    }

    override fun Update(result: String?) {
        matches.add(result!!)
    }
}