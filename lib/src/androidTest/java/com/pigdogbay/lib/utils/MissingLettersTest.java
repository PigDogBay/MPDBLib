package com.pigdogbay.lib.utils;

import androidx.test.runner.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MissingLettersTest {

    @Test
    public void findPositions1(){
        MissingLetters target = new MissingLetters("gaim");
        int actual = target.findPositions("magic");
        assertThat(actual, is(1));
        assertThat(target.getPositionAt(0), is(4));
    }
    @Test
    public void findPositions2(){
        MissingLetters target = new MissingLetters("aage");
        int actual = target.findPositions("babbage");
        assertThat(actual, is(3));
        assertThat(target.getPositionAt(0), is(0));
        assertThat(target.getPositionAt(1), is(2));
        assertThat(target.getPositionAt(2), is(3));
    }
    @Test
    public void findPositions3(){
        MissingLetters target = new MissingLetters("cigam");
        int actual = target.findPositions("magic");
        assertThat(actual, is(0));
    }
    @Test
    public void findPositions4(){
        MissingLetters target = new MissingLetters("");
        int actual = target.findPositions("magic");
        assertThat(actual, is(5));
        assertThat(target.getPositionAt(0), is(0));
        assertThat(target.getPositionAt(2), is(2));
        assertThat(target.getPositionAt(4), is(4));
    }
    @Test
    public void findPositions5(){
        MissingLetters target = new MissingLetters("magic");
        int actual = target.findPositions("");
        assertThat(actual, is(0));
    }
    @Test
    public void findPositions6(){
        MissingLetters target = new MissingLetters("magic");
        int actual = target.findPositions("cog");
        assertThat(actual, is(1));
        assertThat(target.getPositionAt(0), is(1));
    }
    @Test
    public void findPositions7(){
        MissingLetters target = new MissingLetters("kayleigh");
        int actual = target.findPositions("breathtakingly");
        assertThat(actual, is(6));
        assertThat(target.getPositionAt(0), is(0));
        assertThat(target.getPositionAt(1), is(1));
        assertThat(target.getPositionAt(2), is(4));
        assertThat(target.getPositionAt(3), is(6));
        assertThat(target.getPositionAt(4), is(7));
        assertThat(target.getPositionAt(5), is(10));
    }
    @Test
    public void findPositions8(){
        MissingLetters target = new MissingLetters("holly");
        int actual = target.findPositions("llanfairpwllgwyngyllgogeryqjxzchwyrndrobwllllantysiliogogogoch");
        assertThat(actual, is(57));
        assertThat(target.getPositionAt(0), is(2));
    }

    @Test
    public void highlightMissingLetters1(){
        MissingLetters target = new MissingLetters("gaim");
        String actual = target.highlightMissingLetters("magic","<b>","</b>");
        assertThat(actual, is("magi<b>c</b>"));
    }
    @Test
    public void highlightMissingLetters2(){
        MissingLetters target = new MissingLetters("aage");
        String actual = target.highlightMissingLetters("babbbage","<b>","</b>");
        assertThat(actual, is("<b>b</b>a<b>bbb</b>age"));
    }

}
