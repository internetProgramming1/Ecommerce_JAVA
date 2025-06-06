package Ecommerce_JAVA.AdvancedProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Advanced_Project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pass1234";

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
                        rs.getInt("stock_quantity"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static void addToCart(int userId, int productId, int quantity) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        // Check if product exists and has sufficient stock
        if (!isProductAvailable(productId, quantity)) {
            throw new IllegalStateException("Product not available in requested quantity");
        }

        String sql;
        // Check if product already exists in cart
        if (isProductInCart(userId, productId)) {
            sql = "UPDATE cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
        } else {
            sql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (isProductInCart(userId, productId)) {
                pstmt.setInt(1, quantity);
                pstmt.setInt(2, userId);
                pstmt.setInt(3, productId);
            } else {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, productId);
                pstmt.setInt(3, quantity);
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding to cart failed, no rows affected.");
            }

            // Update product stock
            updateProductStock(productId, -quantity);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add product to cart", e);
        }
    }

    private static boolean isProductAvailable(int productId, int requestedQuantity) {
        String sql = "SELECT stock_quantity FROM products WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int availableQuantity = rs.getInt("stock_quantity");
                    return availableQuantity >= requestedQuantity;
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isProductInCart(int userId, int productId) {
        String sql = "SELECT 1 FROM cart WHERE user_id = ? AND product_id = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void updateProductStock(int productId, int quantityChange) {
        String sql = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantityChange);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update product stock", e);
        }
    }

    // Additional useful cart methods
    public static List<CartItem> getCartItems(int userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.id, p.id as product_id, p.name, p.price, c.quantity, p.image_path " +
                "FROM cart c JOIN products p ON c.product_id = p.id " +
                "WHERE c.user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem(
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getString("image_path"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static void removeFromCart(int cartItemId) {
        // First get the quantity and product ID to restore stock
        String selectSql = "SELECT product_id, quantity FROM cart WHERE id = ?";
        String deleteSql = "DELETE FROM cart WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            // Get the item details first
            selectStmt.setInt(1, cartItemId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");

                    // Delete the item
                    deleteStmt.setInt(1, cartItemId);
                    deleteStmt.executeUpdate();

                    // Restore stock
                    updateProductStock(productId, quantity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to remove item from cart", e);
        }
    }

    public static void updateCartItemQuantity(int cartItemId, int newQuantity) {
        if (newQuantity <= 0) {
            removeFromCart(cartItemId);
            return;
        }

        // First get the current quantity and product ID
        String selectSql = "SELECT product_id, quantity FROM cart WHERE id = ?";
        String updateSql = "UPDATE cart SET quantity = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Get current item details
            selectStmt.setInt(1, cartItemId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int currentQuantity = rs.getInt("quantity");
                    int quantityDifference = newQuantity - currentQuantity;

                    // Check if product has enough stock
                    if (quantityDifference > 0 && !isProductAvailable(productId, quantityDifference)) {
                        throw new IllegalStateException("Not enough stock available");
                    }

                    // Update quantity
                    updateStmt.setInt(1, newQuantity);
                    updateStmt.setInt(2, cartItemId);
                    updateStmt.executeUpdate();

                    // Update product stock
                    updateProductStock(productId, -quantityDifference);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update cart item quantity", e);
        }
    }

    public static double getCartTotal(int userId) {
        String sql = "SELECT SUM(p.price * c.quantity) as total " +
                "FROM cart c JOIN products p ON c.product_id = p.id " +
                "WHERE c.user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}