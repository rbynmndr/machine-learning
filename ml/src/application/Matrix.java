package application;

import java.util.Random;

public class Matrix {
    Random rand = new Random();

    /**
     * Create random normal distributed matrix of row and column provided. The default value of matrix range between -1 to 1.
     * @param mean Mean value.
     * @param row Intention number of row in matrix
     * @param column Intertion number of column in matrix
     * @return Return 2D array of data type double
     */
    public double[][] createRandomMatrix(double mean, int row, int column){
        if(row < 1 || column < 1){
            throw new IllegalArgumentException("Row and Column must be greater than zero.");
        }
        double[][] matrix = new double[row][column];
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++){
                matrix[i][j] = mean + rand.nextGaussian() * Math.pow(row, -0.5);                    
            }
        }
        return matrix;
    }

    /**
     * Create matrix with range specified.
     * @param row Indicate row of matrix.
     * @param column Indicate column of matrix.
     * @param from Start point of range.
     * @param to End point of range.
     * @return Return 2D array in term of matrix
     */
    public double[][] createRandomMatrix(int row, int column, int from, int to){
        if(row < 1 || column < 1){
            throw new IllegalArgumentException("Row and Column must be greater than zero.");
        }
        double[][] matrix = new double[row][column];
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++){
                matrix[i][j] = rand.nextInt(to - from + 1) + from;                    
            }
        }
        return matrix;
    }

    /**
     * Return transpose of provided matrix.
     * @param matrix 2D array in term of matrix.
     * @return Return 2D array in term of transpose matrix.
     */
    public double[][] transpose(double[][] matrix){
        double[][] transposeMatrix;
        if(matrix == null) throw new IllegalArgumentException("The given matrix is null");
        if(matrix.length == matrix[0].length) {
            int row = matrix.length;
            transposeMatrix = new double[row][row];
            for(int i=0; i < row; i++){
                for(int j=0; j< row; j++){
                    transposeMatrix[j][i] = matrix[i][j];
                }
            }
        }
        else{
            int row = matrix[0].length;
            int column = matrix.length;
            transposeMatrix = new double[row][column];
            for(int i=0; i < column; i++){
                for(int j=0; j< row; j++){
                    transposeMatrix[j][i] = matrix[i][j];
                }
            }
        }
        
        return transposeMatrix;
    }

    /**
     * Apply sigmoid function to each of the matrix element.
     * @param matrix Matrix to calculate sigmoid function on each element.
     * @return Return 2D array in terms of matrix.
     */
    public double[][] sigmoidMatrix(double[][] matrix){
        if(matrix == null) throw new IllegalArgumentException("The given matrix is null");        
        int row = matrix.length;
        int column = matrix[0].length;
        double[][] sigMat = new double[row][column];
        for(int i=0; i < row; i++){
            for(int j=0; j < column; j++){
                sigMat[i][j] = 1 / (1 + Math.exp(-matrix[i][j]));
            }
        }
        return sigMat;
    }

    //Arthematic Operation of Matrix
    /**
     * Perform dot product between two matrixes. The column of <strong>matrixA</strong> must be same as row of <strong>matrixB</strong>.
     * Otherwise, it throw <strong>IllegalArgumentException</strong>.
     * @param matrixA First matrix of size <i>(a x b)</i>.
     * @param matrixB Second matrix of size <i>(b x c)</i>.
     * @return Return 2D array in terms of Matrix. The size of returned matrix will be <i>(a x c)</i>.
     */
    public double[][] dot(double[][] matrixA, double[][] matrixB){
        if(matrixA == null || matrixB == null) throw new IllegalArgumentException("Matrix cannot be null.");
        if(matrixA[0].length != matrixB.length) throw new IllegalArgumentException("Connot perform operation because column of matrixA is not same as row of matrixB.");
        int row = matrixA.length;
        int column = matrixB[0].length;
        double[][] resMatrix = new double[row][column];
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++){
                for(int k=0; k<matrixB.length; k++){
                    resMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return resMatrix;
    }

    /**
     * Perform addition between two matrixes. The dimensions of both matrixes must be same.
     * Otherwise, it throw <strong>IllegalArgumentException</strong>.
     * @param matrixA First matrix of size <i>(a x b)</i>.
     * @param matrixB Second matrix of size <i>(a x b)</i>.
     * @return Return 2D array in terms of Matrix. The size of returned matrix will be <i>(a x b)</i>.
     */
    public double[][] add(double[][] matrixA, double[][] matrixB){
        if(matrixA == null || matrixB == null) throw new IllegalArgumentException("Matrix cannot be null.");
        if(matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) throw new IllegalArgumentException("Incorrect matrix dimensions.");
        int row = matrixA.length;
        int column = matrixA[0].length;
        double[][] resMatrix = new double[row][column];
        for(int i=0; i< row; i++){
            for(int j=0; j< column; j++){
                resMatrix[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
        return resMatrix;
    }

    /**
     * Element wise subtraction
     *
     * @param a scaler
     * @param b matrix
     * @return c = a - b
     */
    public double[][] subtract(double a, double[][] b) {
        int m = b.length;
        int n = b[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a - b[i][j];
            }
        }
        return c;
    }
    /**
     * Element wise subtract
     *
     * @param a matrix
     * @param x matrix
     * @return y = a - x
     */
    public double[][] subtract(double[][] x, double[][] a) {
        int m = a.length;
        int n = a[0].length;

        if (x.length != m || x[0].length != n) {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        double[][] y = new double[m][n];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                y[j][i] = x[j][i] - a[j][i];
            }
        }
        return y;
    }

    /**
     * Element wise multiplication
     *
     * @param a matrix
     * @param x matrix
     * @return y = a * x
     */
    public double[][] multiply(double[][] x, double[][] a) {
        int m = a.length;
        int n = a[0].length;

        if (x.length != m || x[0].length != n) {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        double[][] y = new double[m][n];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                y[j][i] = a[j][i] * x[j][i];
            }
        }
        return y;
    }

    /**
     * Element wise multiplication
     *
     * @param a matrix
     * @param x scaler
     * @return y = a * x
     */
    public double[][] multiply(double x, double[][] a) {
        int m = a.length;
        int n = a[0].length;

        double[][] y = new double[m][n];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                y[j][i] = a[j][i] * x;
            }
        }
        return y;
    }
}
