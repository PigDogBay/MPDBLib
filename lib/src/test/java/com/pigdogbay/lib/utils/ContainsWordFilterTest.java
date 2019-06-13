package com.pigdogbay.lib.utils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ContainsWordFilterTest implements WordListCallback {

    private String result = null;
    @Override
    public void Update(String result) {
        this.result = result;
    }

    @Test
    public void update1(){
        ContainsWordFilter filter = new ContainsWordFilter(this, "mark");
        filter.Update("mark");
        assertEquals("mark",result);
        filter.Update("marksman");
        assertEquals("marksman",result);
        filter.Update("birthmark");
        assertEquals("birthmark",result);
        filter.Update("pockmarked");
        assertEquals("pockmarked",result);
        result="";
        filter.Update("bark");
        assertEquals("",result);
        filter.Update("marrow");
        assertEquals("",result);
        filter.Update("marak");
        assertEquals("",result);
        filter.Update("mrak");
        assertEquals("",result);
    }
    @Test
    public void update2() {
        ContainsWordFilter filter = new ContainsWordFilter(this, "");
        filter.Update("spectrum");
        assertEquals("spectrum",result);
    }
    @Test
    public void update3() {
        ContainsWordFilter filter = new ContainsWordFilter(this, "t");
        filter.Update("spectrum");
        assertEquals("spectrum",result);
    }
    /*
        Regression test for bug where spaces caused a crash
     */
    @Test
    public void update4() {
        ContainsWordFilter filter = new ContainsWordFilter(this, "swift");
        filter.Update("as swift as an arrow");
        assertEquals("as swift as an arrow",result);
    }

}
