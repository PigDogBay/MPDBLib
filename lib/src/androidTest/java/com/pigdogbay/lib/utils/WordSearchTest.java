package com.pigdogbay.lib.utils;


import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class WordSearchTest {
    @Test
    public void preProcessQuery1() {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);
        String actual = target.preProcessQuery("m.g.c");
        assertEquals("m.g.c", actual);
    }
    @Test
    public void preProcessQuery2() {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);
        String actual = target.preProcessQuery("m4k2");
        assertEquals("m....k..", actual);
    }
    @Test
    public void preProcessQuery3() {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);
        String actual = target.preProcessQuery("M.G.C");
        assertEquals("m.g.c", actual);
    }
    @Test
    public void preProcessQuery4() {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);
        String actual = target.preProcessQuery("the quick lazy fox jumped over the lazy dog");
        assertEquals(WordSearch.MAX_WORD_LEN, actual.length());
    }
    @Test
    public void preProcessQuery5() {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);
        String actual = target.preProcessQuery("m?g?c");
        assertEquals("m.g.c", actual);
    }
    @Test
    public void preProcessQuery6() {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);
        String actual = target.preProcessQuery("@ace");
        assertEquals("#ace", actual);
    }

    @Test
    public void getQueryType() {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);
        assertEquals(WordSearch.SearchType.Crossword, target.getQueryType("m.g.c"));
        assertEquals(WordSearch.SearchType.WildcardAndCrossword, target.getQueryType("m.g#"));
        assertEquals(WordSearch.SearchType.Wildcard, target.getQueryType("mag#"));
        assertEquals(WordSearch.SearchType.Anagram, target.getQueryType("magic"));
        assertEquals(WordSearch.SearchType.Blanks, target.getQueryType("magic++"));
        assertEquals(WordSearch.SearchType.Supergram, target.getQueryType("magic*"));
        assertEquals(WordSearch.SearchType.TwoWordAnagram, target.getQueryType("monkey magic"));

    }
    @Test
    public void postProcessQuery1() {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);
        assertEquals("m.g.c", target.postProcessQuery("m.g.c", WordSearch.SearchType.Crossword));
        assertEquals("m.g#", target.postProcessQuery("m.g#", WordSearch.SearchType.WildcardAndCrossword));
        assertEquals("#mag#", target.postProcessQuery("#mag#", WordSearch.SearchType.Wildcard));
        assertEquals("magic", target.postProcessQuery("magic", WordSearch.SearchType.Anagram));
        assertEquals("magic++", target.postProcessQuery("magic++", WordSearch.SearchType.Blanks));
        assertEquals("magic*", target.postProcessQuery("magic*", WordSearch.SearchType.Supergram));
        assertEquals("monkey magic", target.postProcessQuery("monkey magic", WordSearch.SearchType.TwoWordAnagram));

    }
    @Test
    public void postProcessQuery2() {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);
        assertEquals("m.g.c", target.postProcessQuery("?m.?g.c??", WordSearch.SearchType.Crossword));
        assertEquals("m.g#", target.postProcessQuery("m.!g#*", WordSearch.SearchType.WildcardAndCrossword));
        assertEquals("#mag#", target.postProcessQuery("#mAaBg$.#", WordSearch.SearchType.Wildcard));
        assertEquals("magic", target.postProcessQuery("magic.#*+", WordSearch.SearchType.Anagram));
        assertEquals("magic++", target.postProcessQuery("magic**++", WordSearch.SearchType.Blanks));
        assertEquals("magic*", target.postProcessQuery("magic*++", WordSearch.SearchType.Supergram));
        assertEquals("monkey magic", target.postProcessQuery("mo.n+key m*KagSiXc", WordSearch.SearchType.TwoWordAnagram));

    }
    @Test
    public void postProcessQuery3() {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Crossword));
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.WildcardAndCrossword));
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Wildcard));
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Anagram));
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Blanks));
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.Supergram));
        assertEquals("", target.postProcessQuery("", WordSearch.SearchType.TwoWordAnagram));

    }

    @Test
    public void clean1()
    {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);

        String expected = "";
        String actual = target.clean(expected);
        assertEquals(expected,actual);

        expected = "m.g..";
        actual = target.clean(expected);
        assertEquals(expected,actual);

        expected = "#ace";
        actual = target.clean(expected);
        assertEquals(expected,actual);

        expected = "manchester united";
        actual = target.clean(expected);
        assertEquals(expected,actual);

        expected = "1z9";
        actual = target.clean(expected);
        assertEquals(expected,actual);

        expected = "abcdefghijklmnopqrstuvwxyz";
        actual = target.clean(expected);
        assertEquals(expected,actual);
    }
    @Test
    public void clean2()
    {
        WordList wordList = new WordList();
        WordSearch target = new WordSearch(wordList);

        String actual = target.clean("            ");
        assertEquals("",actual);

        actual = target.clean("        whitespace        ");
        assertEquals("whitespace",actual);

        actual = target.clean("°☺☻♥♦♣¶┬§ÄÕÀ±╝Ï÷╩ıÓ");
        assertEquals("",actual);

        actual = target.clean("°☺☻♥♦♣¶┬§okÄÕÀ±╝Ï÷╩ıÓ");
        assertEquals("ok",actual);

        actual = target.clean("°☺☻♥♦♣¶┬§    o k   ÄÕÀ±╝Ï÷╩ıÓ");
        assertEquals("o k",actual);

        actual = target.clean("AZ");
        assertEquals("az",actual);

    }



}
