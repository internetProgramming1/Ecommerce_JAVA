package Ecommerce_JAVA.AdvancedProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public static boolean registerAdmin(String fullName, String email, String password) {
        String sql = "INSERT INTO admins (full_name, email, password) VALUES (?, ?, ?)";

        // Store password as plain text for now
        String storedPassword = password;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, storedPassword);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Admin registration failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean loginAdmin(String email, String password) {
        String sql = "SELECT password FROM admins WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // Plain text password comparison
                if (password.equals(storedPassword)) {
                    return true;  // password matches
                }
            }
            return false;  // email not found or password mismatch

        } catch (SQLException e) {
            System.out.println("Admin login failed: " + e.getMessage());
            return false;
        }
    }
}
