package com.pigdogbay.lib.utils;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.SmallTest;
import android.util.Log;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
@SmallTest
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
}
