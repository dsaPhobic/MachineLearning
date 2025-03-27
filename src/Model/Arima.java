package Model;

import java.text.DecimalFormat;
import java.util.*;
import MathOperator.RevenueSummary;
import Repository.RevenueRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public double[] predictNextDays(double[] initialData, int days) {
        double[] futurePredictions = new double[days];
        double[] currentData = Arrays.copyOf(initialData, initialData.length);
        for (int i = 0; i < days; i++) {
            double predictedValue = predict(currentData);
            futurePredictions[i] = predictedValue;
            currentData = Arrays.copyOf(currentData, currentData.length + 1);
            currentData[currentData.length - 1] = predictedValue;
        }
        return futurePredictions;
    }

    public static void main(String[] args) {
        String baseFolder = "C:/Users/hmqua/OneDrive/Documents/NetBeansProjects/RevenuePrediction/RevenueByDay";
        List<String> folders = new ArrayList<>(Arrays.asList("09_03_2025", "11_03_2025", "12_03_2025", "13_03_2025", "14_03_2025", "17_03_2025", "19_03_2025"));
        RevenueSummary summary = new RevenueSummary(baseFolder, folders);
        summary.calculateRevenue();

        Map<String, Double> revenueData = summary.getDailyRevenue();
        double[] revenueArray = revenueData.values().stream().mapToDouble(Double::doubleValue).toArray();

        int p = 2, d = 1, q = 1;
        Arima arima = new Arima(p, d, q);
        arima.train(revenueArray);
        summary.displaySummary();

        System.out.println("Next 10 days prediction");
        double[] next10Days = arima.predictNextDays(revenueArray, 10);
        DecimalFormat df = new DecimalFormat("#,###");

        LocalDate today = LocalDate.now();
        ArrayList<Double> revenueList = new ArrayList<>();

        for (int i = 0; i < next10Days.length; i++) {
            LocalDate predictionDate = today.plusDays(i + 1);
            System.out.println("Day " + (i + 1) + " (" + predictionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "): " + df.format(next10Days[i]));
            revenueList.add(next10Days[i]);
        }

        RevenueRepository.savePredictions(revenueList,next10Days.length);
    }

}
