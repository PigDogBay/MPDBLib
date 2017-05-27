package com.pigdogbay.lib.math;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * Created by Mark on 27/05/2017.
 *
 */
public class MatrixTest {

    private double[][] createArray(){
        double[][] array = {
                {1.0,2.056789,3.0},
                {4.0,5.0,6.0}
        };
        return array;
    }

    @Test
    public void inverseTest1()
    {
        Matrix identityMatrix = new Matrix(new double[][] { { 1, 0 }, { 0, 1 } });
        Matrix matrix = new Matrix(new double[][] { { 1, 3 }, { 2, 4 } });
        Matrix inverse = matrix.inverse();
        Matrix expected = new Matrix(new double[][] { { 4, -3 }, { -2, 1 } }, -0.5);
        assertThat(inverse,is(expected));

        Matrix multA = matrix.multiply(inverse);
        Matrix multB = inverse.multiply(matrix);
        assertThat(multA,is(identityMatrix));
        assertThat(multB,is(identityMatrix));
    }
    @Test
    public void inverseTest2()
    {
        Matrix matrix = new Matrix(new double[][] {
        { 1, 3, 0 },
        { 0, -2, 1 },
        {-1,0,0}});
        Matrix expected = new Matrix(new double[][] {
        { 0, 0, -3 },
        { 1, -0.0, 1 },
        {2,3,2}}, 1d / 3d);

        Matrix inverse = matrix.inverse();
        assertThat(inverse,is(expected));
    }
    @Test
    public void inverseTest3()
    {
        double[][] targetArray = {
            {1,-3,-2,7,9,1},
            {0,4,-1,4,55,7},
            {2,1,0,11,33,44},
            {3,4,5,6,7,8},
            {1,2,3,98,65,3},
            {0,0,33,2,45,67}};
        Matrix target = new Matrix(targetArray);
        Matrix inverse = target.inverse();
        Matrix expected = new Matrix(new double[][]{
        {1,0,0,0,0,0},
        {0,1,0,0,0,0},
        {0,0,1,0,0,0},
        {0,0,0,1,0,0},
        {0,0,0,0,1,0},
        {0,0,0,0,0,1}});
        Matrix actual = inverse.multiply(target);
        assertThat(actual.getElement(0,0),is(1.0));
        assertThat(actual.getElement(5,5),is(1.0));
    }


    @Test
    public void adjointTest1()
    {
			double[][] targetArray = {
            {4,7,0},
            {0,-1,1},
            {2,-2,5}};
        Matrix target = new Matrix(targetArray);
        Matrix adjoint = new Matrix(new double[][]{
        {-3,-35,7},
        {2,20,-4},
        {2,22,-4}});
        assertThat(target.adjoint(), is(adjoint));
    }

    @Test
    public void coFactorTest1()
    {
        double[][] targetArray = {
            {4,7,0},
            {0,-1,1},
            {2,-2,5}};
        Matrix target = new Matrix(targetArray);
        assertThat(-3.0, is(target.coFactor(0, 0)));
        assertThat(2.0,  is(target.coFactor(1, 0)));
        assertThat(2.0,  is(target.coFactor(2, 0)));
        assertThat(-35.0,is(target.coFactor(0, 1)));
        assertThat(20.0, is(target.coFactor(1, 1)));
        assertThat(22.0, is(target.coFactor(2, 1)));
        assertThat(7.0,  is(target.coFactor(0, 2)));
        assertThat(-4.0, is(target.coFactor(1, 2)));
        assertThat(-4.0, is(target.coFactor(2, 2)));
    }

    @Test
    public void minorTest1()
    {
		double[][] targetArray = {
            {1,-3,-2},
            {0,4,-1},
            {2,1,0}};
        Matrix target = new Matrix(targetArray);
        int x = 0;
        int y = 0;
        Matrix expected = new Matrix(new double[][]{
        {4,-1},
        {1,0}});
        Matrix actual;
        actual = target.minor(x, y);
        assertThat(expected,is(actual));
        x = 1;
        y = 1;
        expected = new Matrix(new double[][]{
        {1,-2},
        {2,0}});
        actual = target.minor(x, y);
        assertThat(expected,is(actual));
        x = 2;
        y = 0;
        expected = new Matrix(new double[][]{
        {0,4},
        {2,1}});
        actual = target.minor(x, y);
        assertThat(expected,is(actual));
    }
    @Test
    public void minorTest2()
    {
			double[][] targetArray = {
            {1,-3,-2,7,9,1},
            {0,4,-1,4,55,7},
            {2,1,0,11,33,44},
            {3,4,5,6,7,8},
            {1,2,3,98,65,3},
            {0,0,33,2,45,67}};
        Matrix target = new Matrix(targetArray);
        int x = 0;
        int y = 0;
        Matrix expected = new Matrix(new double[][]{
        {4,-1,4,55,7},
        {1,0,11,33,44},
        {4,5,6,7,8},
        {2,3,98,65,3},
        {0,33,2,45,67}});
        Matrix actual;
        actual = target.minor(x, y);
        assertThat(expected,is(actual));
        x = 3;
        y = 4;
        expected = new Matrix(new double[][]{
        {1,-3,-2,9,1},
        {0,4,-1,55,7},
        {2,1,0,33,44},
        {3,4,5,7,8},
        {0,0,33,45,67}});
        actual = target.minor(x, y);
        assertThat(expected,is(actual));
    }

    @Test
    public void determinantTest1()
    {
			double[][] targetArray = {
            {1,-3,-2},
            {0,4,-1},
            {2,1,0}};

        Matrix target = new Matrix(targetArray);
        double actual = target.determinant();
        assertThat(actual,is(23.0));
    }
    @Test
    public void determinantTest2()
    {
        double[][] targetArray = {
                {4,7,0},
                {0,-1,1},
                {2,-2,5}};

        Matrix target = new Matrix(targetArray);
        double actual = target.determinant();
        assertThat(actual,is(2.0));
    }
    @Test
    public void determinantTest3()
    {
        double[][] targetArray = {
                {4,7,0,56,78,12},
                {4,7,0,56,78,12},
                {4,7,0,56,78,12},
                {0,0,0,0,0,0},
                {4,7,0,56,78,12},
                {2,-2,51,22,33,3}};
        Matrix target = new Matrix(targetArray);
        double actual = target.determinant();
        assertThat(actual,is(0.0));
    }

    @Test
    public void multiplyTest1()
    {
        double[][] arrayA = {
                {1,2},
                {3,4}};
        double[][] arrayB = {
                {5,6},
                {7,8}};
        double[][] arrayExpected = {
                {19,22},
                {43,50}};
        Matrix matrixA = new Matrix(arrayA);
        Matrix matrixB = new Matrix(arrayB);
        Matrix expected = new Matrix(arrayExpected);
        Matrix result = matrixA.multiply(matrixB);
        assertThat(result,is(expected));
    }
    @Test
    public void multiplyTest2()
    {
        double[][] arrayA = {
                {1,2,3}};
        double[][] arrayB = {
                {1},
                {2},
                {3}};
        double[][] arrayExpected = {
                {14}};
        Matrix matrixA = new Matrix(arrayA);
        Matrix matrixB = new Matrix(arrayB);
        Matrix expected = new Matrix(arrayExpected);
        Matrix result = matrixA.multiply(matrixB);
        assertThat(result,is(expected));
    }
    @Test(expected = IllegalArgumentException.class)
    public void multiplyTest3()
    {
        double[][] arrayA = {
                {1,2,3}};
        Matrix matrixA = new Matrix(arrayA);
        matrixA.multiply(matrixA);
    }
    @Test
    public void multiplyTest4()
    {
        double[][] arrayA = {
                { 1,1 },
                { 2,2 },
                { 3,3 } };
        double[][] arrayB = {
                {1,2,3},
                {4,5,6}};
        double[][] arrayExpected = {
                {5,7,9},
                {10,14,18},
                {15,21,27}};
        Matrix matrixA = new Matrix(arrayA);
        Matrix matrixB = new Matrix(arrayB);
        Matrix expected = new Matrix(arrayExpected);
        Matrix result = matrixA.multiply(matrixB);
        assertThat(result,is(expected));
    }

    @Test
    public void addTest1(){
        Matrix matrixA = new Matrix(new double[][]{
            {1,2,3},
            {4,5,6},
            {7,8,9}});
        Matrix matrixB = new Matrix(new double[][]{
            {-4,0,2},
            {2,-1,-1},
            {6,-5,2}});
        Matrix expected = new Matrix(new double[][]{
            {-3,2,5},
            {6,4,5},
            {13,3,11}});
        Matrix result = matrixA.add(matrixB);
        assertThat(result,is(expected));
    }
    @Test
    public void subtractTest1(){
        Matrix matrixA = new Matrix(new double[][]{
                {1,2,3},
                {4,5,6},
                {7,8,9}});
        Matrix matrixB = new Matrix(new double[][]{
                {-4,0,2},
                {2,-1,-1},
                {6,-5,2}});
        Matrix expected = new Matrix(new double[][]{
                {5,2,1},
                {2,6,7},
                {1,13,7}});
        Matrix result = matrixA.subtract(matrixB);
        assertThat(result,is(expected));
    }

    @Test
    public void transpose1(){
			double[][] targetArray = {
                {1,2},
                {3,4}};
			double[][] expectedArray ={
                {1,3},
                {2,4}};

        Matrix target = new Matrix(targetArray);
        Matrix result = target.transpose();
        Matrix expected = new Matrix(expectedArray);
        assertThat(result,is(expected));
    }

    @Test
    public void transpose2(){
        double[][] targetArray = {
                {1,2},
                {3,4},
                {5,6}};
        double[][] expectedArray ={
                {1,3,5},
                {2,4,6}};

        Matrix target = new Matrix(targetArray);
        Matrix result = target.transpose();
        Matrix expected = new Matrix(expectedArray);
        assertThat(result,is(expected));
    }
    @Test
    public void transpose3(){
        double[][] targetArray = {
                {1,2,3},
                {4,5,6},
                {7,8,9}};
        double[][] expectedArray ={
                {1,4,7},
                {2,5,8},
                {3,6,9}};

        Matrix target = new Matrix(targetArray);
        Matrix result = target.transpose();
        Matrix expected = new Matrix(expectedArray);
        assertThat(result,is(expected));
        assertThat(result,not(target));
    }

    @Test
    public void getRowsColumns1(){
        Matrix matrix = new Matrix(createArray());
        assertThat(matrix.getRows(),is(2));
        assertThat(matrix.getColumns(),is(3));
    }
    @Test
    public void getElement1(){
        Matrix matrix = new Matrix(createArray());
        assertThat(matrix.getElement(1,1),is(5.0D));
    }
    @Test
    public void getElement2(){
        Matrix matrix = new Matrix(createArray(),42.0D);
        assertThat(matrix.getElement(1,1),is(5.0D*42.0D));
    }
    @Test
    public void toString1(){
        Matrix matrix = new Matrix(createArray());
        assertThat(matrix.toString(),is("[[1.0, 2.056789, 3.0], [4.0, 5.0, 6.0]]"));
    }
    @Test
    public void toEqualsHashcode1(){
        Matrix matrix1 = new Matrix(createArray(),5.345);
        Matrix matrix2 = new Matrix(createArray(),5.345);
        assertThat(matrix1.equals(matrix2),is(true));
        assertThat(matrix1.hashCode(),is(matrix2.hashCode()));
    }
    @Test
    public void toEqualsHashcode2(){
        Matrix matrix1 = new Matrix(createArray(),5.345);
        Matrix matrix2 = new Matrix(createArray(),5.0);
        assertThat(matrix1.equals(matrix2),is(false));
        assertThat(matrix1.hashCode(),not(matrix2.hashCode()));
    }

}
