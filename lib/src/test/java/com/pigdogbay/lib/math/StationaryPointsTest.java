package com.pigdogbay.lib.math;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 03/07/2015.
 */
public class StationaryPointsTest
{

    @Test
    public void calculateTurningPoints1()
    {
        //y = x*x + 2x -1
        //y' = 2x +2
        //Stationary point at x = - 1
        List<DPoint> points = new ArrayList<>();
        for (int i = -100; i < 101; i++)
        {
            double x = (double)i;
            double y = x * x + 2d * x - 1d;
            points.add(new DPoint(x, y));
        }
        StationaryPoints target = new StationaryPoints(points);
        List<TurningPoint> actual;
        actual = target.calculateTurningPoints(0.0);
        assertEquals(1, actual.size());
        assertFalse(actual.get(0).isPeak());
        assertEquals(-1d, actual.get(0).getPoint().getX(),0.001);
        //Y = -2 at x = -1. Note turning point y value is NOT the differential
        assertEquals(-2d, actual.get(0).getPoint().getY(),0.001);
        assertEquals(99, actual.get(0).getIndex());
    }

    @Test
    public void calculateTurningPoints2()
    {
        //y = x*x*x - x*x - x +1
        //y' = 3x*x -2x -1 = (3x+1)(x-1)
        //Stationary points solutions x = -1/3 (peak) , +1 (trough)
        List<DPoint> points = new ArrayList<>();
        for (double x = -10d; x < 10d; x=x+0.01d)
        {
            double y = x * x * x - x * x - x + 1d;
            points.add(new DPoint(x, y));
        }
        StationaryPoints target = new StationaryPoints(points);
        List<TurningPoint> actual;
        actual = target.calculateTurningPoints(0.0);
        assertEquals(2, actual.size());
        assertTrue(actual.get(0).isPeak());
        assertEquals(-0.33D, actual.get(0).getPoint().getX(), 0.005D);
        assertFalse(actual.get(1).isPeak());
        assertEquals(1.00D, actual.get(1).getPoint().getX(), 0.005D);
    }

}
