package com.pigdogbay.lib.math;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Created by Mark on 20/06/2015.
 */
public class QuadraticEquationTest
{
    private static final double DELTA = 0.00001D;
    @Test
    public void linearEqu1()
    {
        //Linear equation y = 2x;
        QuadraticEquation target = new QuadraticEquation(0D,0D,1D,2D,2D,4D);
        assertEquals(0D,target.getA(),DELTA);
        assertEquals(2D,target.getB(),DELTA);
        assertEquals(0D,target.getC(),DELTA);

        double x = 5D, y = 10D;
        assertEquals(y,target.getY(x),DELTA);
        assertEquals(x,target.getX(y),DELTA);

    }
    @Test
    public void linearEqu2()
    {
        //Linear equation y = 3x-5;
        QuadraticEquation target = new QuadraticEquation(0D,-5D,1D,-2D,2D,1D);
        assertEquals(0D,target.getA(),DELTA);
        assertEquals(3D,target.getB(),DELTA);
        assertEquals(-5D,target.getC(),DELTA);

        double x = 5D, y = 10D;
        assertEquals(y,target.getY(x),DELTA);
        assertEquals(x,target.getX(y),DELTA);
    }
    @Test
    public void pathological0()
    {
        QuadraticEquation target = new QuadraticEquation(0D,0D,0D,0D,0D,0D);
    }
    @Test
    public void quadraticEqu1()
    {
        //Linear equation y = 2x^2+3x-5;
        QuadraticEquation target = new QuadraticEquation(0D,-5D,1D,0D,2D,9D);
        assertEquals(2D,target.getA(),DELTA);
        assertEquals(3D,target.getB(),DELTA);
        assertEquals(-5D,target.getC(),DELTA);

        double x = 5D, y = 60D;
        assertEquals(y,target.getY(x),DELTA);
        assertEquals(x,target.getX(y),DELTA);
    }
    @Test
    public void quadraticEqu2()
    {
        //Real data
        QuadraticEquation target = new QuadraticEquation(60D,57.187D,100D,94.424D,200D,191.129D);

        double x = 60D, y = 57.187D;
        assertEquals(y,target.getY(x),DELTA);
        assertEquals(x,target.getX(y),DELTA);
        x = 100D;
        y = 94.424D;
        assertEquals(y,target.getY(x),DELTA);
        assertEquals(x,target.getX(y),DELTA);
        x = 200D;
        y = 191.129D;
        assertEquals(y,target.getY(x),DELTA);
        assertEquals(x,target.getX(y),DELTA);
    }

    @Test
    public void quadraticEqu3()
    {
        //Real data
        QuadraticEquation target = new QuadraticEquation(60D,59.8D,100D,99.3D,200D,197.5D);
        double x = 60D, y = 59.8D;
        assertEquals(y,target.getY(x),DELTA);
        assertEquals(x,target.getX(y),DELTA);
        x = 100D;
        y = 99.3D;
        assertEquals(y,target.getY(x),DELTA);
        assertEquals(x,target.getX(y),DELTA);
        x = 200D;
        y = 197.5D;
        assertEquals(y,target.getY(x),DELTA);
        assertEquals(x,target.getX(y),DELTA);
    }
}
