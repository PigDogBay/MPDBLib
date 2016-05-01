package com.pigdogbay.lib.math;


/**
 * Created by peter.bradbury on 18/12/2014.
 */

//y = ax^2 + bx + c
public class  QuadraticEquation
{
    private double _A = 0, _B = 0, _C = 0;


    public QuadraticEquation(double x0, double y0, double x1, double y1, double x2, double y2) {
        //b = [(y₀-y₁)(x₁²-x₂²)+(y₂-y₁)(x₀²-x₁²)] ⁄ [(x₀-x₁)(x₁²-x₂²)+(x₂-x₁)(x₀²-x₁²)]
        double bNumerator = ((y0 - y1) * ((x1 * x1) - (x2 * x2)) + (y2 - y1) * ((x0 * x0) - (x1 * x1))),
                bDenominator = ((x0 - x1) * ((x1 * x1) - (x2 * x2)) + (x2 - x1) * ((x0 * x0) - (x1 * x1)));
        if (bDenominator != 0) {
            _B = bNumerator / bDenominator;
        } else
        {
            if (bNumerator == 0)_B = 1;
        }

        //a = [y₀-y₁-b⋅(x₀-x₁)] ⁄ (x₀²-x₁²)
        _A = ( y0-y1-_B*(x0-x1) ) / ( (x0*x0)-(x1*x1) );

        //c = y₀ - a⋅x₀² - b⋅x₀
        _C = y0 - (_A*(x0*x0)) - (_B*(x0));
    }

    public QuadraticEquation(double a, double b, double c)
    {
        _A = a;
        _B = b;
        _C = c;
    }

    public double getY(double x)
    {
        double y;
        y = _A*(x*x) + _B*x + _C;
        return y;
    }

    //x = ( -b +- (b² - 4a(c-y))^0.5 )/2a
    public double getX(double y)
    {
        double result;
        if(_A!=0) {
            double squareRoot = Math.sqrt((_B * _B) - (4 * _A * (_C - y)));
            double add = (-_B + squareRoot) / (2 * _A);

            if (add >= 0) result = add;
            else {
                double subtract = (-_B - squareRoot) / (2 * _A);
                result =  subtract;
            }
        }
        else if(_B!=0)
        {
            //x= (c-y)/b
            result = (y-_C)/_B;
        }
        else
        {
            result = y-_C;
        }
        return result;
    }

    public void ScaleEquation(double scaleFactor)
    {
        _A *= scaleFactor;
        _B *= scaleFactor;
        _C *= scaleFactor;
    }

    public double getA() {
        return _A;
    }

    public void setA(double _A) {
        this._A = _A;
    }

    public double getB() {
        return _B;
    }

    public void setB(double _B) {
        this._B = _B;
    }

    public double getC() {
        return _C;
    }

    public void setC(double _C) {
        this._C = _C;
    }
}