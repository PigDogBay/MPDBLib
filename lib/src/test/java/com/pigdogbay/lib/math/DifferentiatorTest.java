package com.pigdogbay.lib.math;

import org.junit.Test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Mark on 03/07/2015.
 */
public class DifferentiatorTest {

    private List<DPoint> createPoints()
    {
        ArrayList<DPoint> points = new ArrayList<>(1000);
        int index = 0;
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 100; j++)
            {
                points.add(new DPoint(index, j));
                index++;
            }
            for (int j = 0; j < 100; j++)
            {
                points.add(new DPoint(index, 100-j));
                index++;
            }
        }
        return points;
    }

    @Test
    public void add1()
    {
        Differentiator target = new Differentiator();
        //y = 2xx +3x + 4
        //y' = 4x +3
        target.Add(0D, 4D);
        target.Add(1D, 9D);
        target.Add(2D, 18D);
        List<DPoint> points = target.getPoints();
        assertEquals(3, Objects.requireNonNull(points).size());
        assertEquals(0D, points.get(0).getX(),0.001);
        assertEquals(4D, points.get(0).getY(),0.001);
        assertEquals(1D, points.get(1).getX(),0.001);
        assertEquals(9D, points.get(1).getY(),0.001);
        assertEquals(2D, points.get(2).getX(),0.001);
        assertEquals(18D, points.get(2).getY(),0.001);
    }
    @Test
    public void differentiate1()
    {
        List<DPoint> points = createPoints();
        Differentiator target = new Differentiator(points);
        List<DPoint> diffPoints = target.Differentiate();

        assertEquals(points.size(), diffPoints.size());
        assertEquals(points.get(50).getX(), diffPoints.get(50).getX(),0.001);
        assertEquals(points.get(500).getX(), diffPoints.get(500).getX(),0.001);
        //gradient = 1
        assertEquals(1d, diffPoints.get(50).getY(),0.001);
        //gradient  = -1
        assertEquals(-1d, diffPoints.get(150).getY(),0.001);
        //turning point
        assertEquals(0d, diffPoints.get(100).getY(),0.001);

    }
    @Test
    public void endPoints()
    {
        List<DPoint> points = createPoints();
        Differentiator target = new Differentiator(points);
        List<DPoint> diffPoints = target.Differentiate();
        assertEquals(0D, diffPoints.get(0).getX(),0.001);
        assertEquals(1D, diffPoints.get(0).getY(),0.001);
        assertEquals(999D, diffPoints.get(999).getX(),0.001);
        assertEquals(-1D, diffPoints.get(999).getY(),0.001);

    }
    @Test
    public void sampleSize()
    {
        Differentiator target = new Differentiator();
        target.setSampleSize(9);
        //y=2x+1
        target.Add(0D, 1D);
        target.Add(1D, 3D);
        target.Add(2D, 5D);
        List<DPoint> diffPoints = target.Differentiate();
        assertEquals(3, diffPoints.size());
        //expect best line fit, which has gadient of 2
        assertEquals(2D, diffPoints.get(0).getY(),0.001);
        assertEquals(2D, diffPoints.get(1).getY(),0.001);
        assertEquals(2D, diffPoints.get(2).getY(),0.001);
    }

    /// <summary>
    /// Check algorithm gives resaonable results for differentiating a curve
    /// y = 3x*x -2x + 1
    /// y' = 6x-2
    /// </summary>
    @Test
    public void accuracy()
    {
        List<DPoint> points = new ArrayList<>();
        for (int i = -100; i < 101; i++)
        {
            double x = (double)i;
            double y = 3 * x * x - 2 * x + 1;
            points.add(new DPoint(x, y));
        }
        Differentiator target = new Differentiator(points);
        DPoint[] diffPoints = target.Differentiate().toArray(new DPoint[points.size()]);
        //x=0, y' = -2
        assertEquals(0d, diffPoints[100].getX(),0.001);
        assertEquals(-2d, diffPoints[100].getY(),0.001);
        //x=1, y'=4
        assertEquals(1d, diffPoints[101].getX(),0.001);
        assertEquals(4d, diffPoints[101].getY(),0.001);
        //x=-1, y'=-8
        assertEquals(-1d, diffPoints[99].getX(),0.001);
        assertEquals(-8d, diffPoints[99].getY(),0.001);
        //x=42, y'=250
        assertEquals(42d, diffPoints[142].getX(),0.001);
        assertEquals(250d, diffPoints[142].getY(),0.001);
    }
}
