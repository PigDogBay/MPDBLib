package com.pigdogbay.lib.utils;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class WordMatchesTest
{
    @Test
    public void newSearch1(){
        WordMatches target = new WordMatches();
        target.newSearch("babbage", WordSearch.SearchType.Anagram);
        assertThat(target.getQuery(), is("babbage"));
        assertThat(target.getSearchType(), is(WordSearch.SearchType.Anagram));
    }
    @Test
    public void getFormattedWord1(){
        WordMatches target = new WordMatches();
        target.newSearch("aage+++", WordSearch.SearchType.Blanks);
        assertThat(target.getFormattedWord("babbage"), is("<b>b</b>a<b>bb</b>age"));
    }
    @Test
    public void getFormattedWord2(){
        WordMatches target = new WordMatches();
        target.newSearch("magicka", WordSearch.SearchType.Blanks);
        assertThat(target.getFormattedWord("magic"), is("magic (ka)"));
    }
    @Test
    public void getFormattedWord3(){
        WordMatches target = new WordMatches();
        target.newSearch("magika+++", WordSearch.SearchType.Blanks);
        assertThat(target.getFormattedWord("magic"), is("magi<b>c</b> (ka)"));
    }
    @Test
    public void getFormattedWord4(){
        WordMatches target = new WordMatches();
        target.newSearch("magika+++", WordSearch.SearchType.Blanks);
        assertThat(target.getFormattedWord("a"), is("a (mgika)"));
    }
    @Test
    public void getFormattedWord10(){
        WordMatches target = new WordMatches();
        target.newSearch("magicka", WordSearch.SearchType.Anagram);
        assertThat(target.getFormattedWord("magic"), is("magic (ka)"));
    }
    @Test
    public void getFormattedWord11(){
        WordMatches target = new WordMatches();
        target.newSearch("magicka", WordSearch.SearchType.Anagram);
        assertThat(target.getFormattedWord("i"), is("i (magcka)"));
    }
    @Test
    public void getFormattedWord21(){
        WordMatches target = new WordMatches();
        target.newSearch("kayleigh", WordSearch.SearchType.Supergram);
        assertThat(target.getFormattedWord("breathtakingly"), is("<b>br</b>ea<b>t</b>h<b>ta</b>ki<b>n</b>gly"));
    }

}
