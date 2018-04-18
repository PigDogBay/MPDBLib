package com.pigdogbay.lib.utils;

import org.junit.Test;
import static junit.framework.Assert.*;


public class RegexFilterTest implements WordListCallback {
    private String result = null;
    @Override
    public void Update(String result) {
        this.result = result;
    }

    @Test
    public void update1(){
        RegexFilter filter = new RegexFilter(this, "spect[a-z][a-z][a-z]");
        filter.Update("spectrum");
        assertEquals("spectrum",result);
        result = "";
        filter.Update("commodore");
        assertEquals("",result);
    }
    @Test
    public void update2(){
        RegexFilter filter = new RegexFilter(this, "");
        result = "";
        filter.Update("commodore");
        assertEquals("",result);
    }
    /*
        Bad pattern
     */
    @Test
    public void update3(){
        RegexFilter filter = new RegexFilter(this, "[a-[-z[]\\");
        result = "";
        filter.Update("commodore");
        assertEquals("",result);
    }

}
