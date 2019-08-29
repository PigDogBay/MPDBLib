package com.pigdogbay.lib.math

import java.util.ArrayList
import java.util.Collections
import java.util.Locale

/*
 * Structure to hold a X,Y data point
 */
class DPoint(val X: Double, val Y: Double) {

    override fun toString(): String {
        return String.format(Locale.US, "(%f,%f)", X, Y)
    }

    companion object {

        fun createList(data: IntArray): List<DPoint> {
            val list = ArrayList<DPoint>(data.size)
            for (i in data.indices) {
                list.add(DPoint(i.toDouble(), data[i].toDouble()))
            }
            return list
        }

        fun sortByX(points: MutableList<DPoint>?) {
            points?.sortWith(Comparator { lhs, rhs -> lhs.X.compareTo(rhs.X) })
        }

        fun getMinY(points: List<DPoint>): DPoint {
            var min = points[0]
            for (point in points) {
                if (point.Y < min.Y) {
                    min = point
                }
            }
            return min
        }

        fun getMaxY(points: List<DPoint>): DPoint {
            var max = points[0]
            for (point in points) {
                if (point.Y > max.Y) {
                    max = point
                }
            }
            return max
        }
    }
}
