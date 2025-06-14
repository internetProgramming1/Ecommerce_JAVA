package Ecommerce_JAVA.AdvancedProject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class AdminDAO {
    private Connection connection;

    public AdminDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Advanced_Project", "root",
                    "pass1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean isAdminExists(String adminId) {
        try {
            String query = "SELECT COUNT(*) FROM admins WHERE admin_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, adminId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveAdmin(Admin admin) {
        try {
            String query = "INSERT INTO admins (admin_id, full_name, email, phone, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, admin.getAdminId());
            stmt.setString(2, admin.getFullName());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPhone());
            stmt.setString(5, admin.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Login with adminId and password
    public Admin login(String adminId, String plainPassword) {
        try {
            String query = "SELECT * FROM admins WHERE admin_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, adminId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                String inputHash = AdminDAO.hashPassword(plainPassword);

                if (storedHash.equals(inputHash)) {
                    Admin admin = new Admin();
                    admin.setAdminId(adminId);
                    admin.setFullName(rs.getString("full_name"));
                    admin.setEmail(rs.getString("email"));
                    admin.setPhone(rs.getString("phone"));
                    admin.setPassword(storedHash); // optional

                    return admin;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
