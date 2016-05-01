package com.pigdogbay.lib.utils;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.pigdogbay.lib.test.R;

import org.junit.After;
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
public class WordSolverTests {

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
        WordSolver target = new WordSolver();
        target.matches.add("tinker");
        target.matches.add("tailor");
        target.matches.add("soldier");
        target.matches.add("smiley (george)");
        assertEquals("http://www.google.com/search?q=define:soldier",target.getWordURL(2));
        assertEquals("http://www.google.com/search?q=define:smiley",target.getWordURL(3));
    }

    /**
     * Test loading, searching and state change callback
     */
    @Test
    public void search1()
    {
        WordSolver target = new WordSolver();
        target.stateObservable.addObserver(new ObservableProperty.PropertyChangedObserver<WordSolver.States>() {
            @Override
            public void update(ObservableProperty<WordSolver.States> sender, WordSolver.States update) {
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
            }
        });
        target.loadDictionary(getInstrumentation().getContext(), R.raw.standard);
        while (target.stateObservable.getValue()!= WordSolver.States.ready){
            try {Thread.sleep(100);} catch (InterruptedException e) {}
        }

        target.setAndValidateQuery("largebaps");
        target.prepareToSearch();
        target.search();
        while (target.stateObservable.getValue()!= WordSolver.States.finished){
            try {Thread.sleep(100);} catch (InterruptedException e) {}
        }
        assertEquals(263, target.matches.size());
        assertEquals("graspable", target.matches.get(0));
    }

    /**
     * Test the match found callback
     */
    int callbackCount=0;
    @Test
    public void search2()
    {
        WordSolver target = new WordSolver();
        callbackCount=0;
        target.matchObservable.addObserver(new ObservableProperty.PropertyChangedObserver<String>() {

            @Override
            public void update(ObservableProperty<String> sender, String update) {
                WordSolverTests.this.callbackCount++;
                Log.v("wstests", update);
            }

        });
        target.loadDictionary(getInstrumentation().getContext(), R.raw.standard);
        while (target.stateObservable.getValue()!= WordSolver.States.ready){
            try {Thread.sleep(100);} catch (InterruptedException e) {}
        }

        target.setAndValidateQuery("largebaps");
        target.prepareToSearch();
        target.search();
        while (target.stateObservable.getValue()!= WordSolver.States.finished){
            try {Thread.sleep(100);} catch (InterruptedException e) {}
        }

        assertEquals(WordSolver.TABLE_MAX_COUNT_TO_RELOAD, callbackCount);
    }



}
