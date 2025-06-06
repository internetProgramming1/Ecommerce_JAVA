package Ecommerce_JAVA.AdvancedProject;

public class CartItem {
    private int id;
    private String productName;
    private double price;
    private int quantity;
    private String imagePath;
    int product_id;

    public CartItem(int id, int product_id, String productName, double price, int quantity, String imagePath) {
        this.id = id;
        this.product_id = product_id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}
