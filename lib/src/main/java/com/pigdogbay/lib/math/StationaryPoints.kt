package com.pigdogbay.lib.math

import java.util.ArrayList
import kotlin.math.abs

/**
 * Co-ordinate information and whether the point represents a peak or trough
 */
class TurningPoint(
        /**
         * @return X and Y value of the data point
         */
        val point: DPoint,
        /**
         * @return True - turning point is a peak, False - turning point is a trough
         */
        val isPeak: Boolean,
        /**
         * @return index of the point in the data set
         */
        val index: Int)


internal enum class SPSS {
    Nothing,
    StartOfPeak,
    EndOfPeak,
    StartOfTrough,
    EndOfTrough
}

/**
 *
 * Co-ordinate information and whether the point represents a peak or trough
 *
 * Created by Mark on 02/07/2015.
 */
class StationaryPoints(val points: List<DPoint>) {

    private val diffPoints = Differentiator(points).Differentiate()

    /**
     * Calculates the peaks and troughs of the data set.
     *
     * @param threshold minimum allowed value for the differentiated points
     * @return list of  the peaks and troughs of the data set
     */
    fun calculateTurningPoints(threshold : Double): List<TurningPoint>
            = findTurningPoints(smoothDiffPoints(threshold))

    private fun smoothDiffPoints(threshold : Double) : List<DPoint> {
        val yMin = DPoint.getMinY(diffPoints).Y
        val range = DPoint.getMaxY(diffPoints).Y - yMin
        return diffPoints.map {p->
            val d = abs(p.Y / range)
            if (d < threshold) {
                DPoint(p.X, 0.0)
            } else {
                DPoint(p.X,p.Y)
            }
        }
    }

    private fun findTurningPoints(smoothPoints : List<DPoint>) : List<TurningPoint>{
        val turningPoints = ArrayList<TurningPoint>()
        var startIndex = 0
        var state = SPSS.Nothing
        for (i in smoothPoints.indices) {
            val y = smoothPoints[i].Y
            if (y == 0.0) {
                continue
            }
            when (state) {
                SPSS.Nothing -> {
                    state = SPSS.StartOfPeak
                    if (y < 0) {
                        state = SPSS.StartOfTrough
                    }
                    startIndex = i
                }
                SPSS.StartOfPeak -> if (y < 0) {
                    state = SPSS.EndOfPeak
                }
                SPSS.EndOfPeak -> {
                    turningPoints.add(scanForPeak(startIndex, i))
                    state = SPSS.Nothing
                }
                SPSS.StartOfTrough -> if (y > 0) {
                    state = SPSS.EndOfTrough
                }
                SPSS.EndOfTrough -> {
                    turningPoints.add(scanForTrough(startIndex, i))
                    state = SPSS.Nothing
                }
            }
        }
        return turningPoints
    }

    private fun scanForPeak(startIndex: Int, endIndex: Int) : TurningPoint {
        var pt = points[startIndex]
        var index = startIndex
        for (i in startIndex..endIndex) {
            if (pt.Y < points[i].Y) {
                pt = points[i]
                index = i
            }
        }
        return TurningPoint(pt, true, index)
    }

    private fun scanForTrough(startIndex: Int, endIndex: Int) : TurningPoint{
        var pt = points[startIndex]
        var index = startIndex
        for (i in startIndex..endIndex) {
            if (pt.Y > points[i].Y) {
                pt = points[i]
                index = i
            }
        }
        return TurningPoint(pt, false, index)
    }
}
