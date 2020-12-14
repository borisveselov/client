package edu.courseproject.client.entity;

public class Product {
    private long idProduct;
    private String name;
    private String category;
    private double price;

    public Product() {
    }

    public Product(long idProduct, String name, String category, double price) {
        this.idProduct = idProduct;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(long idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
