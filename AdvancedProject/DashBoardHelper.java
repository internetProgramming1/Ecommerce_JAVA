package Ecommerce_JAVA.AdvancedProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashBoardHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Advanced_Project";
    private static final String USER = "root";
    private static final String PASS = "pass1234";

    public int getCount(String query) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // public List<SalesData> getSalesData() {
    // List<SalesData> salesData = new ArrayList<>();
    // String query = "SELECT DATE(order_date) as day, SUM(total_amount) as amount "
    // +
    // "FROM orders GROUP BY DATE(order_date) ORDER BY day DESC LIMIT 7";

    // try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
    // Statement stmt = conn.createStatement();
    // ResultSet rs = stmt.executeQuery(query)) {
    // while (rs.next()) {
    // salesData.add(new SalesData(
    // rs.getDate("day"),
    // rs.getDouble("amount")));
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return salesData;
    // }

    public List<String> getRecentActivities() {
        List<String> activities = new ArrayList<>();
        String query = "SELECT CONCAT('User ', username, ' registered') AS activity_text, created_at AS timestamp FROM users "
                +
                "UNION ALL " +
                "SELECT CONCAT('product \"', name, '\" added') AS activity_text, created_at AS timestamp FROM products "
                +
                "UNION ALL " +
                "SELECT CONCAT('Order #', id, ' placed by ', username) AS activity_text, order_date AS timestamp FROM `orders` "
                +
                "ORDER BY timestamp DESC " +
                "LIMIT 5";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                activities.add(rs.getString("activity_text") + " - " + rs.getTimestamp("timestamp"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT id, name, description, price, image_path,stock_quantity,status FROM products";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("image_path"),
                        rs.getInt("stock_quantity"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, email,phoneNumber FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phoneNumber"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public List<Order1> getOrders() {
        List<Order1> orders = new ArrayList<>();
        String query = "SELECT id, username, order_date, product_name, quantity, total,price, status,delivery_address FROM orders";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {

                Order1 order = new Order1();
                order.setId(rs.getInt("id"));
                order.setUsername(rs.getString("username"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setProductName(rs.getString("product_name"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotal(rs.getDouble("total"));
                order.setPrice(rs.getDouble("price"));
                order.setStatus(rs.getString("status"));
                order.setDeliveryAddress(rs.getString("delivery_address"));

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, status, image_path, stock_quantity) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (product.getStockQuantity() > 0) {
                stmt.setString(4, "In Stock");
            }
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());

            stmt.setString(5, product.getImagePath());
            stmt.setInt(6, product.getStockQuantity());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            return false;
        }
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        Product product = new Product(0, "", "", 0.0, "", 0, "");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStockQuantity(rs.getInt("stock"));
                    product.setImagePath(rs.getString("imagePath"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting product by ID: " + e.getMessage());
        }

        return product;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_quantity = ?, status = ?, image_path = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setString(5, product.getStatus());
            stmt.setString(6, product.getImagePath());
            stmt.setInt(7, product.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }

    public List<Product> searchProducts(String query) {
        List<Product> products = new ArrayList<>();
        String searchQuery = "%" + query.toLowerCase() + "%";

        String sql = "SELECT * FROM products WHERE " +
                "LOWER(name) LIKE ? OR " +
                "LOWER(description) LIKE ? OR " +
                "LOWER(price) LIKE ? OR " +
                "CAST(image_path AS CHAR) LIKE ? OR " +
                "CAST(stock_quantity AS CHAR) LIKE ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, searchQuery);
            stmt.setString(2, searchQuery);
            stmt.setString(3, searchQuery);
            stmt.setString(4, searchQuery);
            stmt.setString(5, searchQuery);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = null;
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStockQuantity(rs.getInt("stock"));
                    product.setImagePath(rs.getString("category"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }

        return products;
    }

}