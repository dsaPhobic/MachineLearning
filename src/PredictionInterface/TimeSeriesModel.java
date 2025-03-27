/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package PredictionInterface;

/**
 *
 * @author hmqua
 */
public interface TimeSeriesModel {

    void train(double[][] X, double[] y);

    double predict(double[] x);
}
