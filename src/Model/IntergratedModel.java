package Model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author hmqua
 */
public class IntergratedModel {

    private int d;

    public IntergratedModel (int d) {
        this.d = d;
    }

    public double[] difference(double[] data) {
        return differenceRecursive(data, d);
    }

    private double[] differenceRecursive(double[] data, int order) {
        if (order == 0) {
            return data;
        }
        double[] diff = new double[data.length - 1];
        for (int i = 1; i < data.length; i++) {
            diff[i - 1] = data[i] - data[i - 1];
        }
        return differenceRecursive(diff, order - 1);
    }

    public double restore(double[] lastValues, double predicted) {
        double restored = predicted;
        for (int i = d - 1; i >= 0; i--) {
            restored += lastValues[i];
        }
        return restored;
    }

}
