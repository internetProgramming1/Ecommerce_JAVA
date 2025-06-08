package Ecommerce_JAVA.AdvancedProject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
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

    public boolean isUserExists(String username, String email) {
        try {
            String query = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveUser(User user) {
        try {
            String query = "INSERT INTO users (fullname, email, phoneNumber, username, password_hash) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getUsername());
            stmt.setString(5, user.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User login(String username, String plainPassword) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password_hash");

                // Hash input password and compare
                String hashedInput = UserDAO.hashPassword(plainPassword);
                if (storedHashedPassword.equals(hashedInput)) {
                    User user = new User();
                    user.setFullName(rs.getString("fullname"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phoneNumber"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(storedHashedPassword); // optional

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // login failed
    }
}
