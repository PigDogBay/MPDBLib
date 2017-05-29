package com.pigdogbay.lib.math;

/**
 * Created by Mark on 28/05/2017.
 *  Ported from C# Maths Libraray
 */
public class QuadraticFit extends LeastSquares{

    /**
     * A quadratic function has the form:
     * f(X) = a +bX + cXX
     *
     * This method calculates the matrix of the X terms for each data point, without the coefficients (eg a=b=c=1)
     *
     * @return Matrix of the X terms of an quadratic equation
     */
    @Override
    public Matrix getXTermsMatrix() {

        double[][] matrixX = new double[points.size()][3];
        for (int i = 0; i < points.size(); i++)
        {
            //constant
            matrixX[i][0] = 1;
            //X
            matrixX[i][1] = points.get(i).X;
            //X squared
            matrixX[i][2] = points.get(i).X * points.get(i).X;
        }
        return new Matrix(matrixX);
    }

    /**
     *
     * @param x value
     * @return y value
     */
    @Override
    public double calculate(double x) {
        if (parameters == null)
        {
            throw new IllegalArgumentException("The equation parameters must be calculated first");
        }
        return parameters.getElement(0,0) + parameters.getElement(1,0) * x + parameters.getElement(2,0) * x * x;
    }
}
