package com.pigdogbay.lib.math;

import java.util.Arrays;

/**
 * Created by Mark on 27/05/2017.
 *
 * Ported from C#, however scalars are automatically factored into
 *
 * The matrix has you
 *
 *  The matrix wraps a rectangular double array, double[row][column]
 *  Rectangular array notes:
 *  double[row][column] matrix = {
 * 							{1,2,3},
 * 							{3,4,5}};
 *
 *
 * The matrix has 2 rows and 3 columns, corresponding to double[2][3].
 *
 */
public class Matrix {

    private double[][] matrix;

    public int getColumns() {
        return matrix[0].length;
    }

    public int getRows() {
        return matrix.length;
    }

    public double getElement(int row, int column){
        return matrix[row][column];
    }

    public Matrix(double[][] matrix, double scalar){
        this.matrix = matrix;
        factorInScalar(matrix,scalar);
    }
    public Matrix(double[][] matrix){
        this.matrix = matrix;
    }

    /**
     * Transpose the rows into columns
     * 1,2,3  => 1,4
     * 4,5,6     2,5
     *           3,6
     *
     * @return New matrix that is the transpose of this
     */
    public Matrix transpose()
    {
        double[][] transpose = new double[getColumns()][getRows()];
        for (int y = 0; y < getRows(); y++)
        {
            for (int x = 0; x < getColumns(); x++)
            {
                transpose[x][y] = matrix[y][x];
            }
        }
        return new Matrix(transpose);
    }

    /**
     * Adds the two matrices together
     * Result = A + B = B + A
     *
     * @param matrixB Specified matrix to add to this one
     * @return Result
     */
    public Matrix add(Matrix matrixB) {
        if (matrixB.getColumns() != this.getColumns() || matrixB.getRows() != this.getRows()){
            throw new IllegalArgumentException("Matrices can only be added when both matrices have the same number columns and rows");
        }
        double[][] result = new double[getRows()][getColumns()];
        for (int y = 0; y < getRows(); y++)
        {
            for (int x = 0; x < getColumns(); x++)
            {
                result[y][x] = matrix[y][x] + matrixB.matrix[y][x];
            }
        }
        return new Matrix(result);
    }
    /**
     * Subtracts the specified matrix from this matrix and returns a new matrix
     * Result = A - B
     *
     * @param matrixB Specified matrix to subtract from this one
     * @return new matrix Result
     */
    public Matrix subtract(Matrix matrixB) {
        if (matrixB.getColumns() != this.getColumns() || matrixB.getRows() != this.getRows()){
            throw new IllegalArgumentException("Matrices can only be subtracted when both matrices have the same number columns and rows");
        }
        double[][] result = new double[getRows()][getColumns()];
        for (int y = 0; y < getRows(); y++)
        {
            for (int x = 0; x < getColumns(); x++)
            {
                result[y][x] = matrix[y][x] - matrixB.matrix[y][x];
            }
        }
        return new Matrix(result);
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
    public Matrix multiply(Matrix matrixB)
    {
        int colsA = getColumns();
        int rowsA = getRows();
        int colsB = matrixB.getColumns();
        int rowsB = matrixB.getRows();

        //check matrices can be multiplied
        if (colsA != rowsB)
        {
            throw new IllegalArgumentException("Matrices can only be multiplied when the columns of matrix A is the same as rows of matrix B");
        }
        double[][] result = new double[rowsA][colsB];

        //row
        for (int y = 0; y < rowsA; y++)
        {
            //column
            for (int x = 0; x < colsB; x++)
            {
                //multiply the row(y) of matrix A with column(x) of matrix B
                double total = 0;
                for (int i = 0; i < colsA; i++)
                {
                    total += (matrix[y][i] * matrixB.matrix[i][x]);
                }
                result[y][x] = total;
            }
        }
        return new Matrix(result);
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
    public double determinant()
    {
        if (getColumns() != getRows())
        {
            throw new IllegalArgumentException("Matrix must have the same number of rows and columns for the determinant of the matrix to be calculated");
        }
        return determinant(matrix);
    }

    /**
     * The minor is the matrix that remains when the row and column containing the specified cell are removed.
     * @param col column number (starting at 0)
     * @param row row number (starting at 0)
     * @return the minor matrix
     */
    public Matrix minor(int col, int row)
    {
        if (getColumns() != getRows())
        {
            throw new IllegalArgumentException("Matrix must have the same number of rows and columns for the minor to be calculated");
        }
		double[][] array = getMinor(matrix, col, row);
        return new Matrix(array);
    }

    /**
     *
     * The co-factor is the determinant of the matrix that remains when the row and column containing the
     * specified element is removed. The co-factor may also be multiplied by -1, depending on the element's position:
     *
     *      + - + -
     *      - + - +
     *      + - + -
     *
     * @param col column number (starting at 0)
     * @param row row number (starting at 0)
     * @return The cofactor of the specified element
     */
    public double coFactor(int col, int row)
    {
        if (getColumns() != getRows())
        {
            throw new IllegalArgumentException("Matrix must have the same number of rows and columns for the co-factor to be calculated");
        }
        double[][] array = getMinor(matrix, col, row);
        double cofactor = determinant(array);
        //need to work out sign:
        int i = col - row;
        if ((i % 2) != 0)
        {
            cofactor = -cofactor;
        }
        return cofactor;
    }

    /**
     * The adjoint of a matrix is defined as the transpose of the matrix of its co-factors
     * @return The adjoint of the matrix
     */
    public Matrix adjoint()
    {
        if (getColumns() != getRows())
        {
            throw new IllegalArgumentException("Matrix must have the same number of rows and columns for the adjoint to be calculated");
        }
        //create array to hold the adjoint elements
        double[][] tmpArray = new double[getColumns()][getRows()];
        int length = matrix.length;

        for (int y = 0; y < length; y++)
        {
            for (int x = 0; x < length; x++)
            {
                tmpArray[y][x] = coFactor(x, y);
            }
        }
        Matrix adjoint = new Matrix(tmpArray);
        return adjoint.transpose();
    }

    /**
     * Calculates the inverse of the matrix
     * A' A = A' A = I, where A' is the inverse
     *
     * The inverse matrix of A, is defined as the Adjoint of A divided by the determinant of A
     *
     * @return Inverse Matrix
     */
    public Matrix inverse()
    {
        if (getColumns() != getRows())
        {
            throw new IllegalArgumentException("Matrix must have the same number of rows and columns for the inverse matrix to be calculated");
        }
        Matrix invMatrix;
        if (getRows() == 2)
        {
            //( a b ) -1     __1_ (d -b)
            //( c d )     =	ad-bc (-c a)
            double[][] inverse = new double[getColumns()][ getRows()];
            double det = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
            inverse[0][0] =  matrix[1][1];
            inverse[1][1] =  matrix[0][0];
            inverse[0][1] = -matrix[0][1];
            inverse[1][0] = -matrix[1][0];
            invMatrix = new Matrix(inverse, 1/det);
        }
        else
        {
            // The inverse matrix of A, is defined as the Adjoint of A divided by the determinant of A
            invMatrix = adjoint();
            factorInScalar(invMatrix.matrix, 1/determinant());
        }
        return invMatrix;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(matrix);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix1 = (Matrix) o;

        return Arrays.deepEquals(matrix, matrix1.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

    private static void factorInScalar(double[][] matrix, double scalar){
        for (int y = 0; y<matrix.length; y++){
            for (int x = 0; x<matrix[0].length; x++){
                matrix[y][x] = matrix[y][x]*scalar;
            }
        }
    }

    private double determinant(double[][] array)
    {
        int length = array.length;
        if (length == 2)
        {
            return (array[0][0] * array[1][1] - array[0][1] * array[1][0]);
        }
        double det = 0;
        //get minors and recurse down
        for (int i = 0; i < length; i++)
        {
            //get the minor
            double[][] minor = getMinor(array, i, 0);
            //find correct sign
            if (i % 2 == 0)
            {
                det += determinant(minor) * array[0][i];
            }
            else
            {
                det -= determinant(minor) * array[0][i];
            }
        }
        return det;
    }

    private double[][] getMinor(double[][] array, int xPos, int yPos)
    {
        int length = array.length;
        //get the minor
        double[][] minor = new double[length - 1][length - 1];
        int mY = 0;
        for (int y = 0; y < length; y++)
        {
            if (y == yPos)
            {
                //skip this one
                continue;
            }
            int mX = 0;
            for (int x = 0; x < length; x++)
            {
                if (x == xPos)
                {
                    //skip this one
                    continue;
                }
                minor[mY][mX] = array[y][x];
                mX++;
            }
            mY++;
        }
        return minor;
    }

}
