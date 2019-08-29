package com.pigdogbay.lib.math

import java.util.ArrayList

/**
 * Created by Mark on 02/07/2015.
 *
 * Differentiates a data set
 */
class Differentiator {
    /**
     * The differential algorithm takes a sample of data around a point to calculate the best line fit, the slope of the best line
     * is then considered to be that points differential value. Use smaller sample sizes if you have smooth continuous data, uses larger sample sizes
     * if the data is noisy, as larger sample sample sizes will give a better average.
     */
    var sampleSize = 9
    private val points = ArrayList<DPoint>()

    fun getPoints(): List<DPoint>? {
        return points
    }

    constructor() {
    }

    constructor(points: List<DPoint>) {
        this.points.addAll(points)
    }

    fun Add(x: Double, y: Double) {
        points.add(DPoint(x, y))
    }

    /**
     *
     * If there is enough data, a moving sample averaged differentiation is performed.
     * Here a sample of data is taken, a best line fitted, the differential of the mid point is the slope of the best line.
     * The sample statrting index is then moved along by one, for the next differential to be calculated.
     * Since only the midpoint of the sample is calculated, the start and end points of the entire data set will be missed. In these
     * cases a best line is fitted for these values.
     *
     * For data sets smaller than the sample size, a best line is fitted whose slope is assigned to each points differential value.
     *
     * Note that the points are first sorted in ascending order
     * Note that points with the same x value will give have a NaN differential value
     *
     * @return Array of points whose Y-values are the differential values
     */
    fun Differentiate(): List<DPoint> {
        return if (points.size > sampleSize) SampleAveragedDifferentiation() else SmallDataSet()
    }

    /**
     * Uses a moving sample and best line slope to calculate the differentials for each data point
     * @return list of points
     */
    private fun SampleAveragedDifferentiation(): List<DPoint> {
        //ensure points are sorted, in order of x value
        DPoint.sortByX(points)
        val diffPoints = ArrayList<DPoint>()
        val start = sampleSize / 2
        val end = points.size - start

        //Start points: calculate best line fit of the first batch of points
        //and set the differential as the lines slope.
        var selection = points.subList(0, sampleSize)
        var bl = BestLineFit(selection)
        for (i in 0 until start) {
            diffPoints.add(DPoint(points[i].X, bl.slope))
        }
        //mid points
        for (i in start until end) {
            selection = points.subList(i - start, i - start + sampleSize)
            bl = BestLineFit(selection)
            diffPoints.add(DPoint(points[i].X, bl.slope))
        }
        //end points, use the last calculated best line fit
        for (i in end until points.size) {
            diffPoints.add(DPoint(points[i].X, bl.slope))
        }
        return diffPoints
    }

    /**
     * Simply fits a best line to calculate the differential
     * @return list of points
     */
    private fun SmallDataSet(): List<DPoint> {
        val diffPoints = ArrayList<DPoint>()
        val bl = BestLineFit(points)
        for (i in points.indices) {
            diffPoints.add(DPoint(points[i].X, bl.slope))
        }
        return diffPoints

    }
}
