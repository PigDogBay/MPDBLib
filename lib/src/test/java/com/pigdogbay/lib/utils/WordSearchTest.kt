package com.pigdogbay.lib.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class WordSearchTest {
    @Test
    fun preProcessQuery1() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        val actual = target.preProcessQuery("m.g.c")
        assertEquals("m.g.c", actual)
    }

    @Test
    fun preProcessQuery2() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        val actual = target.preProcessQuery("m4k2")
        assertEquals("m....k..", actual)
    }

    @Test
    fun preProcessQuery3() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        val actual = target.preProcessQuery("M.G.C")
        assertEquals("m.g.c", actual)
    }

    @Test
    fun preProcessQuery4() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        val actual = target.preProcessQuery("the quick lazy fox jumped over the lazy dog")
        assertEquals(WordSearch.MAX_WORD_LEN.toLong(), actual.length.toLong())
    }

    @Test
    fun preProcessQuery5() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        val actual = target.preProcessQuery("m?g?c")
        assertEquals("m.g.c", actual)
    }

    @Test
    fun preProcessQuery6() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        val actual = target.preProcessQuery("@ace")
        assertEquals("#ace", actual)
    }

    @Test
    fun preProcessQuery7() {
        val target = WordSearch(WordList())
        val actual = target.preProcessQuery(".a112332")
        assertEquals(".a112332", actual)
    }

    @Test
    fun preProcessQuery8() {
        val target = WordSearch(WordList())
        val actual = target.preProcessQuery("?a112332")
        assertEquals(".a112332", actual)
    }

    @Test
    fun preProcessQuery9() {
        val target = WordSearch(WordList())
        val actual = target.preProcessQuery("?a12332")
        assertEquals(".a...........", actual)
    }

    @Test
    fun preProcessQuery10() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        val actual = target.preProcessQuery("m??k??-m?g?c")
        assertEquals("m..k.. m.g.c", actual)
    }

    @Test
    fun getQueryType() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        assertEquals(WordSearch.SearchType.Crossword, target.getQueryType("m.g.c"))
        assertEquals(WordSearch.SearchType.CrosswordPhrase, target.getQueryType("m..k.. m.g.c"))
        assertEquals(WordSearch.SearchType.WildcardAndCrossword, target.getQueryType("m.g#"))
        assertEquals(WordSearch.SearchType.Wildcard, target.getQueryType("mag#"))
        assertEquals(WordSearch.SearchType.Anagram, target.getQueryType("magic"))
        assertEquals(WordSearch.SearchType.Blanks, target.getQueryType("magic++"))
        assertEquals(WordSearch.SearchType.Supergram, target.getQueryType("magic*"))
        assertEquals(WordSearch.SearchType.TwoWordAnagram, target.getQueryType("monkey magic"))
        assertEquals(WordSearch.SearchType.Codeword, target.getQueryType(".a112332"))
        assertEquals(WordSearch.SearchType.Codeword, target.getQueryType("1.a23321"))
        assertEquals(WordSearch.SearchType.Crossword, target.getQueryType(".a12332"))
    }

    @Test
    fun postProcessQuery1() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        assertEquals("m.g.c", target.postProcessQuery("m.g.c", WordSearch.SearchType.Crossword))
        assertEquals("m..k.. m.g.c", target.postProcessQuery("m..k.. m.g.c", WordSearch.SearchType.CrosswordPhrase))
        assertEquals("m.g#", target.postProcessQuery("m.g#", WordSearch.SearchType.WildcardAndCrossword))
        assertEquals("#mag#", target.postProcessQuery("#mag#", WordSearch.SearchType.Wildcard))
        assertEquals("magic", target.postProcessQuery("magic", WordSearch.SearchType.Anagram))
        assertEquals("magic++", target.postProcessQuery("magic++", WordSearch.SearchType.Blanks))
        assertEquals("magic*", target.postProcessQuery("magic*", WordSearch.SearchType.Supergram))
        assertEquals("monkey magic", target.postProcessQuery("monkey magic", WordSearch.SearchType.TwoWordAnagram))
        assertEquals(".a112332", target.postProcessQuery(".a112332", WordSearch.SearchType.Codeword))
    }

    @Test
    fun postProcessQuery2() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        assertEquals("m.g.c", target.postProcessQuery("?m.?g.c??", WordSearch.SearchType.Crossword))
        assertEquals("m..k.. m.g.c", target.postProcessQuery("m..k.. m.g.c?#", WordSearch.SearchType.CrosswordPhrase))
        assertEquals("m.g#", target.postProcessQuery("m.!g#*", WordSearch.SearchType.WildcardAndCrossword))
        assertEquals("#mag#", target.postProcessQuery("#mAaBg$.#", WordSearch.SearchType.Wildcard))
        assertEquals("magic", target.postProcessQuery("magic.#*+", WordSearch.SearchType.Anagram))
        assertEquals("magic++", target.postProcessQuery("magic**++", WordSearch.SearchType.Blanks))
        assertEquals("magic*", target.postProcessQuery("magic*++", WordSearch.SearchType.Supergram))
        assertEquals("monkey magic", target.postProcessQuery("mo.n+key m*KagSiXc", WordSearch.SearchType.TwoWordAnagram))
        assertEquals(".az112.332", target.postProcessQuery(".az#+@112.33#+@*2", WordSearch.SearchType.Codeword))
        assertEquals(".a", target.postProcessQuery(".a112332", WordSearch.SearchType.Crossword))
    }

    @Test
    fun postProcessQuery3() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Crossword))
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.CrosswordPhrase))
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.WildcardAndCrossword))
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Wildcard))
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Anagram))
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Blanks))
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Supergram))
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.TwoWordAnagram))
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Codeword))
    }

    @Test
    fun clean1() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        var expected = ""
        var actual = target.clean(expected)
        assertEquals(expected, actual)
        expected = "m.g.."
        actual = target.clean(expected)
        assertEquals(expected, actual)
        expected = "m??k??-m.g.."
        actual = target.clean(expected)
        assertEquals(expected, actual)
        expected = "#ace"
        actual = target.clean(expected)
        assertEquals(expected, actual)
        expected = "manchester united"
        actual = target.clean(expected)
        assertEquals(expected, actual)
        expected = "1z9"
        actual = target.clean(expected)
        assertEquals(expected, actual)
        expected = "abcdefghijklmnopqrstuvwxyz"
        actual = target.clean(expected)
        assertEquals(expected, actual)
    }

    @Test
    fun clean2() {
        val wordList = WordList()
        val target = WordSearch(wordList)
        var actual = target.clean("            ")
        assertEquals("", actual)
        actual = target.clean("        whitespace        ")
        assertEquals("whitespace", actual)
        actual = target.clean("°☺☻♥♦♣¶┬§ÄÕÀ±╝Ï÷╩ıÓ")
        assertEquals("", actual)
        actual = target.clean("°☺☻♥♦♣¶┬§okÄÕÀ±╝Ï÷╩ıÓ")
        assertEquals("ok", actual)
        actual = target.clean("°☺☻♥♦♣¶┬§    o k   ÄÕÀ±╝Ï÷╩ıÓ")
        assertEquals("o k", actual)
        actual = target.clean("AZ")
        assertEquals("az", actual)
    }

}