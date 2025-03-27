package MathOperator;

import java.text.DecimalFormat;
import java.io.IOException;
import java.util.*;
import Model.Book;
import Repository.BookRepository;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class RevenueSummary {

    private String baseFolder;
    private List<String> folders;
    private DecimalFormat df;
    private Map<String, Double> dailyRevenue;

    public RevenueSummary(String baseFolder, List<String> folders) {
        this.baseFolder = baseFolder;
        this.folders = folders;
        this.df = new DecimalFormat("#,### VND");
        this.dailyRevenue = new LinkedHashMap<>();
    }

    public void calculateRevenue() {
        for (String folder : folders) {
            String folderPath = baseFolder + "/" + folder;
            try {
                List<Book> books = BookRepository.readBooks(folderPath);
                double totalRevenue = books.stream().mapToDouble(b -> b.getPrice() * b.getSaleAmount()).sum();
                dailyRevenue.put(folder, totalRevenue);
            } catch (IOException e) {
                System.err.println("Error processing folder: " + folder);
            }
        }
    }

    public void writeRevenueToFile(List<String> filenames) {
        for (String filename : filenames) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write("=== Revenue Summary ===\n");
                for (Map.Entry<String, Double> entry : dailyRevenue.entrySet()) {
                    writer.write(entry.getKey() + ": " + df.format(entry.getValue()) + "\n");
                }
                double total = dailyRevenue.values().stream().mapToDouble(Double::doubleValue).sum();
                writer.write("Total revenue: " + df.format(total) + "\n");
                System.out.println("Revenue summary written to " + filename);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + filename);
            }
        }
    }

    public void displaySummary() {
        System.out.println("\n=== Revenue summarizing ===");
        dailyRevenue.forEach((day, revenue) -> System.out.println(day + ": " + df.format(revenue)));
    }

    public Map<String, Double> getDailyRevenue() {
        return dailyRevenue;
    }
}
