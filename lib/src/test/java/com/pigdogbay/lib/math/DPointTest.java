package com.pigdogbay.lib.math;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 03/07/2015.
 */
public class DPointTest
{
    @Test
    public void createList1()
    {
        List<DPoint> target = DPoint.Companion.createList(new int[]{42,9,8,40});
        assertEquals(4,target.size());
        assertEquals(0D,target.get(0).getX(),0.1);
        assertEquals(2D,target.get(2).getX(),0.1);
        assertEquals(8D,target.get(2).getY(),0.1);
    }

    @Test
    public void sortByX1()
    {
        DPoint dpoint1 = new DPoint(42D,5D);
        DPoint dpoint2 = new DPoint(37D,9D);
        DPoint dpoint3 = new DPoint(38D,10D);

        List<DPoint> list = new ArrayList<>();
        list.add(dpoint1);
        list.add(dpoint2);
        list.add(dpoint3);
        DPoint.Companion.sortByX(list);
        assertEquals(dpoint2,list.get(0));
        assertEquals(dpoint3, list.get(1));
        assertEquals(dpoint1,list.get(2));

    }

    @Test
    public void getMinY()
    {
        List<DPoint> target = DPoint.Companion.createList(new int[]{42,9,8,40});
        DPoint actual = DPoint.Companion.getMinY(target);
        assertEquals(8D,actual.getY(),0.001);
    }
    @Test
    public void getMaxY()
    {
        List<DPoint> target = DPoint.Companion.createList(new int[]{42,49,8,40});
        DPoint actual = DPoint.Companion.getMaxY(target);
        assertEquals(49D,actual.getY(),0.001);
    }
}
