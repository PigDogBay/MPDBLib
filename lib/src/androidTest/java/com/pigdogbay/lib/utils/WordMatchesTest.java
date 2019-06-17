package com.pigdogbay.lib.utils;

import androidx.test.runner.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class WordMatchesTest
{
    @Test
    public void sortAZ1(){
        WordMatches target = new WordMatches();
        target.getMatches().add("orange");
        target.getMatches().add("apple");
        target.getMatches().add("banana");
        target.sortAZ();
        assertThat(target.getMatches().get(0),is("apple"));
        assertThat(target.getMatches().get(2),is("orange"));

    }
    @Test
    public void sortZA1(){
        WordMatches target = new WordMatches();
        target.getMatches().add("banana");
        target.getMatches().add("orange");
        target.getMatches().add("apple");
        target.sortZA();
        assertThat(target.getMatches().get(0),is("orange"));
        assertThat(target.getMatches().get(2),is("apple"));
    }
    @Test
    public void sortLengthAsc1(){
        WordMatches target = new WordMatches();
        target.getMatches().add("orange");
        target.getMatches().add("apple");
        target.getMatches().add("banana");
        target.sortLengthAsc();
        assertThat(target.getMatches().get(0),is("apple"));
        assertThat(target.getMatches().get(2),is("orange"));
    }
    @Test
    public void sortLengthDesc1(){
        WordMatches target = new WordMatches();
        target.getMatches().add("apple");
        target.getMatches().add("orange");
        target.getMatches().add("banana");
        target.sortLengthDesc();
        assertThat(target.getMatches().get(1),is("orange"));
        assertThat(target.getMatches().get(2),is("apple"));
    }
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
    public void getFormattedWord5(){
        WordMatches target = new WordMatches();
        target.newSearch("llanfairllgwyngyllgogeryqjxzchwyrndrobwllllantysiliogogogoch++", WordSearch.SearchType.Blanks);
        assertThat(target.getFormattedWord("llanfairpwllgwyngyllgogeryqjxzchwyrndrobwllllantysiliogogogoch"), is("llanfair<b>p</b>wllgwyngyllgogeryqjxzchwyrndrob<b>w</b>llllantysiliogogogoch"));
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
    public void getFormattedWord12(){
        WordMatches target = new WordMatches();
        target.newSearch("llanfairpwllgwyngyllgogeryqjxzchwyrndrobwllllantysiliogogogoch", WordSearch.SearchType.Anagram);
        assertThat(target.getFormattedWord("llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch"), is("llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch (qjxz)"));
    }
    @Test
    public void getFormattedWord21(){
        WordMatches target = new WordMatches();
        target.newSearch("kayleigh*", WordSearch.SearchType.Supergram);
        assertThat(target.getFormattedWord("breathtakingly"), is("<b>br</b>ea<b>t</b>h<b>ta</b>ki<b>n</b>gly"));
    }
    @Test
    public void getFormattedWord22(){
        WordMatches target = new WordMatches();
        target.newSearch("holly*", WordSearch.SearchType.Supergram);
        assertThat(target.getFormattedWord("thermodynamically"), is("<b>t</b>h<b>erm</b>o<b>d</b>y<b>namica</b>ll<b>y</b>"));
    }
    @Test
    public void getFormattedWord23(){
        WordMatches target = new WordMatches();
        target.newSearch("holly*", WordSearch.SearchType.Supergram);
        assertThat(target.getFormattedWord(
                "llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch"),
                is("ll<b>anfairpwllgw</b>y<b>ngyllg</b>o<b>geryc</b>h<b>wyrndrobwllllantysiliogogogoch</b>"));
    }

}
