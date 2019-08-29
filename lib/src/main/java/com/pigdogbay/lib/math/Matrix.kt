package com.pigdogbay.lib.math

import java.util.Arrays

/**
 * Created by Mark on 27/05/2017.
 *
 * Ported from C#, however scalars are automatically factored into
 *
 * The matrix has you
 *
 * The matrix wraps a rectangular double array, double(row)(column)
 * Rectangular array notes:
 * double(row)(column) matrix = {
 * {1,2,3},
 * {3,4,5}};
 *
 *
 * The matrix has 2 rows and 3 columns, corresponding to double[2][3].
 *
 */
class Matrix {

    private var matrix: Array<DoubleArray>? = null

    val columns: Int
        get() = matrix!![0].size

    val rows: Int
        get() = matrix!!.size

    fun getElement(row: Int, column: Int): Double {
        return matrix!![row][column]
    }

    constructor(matrix: Array<DoubleArray>, scalar: Double) {
        this.matrix = matrix
        factorInScalar(matrix, scalar)
    }

    constructor(matrix: Array<DoubleArray>) {
        this.matrix = matrix
    }

    /**
     * Transpose the rows into columns
     * 1,2,3  => 1,4
     * 4,5,6     2,5
     * 3,6
     *
     * @return New matrix that is the transpose of this
     */
    fun transpose(): Matrix {
        val transpose = Array(columns) { DoubleArray(rows) }
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                transpose[x][y] = matrix!![y][x]
            }
        }
        return Matrix(transpose)
    }

    /**
     * Adds the two matrices together
     * Result = A + B = B + A
     *
     * @param matrixB Specified matrix to add to this one
     * @return Result
     */
    fun add(matrixB: Matrix): Matrix {
        require(!(matrixB.columns != this.columns || matrixB.rows != this.rows)) { "Matrices can only be added when both matrices have the same number columns and rows" }
        val result = Array(rows) { DoubleArray(columns) }
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                result[y][x] = matrix!![y][x] + matrixB.matrix!![y][x]
            }
        }
        return Matrix(result)
    }

    /**
     * Subtracts the specified matrix from this matrix and returns a new matrix
     * Result = A - B
     *
     * @param matrixB Specified matrix to subtract from this one
     * @return new matrix Result
     */
    fun subtract(matrixB: Matrix): Matrix {
        require(!(matrixB.columns != this.columns || matrixB.rows != this.rows)) { "Matrices can only be subtracted when both matrices have the same number columns and rows" }
        val result = Array(rows) { DoubleArray(columns) }
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                result[y][x] = matrix!![y][x] - matrixB.matrix!![y][x]
            }
        }
        return Matrix(result)
    }

    /**
     * Multiplies this matrix (matrix A) with input matrix B and returns the result matrix, C:
     * AB = C
     *
     * Note that matrix multiplication is not commmutative, eg
     * AB does not equal BA
     *
     * @param matrixB Right hand sidde matrix of the multiplication
     * @return Returns result matrix, C
     */
    fun multiply(matrixB: Matrix): Matrix {
        val colsA = columns
        val rowsA = rows
        val colsB = matrixB.columns
        val rowsB = matrixB.rows

        //check matrices can be multiplied
        require(colsA == rowsB) { "Matrices can only be multiplied when the columns of matrix A is the same as rows of matrix B" }
        val result = Array(rowsA) { DoubleArray(colsB) }

        //row
        for (y in 0 until rowsA) {
            //column
            for (x in 0 until colsB) {
                //multiply the row(y) of matrix A with column(x) of matrix B
                var total = 0.0
                for (i in 0 until colsA) {
                    total += matrix!![y][i] * matrixB.matrix!![i][x]
                }
                result[y][x] = total
            }
        }
        return Matrix(result)
    }

    /**
     * Uses the Laplace expansion to recursively compute the determinant, eg
     *
     * | a b c |
     * | d e f | = a. |e f|  - b.|d f| + c.|d e|
     * | g h i |        |h i|       |g i|       |g h|
     *
     * @return Determinant of the matrix
     */
    fun determinant(): Double {
        require(columns == rows) { "Matrix must have the same number of rows and columns for the determinant of the matrix to be calculated" }
        return determinant(matrix!!)
    }

    /**
     * The minor is the matrix that remains when the row and column containing the specified cell are removed.
     * @param col column number (starting at 0)
     * @param row row number (starting at 0)
     * @return the minor matrix
     */
    fun minor(col: Int, row: Int): Matrix {
        require(columns == rows) { "Matrix must have the same number of rows and columns for the minor to be calculated" }
        val array = getMinor(matrix!!, col, row)
        return Matrix(array)
    }

    /**
     *
     * The co-factor is the determinant of the matrix that remains when the row and column containing the
     * specified element is removed. The co-factor may also be multiplied by -1, depending on the element's position:
     *
     * + - + -
     * - + - +
     * + - + -
     *
     * @param col column number (starting at 0)
     * @param row row number (starting at 0)
     * @return The cofactor of the specified element
     */
    fun coFactor(col: Int, row: Int): Double {
        require(columns == rows) { "Matrix must have the same number of rows and columns for the co-factor to be calculated" }
        val array = getMinor(matrix!!, col, row)
        var cofactor = determinant(array)
        //need to work out sign:
        val i = col - row
        if (i % 2 != 0) {
            cofactor = -cofactor
        }
        return cofactor
    }

    /**
     * The adjoint of a matrix is defined as the transpose of the matrix of its co-factors
     * @return The adjoint of the matrix
     */
    fun adjoint(): Matrix {
        require(columns == rows) { "Matrix must have the same number of rows and columns for the adjoint to be calculated" }
        //create array to hold the adjoint elements
        val tmpArray = Array(columns) { DoubleArray(rows) }
        val length = matrix!!.size

        for (y in 0 until length) {
            for (x in 0 until length) {
                tmpArray[y][x] = coFactor(x, y)
            }
        }
        val adjoint = Matrix(tmpArray)
        return adjoint.transpose()
    }

    /**
     * Calculates the inverse of the matrix
     * A' A = A' A = I, where A' is the inverse
     *
     * The inverse matrix of A, is defined as the Adjoint of A divided by the determinant of A
     *
     * @return Inverse Matrix
     */
    fun inverse(): Matrix {
        require(columns == rows) { "Matrix must have the same number of rows and columns for the inverse matrix to be calculated" }
        val invMatrix: Matrix
        if (rows == 2) {
            //( a b ) -1     __1_ (d -b)
            //( c d )     =	ad-bc (-c a)
            val inverse = Array(columns) { DoubleArray(rows) }
            val det = matrix!![0][0] * matrix!![1][1] - matrix!![0][1] * matrix!![1][0]
            inverse[0][0] = matrix!![1][1]
            inverse[1][1] = matrix!![0][0]
            inverse[0][1] = -matrix!![0][1]
            inverse[1][0] = -matrix!![1][0]
            invMatrix = Matrix(inverse, 1 / det)
        } else {
            // The inverse matrix of A, is defined as the Adjoint of A divided by the determinant of A
            invMatrix = adjoint()
            factorInScalar(invMatrix.matrix!!, 1 / determinant())
        }
        return invMatrix
    }

    override fun toString(): String {
        return Arrays.deepToString(matrix)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val matrix1 = other as Matrix?

        return Arrays.deepEquals(matrix, matrix1!!.matrix)
    }

    override fun hashCode(): Int {
        return Arrays.deepHashCode(matrix)
    }

    private fun factorInScalar(matrix: Array<DoubleArray>, scalar: Double) {
        for (y in matrix.indices) {
            for (x in matrix[0].indices) {
                matrix[y][x] = matrix[y][x] * scalar
            }
        }
    }

    private fun determinant(array: Array<DoubleArray>): Double {
        val length = array.size
        if (length == 2) {
            return array[0][0] * array[1][1] - array[0][1] * array[1][0]
        }
        var det = 0.0
        //get minors and recurse down
        for (i in 0 until length) {
            //get the minor
            val minor = getMinor(array, i, 0)
            //find correct sign
            if (i % 2 == 0) {
                det += determinant(minor) * array[0][i]
            } else {
                det -= determinant(minor) * array[0][i]
            }
        }
        return det
    }

    private fun getMinor(array: Array<DoubleArray>, xPos: Int, yPos: Int): Array<DoubleArray> {
        val length = array.size
        //get the minor
        val minor = Array(length - 1) { DoubleArray(length - 1) }
        var mY = 0
        for (y in 0 until length) {
            if (y == yPos) {
                //skip this one
                continue
            }
            var mX = 0
            for (x in 0 until length) {
                if (x == xPos) {
                    //skip this one
                    continue
                }
                minor[mY][mX] = array[y][x]
                mX++
            }
            mY++
        }
        return minor
    }

}
