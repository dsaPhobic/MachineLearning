package Model;

import MathOperator.MatrixOperator;

public class MovingAverageModel {

    private int q;
    private double[] theta;
    private double[] residuals;

    public MovingAverageModel(int q) {
        this.q = q;
    }

    public void train(double[] data) {
        int n = data.length;
        if (n <= q) {
            System.out.println("Not enough data for training");
            return;
        }
        residuals = new double[n - 1];
        for (int i = 1; i < n; i++) {
            residuals[i - 1] = data[i] - data[i - 1];
        }

        double[][] X = new double[n - 1 - q][q];
        double[] Y = new double[n - 1 - q];

        for (int i = 0; i < n - 1 - q; i++) {
            Y[i] = residuals[i + q];
            for (int j = 0; j < q; j++) {
                X[i][j] = residuals[i + q - j - 1];
            }
        }
        double[][] Xt = MatrixOperator.transpose(X);
        double[][] XtX = MatrixOperator.multiply(Xt, X);
        double[][] XtX_inv = MatrixOperator.inverse(XtX);
        double[] XtY = MatrixOperator.multiply(Xt, Y);
        this.theta = MatrixOperator.multiply(XtX_inv, XtY);
    }

    public double predict(double[] prevResiduals) {
        if (prevResiduals.length != q) {
            throw new IllegalArgumentException("Invalid input length");
        }
        return MatrixOperator.multiply(new double[][]{theta}, prevResiduals)[0];
    }

    public double[] getResiduals() {
        return residuals;
    }
}
