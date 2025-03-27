package Model;

import PredictionInterface.TimeSeriesModel;
import MathOperator.MatrixOperator;

class LinearRegression implements TimeSeriesModel {

    private double[] coefficients;

    @Override
    public void train(double[][] X, double[] y) {
        int n = X.length, m = X[0].length;
        double[][] X_augmented = new double[n][m + 1];

        for (int i = 0; i < n; i++) {
            X_augmented[i][0] = 1;
            System.arraycopy(X[i], 0, X_augmented[i], 1, m);
        }

        double[][] X_transpose = MatrixOperator.transpose(X_augmented);
        double[][] XTX = MatrixOperator.multiply(X_transpose, X_augmented);
        double[][] XTX_inv = MatrixOperator.inverse(XTX);
        double[][] XTy = new double[m + 1][1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j < n; j++) {
                XTy[i][0] += X_transpose[i][j] * y[j];
            }
        }

        double[][] result = MatrixOperator.multiply(XTX_inv, XTy);
        coefficients = new double[m + 1];
        for (int i = 0; i <= m; i++) {
            coefficients[i] = result[i][0];
        }
    }

    @Override
    public double predict(double[] x) {
        double prediction = coefficients[0];
        for (int i = 0; i < x.length; i++) {
            prediction += coefficients[i + 1] * x[i];
        }
        return prediction;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public static void main(String[] args) {
        double[][] X = {{1, 2}, {2, 3}, {3, 4}, {4, 5}};
        double[] y = {2, 3, 4, 5};

        LinearRegression model = new LinearRegression();
        model.train(X, y);

        double[] newInput = {5, 6};
        double prediction = model.predict(newInput);
        System.out.println("Predicted Value: " + prediction);
    }

}
