/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author NTKC
 */
public class Book {
    String name;
    double price;
    int saleAmount;
    String productCode;

    public Book(String name, double price, int saleAmount, String productCode) {
        this.name = name;
        this.price = price;
        this.saleAmount = saleAmount;
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(int saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    @Override
    public String toString() {
        return String.format("Name: %s | Price: %.2f | Sale: %d | Code: %s", name, price, saleAmount, productCode);
    }
}
