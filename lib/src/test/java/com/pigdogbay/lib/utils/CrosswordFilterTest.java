package com.pigdogbay.lib.utils;

import org.junit.Test;
import static junit.framework.Assert.*;

public class CrosswordFilterTest implements WordListCallback {
    private String result = null;
    @Override
    public void Update(String result) {
        this.result = result;
    }

    @Test
    public void update1(){
        CrosswordFilter filter = new CrosswordFilter(this, ".p.c...m");
        filter.Update("spectrum");
        assertEquals("spectrum",result);
        result = "";
        filter.Update("commodore");
        assertEquals("",result);
    }
    @Test
    public void update2(){
        CrosswordFilter filter = new CrosswordFilter(this, "@rum");
        filter.Update("spectrum");
        assertEquals("spectrum",result);
        result = "";
        filter.Update("commodore");
        assertEquals("",result);
    }
    @Test
    public void update3(){
        CrosswordFilter filter = new CrosswordFilter(this, ".p@um");
        filter.Update("spectrum");
        assertEquals("spectrum",result);
        result = "";
        filter.Update("commodore");
        assertEquals("",result);
    }
    @Test
    public void update4(){
        CrosswordFilter filter = new CrosswordFilter(this, "");
        result="";
        filter.Update("spectrum");
        assertEquals("",result);
    }
    @Test
    public void update5(){
        CrosswordFilter filter = new CrosswordFilter(this, "........");
        filter.Update("spectrum");
        assertEquals("spectrum",result);
        result = "";
        filter.Update("commodore");
        assertEquals("",result);
    }
}
