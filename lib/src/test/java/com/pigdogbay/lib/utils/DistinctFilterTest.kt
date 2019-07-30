package com.pigdogbay.lib.utils
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class DistinctFilterTest : WordListCallback{
    private var result: String? = null

    override fun Update(result: String?) {
        this.result = result
    }

    @Before
    fun setup(){
        this.result = null
    }

    @Test
    fun update1() {
        val filter = WordListCallback.DistinctFilter(this)
        filter.Update("spectrum")
        assertEquals("spectrum", result)
    }

    @Test
    fun update2() {
        val filter = WordListCallback.DistinctFilter(this)
        filter.Update("commodore")
        assertEquals(null, result)
    }

    @Test
    fun update3() {
        val filter = WordListCallback.DistinctFilter(this)
        filter.Update("")
        assertEquals(null, result)
    }
}