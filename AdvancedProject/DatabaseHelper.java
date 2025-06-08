package Ecommerce_JAVA.AdvancedProject;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Advanced_Project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pass1234";

    public static boolean saveOrder(String username, List<Cart.CartItem> items, double total, String deliveryAddress) {
        String insertOrderSQL = "INSERT INTO orders (username, total, delivery_address, product_id, product_name, quantity, price) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateStockSQL = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL);
                PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL)) {

            conn.setAutoCommit(false); // Begin transaction

            // Insert each item as a separate order record
            for (Cart.CartItem item : items) {
                orderStmt.setString(1, username);
                orderStmt.setDouble(2, total);
                orderStmt.setString(3, deliveryAddress);
                orderStmt.setInt(4, item.getProduct().getId());
                orderStmt.setString(5, item.getProduct().getName());
                orderStmt.setInt(6, item.getQuantity());
                orderStmt.setDouble(7, item.getProduct().getPrice());
                orderStmt.addBatch();

                // Update product stock
                updateStockStmt.setInt(1, item.getQuantity());
                updateStockStmt.setInt(2, item.getProduct().getId());
                updateStockStmt.addBatch();
            }

            // Execute all batch operations
            orderStmt.executeBatch();
            updateStockStmt.executeBatch();
            conn.commit(); // Success

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("image_path"),
                        rs.getInt("stock_quantity"),
                        rs.getString("status"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

}