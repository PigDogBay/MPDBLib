package com.pigdogbay.lib.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContainsFilterTest implements WordListCallback{

    private String result = null;
    @Override
    public void Update(String result) {
        this.result = result;
    }

    @Test
    public void update1(){
        ContainsFilter filter = new ContainsFilter(this, "pbh");
        filter.Update("phlebas");
        assertEquals("phlebas",result);
        result = "";
        filter.Update("public");
        assertEquals("",result);
    }

    //Multiple letters
    @Test
    public void update2(){
        ContainsFilter filter = new ContainsFilter(this, "ssaa");
        filter.Update("badass");
        assertEquals("badass",result);
        filter.Update("badasses");
        assertEquals("badasses",result);
        result = "";
        filter.Update("bad");
        assertEquals("",result);
        filter.Update("ass");
        assertEquals("",result);
        filter.Update("sinbad");
        assertEquals("",result);
    }
    @Test
    public void update3(){
        ContainsFilter filter = new ContainsFilter(this, "mm");
        filter.Update("commodore");
        assertEquals("commodore",result);
        result = "";
        filter.Update("spectrum");
        assertEquals("",result);
    }

    @Test
    public void update4(){
        ContainsFilter filter = new ContainsFilter(this, "iiiissssppm");
        filter.Update("mississippi");
        assertEquals("mississippi",result);
        filter.Update("mississiippi");
        assertEquals("mississiippi",result);
        result = "";
        filter.Update("mississppi");
        assertEquals("",result);
    }
    @Test
    public void update5(){
        ContainsFilter filter = new ContainsFilter(this, "ismp");
        filter.Update("mississippi");
        assertEquals("mississippi",result);
        result = "";
        filter.Update("mississiippi");
        assertEquals("mississiippi",result);
    }
    /*
        Regression test for bug where spaces caused a crash
     */
    @Test
    public void update6() {
        ContainsFilter filter = new ContainsFilter(this, "swift");
        filter.Update("words with friends");
        assertEquals("words with friends",result);
    }

}
