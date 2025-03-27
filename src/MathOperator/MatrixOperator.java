package MathOperator;

public class MatrixOperator {

    public static double[][] transpose(double[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("Matrix cannot be empty.");
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] transposed = new double[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }
    
    public static double[][] multiply(double[][] a, double[][] b) {
        int rows = a.length, cols = b[0].length, common = b.length;
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < common; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    public static double[] multiply(double[][] a, double[] b) {
        int rows = a.length, cols = a[0].length;
        double[] result = new double[rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i] += a[i][j] * b[j];
            }
        }
        return result;
    }

    public static double[][] inverse(double[][] matrix) {
        int n = matrix.length;
        double[][] augmented = new double[n][2 * n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, augmented[i], 0, n);
            augmented[i][n + i] = 1;
        }

        if (determinant(matrix) == 0) {
            throw new ArithmeticException("Matrix is singular and cannot be inverted.");
        }

        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(augmented[k][i]) > Math.abs(augmented[maxRow][i])) {
                    maxRow = k;
                }
            }
            if (i != maxRow) {
                double[] temp = augmented[i];
                augmented[i] = augmented[maxRow];
                augmented[maxRow] = temp;
            }

            double diag = augmented[i][i];
            if (diag == 0) {
                throw new ArithmeticException("Matrix inversion failed due to zero pivot.");
            }
            for (int j = 0; j < 2 * n; j++) {
                augmented[i][j] /= diag;
            }

            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmented[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmented[k][j] -= factor * augmented[i][j];
                    }
                }
            }
        }

        double[][] inverse = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(augmented[i], n, inverse[i], 0, n);
        }
        return inverse;
    }

    public static double determinant(double[][] matrix) {
        int n = matrix.length;
        double det = 1.0;
        double[][] temp = new double[n][n];
        for (int i = 0; i < n; i++) {
            temp[i] = matrix[i].clone();
        }

        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(temp[k][i]) > Math.abs(temp[maxRow][i])) {
                    maxRow = k;
                }
            }
            if (i != maxRow) {
                double[] swap = temp[i];
                temp[i] = temp[maxRow];
                temp[maxRow] = swap;
                det *= -1;
            }
            if (temp[i][i] == 0) {
                return 0;
            }
            det *= temp[i][i];

            for (int k = i + 1; k < n; k++) {
                double factor = temp[k][i] / temp[i][i];
                for (int j = i; j < n; j++) {
                    temp[k][j] -= factor * temp[i][j];
                }
            }
        }
        return det;
    }

}
