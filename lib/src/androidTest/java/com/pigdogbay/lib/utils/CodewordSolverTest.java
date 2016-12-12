package com.pigdogbay.lib.utils;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Mark on 03/12/2016.
 * Unit tests for CodewordSolver
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class CodewordSolverTest {

    /**
     * Known letters
     */
    @Test
    public void knownLetters1(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse(".a.");
        assertThat(codewordSolver.isMatch("cat"), is(true));
        assertThat(codewordSolver.isMatch("dog"), is(false));
    }
    @Test
    public void knownLetters2(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("p..i.e");
        assertThat(codewordSolver.isMatch("praise"), is(true));
        assertThat(codewordSolver.isMatch("praist"), is(false));
        assertThat(codewordSolver.isMatch("xraise"), is(false));
        assertThat(codewordSolver.isMatch("prazse"), is(false));
    }
    /**
     * Known letters are different to unknown
     */
    @Test
    public void knownLetters3(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("st...");
        assertThat(codewordSolver.isMatch("stand"), is(true));
        assertThat(codewordSolver.isMatch("stats"), is(false));
        assertThat(codewordSolver.isMatch("stans"), is(false));
        assertThat(codewordSolver.isMatch("stint"), is(false));
    }
    /**
     * Multiple Known letters
     */
    @Test
    public void knownLetters4(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("aar..ar.");
        assertThat(codewordSolver.isMatch("aardvark"), is(true));
    }

    /**
     * Same letters
     */
    @Test
    public void sameLetters1(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("..11..");
        assertThat(codewordSolver.isMatch("balled"), is(true));
        assertThat(codewordSolver.isMatch("balked"), is(false));
    }
    @Test
    public void sameLetters2(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("112..12.");
        assertThat(codewordSolver.isMatch("aardvark"), is(true));
        assertThat(codewordSolver.isMatch("aardverk"), is(false));
        assertThat(codewordSolver.isMatch("aardvask"), is(false));
        assertThat(codewordSolver.isMatch("wardvark"), is(false));
    }
    @Test
    public void sameLetters4(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("12345671234567");
        assertThat(codewordSolver.isMatch("abcdefgabcdefg"), is(true));
        assertThat(codewordSolver.isMatch("abcdefgabcdefk"), is(false));
    }

    /**
     * Same letters - unknown letter is the same
     */
    @Test
    public void sameLetters3(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("112..12.");
        assertThat(codewordSolver.isMatch("aardvark"), is(true));
        assertThat(codewordSolver.isMatch("aardaark"), is(false));
        assertThat(codewordSolver.isMatch("aardvara"), is(false));
        assertThat(codewordSolver.isMatch("aardrark"), is(false));

    }

    /**
     * Unknown letters - check no groups in unknown letters
     */
    @Test
    public void unknownLetters1(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse(".l.ph...");
        assertThat(codewordSolver.isMatch("elephant"), is(false));
        assertThat(codewordSolver.isMatch("eliphant"), is(true));
    }

    /**
     * Found letters - check grouped letters haven't already been found
     */
    @Test
    public void foundLetters1(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("112..12.");
        codewordSolver.setFoundLetters("xst");
        assertThat(codewordSolver.isMatch("aardvark"), is(true));
        codewordSolver.setFoundLetters("stax");
        assertThat(codewordSolver.isMatch("aardvark"), is(false));
        codewordSolver.setFoundLetters("strx");
        assertThat(codewordSolver.isMatch("aardvark"), is(false));
    }

    /**
     * Found letters - check that unknown letters are not in the found letters
     */
    @Test
    public void foundLetters2(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("...");
        codewordSolver.setFoundLetters("xst");
        assertThat(codewordSolver.isMatch("dog"), is(true));
        assertThat(codewordSolver.isMatch("cat"), is(false));
    }

    /**
     * Real examples
     */
    @Test
    public void realExamples1(){
        CodewordSolver codewordSolver = new CodewordSolver();
        codewordSolver.parse("123423a14");
        codewordSolver.setFoundLetters("ba");
        assertThat(codewordSolver.isMatch("nurturant"), is(true));
    }




}
