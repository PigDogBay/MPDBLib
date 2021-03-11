package com.pigdogbay.lib.utils
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class WordListTest : WordListCallback {
    private val wordList = listOf("close","shave","close shave","narrow escape","jack-o-lantern")
    private val matches = ArrayList<String>()

    @Before
    fun setup(){
        matches.clear()
    }

    @Test
    fun anagrams1() {
        val target = WordList()
        target.setWordList(wordList)
        target.findAnagrams("vasechesol", this)
        assertEquals(1,matches.count())
        assertEquals("close shave",matches[0])
    }

    @Test
    fun crossword1() {
        val target = WordList()
        target.setWordList(wordList)
        target.findPartialWords("j..ko....e..", this)
        assertEquals(1,matches.count())
        assertEquals("jack-o-lantern",matches[0])
    }

    override fun Update(result: String?) {
        matches.add(result!!)
    }
}