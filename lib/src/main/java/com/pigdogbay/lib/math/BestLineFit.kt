package com.pigdogbay.lib.math

/*
 *
 * This class wraps the formula for calculating the line of best for a set of data points
 * The class itself doesn't store any data, but keeps a running total of various summations of the various x,y values
 *
 * See,
 * http://en.wikipedia.org/wiki/Method_of_least_squares
 * http://en.wikipedia.org/wiki/Correlation
 * http://people.hofstra.edu/stefan_waner/realworld/calctopic1/regression.html
 *
 */
class BestLineFit {
    //Sum of x-values
    private var sx = 0.0
    //Sum of y-values
    private var sy = 0.0
    //Sum of the x squared values
    private var sxx = 0.0
    //Sum of the y squared values
    private var syy = 0.0
    //sum of xy values
    private var sxy = 0.0
    //number of points
    private var count = 0

    /*
	/// Gets the slope of the best line of fit for the data
	/// Straight line equation is
	///		y = mx + c
	///
	/// Slope, m = (nSxy-SxSy)/(nSxx-SxSx)
	///
	/// Where,
	///		Sx = x1+x2+x3+..+xn						Sum of x values
	///		Sy = y1+y2+y3+..+yn						Sum of y values
	///		Sxx = x1*x1+x2*x2+...+xn*xn				Sum of x square values
	///		Sxy = x1y1+x2y2+...+xnyn				Sum of xy values
	///		Syy = y1y1+y2y2+...						Sum of y square values
	///		n										Total number of data points
	///		m										Slope
	///		c										Intercept
	 */
    val slope: Double
        get() = (count * sxy - sx * sy) / (count * sxx - sx * sx)

    /*
	/// Gets the intercept of the line of best fit for the data
	/// Straight line equation is
	///		y = mx + c
	///
	/// Intercept, c = (Sy-mSx)/n
	///
	/// Where,
	///		Sx = x1+x2+x3+..+xn						Sum of x values
	///		Sy = y1+y2+y3+..+yn						Sum of y values
	///		Sxx = x1*x1+x2*x2+...+xn*xn				Sum of x square values
	///		Sxy = x1y1+x2y2+...+xnyn				Sum of xy values
	///		Syy = y1y1+y2y2+...						Sum of y square values
	///		n										Total number of data points
	///		m										Slope
	///		c										Intercept
	 */
    val intercept: Double
        get() = (sy - sx * slope) / count

    /*
	/// Gets the slope of the best line of fit that passes through the origin
	///
	/// Zero Slope = sxy/sxx
	 */
    val slopeThroughZero: Double
        get() = sxy / sxx
    /*
	/// Gets the sample correlation coefficient, r.
	/// This is a measure of the how well the line fits the data, r has a value between -1 to 1. The closer to -1 or 1 the
	/// better the fit.
	///
	/// Coefficient of correlation, r = nSxy-SxSy / Sqrt((nSxx-Sx^2)(nSyy-Sy^2))
	/// Where,
	///		Sx = x1+x2+x3+..+xn						Sum of x values
	///		Sy = y1+y2+y3+..+yn						Sum of y values
	///		Sxx = x1*x1+x2*x2+...+xn*xn			Sum of x square values
	///		Sxy = x1y1+x2y2+...+xnyn					Sum of xy values
	///		Syy = y1y1+y2y2+...						Sum of y square values
	 */
    val r: Double
        get() {
            var d = (count * sxx - sx * sx) * (count * syy - sy * sy)
            d = Math.sqrt(d)
            return (count * sxy - sx * sy) / d
        }
    /*
	/// Gets the coefficient of determination which is the square of the sample correlation coefficient
	 */
    val rSquared: Double
        get() {
            var nom = count * sxy - sx * sy
            nom *= nom
            val denom = (count * sxx - sx * sx) * (count * syy - sy * sy)
            return nom / denom
        }

    constructor() {}
    constructor(points: List<DPoint>) {
        for (point in points) {
            Add(point.X, point.Y)
        }
    }

    fun add(point: DPoint) {
        Add(point.X, point.Y)
    }

    fun Add(x: Double, y: Double) {
        sx += x
        sy += y
        sxy += x * y
        sxx += x * x
        syy += y * y
        count++
    }

    fun calculateX(y: Double): Double {
        return (y - intercept) / slope
    }

    fun calculateY(x: Double): Double {
        return x * slope + intercept
    }


}
