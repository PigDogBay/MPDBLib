package com.pigdogbay.lib.utils
import org.junit.Test

import org.junit.Assert.*

class LetterSetTest {
    @Test
    fun isAnagram1() {
        //cat+++
        val letterSet = LetterSet("cat")
        assertTrue(letterSet.isAnagram("chart", 3))
        assertTrue(letterSet.isAnagram("charts", 3))
        assertFalse(letterSet.isAnagram("charted", 3))
    }

    @Test
    fun isAnagram2() {
        val letterSet = LetterSet("")
        assertTrue(letterSet.isAnagram("", 0))
        assertTrue(letterSet.isAnagram("cat", 3))
        assertFalse(letterSet.isAnagram("cats", 3))
    }

    @Test
    fun isAnagram3() {
        val letterSet = LetterSet("black")
        assertTrue(letterSet.isAnagram("black", 0))
        assertFalse(letterSet.isAnagram("clack", 0))
        assertTrue(letterSet.isAnagram("clack", 1))
    }

    /*
        Regression test for a bug, Letterset could not handle spaces
     */
    @Test
    fun isSubgram() {
        val letterSet = LetterSet("words with friends")
        assertTrue(letterSet.isSubgram("finds"))
    }

    @Test
    fun isDistinct1() {
        val letterSet = LetterSet("spectrum")
        assertTrue(letterSet.isDistinct)
    }

    @Test
    fun isDistinct2() {
        val letterSet = LetterSet("electron")
        assertFalse(letterSet.isDistinct)
    }

    @Test
    fun isDistinct3() {
        val letterSet = LetterSet("")
        assertFalse(letterSet.isDistinct)
    }

}