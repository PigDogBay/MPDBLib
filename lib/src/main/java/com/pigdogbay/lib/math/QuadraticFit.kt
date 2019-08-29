package com.pigdogbay.lib.math

/**
 * Created by Mark on 28/05/2017.
 * Ported from C# Maths Libraray
 */
class QuadraticFit : LeastSquares() {

    /**
     * A quadratic function has the form:
     * f(X) = a +bX + cXX
     *
     * This method calculates the matrix of the X terms for each data point, without the coefficients (eg a=b=c=1)
     *
     * @return Matrix of the X terms of an quadratic equation
     */
    override//constant
    //X
    //X squared
    val xTermsMatrix: Matrix
        get() {

            val matrixX = Array(points.size) { DoubleArray(3) }
            for (i in points.indices) {
                matrixX[i][0] = 1.0
                matrixX[i][1] = points[i].X
                matrixX[i][2] = points[i].X * points[i].X
            }
            return Matrix(matrixX)
        }

    /**
     *
     * @param x value
     * @return y value
     */
    override fun calculate(x: Double): Double {
        requireNotNull(parameters) { "The equation parameters must be calculated first" }
        return parameters!!.getElement(0, 0) + parameters!!.getElement(1, 0) * x + parameters!!.getElement(2, 0) * x * x
    }
}
