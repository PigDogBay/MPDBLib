package com.pigdogbay.lib.utils;


import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.pigdogbay.lib.test.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class WordSolverTests {

    private static class LimitTestFilterFactory implements WordListCallbackAbstractFactory{
        @Override
        public WordListCallback createChainedCallback(WordListCallback lastCallback) {
            return new WordListCallback.LessThanFilter(lastCallback,5);
        }
    }

    @Test
    public void wordLimitTest(){
        WordSolver target = new WordSolver();
        target.wordListCallbackFactory = new LimitTestFilterFactory();
        target.setResultsLimit(42);
        target.loadDictionary(InstrumentationRegistry.getInstrumentation().getContext(), R.raw.standard);
        while (target.stateObservable.getValue()!= WordSolver.States.ready){
            try {Thread.sleep(100);} catch (InterruptedException ignored) {}
        }

        target.setAndValidateQuery("procureblast");
        target.prepareToSearch();
        target.search();
        while (target.stateObservable.getValue()!= WordSolver.States.finished){
            try {Thread.sleep(100);} catch (InterruptedException ignored) {}
        }
        assertThat(target.wordMatches.getMatches().size(),is(42));
    }


    @Test
    public void setAndValidateQuery1()
    {
        WordSolver target = new WordSolver();
        target.setAndValidateQuery("largebaps");
        assertEquals("largebaps",target.getQuery());
    }

    @Test
    public void getWordURL1()
    {
        assertEquals("https://www.google.com/search?q=dictionary:soldier",WordSolver.getWordURL("soldier"));
    }

    /**
     * Test loading, searching and state change callback
     */
    @Test
    public void search1()
    {
        WordSolver target = new WordSolver();
        target.stateObservable.addObserver((sender, update) -> {
            switch (update) {
                case uninitialized:
                    Log.v("wstests", "unitialized");
                    break;
                case loading:
                    Log.v("wstests", "loading");
                    break;
                case loadError:
                    Log.v("wstests", "load error");
                    fail();
                    break;
                case ready:
                    Log.v("wstests", "ready");
                    break;
                case searching:
                    Log.v("wstests", "searching");
                    break;
                case finished:
                    Log.v("wstests", "finished");
                    break;
                default:
                    break;

            }
        });
        target.loadDictionary(InstrumentationRegistry.getInstrumentation().getContext(), R.raw.standard);
        while (target.stateObservable.getValue()!= WordSolver.States.ready){
            try {Thread.sleep(100);} catch (InterruptedException ignored) {}
        }

        target.setAndValidateQuery("largebaps");
        target.prepareToSearch();
        target.search();
        while (target.stateObservable.getValue()!= WordSolver.States.finished){
            try {Thread.sleep(100);} catch (InterruptedException ignored) {}
        }
        assertEquals(347, target.wordMatches.getMatches().size());
        assertEquals("graspable", target.wordMatches.getWord(0));
    }

    /**
     * Test the match found callback
     */
    private int callbackCount=0;
    @Test
    public void search2()
    {
        WordSolver target = new WordSolver();
        callbackCount=0;
        target.matchObservable.addObserver((sender, update) -> {
            WordSolverTests.this.callbackCount++;
            Log.v("wstests", update);
        });
        target.loadDictionary(InstrumentationRegistry.getInstrumentation().getContext(), R.raw.standard);
        while (target.stateObservable.getValue()!= WordSolver.States.ready){
            try {Thread.sleep(100);} catch (InterruptedException ignored) {}
        }

        target.setAndValidateQuery("largebaps");
        target.prepareToSearch();
        target.search();
        while (target.stateObservable.getValue()!= WordSolver.States.finished){
            try {Thread.sleep(100);} catch (InterruptedException ignored) {}
        }

        assertEquals(WordSolver.TABLE_MAX_COUNT_TO_RELOAD, callbackCount);
    }

    @Test
    public void loadTest1(){
        WordSolver target = new WordSolver();
        target.loadDictionary(InstrumentationRegistry.getInstrumentation().getContext(), R.raw.standard,R.raw.phrases);
        while (target.stateObservable.getValue()!= WordSolver.States.ready){
            try {Thread.sleep(100);} catch (InterruptedException ignored) {}
        }
    }



}
