package com.pigdogbay.lib.math;

import org.junit.Test;

import static junit.framework.Assert.*;

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
        List<DPoint> target = DPoint.createList(new int[]{42,9,8,40});
        assertEquals(4,target.size());
        assertEquals(0D,target.get(0).X);
        assertEquals(2D,target.get(2).X);
        assertEquals(8D,target.get(2).Y);
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
        DPoint.sortByX(list);
        assertEquals(dpoint2,list.get(0));
        assertEquals(dpoint3, list.get(1));
        assertEquals(dpoint1,list.get(2));

    }

    @Test
    public void getMinY()
    {
        List<DPoint> target = DPoint.createList(new int[]{42,9,8,40});
        DPoint actual = DPoint.getMinY(target);
        assertEquals(8D,actual.Y);
    }
    @Test
    public void getMaxY()
    {
        List<DPoint> target = DPoint.createList(new int[]{42,49,8,40});
        DPoint actual = DPoint.getMaxY(target);
        assertEquals(49D,actual.Y);
    }
}
