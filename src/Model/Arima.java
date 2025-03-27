package Model;

import java.text.DecimalFormat;
import java.util.*;
import MathOperator.RevenueSummary;

public class Arima {

    private int p, d, q;
    private AutoRegressiveModel arModel;
    private IntergratedModel integratedModel;
    private MovingAverageModel maModel;

    public Arima(int p, int d, int q) {
        this.p = p;
        this.d = d;
        this.q = q;
        this.arModel = new AutoRegressiveModel(p);
        this.integratedModel = new IntergratedModel(d);
        this.maModel = new MovingAverageModel(q);
    }

    public void train(double[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Training data is empty.");
        }
        double[] differencedData = integratedModel.difference(data);
        arModel.train(differencedData);

        double[] residuals = new double[differencedData.length - p];
        for (int i = 0; i < residuals.length; i++) {
            double[] prevValues = new double[p];
            System.arraycopy(differencedData, i, prevValues, 0, p);
            residuals[i] = differencedData[i + p] - arModel.predict(prevValues);
        }
        maModel.train(residuals);
    }

    public double predict(double[] prevData) {
        double[] differencedData = integratedModel.difference(prevData);
        double[] prevAR = Arrays.copyOfRange(differencedData, differencedData.length - p, differencedData.length);
        double arPrediction = arModel.predict(prevAR);

        double[] residuals = maModel.getResiduals();
        double[] prevMA = Arrays.copyOfRange(residuals, residuals.length - q, residuals.length);
        double maPrediction = maModel.predict(prevMA);

        return integratedModel.restore(prevData, arPrediction + maPrediction);
    }
    
    public static void main(String[] args) {
        String baseFolder = "C:/Users/hmqua/OneDrive/Documents/NetBeansProjects/RevenuePrediction/";
        List<String> folders = new ArrayList<>(Arrays.asList("09_03_2025", "11_03_2025", "12_03_2025", "13_03_2025", "14_03_2025", "17_03_2025", "19_03_2025"));
        RevenueSummary summary = new RevenueSummary(baseFolder, folders);
        summary.calculateRevenue();
        Map<String, Double> revenueData = summary.getDailyRevenue();
        double[] revenueArray = revenueData.values().stream().mapToDouble(Double::doubleValue).toArray();

        int p = 2, d = 1, q = 1;
        Arima arima = new Arima(p, d, q);
        arima.train(revenueArray);
        summary.displaySummary();

        double[] futurePredictions = new double[10];
        double[] currentData = Arrays.copyOf(revenueArray, revenueArray.length);
        for (int i = 0; i < 10; i++) {
            double predictedValue = arima.predict(currentData);
            futurePredictions[i] = predictedValue;
            currentData = Arrays.copyOf(currentData, currentData.length + 1);
            currentData[currentData.length - 1] = predictedValue;
        }

        DecimalFormat df = new DecimalFormat("#,### VND");
        System.out.println("Next 10 days prediction:");
        for (int i = 0; i < futurePredictions.length; i++) {
            System.out.println("Day " + (i + 1) + ": " + df.format(futurePredictions[i]));
        }

    }
}

