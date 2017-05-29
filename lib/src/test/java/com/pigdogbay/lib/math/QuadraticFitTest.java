package com.pigdogbay.lib.math;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mark on 28/05/2017.
 *
 */

public class QuadraticFitTest {



    /**
     * Data from F200 / SMP40 temperature measurements
     * Exel gives the following formula:
     * y = -2E-05x2 + 1.0171x - 0.1298
     *
     */
    private DPoint[] realData()
    {
        return new DPoint[]{
                new DPoint(49.98,50.527d),
                new DPoint(59.98,60.734d),
                new DPoint(69.98,70.938d),
                new DPoint(79.99	,81.12d),
                new DPoint(89.99	,91.288d),
                new DPoint(99.99	,101.451d),
                new DPoint(110	,111.609d),
                new DPoint(119.98	,121.752d),
                new DPoint(129.99	,131.88d),
                new DPoint(139.98	,142.008d),
                new DPoint(149.99	,152.14d),
                new DPoint(159.99	,162.253d),
                new DPoint(169.99	,172.361d),
                new DPoint(179.99	,182.474d),
                new DPoint(190	,192.591d),
                new DPoint(199.99	,202.686d),
                new DPoint(209.99	,212.796d),
                new DPoint(219.99	,222.898d),
                new DPoint(229.99	,233.01d),
                new DPoint(239.99	,243.097d),
                new DPoint(249.99	,253.199d),
                new DPoint(259.99	,263.276d),
                new DPoint(270	,273.357d),
                new DPoint(279.99	,283.424d),
                new DPoint(289.98	,293.484d),
                new DPoint(300	,303.556d),
                new DPoint(310	,313.63d),
                new DPoint(319.99	,323.689d),
                new DPoint(330	,333.758d),
                new DPoint(340	,343.827d),
                new DPoint(349.98	,353.888d),
                new DPoint(359.99	,363.98d),
                new DPoint(370	,374.092d),
                new DPoint(379.98	,384.186d),
                new DPoint(390	,394.349d),
                new DPoint(399.99	,404.49d)};
    }


    @Test
    public void getXTermsMatrixTest1()
    {
        //Excel generated data, y = 5 -7x +2xx
        QuadraticFit target = new QuadraticFit();
        target.getPoints().add(new DPoint(0, 5));
        target.getPoints().add(new DPoint(1, 0));
        target.getPoints().add(new DPoint(2, -1));
        target.getPoints().add(new DPoint(3, 2));
        target.getPoints().add(new DPoint(4, 9));
        target.getPoints().add(new DPoint(5, 20));

        Matrix expected = new Matrix(new double[][]{
        {1,0,0},
        {1,1,1},
        {1,2,4},
        {1,3,9},
        {1,4,16},
        {1,5,25}});

        assertThat(target.getXTermsMatrix(),is(expected));
    }

    @Test
    public void parametersTest1()
    {
        QuadraticFit target = new QuadraticFit();
        target.getPoints().addAll(Arrays.asList(realData()));
        Matrix parameters = target.calculateParameters();
        /// Exel gives the following formula:
        /// y = -2E-05x2 + 1.0171x - 0.1298
        //Constant term
        assertEquals(-0.1298, parameters.getElement(0,0),0.0001);
        //x term
        assertEquals(1.0171, parameters.getElement(1,0),0.0001);
        //x squared term
        assertEquals(-0.00002, parameters.getElement(2,0),0.00001);
    }

    @Test
    public void calculateTest1()
    {
        //Excel generated data, y = 5 -7x +2xx
        QuadraticFit target = new QuadraticFit();
        target.getPoints().add(new DPoint(0, 5));
        target.getPoints().add(new DPoint(1, 0));
        target.getPoints().add(new DPoint(2, -1));
        target.getPoints().add(new DPoint(3, 2));
        target.getPoints().add(new DPoint(4, 9));
        target.getPoints().add(new DPoint(5, 20));
        target.calculateParameters();

        assertEquals(target.calculate(42.0),3239.0,0.1);
    }

}
