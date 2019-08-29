package com.pigdogbay.lib.math

import java.util.ArrayList

/**
 * Created by Mark on 28/05/2017.
 * Ported from C# Maths Libraray
 *
 * This class fits a function, y=f(x), to a set of X,Y data values
 * using the method of least squares.
 *
 * Matrices are used to calculate the fit, see CalculateParameters()
 *
 * Subclasses implement the following methods that implement the function details:
 * calculate(double x): returns f(x)
 * getXTermsMatrix(): returns a matrix of each of the terms of the function, so that
 *
 * Y = Xβ   (Y is column vector of Y values, β is column vector of the function parameters/coefficients)
 *
 * For a quadratic, X matrix is implemented as such
 * 1   x1   x1^2
 * 1   x2   x2^2
 * 1   x3   x3^2    where x1, x2, x3... are the various x data values
 *
 * Y column vector
 * y1
 * y2
 * y3		where y1,y2,y3...are the various y data values
 *
 * β column vector of the coefficients of the polynomial terms:
 * a
 * b
 * c   (where y = a + bx + cx^2)
 *
 * Reference:
 * http://en.wikipedia.org/wiki/Linear_least_squares
 *
 */
abstract class LeastSquares {

    /**
     * @return the list of DataPoints that specify the X,Y data values that least squares algorithm will function fit
     */
    var points: List<DPoint> = ArrayList()
        protected set
    protected var parameters: Matrix? = null

    /**
     *
     * For a quadratic, X matrix is implemented as such
     * 1   x1   x1^2
     * 1   x2   x2^2
     * 1   x3   x3^2    where x1, x2, x3... are the various x data values
     *
     * (Note there are no coefficients,as these are the what we are ultimately trying to find and will be calculated in CalculateParameters())
     * @return Matrix of the X terms of a function for each x value in the list of X,Y data points
     */
    abstract val xTermsMatrix: Matrix

    /**
     *
     * Column vector of the y data values
     *
     * @return Column matrix of the y values
     */
    val yValuesMatrix: Matrix
        get() {
            val yvalues = Array(points.size) { DoubleArray(1) }
            for (i in points.indices) {
                yvalues[i][0] = points[i].Y
            }
            return Matrix(yvalues)
        }

    /**
     * Calculates the  y value, y = f(x)
     * Where f(x) is the function that has been calculated to fit the data points
     * Please note that the function parameters must be calculated first before this method can be called
     *
     * @param x value
     * @return Calculated y value
     */
    abstract fun calculate(x: Double): Double

    /**
     * Calculatest the function parameters, β column vector
     * The function uses the following matrix equation to calculate the parameters
     * β = Inverse(transpose(X) X) transpose(X)Y
     * Where X is the matrix returned by getXTermsMatrix() and Y is a column vector of the Y data values
     *
     * @return Column matrix, β, of the function parameters
     */
    fun calculateParameters(): Matrix {
        val X = xTermsMatrix
        val transposeX = X.transpose()
        var tmp = transposeX.multiply(X)
        tmp = tmp.inverse()
        tmp = tmp.multiply(transposeX)
        tmp = tmp.multiply(yValuesMatrix)
        this.parameters = tmp
        return tmp
    }

    /**
     * Calculates the mean Y value of the data
     *
     * @return mean Y value of the data
     */
    fun meanYValue(): Double {
        var mean = 0.0
        for (point in points) {
            mean += point.Y
        }
        return mean / points.size
    }

    /**
     * Sum of the squared errors is defined as
     * SSE = Σ (y-f(x))^2
     * Where y is the y value of a data point, f(x) is the calculated value of y
     *
     * @return Sum of the squared errors
     */
    fun sumOfSquaredErrors(): Double {
        var sse = 0.0
        for (point in points) {
            val difference = point.Y - calculate(point.X)
            sse += difference * difference
        }
        return sse
    }

    /**
     * Total sum of squares:
     * SST = Σ (y-ŷ)^2
     * Where ŷ is the mean of the y data values.
     *
     * @return Total sum of squares
     */
    fun totalSumOfSquares(): Double {
        var sst = 0.0
        val mean = meanYValue()
        for (point in points) {
            val difference = point.Y - mean
            sst += difference * difference
        }
        return sst
    }

    /**
     * The coefficient of determination (R-squared value) gives a indication of how well the model fits the data.
     * The values range from 0 to 1, 0 - poor fit, 1 - perfect fit
     *
     * R^2 = 1 - (SSE/SST)
     *
     * SSE - Sum of squared errors
     * SST - Total sum of squares
     *
     * @return Coefficient of determination (R-squared Value)
     */
    fun coefficientOfDetermination(): Double {
        return 1.0 - (sumOfSquaredErrors() / totalSumOfSquares())
    }
}
