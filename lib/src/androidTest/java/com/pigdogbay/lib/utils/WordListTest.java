package com.pigdogbay.lib.utils;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.SmallTest;
import android.util.Log;

import com.pigdogbay.lib.test.R;
import com.pigdogbay.lib.diagnostics.Timing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class WordListTest {

    private static final String TAG = "WordListTest";
    private class TestWordListCallback implements WordListCallback
    {
        ArrayList<String> matches = new ArrayList<String>();
        public List<String> GetMatches() {return matches;}

        @Override
        public void Update(String result)
        {
            matches.add(result);
        }
    }

    List<String> CreateList()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("spectrum");
        list.add("commodore");
        list.add("oric");
        list.add("dragon");
        list.add("nextstep");
        list.add("vale");
        list.add("evesham");
        list.add("vaio");
        list.add("acorn");
        list.add("electron");
        list.add("amiga");
        list.add("atari");
        list.add("micro");
        list.add("eight");
        list.add("pectrums");
        list.add("bit");
        list.add("murtpecs");
        list.add("sinclair");
        list.add("research");
        return list;
    }
    List<String> LoadList()
    {
        InputStream inputStream = null;
        try
        {
            inputStream = getInstrumentation().getContext().getResources().openRawResource(R.raw.standard);
            return FileUtils.ReadLines(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally
        {
            if (inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }

    }
    @Test
    public void findCodeWords1()
    {
        TestWordListCallback callback = new TestWordListCallback();
        WordList target = new WordList();
        target.SetWordList(LoadList());
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("112..12.");
        codewordSolver.setFoundLetters("");
        Timing timing = new Timing();
        target.findCodewords(codewordSolver,callback);
        assertEquals(1, callback.GetMatches().size());
        assertThat(callback.GetMatches().get(0),is("aardvark"));
        timing.LogDuration(TAG);
    }

    @Test
    public void FindWildcardWords()
    {
        TestWordListCallback callback = new TestWordListCallback();
        WordList target = new WordList();
        target.SetWordList(LoadList());
        Timing timing = new Timing();
        target.FindWildcardWords("t#and",callback);
        assertEquals(10, callback.GetMatches().size());
        timing.LogDuration(TAG);
    }

    @Test
    public void FindMultiwordAnagramsTest1()
    {
        TestWordListCallback callback = new TestWordListCallback();
        WordList target = new WordList();
        target.SetWordList(LoadList());
        Timing timing = new Timing();
        target.FindMultiwordAnagrams("manchester", "united", callback);
        assertEquals(51, callback.GetMatches().size());
        assertEquals("detachment insure",callback.GetMatches().get(7));
        assertEquals("unmastered ethnic",callback.GetMatches().get(49));
        timing.LogDuration(TAG);
    }
    @Test
    public void FindMultiwordAnagramsTest2()
    {
        TestWordListCallback callback = new TestWordListCallback();
        WordList target = new WordList();
        target.SetWordList(LoadList());
        Timing timing = new Timing();
        target.FindMultiwordAnagrams("united","manchester", callback);
        assertEquals(51, callback.GetMatches().size());
        assertEquals("dement raunchiest",callback.GetMatches().get(7));
        assertEquals("tamest unenriched",callback.GetMatches().get(42));
        timing.LogDuration(TAG);
    }

    @Test
    public void FindPartialWordsTest1()
    {
        TestWordListCallback callback = new TestWordListCallback();
        WordList target = new WordList();
        target.SetWordList(CreateList());
        target.FindPartialWords("a....",callback);
        assertEquals(3,callback.GetMatches().size());
        assertEquals("acorn",callback.GetMatches().get(0));
        assertEquals("amiga",callback.GetMatches().get(1));
        assertEquals("atari",callback.GetMatches().get(2));
    }

    @Test
    public void FindAnagramsTest1()
    {
        TestWordListCallback callback = new TestWordListCallback();
        WordList target = new WordList();
        target.SetWordList(CreateList());
        target.FindAnagrams("trumspec",callback);
        assertEquals(3,callback.GetMatches().size());
        assertEquals("spectrum",callback.GetMatches().get(0));
        assertEquals("pectrums",callback.GetMatches().get(1));
        assertEquals("murtpecs",callback.GetMatches().get(2));
    }
    @Test
    public void FindAnagramsTest2()
    {
        TestWordListCallback callback = new TestWordListCallback();
        WordList target = new WordList();
        target.SetWordList(CreateList());
        target.FindAnagrams("think",2,callback);
        assertEquals(2,callback.GetMatches().size());
        assertEquals("eight",callback.GetMatches().get(0));
        assertEquals("bit",callback.GetMatches().get(1));
    }

    /*
     * convertQuery() and runQuery() have been removed, parsing is now done by WordSearch
     * These tests have been updated as they still stress the underlying search algorithms
     */
    TestWordListCallback runQueryHelper(String query)
    {
        TestWordListCallback callback = new TestWordListCallback();
        WordList wordlist = new WordList();
        wordlist.SetWordList(CreateList());
        WordSearch wordSearch = new WordSearch(wordlist);
        query = wordSearch.preProcessQuery(query);
        WordSearch.SearchType type = wordSearch.getQueryType(query);
        query = wordSearch.postProcessQuery(query, type);
        wordSearch.runQuery(query, type, callback);
        return callback;

    }
    @Test
    public void RunQueryTest1()
    {
        TestWordListCallback callback = runQueryHelper("CRAno");
        assertTrue(callback.GetMatches().size() == 1);
        assertEquals("acorn",callback.GetMatches().get(0));
    }


    @Test
    public void RunQueryTest2()
    {
        TestWordListCallback callback = runQueryHelper("murTCeps");
        assertTrue(callback.GetMatches().size() == 3);
        assertEquals("spectrum", callback.GetMatches().get(0));
        assertEquals("pectrums", callback.GetMatches().get(1));
    }


    @Test
    public void RunQueryTest3()
    {
        TestWordListCallback callback = runQueryHelper("IBM");
        assertTrue(callback.GetMatches().size() == 0);
    }


    @Test
    public void RunQueryTest4()
    {
        TestWordListCallback callback = runQueryHelper("sp.c....");
        assertTrue(callback.GetMatches().size() == 1);
        assertEquals("spectrum", callback.GetMatches().get(0));
    }


    @Test
    public void RunQueryTest5()
    {
        TestWordListCallback callback = runQueryHelper("a..r.");
        assertEquals(2, callback.GetMatches().size());
        assertEquals("acorn", callback.GetMatches().get(0));
        assertEquals("atari", callback.GetMatches().get(1));
    }


    @Test
    public void RunQueryTest6()
    {
        TestWordListCallback callback = runQueryHelper("zx????");
        assertTrue(callback.GetMatches().size() == 0);
    }

    @Test
    public void RunQueryTest7()
    {
        TestWordListCallback callback = runQueryHelper("sp3r2");
        assertEquals(1, callback.GetMatches().size());
        assertEquals("spectrum", callback.GetMatches().get(0));
    }

    @Test
    public void RunQueryTest8()
    {
        TestWordListCallback callback = runQueryHelper("#on");
        assertTrue(callback.GetMatches().size() == 2);
        assertEquals("dragon", callback.GetMatches().get(0));
        assertEquals("electron", callback.GetMatches().get(1));
    }

    @Test
    public void RunQueryTest9()
    {
        TestWordListCallback callback = runQueryHelper("#o.");
        assertTrue(callback.GetMatches().size() == 2);
        assertEquals("dragon", callback.GetMatches().get(0));
        assertEquals("electron", callback.GetMatches().get(1));
    }


    @Test
    public void FindSubAnagramsTest1()
    {
        TestWordListCallback callback = new TestWordListCallback();
        List<String> list = LoadList();
        WordList target = new WordList();
        target.SetWordList(list);
        target.FindSubAnagrams("earths",callback);
        assertEquals(128, callback.GetMatches().size());

    }

    @Test
    public void FindSubAnagramsTest2()
    {
        TestWordListCallback callback = new TestWordListCallback();
        List<String> list = LoadList();
        WordList target = new WordList();
        target.SetWordList(list);
        target.FindSubAnagrams("galls",callback);
        assertEquals(13, callback.GetMatches().size());

    }

    @Test
    public void FindSubAnagramsTest3()
    {
        TestWordListCallback callback = new TestWordListCallback();
        List<String> list = LoadList();
        WordList target = new WordList();
        target.SetWordList(list);
        target.FindSubAnagrams("X",callback);
        assertEquals(0, callback.GetMatches().size());

    }

    @Test
    public void FindSubAnagramsTest4()
    {
        TestWordListCallback callback = new TestWordListCallback();
        List<String> list = LoadList();
        WordList target = new WordList();
        target.SetWordList(list);
        target.FindSubAnagrams("xy",callback);
        assertEquals(0, callback.GetMatches().size());

    }
    @Test
    public void FindSupergrams1()
    {
        TestWordListCallback callback = new TestWordListCallback();
        WordList target = new WordList();
        target.SetWordList(LoadList());
        Timing timing = new Timing();
        target.FindSupergrams("kayleigh",callback,0);
        assertEquals(2, callback.GetMatches().size());
        timing.LogDuration(TAG);
    }
    @Test
    public void FindSupergrams2()
    {
        TestWordListCallback callback = new TestWordListCallback();
        WordList target = new WordList();
        target.SetWordList(LoadList());
        Timing timing = new Timing();
        target.FindSupergrams("kayleigh",callback,14);
        assertEquals(1, callback.GetMatches().size());
        assertEquals("breathtakingly", callback.GetMatches().get(0));
        target.FindSupergrams("kayleigh",callback,15);
        assertEquals(2, callback.GetMatches().size());
        assertEquals("heartbreakingly", callback.GetMatches().get(1));
        timing.LogDuration(TAG);
    }
    @Test
    public void IsSupergram1() {
        LetterSet target = new LetterSet("err");
        assertTrue(target.isSupergram("supergram"));
    }
    @Test
    public void IsSupergram2() {
        LetterSet target = new LetterSet("supergram");
        assertTrue(target.isSupergram("supergram"));
    }
    @Test
    public void IsSupergram3() {
        LetterSet target = new LetterSet("eri");
        assertFalse(target.isSupergram("supergram"));
    }
    @Test
    public void IsSupergram4() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        LetterSet target = new LetterSet(alphabet);
        assertTrue(target.isSupergram(alphabet));
    }

    @Test
    public void Timing1()
    {
        LetterSet target = new LetterSet("super");
        Timing t = new Timing();
        for (int i=0;i<10000;i++)
        {
            boolean actual = target.isSupergram("supergram");
            assertTrue(actual);
        }
        t.LogDuration("ASUT");

    }

}
