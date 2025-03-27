package Model;

import MathOperator.MatrixOperator;
import PredictionInterface.TimeSeriesModel;

public class AutoRegressiveModel{

    private int p;
    private double[] phi;

    public AutoRegressiveModel(int p) {
        if (p <= 0) {
            throw new IllegalArgumentException("The order of the model must be greater than zero.");
        }
        this.p = p;
    }

    public void train(double[] data) {
        int n = data.length;
        if (n <= p) {
            System.out.println("Not enough data to train the model.");
            return;
        }

        double[][] x = new double[n - p][p];
        double[] y = new double[n - p];
        for (int i = 0; i < n - p; i++) {
            y[i] = data[i + p];
            for (int j = 0; j < p; j++) {
                x[i][j] = data[i + p - j - 1];
            }
        }

        double[][] xt = MatrixOperator.transpose(x);
        double[][] xtx = MatrixOperator.multiply(xt, x);
        double[][] xtx_inv = MatrixOperator.inverse(xtx);
        double[] xty = MatrixOperator.multiply(xt, y);
        this.phi = MatrixOperator.multiply(xtx_inv, xty);

        System.out.println("Model trained successfully.");
        System.out.println("Coefficients (phi):");
        for (double coefficient : phi) {
            System.out.println(coefficient);
        }
    }

    public double predict(double[] prevValues) {
        if (prevValues.length != p) {
            throw new IllegalArgumentException("Invalid input length: expected " + p + " previous values.");
        }
        
        double prediction = MatrixOperator.multiply(new double[][]{phi}, prevValues)[0];
        
        return prediction;
    }

}
