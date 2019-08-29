package com.pigdogbay.lib.math;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mark on 03/07/2015.
 */
public class BestLineFitTest
{
    /*
//Excel calculates for the points:
// y = 2.7x -1.7
//R^2 = 0.9406
//Zero line fit, y = 2.2593x
 */
    @Test
    public void bestLineFit1()
    {
        BestLineFit target = new BestLineFit();
        target.Add(2, 3);
        target.Add(3, 7);
        target.Add(4, 10);
        target.Add(5, 11);

        assertEquals(2.7D,target.getSlope(),0.0001D);
        assertEquals(-1.7000D,target.getIntercept(),0.0001D);
        assertEquals(0.9406D,target.getRSquared(),0.0001D);
        assertEquals(2.2593D,target.getSlopeThroughZero(),0.0001D);
    }
    @Test
    public void bestLineFit2()
    {
        BestLineFit target = new BestLineFit();
        //y=6x-5
        target.Add(1, 1);
        target.Add(2, 7);
        target.Add(3, 13);
        target.Add(4, 19);

        assertEquals(7d, target.calculateY(2), 0.0);
        assertEquals(2d, target.calculateX(7), 0.0);
    }
    @Test
    public void bestLineFit3()
    {
        BestLineFit target = new BestLineFit();
        assertTrue(Double.isNaN(target.calculateY(0)));
        assertTrue(Double.isNaN(target.calculateX(0)));
        assertTrue(Double.isNaN(target.calculateY(42)));
        assertTrue(Double.isNaN(target.calculateX(42)));
    }
}
