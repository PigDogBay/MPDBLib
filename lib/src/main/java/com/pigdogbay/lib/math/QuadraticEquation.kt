package com.pigdogbay.lib.math


/**
 * Created by peter.bradbury on 18/12/2014.
 */

//y = ax^2 + bx + c
class QuadraticEquation {
    val a: Double
    val b: Double
    val c: Double

    constructor(x0: Double, y0: Double, x1: Double, y1: Double, x2: Double, y2: Double) {
        //b = [(y₀-y₁)(x₁²-x₂²)+(y₂-y₁)(x₀²-x₁²)] ⁄ [(x₀-x₁)(x₁²-x₂²)+(x₂-x₁)(x₀²-x₁²)]
        val bNumerator = (y0 - y1) * (x1 * x1 - x2 * x2) + (y2 - y1) * (x0 * x0 - x1 * x1)
        val bDenominator = (x0 - x1) * (x1 * x1 - x2 * x2) + (x2 - x1) * (x0 * x0 - x1 * x1)
        if (bDenominator != 0.0) {
            b = bNumerator / bDenominator
        } else if (bNumerator == 0.0) {
            b = 1.0
        } else {
            b = 0.0
        }

        //a = [y₀-y₁-b⋅(x₀-x₁)] ⁄ (x₀²-x₁²)
        a = (y0 - y1 - b * (x0 - x1)) / (x0 * x0 - x1 * x1)

        //c = y₀ - a⋅x₀² - b⋅x₀
        c = y0 - a * (x0 * x0) - b * x0
    }

    constructor(a: Double, b: Double, c: Double) {
        this.a = a
        this.b = b
        this.c = c
    }

    fun getY(x: Double): Double {
        val y: Double
        y = a * (x * x) + b * x + c
        return y
    }

    //x = ( -b +- (b² - 4a(c-y))^0.5 )/2a
    fun getX(y: Double): Double {
        val result: Double
        if (a != 0.0) {
            val squareRoot = Math.sqrt(b * b - 4.0 * a * (c - y))
            val add = (-b + squareRoot) / (2 * a)

            if (add >= 0)
                result = add
            else {
                result = (-b - squareRoot) / (2 * a)
            }
        } else if (b != 0.0) {
            //x= (c-y)/b
            result = (y - c) / b
        } else {
            result = y - c
        }
        return result
    }

    fun ScaleEquation(scaleFactor: Double): QuadraticEquation {
        return QuadraticEquation(a * scaleFactor, b * scaleFactor, c * scaleFactor)
    }
}