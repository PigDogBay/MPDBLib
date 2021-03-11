package com.pigdogbay.lib.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.pigdogbay.lib.diagnostics.Timing
import com.pigdogbay.lib.test.R
import org.junit.Test

class WordListPerformance {

    fun loadWordList(id: Int) = InstrumentationRegistry.getInstrumentation()
            .context.resources.openRawResource(id)
            .bufferedReader(Charsets.UTF_8)
            .readLines()

    @Test
    fun setWordList1(){
        val words = loadWordList(R.raw.idea)
        val wordList = WordList()
        val timing = Timing()
        wordList.setWordList(words)
        timing.logDuration("mpdb")
    }
}