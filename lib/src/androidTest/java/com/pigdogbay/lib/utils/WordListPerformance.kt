package com.pigdogbay.lib.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.pigdogbay.lib.diagnostics.Timing
import com.pigdogbay.lib.test.R
import org.junit.Test

class WordListPerformance {

    private fun loadWordList(id: Int) = InstrumentationRegistry.getInstrumentation()
            .context.resources.openRawResource(id)
            .bufferedReader(Charsets.UTF_8)
            .readLines()

    @Test
    fun setWordList1(){
        val words = loadWordList(R.raw.phrases)
        val wordList = PhraseWordList()
        val timing = Timing()
        wordList.setWordList(words)
        timing.logDuration("mpdb")
    }
}