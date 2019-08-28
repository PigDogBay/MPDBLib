package com.pigdogbay.lib.diagnostics

import android.util.Log

class Timing {
    private val start: Long = System.nanoTime()
    val milliseconds: Long
        get() = (System.nanoTime() - start) / 1000000L

    fun logDuration(tag: String) {
        Log.v(tag, "${milliseconds}ms")
    }
}
