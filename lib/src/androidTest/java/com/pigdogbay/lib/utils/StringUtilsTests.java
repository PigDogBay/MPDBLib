package com.pigdogbay.lib.utils;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import com.pigdogbay.lib.diagnostics.Timing;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StringUtilsTests {


    private static final int LOOP = 100000;
    private static final String TAG = "StringUtilsTests";
    @Test
    public void wordSort() {
        Log.v(TAG, "Wordsort Time");
        Timing t = new Timing();
        String word = null;
        for (int i=0;i<LOOP;i++){
            word = StringUtils.WordSort("qazwsxedc");
        }
        t.logDuration(TAG);
        assertEquals("acdeqswxz", word);

    }

    /**
     * Check that each word is unique
     */
    @Test
    public void getSubWords1(){
        List<String> actual = StringUtils.GetSubWords("apple");
        //appl, appe, aple (this could appear twice as 2p's), ppl
        assertEquals(4,actual.size());
    }
    /**
     * Check that each word is unique
     */
    @Test
    public void getSubWords2(){
        List<String> actual = StringUtils.GetSubWords("people");
        assertEquals(4,actual.size());
    }
    /**
     * Check that each word is unique
     */
    @Test
    public void getSubWords3(){
        List<String> actual = StringUtils.GetSubWords("abcdefghi");
        assertEquals(9,actual.size());
    }
    /**
     * Check that each word is unique
     */
    @Test
    public void getSubWordsRecursive(){
        Timing t = new Timing();
        List<String> actual = StringUtils.GetSubWords("abcdefghi",6);
        t.logDuration(TAG);
        //7*8*9 + 8*9 + 9 = 585
        assertEquals(585,actual.size());
        for (String w : actual){
            Log.v(TAG,w);
        }
    }

    /**
     * Function test
     */
    @Test
    public void sortByLengthReverse1(){
        ArrayList<String> target = new ArrayList<>();
        target.add("apple");
        target.add("kiwi");
        target.add("Pineapple");
        target.add("kiw");
        target.add("Banana");
        StringUtils.sortByLengthReverse(target);
        assertEquals("Pineapple",target.get(0));
        assertEquals("kiwi", target.get(3));
        assertEquals("kiw", target.get(4));
    }
    /**
     * Timing test
     */
    @Test
    public void sortByLengthReverse2(){
        List<String> list = StringUtils.GetSubWords("abcdefghi",3);
        Timing t = new Timing();
        StringUtils.sortByLengthReverse(list);
        t.logDuration(TAG);
    }
    @Test
    public void sizeThenAtoZComparator1()
    {
        List<String> list = new ArrayList<>();
        list.add("pear");
        list.add("orange");
        list.add("lemon");
        list.add("banana");
        list.add("apple");
        list.add("grapefruit");

        Collections.sort(list, new StringUtils.sizeThenAtoZComparator());

        assertEquals(list.get(0),"grapefruit");
        assertEquals(list.get(1),"banana");
        assertEquals(list.get(2),"orange");
        assertEquals(list.get(3),"apple");
        assertEquals(list.get(4),"lemon");
        assertEquals(list.get(5),"pear");
    }


}
