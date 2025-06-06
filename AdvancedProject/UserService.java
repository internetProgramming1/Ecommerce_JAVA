package Ecommerce_JAVA.AdvancedProject;

import java.sql.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class UserService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String DB_USER = "your_db_user";
    private static final String DB_PASS = "your_db_password";

    // Register a new customer (already exists in your code)
    public static boolean registerCustomer(String fullName, String email, String password) {
        // ... your existing registration code ...
    }

    // Hash password using PBKDF2 (your existing method)
    private static String hashPassword(String password) throws Exception {
        // ... your existing hashing code ...
    }

    // Generate random salt (your existing method)
    private static byte[] getSalt() throws Exception {
        // ... your existing salt code ...
    }

    // Verify password (your existing method)
    public static boolean verifyPassword(String email, String password) {
        // ... your existing password verification code ...
    }

    private static boolean verifyPasswordHash(String password, String stored) throws Exception {
        // ... your existing hash verification code ...
    }

    // *** Add this login method for authentication ***
    public static boolean login(String email, String password) {
        // Return true if email exists and password matches
        return verifyPassword(email, password);
    }

    // Optional: get user info by email (if needed after login)
    public static Customer getCustomerByEmail(String email) {
        String sql = "SELECT id, full_name, email, created_at FROM customers WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
