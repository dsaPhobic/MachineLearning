package Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import Model.Book;

public class BookRepository {

    public static List<Book> readBooks(String folderPath) throws IOException {
        List<Book> books = new ArrayList<>();

        try {
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Book book = extractBookInfo(file);
                            if (book != null) {
                                books.add(book);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Error reading file: " + file, e);
                        }
                    });
        } catch (IOException e) {
            throw new IOException("Error accessing folder: " + folderPath, e);
        }
        return books;
    }

    private static Book extractBookInfo(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        String name = "", productCode = "";
        double price = 0;
        int saleAmount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.equals("Name:") && i + 1 < lines.size()) {
                name = lines.get(i + 1).trim();
            } else if (line.equals("Price:") && i + 1 < lines.size()) {
                price = parseNumber(lines.get(i + 1).trim(), 0.0);
            } else if (line.equals("Sale amount:") && i + 1 < lines.size()) {
                saleAmount = (int) parseNumber(lines.get(i + 1).trim(), 0);
            } else if (line.equals("Product code:") && i + 1 < lines.size()) {
                productCode = lines.get(i + 1).trim();
            }
        }
        if (name.isEmpty() || productCode.isEmpty()) {
            return null;
        }
        return new Book(name, price, saleAmount, productCode);
    }

    private static double parseNumber(String str, double defaultValue) {
        try {
            return Double.parseDouble(str.replace(".", "").replace(",", ".").replace(" đ", "").replace("Đã bán ", "").trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}