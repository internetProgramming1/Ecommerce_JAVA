package Ecommerce_JAVA.AdvancedProject;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imagePath;
    private int stockQuantity;

    public Product(int id, String name, String description, double price, String imagePath, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
        this.stockQuantity = stockQuantity;
    }

    // Getters and setters
    public int getId() {
        return id;
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
}