package MathOperator;
import java.text.DecimalFormat;
import java.io.IOException;
import java.util.*;
import Model.Book;
import Repository.BookRepository;

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

    public void displaySummary() {
        System.out.println("\n=== Revenue summarizing ===");
        double total = dailyRevenue.values().stream().mapToDouble(Double::doubleValue).sum();
        dailyRevenue.forEach((day, revenue) -> System.out.println(day + ": " + df.format(revenue)));
        System.out.println("Total revenue: " + df.format(total));
    }

    public Map<String, Double> getDailyRevenue() {
        return dailyRevenue;
    }
}
