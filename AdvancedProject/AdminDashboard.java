package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminDashboard extends JFrame {
    // Database connection parameters (modify these for your database)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecommerce_db";
    private static final String DB_USER = "user";
    private static final String DB_PASS = "pass1234";

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add tabs
        tabbedPane.addTab("Dashboard", new DashboardPanel());
        tabbedPane.addTab("Stock Management", new StockManagementPanel());
        tabbedPane.addTab("User Management", new UserManagementPanel());
        tabbedPane.addTab("Orders", new OrdersPanel());

        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }

    // Dashboard Panel
    class DashboardPanel extends JPanel {
        public DashboardPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Summary cards at top
            JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 10, 10));
            summaryPanel.add(createSummaryCard("Total Products", getProductCount(), new Color(70, 130, 180)));
            summaryPanel.add(createSummaryCard("Low Stock", getLowStockCount(), new Color(255, 165, 0)));
            summaryPanel.add(createSummaryCard("Registered Users", getUserCount(), new Color(34, 139, 34)));
            summaryPanel.add(createSummaryCard("Pending Orders", getPendingOrderCount(), new Color(220, 20, 60)));

            add(summaryPanel, BorderLayout.NORTH);

            // Charts panel
            JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            chartsPanel.add(createStockChart());
            chartsPanel.add(createUserActivityChart());

            add(chartsPanel, BorderLayout.CENTER);
        }

        private JPanel createSummaryCard(String title, String value, Color color) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(color);
            card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);

            JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
            valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            valueLabel.setForeground(Color.WHITE);

            card.add(titleLabel, BorderLayout.NORTH);
            card.add(valueLabel, BorderLayout.CENTER);

            return card;
        }

        private JPanel createStockChart() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder("Stock Levels"));
            // In a real application, you would add a chart here
            JLabel placeholder = new JLabel("Stock chart would go here", SwingConstants.CENTER);
            panel.add(placeholder);
            return panel;
        }

        private JPanel createUserActivityChart() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder("User Activity"));
            // In a real application, you would add a chart here
            JLabel placeholder = new JLabel("User activity chart would go here", SwingConstants.CENTER);
            panel.add(placeholder);
            return panel;
        }

        private String getProductCount() {
            return fetchCountFromDB("SELECT COUNT(*) FROM products");
        }

        private String getLowStockCount() {
            return fetchCountFromDB("SELECT COUNT(*) FROM products WHERE quantity < 5");
        }

        private String getUserCount() {
            return fetchCountFromDB("SELECT COUNT(*) FROM users");
        }

        private String getPendingOrderCount() {
            return fetchCountFromDB("SELECT COUNT(*) FROM orders WHERE status = 'pending'");
        }

        private String fetchCountFromDB(String query) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query)) {

                if (rs.next()) {
                    return String.valueOf(rs.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "0";
        }
    }

    // Stock Management Panel
    class StockManagementPanel extends JPanel {
        private JTable stockTable;
        private DefaultTableModel tableModel;

        public StockManagementPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Table model
            String[] columns = { "ID", "Product Name", "Category", "Price", "Quantity", "Status" };
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table non-editable
                }
            };

            // Table
            stockTable = new JTable(tableModel);
            stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(stockTable);

            // Toolbar
            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);

            JButton addBtn = new JButton("Add Product");
            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");
            JButton refreshBtn = new JButton("Refresh");

            addBtn.addActionListener(e -> showAddProductDialog());
            editBtn.addActionListener(e -> editSelectedProduct());
            deleteBtn.addActionListener(e -> deleteSelectedProduct());
            refreshBtn.addActionListener(e -> loadStockData());

            toolBar.add(addBtn);
            toolBar.add(editBtn);
            toolBar.add(deleteBtn);
            toolBar.addSeparator();
            toolBar.add(refreshBtn);

            // Search panel
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JTextField searchField = new JTextField(20);
            JButton searchBtn = new JButton("Search");

            searchBtn.addActionListener(e -> searchProducts(searchField.getText()));

            searchPanel.add(new JLabel("Search:"));
            searchPanel.add(searchField);
            searchPanel.add(searchBtn);

            add(toolBar, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
            add(searchPanel, BorderLayout.SOUTH);

            // Load data
            loadStockData();
        }

        private void loadStockData() {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

                tableModel.setRowCount(0); // Clear existing data

                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity") < 5 ? "Low Stock" : "In Stock"
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading stock data: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void showAddProductDialog() {
            JDialog dialog = new JDialog(AdminDashboard.this, "Add New Product", true);
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(6, 2, 10, 10));

            JTextField nameField = new JTextField();
            JTextField categoryField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField quantityField = new JTextField();
            JTextArea descriptionArea = new JTextArea();

            dialog.add(new JLabel("Product Name:"));
            dialog.add(nameField);
            dialog.add(new JLabel("Category:"));
            dialog.add(categoryField);
            dialog.add(new JLabel("Price:"));
            dialog.add(priceField);
            dialog.add(new JLabel("Quantity:"));
            dialog.add(quantityField);
            dialog.add(new JLabel("Description:"));
            dialog.add(new JScrollPane(descriptionArea));

            JButton saveBtn = new JButton("Save");
            JButton cancelBtn = new JButton("Cancel");

            saveBtn.addActionListener(e -> {
                // Validate and save the product
                try {
                    double price = Double.parseDouble(priceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());

                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                            PreparedStatement pstmt = conn.prepareStatement(
                                    "INSERT INTO products (name, category, price, quantity, description) VALUES (?, ?, ?, ?, ?)")) {

                        pstmt.setString(1, nameField.getText());
                        pstmt.setString(2, categoryField.getText());
                        pstmt.setDouble(3, price);
                        pstmt.setInt(4, quantity);
                        pstmt.setString(5, descriptionArea.getText());
                        pstmt.executeUpdate();

                        loadStockData();
                        dialog.dispose();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for price and quantity",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error saving product: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(saveBtn);
            buttonPanel.add(cancelBtn);

            dialog.add(new JLabel()); // Empty cell
            dialog.add(buttonPanel);

            dialog.setLocationRelativeTo(AdminDashboard.this);
            dialog.setVisible(true);
        }

        private void editSelectedProduct() {
            int selectedRow = stockTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product to edit",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            showEditProductDialog(productId);
        }

        private void showEditProductDialog(int productId) {
            JDialog dialog = new JDialog(AdminDashboard.this, "Edit Product", true);
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(6, 2, 10, 10));

            // Fetch product data
            String name = "";
            String category = "";
            double price = 0;
            int quantity = 0;
            String description = "";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                    PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM products WHERE id = ?")) {

                pstmt.setInt(1, productId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    name = rs.getString("name");
                    category = rs.getString("category");
                    price = rs.getDouble("price");
                    quantity = rs.getInt("quantity");
                    description = rs.getString("description");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(dialog, "Error loading product data: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextField nameField = new JTextField(name);
            JTextField categoryField = new JTextField(category);
            JTextField priceField = new JTextField(String.valueOf(price));
            JTextField quantityField = new JTextField(String.valueOf(quantity));
            JTextArea descriptionArea = new JTextArea(description);

            dialog.add(new JLabel("Product Name:"));
            dialog.add(nameField);
            dialog.add(new JLabel("Category:"));
            dialog.add(categoryField);
            dialog.add(new JLabel("Price:"));
            dialog.add(priceField);
            dialog.add(new JLabel("Quantity:"));
            dialog.add(quantityField);
            dialog.add(new JLabel("Description:"));
            dialog.add(new JScrollPane(descriptionArea));

            JButton saveBtn = new JButton("Save");
            JButton cancelBtn = new JButton("Cancel");

            saveBtn.addActionListener(e -> {
                try {
                    double newPrice = Double.parseDouble(priceField.getText());
                    int newQuantity = Integer.parseInt(quantityField.getText());

                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                            PreparedStatement pstmt = conn.prepareStatement(
                                    "UPDATE products SET name=?, category=?, price=?, quantity=?, description=? WHERE id=?")) {

                        pstmt.setString(1, nameField.getText());
                        pstmt.setString(2, categoryField.getText());
                        pstmt.setDouble(3, newPrice);
                        pstmt.setInt(4, newQuantity);
                        pstmt.setString(5, descriptionArea.getText());
                        pstmt.setInt(6, productId);
                        pstmt.executeUpdate();

                        loadStockData();
                        dialog.dispose();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for price and quantity",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error updating product: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(saveBtn);
            buttonPanel.add(cancelBtn);

            dialog.add(new JLabel()); // Empty cell
            dialog.add(buttonPanel);

            dialog.setLocationRelativeTo(AdminDashboard.this);
            dialog.setVisible(true);
        }

        private void deleteSelectedProduct() {
            int selectedRow = stockTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product to delete",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            String productName = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete '" + productName + "'?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM products WHERE id = ?")) {

                    pstmt.setInt(1, productId);
                    pstmt.executeUpdate();
                    loadStockData();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void searchProducts(String searchTerm) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                    PreparedStatement pstmt = conn.prepareStatement(
                            "SELECT * FROM products WHERE name LIKE ? OR category LIKE ?")) {

                pstmt.setString(1, "%" + searchTerm + "%");
                pstmt.setString(2, "%" + searchTerm + "%");
                ResultSet rs = pstmt.executeQuery();

                tableModel.setRowCount(0); // Clear existing data

                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity") < 5 ? "Low Stock" : "In Stock"
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error searching products: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // User Management Panel
    class UserManagementPanel extends JPanel {
        private JTable userTable;
        private DefaultTableModel tableModel;

        public UserManagementPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Table model
            String[] columns = { "ID", "Username", "Email", "Role", "Registration Date", "Last Login" };
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table non-editable
                }
            };

            // Table
            userTable = new JTable(tableModel);
            userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(userTable);

            // Toolbar
            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);

            JButton editBtn = new JButton("Edit User");
            JButton deleteBtn = new JButton("Delete User");
            JButton refreshBtn = new JButton("Refresh");

            editBtn.addActionListener(e -> editSelectedUser());
            deleteBtn.addActionListener(e -> deleteSelectedUser());
            refreshBtn.addActionListener(e -> loadUserData());

            toolBar.add(editBtn);
            toolBar.add(deleteBtn);
            toolBar.addSeparator();
            toolBar.add(refreshBtn);

            add(toolBar, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);

            // Load data
            loadUserData();
        }

        private void loadUserData() {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

                tableModel.setRowCount(0); // Clear existing data

                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("role"),
                            rs.getDate("registration_date"),
                            rs.getTimestamp("last_login")
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void editSelectedUser() {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to edit",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            showEditUserDialog(userId);
        }

        private void showEditUserDialog(int userId) {
            JDialog dialog = new JDialog(AdminDashboard.this, "Edit User", true);
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(5, 2, 10, 10));

            // Fetch user data
            String username = "";
            String email = "";
            String role = "";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                    PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {

                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    username = rs.getString("username");
                    email = rs.getString("email");
                    role = rs.getString("role");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(dialog, "Error loading user data: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextField usernameField = new JTextField(username);
            JTextField emailField = new JTextField(email);

            JComboBox<String> roleCombo = new JComboBox<>(new String[] { "admin", "customer" });
            roleCombo.setSelectedItem(role);

            dialog.add(new JLabel("Username:"));
            dialog.add(usernameField);
            dialog.add(new JLabel("Email:"));
            dialog.add(emailField);
            dialog.add(new JLabel("Role:"));
            dialog.add(roleCombo);

            JButton saveBtn = new JButton("Save");
            JButton cancelBtn = new JButton("Cancel");

            saveBtn.addActionListener(e -> {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                        PreparedStatement pstmt = conn.prepareStatement(
                                "UPDATE users SET username=?, email=?, role=? WHERE id=?")) {

                    pstmt.setString(1, usernameField.getText());
                    pstmt.setString(2, emailField.getText());
                    pstmt.setString(3, (String) roleCombo.getSelectedItem());
                    pstmt.setInt(4, userId);
                    pstmt.executeUpdate();

                    loadUserData();
                    dialog.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error updating user: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(saveBtn);
            buttonPanel.add(cancelBtn);

            dialog.add(new JLabel()); // Empty cell
            dialog.add(buttonPanel);

            dialog.setLocationRelativeTo(AdminDashboard.this);
            dialog.setVisible(true);
        }

        private void deleteSelectedUser() {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to delete",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String username = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete user '" + username + "'?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {

                    pstmt.setInt(1, userId);
                    pstmt.executeUpdate();
                    loadUserData();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Orders Panel (placeholder - you can implement this similarly)
    class OrdersPanel extends JPanel {
        public OrdersPanel() {
            setLayout(new BorderLayout());
            add(new JLabel("Orders management will be implemented here", SwingConstants.CENTER));
        }
    }
}