package Ecommerce_JAVA.AdvancedProject;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imagePath;
    private int stockQuantity;
    private String status; // e.g., "available", "out of stock"

    public Product(int id, String name, String description, double price, String imagePath, int stockQuantity,
            String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
        this.stockQuantity = stockQuantity;
        this.status = status;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void decreaseStock(int quantity) {
        this.stockQuantity -= quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }

}