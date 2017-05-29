package com.pigdogbay.lib.math;

import com.pigdogbay.lib.math.DPoint;
import com.pigdogbay.lib.math.LeastSquares;
import com.pigdogbay.lib.math.Matrix;
import com.pigdogbay.lib.math.QuadraticFit;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mark on 28/05/2017.
 *
 */
public class LeastSquaresTest {

    @Test
    public void calculateParametersTest1()
    {
        LeastSquares target = new QuadraticFit();
        //Excel generated data, y = 5 -7x +2xx
        target.getPoints().add(new DPoint(0, 5));
        target.getPoints().add(new DPoint(1, 0));
        target.getPoints().add(new DPoint(2, -1));
        target.getPoints().add(new DPoint(3, 2));
        target.getPoints().add(new DPoint(4, 9));
        target.getPoints().add(new DPoint(5, 20));

        Matrix expected = new Matrix(new double[][]{
        {5},
        {-7},
        {2}});
        Matrix actual;
        actual = target.calculateParameters();
        //			Debug.WriteLine(actual.ToString());
        //			Debug.WriteLine(expected.ToString());
        //have to string compare due to double precision
        assertEquals(expected.getElement(0,0),actual.getElement(0,0),0.0001);
        assertEquals(expected.getElement(1,0),actual.getElement(1,0),0.0001);
        assertEquals(expected.getElement(2,0),actual.getElement(2,0),0.0001);
    }

    /// <summary>
    ///A test for CalculateParameters, y = x*x
    ///</summary>
    @Test
    public void calculateParametersTest2()
    {
        LeastSquares target = new QuadraticFit();
        target.getPoints().add(new DPoint(2, 4));
        target.getPoints().add(new DPoint(4, 16));
        target.getPoints().add(new DPoint(5, 25));

        Matrix expected = new Matrix(new double[][]{
        {0},
        {0},
        {1}});
        Matrix actual;
        actual = target.calculateParameters();
        //			Debug.WriteLine(actual.ToString());
        //			Debug.WriteLine(expected.ToString());
        //have to string compare due to double precision
        assertEquals(expected.getElement(0,0),actual.getElement(0,0),0.0001);
        assertEquals(expected.getElement(1,0),actual.getElement(1,0),0.0001);
        assertEquals(expected.getElement(2,0),actual.getElement(2,0),0.0001);
    }

    /// <summary>
    ///A test for GetYValuesMatrix
    ///</summary>
    @Test
    public void getYValuesMatrixTest()
    {
        LeastSquares target = new QuadraticFit();
        //Excel generated data, y = 5 -7x +2xx
        target.getPoints().add(new DPoint(0, 5));
        target.getPoints().add(new DPoint(1, 0));
        target.getPoints().add(new DPoint(2, -1));
        target.getPoints().add(new DPoint(3, 2));
        target.getPoints().add(new DPoint(4, 9));
        target.getPoints().add(new DPoint(5, 20));

        Matrix expected = new Matrix(new double[][]{
        {5},
        {0},
        {-1},
        {2},
        {9},
        {20}});
        Matrix actual = target.getYValuesMatrix();
        assertThat(actual, is(expected));
    }

    /// <summary>
    /// Using Excel:
    /// y = -0.3485x2 + 2.8758x + 1.0909
    /// R2 = 0.9564
    /// Sum(y) = 41
    /// </summary>
    DPoint[] testData1()
    {
        DPoint[] testData = new DPoint[]{
                new DPoint(0,1),
                new DPoint(1,3),
                new DPoint(2,6),
                new DPoint(3,7),
                new DPoint(4,8),
                new DPoint(5,6),
                new DPoint(6,5),
                new DPoint(7,4),
                new DPoint(8,2),
                new DPoint(9,-1)};
        return testData;
    }
    @Test
   public void calculateParametersTest3()
    {
        LeastSquares target = new QuadraticFit();
        target.getPoints().addAll(Arrays.asList(testData1()));
        Matrix coeff = target.calculateParameters();
        assertEquals(1.0909, coeff.getElement(0,0),0.0001);
        assertEquals(2.8758, coeff.getElement(1,0),0.0001);
        assertEquals(-0.3485, coeff.getElement(2,0),0.0001);
    }

    @Test
    public void coefficientOfDeterminationTest()
    {
        LeastSquares target = new QuadraticFit();
        target.getPoints().addAll(Arrays.asList(testData1()));
        Matrix coeff = target.calculateParameters();
        assertEquals(4.1, target.meanYValue(),0.01);
        assertEquals(0.9564, target.coefficientOfDetermination(),0.0001);
    }

}
