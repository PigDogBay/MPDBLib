package com.pigdogbay.lib.utils

import org.junit.Test

import org.junit.Assert.*

class ExtensionsKtTest {

    @Test
    fun toCrosswordRegex1() {
        assertEquals("^h[a-z]n[a-z]y$","h.n.y".toCrosswordRegex())
        assertEquals("^h[a-z]nry$","H.nRy".toCrosswordRegex())
        assertEquals("^h[a-z]nr[a-z]$","h?nr?".toCrosswordRegex())
        assertEquals("^h[a-z]nr[a-z][- ]ba[a-z]ley$","h?nr?-ba?ley".toCrosswordRegex())
        assertEquals("^h[a-z]nr[a-z][- ]a[a-z]d[- ]holl[a-z]$","h?nr? a?d holl?".toCrosswordRegex())
        assertEquals("^hen[a-z]y$","hen1y".toCrosswordRegex(),)
        assertEquals("^h[a-z][a-z][a-z][a-z][a-z][a-z][a-z][a-z][a-z]y$","h9y".toCrosswordRegex())
        assertEquals("^h[a-z]+y$","h@y".toCrosswordRegex())
        assertEquals("^h[a-z]+y$","h#y".toCrosswordRegex())
    }

    @Test
    fun toCrosswordRegex2() {
        //Bad regex
        assertEquals("^h[a-z]nry$", ")h.nry".toCrosswordRegex())
        assertEquals("^h[a-z]nry$", ")[]*$^\\(!£{},,<>/':;&h.nry".toCrosswordRegex())
    }
}