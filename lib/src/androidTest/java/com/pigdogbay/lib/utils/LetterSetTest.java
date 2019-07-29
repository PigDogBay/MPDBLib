package com.pigdogbay.lib.utils;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class LetterSetTest {

    @Test
    public void isAnagram1(){
        //cat+++
        LetterSet letterSet = new LetterSet("cat");
        assertThat(letterSet.isAnagram("chart",3),is(true));
        assertThat(letterSet.isAnagram("charts",3),is(true));
        assertThat(letterSet.isAnagram("charted",3),is(false));
    }
    @Test
    public void isAnagram2(){
        LetterSet letterSet = new LetterSet("");
        assertThat(letterSet.isAnagram("",0),is(true));
        assertThat(letterSet.isAnagram("cat",3),is(true));
        assertThat(letterSet.isAnagram("cats",3),is(false));
    }
    @Test
    public void isAnagram3(){
        LetterSet letterSet = new LetterSet("black");
        assertThat(letterSet.isAnagram("black",0),is(true));
        assertThat(letterSet.isAnagram("clack",0),is(false));
        assertThat(letterSet.isAnagram("clack",1),is(true));
    }

    /*
        Regression test for a bug, Letterset could not handle spaces
     */
    @Test
    public void isSubgram(){
        LetterSet letterSet = new LetterSet("words with friends");
        assertThat(letterSet.isSubgram("finds"),is(true));
    }
}
