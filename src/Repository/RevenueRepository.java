package Repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RevenueRepository {

    private static final String DIRECTORY_PATH = "C:/Users/hmqua/OneDrive/Documents/NetBeansProjects/RevenuePrediction/PredictedData";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DecimalFormat df = new DecimalFormat("#,### VND");

    public static void savePredictions(ArrayList<Double> predictions,int numberOfDate) {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String filePath = DIRECTORY_PATH + "/predictions_" + timestamp + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Next "+numberOfDate+" days revenue prediction:\n");

            LocalDate today = LocalDate.now();
            for (int i = 0; i < predictions.size(); i++) {
                LocalDate predictionDate = today.plusDays(i + 1);
                writer.write("Day " + (i + 1) + " (" + predictionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "): "
                        + df.format(predictions.get(i)) + "\n");
            }

            System.out.println("Predictions successfully saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing predictions to file: " + e.getMessage());
        }
    }
}
