package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.Date;
import javax.swing.table.TableCellRenderer;
import java.util.List;

public class AdminDashboard extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(255, 140, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 35);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private MainApplication mainApp;
    private JTabbedPane tabbedPane;
    private DashBoardHelper dbHelper;

    public AdminDashboard(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.dbHelper = new DashBoardHelper();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(SECONDARY_COLOR);

        JPanel navBar = createNavBar();
        add(navBar, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Products", createProductsPanel());
        tabbedPane.addTab("Users", createUsersPanel());
        tabbedPane.addTab("Orders", createOrdersPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(10, 30, 10, 30)));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("BusyBuy Admin");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY_COLOR);
        logoPanel.add(title);
        navPanel.add(logoPanel, BorderLayout.WEST);

        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        navButtons.setBackground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        styleAccentButton(logoutButton);
        logoutButton.addActionListener(e -> {
            mainApp.showView(MainApplication.LOGIN_VIEW);
        });
        navButtons.add(logoutButton);

        navPanel.add(navButtons, BorderLayout.EAST);

        return navPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout(20, 20));
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        dashboardPanel.setBackground(SECONDARY_COLOR);

        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        summaryPanel.setBackground(SECONDARY_COLOR);

        int totalProducts = dbHelper.getCount("SELECT COUNT(*) FROM products");
        int lowStockItems = dbHelper.getCount("SELECT COUNT(*) FROM products WHERE stock_quantity < 10");
        int activeUsers = dbHelper
                .getCount("SELECT COUNT(*) FROM users WHERE created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY)");
        int pendingOrders = dbHelper.getCount("SELECT COUNT(*) FROM orders WHERE status = 'pending'");

        summaryPanel.add(createSummaryCard("Total Products", String.format("%,d", totalProducts), PRIMARY_COLOR));
        summaryPanel.add(createSummaryCard("Low Stock", String.format("%,d", lowStockItems), new Color(220, 20, 60)));
        summaryPanel.add(createSummaryCard("Active Users", String.format("%,d", activeUsers), new Color(34, 139, 34)));
        summaryPanel.add(createSummaryCard("Pending Orders", String.format("%,d", pendingOrders), ACCENT_COLOR));

        dashboardPanel.add(summaryPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        contentPanel.setBackground(SECONDARY_COLOR);

        JPanel salesPanel = new JPanel(new BorderLayout());
        salesPanel.setBackground(Color.WHITE);
        salesPanel.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel salesTitle = new JLabel("Sales Overview");
        salesTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        salesTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        salesPanel.add(salesTitle, BorderLayout.NORTH);

        // List<SalesData> salesData = dbHelper.getSalesData();
        // JLabel chartPlaceholder = new JLabel("Sales chart would be displayed here",
        // SwingConstants.CENTER);
        // chartPlaceholder.setForeground(new Color(150, 150, 150));
        // salesPanel.add(chartPlaceholder, BorderLayout.CENTER);

        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel activityTitle = new JLabel("Recent Activity");
        activityTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        activityTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        activityPanel.add(activityTitle, BorderLayout.NORTH);

        List<String> activities = dbHelper.getRecentActivities();
        JList<String> activityList = new JList<>(activities.toArray(new String[0]));
        activityList.setBackground(Color.WHITE);
        activityList.setFont(SUBTITLE_FONT);
        activityList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                super.setSelectionInterval(-1, -1);
            }
        });

        activityPanel.add(new JScrollPane(activityList), BorderLayout.CENTER);

        contentPanel.add(salesPanel);
        contentPanel.add(activityPanel);

        dashboardPanel.add(contentPanel, BorderLayout.CENTER);

        return dashboardPanel;
    }

    private JPanel createProductsPanel() {
        JPanel productsPanel = new JPanel(new BorderLayout(10, 10));
        productsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        productsPanel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        productsPanel.add(titleLabel, BorderLayout.NORTH);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(SECONDARY_COLOR);
        toolBar.setBorder(new EmptyBorder(0, 0, 10, 0));

        JButton addButton = new JButton("Add Product");
        styleAccentButton(addButton);
        addButton.addActionListener(e -> showAddProductDialog());

        JButton editButton = new JButton("Edit");
        styleNavButton(editButton);
        editButton.addActionListener(e -> editSelectedProduct());

        JButton deleteButton = new JButton("Delete");
        styleNavButton(deleteButton);
        deleteButton.addActionListener(e -> deleteSelectedProduct());

        JButton refreshButton = new JButton("Refresh");
        styleNavButton(refreshButton);
        refreshButton.addActionListener(e -> loadProductData());

        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);

        productsPanel.add(toolBar, BorderLayout.NORTH);

        String[] columns = { "ID", "Name", "Description", "Price", "Stock", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable productTable = new JTable(model);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setFont(SUBTITLE_FONT);
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        productTable.setRowHeight(30);

        List<Product> products = dbHelper.getProducts();
        for (Product product : products) {
            String status = product.getStockQuantity() == 0 ? "Out of Stock"
                    : (product.getStockQuantity() < 10 ? "Low Stock" : "In Stock");
            model.addRow(new Object[] {
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getImagePath(),
                    status
            });
        }

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        productsPanel.add(scrollPane, BorderLayout.CENTER);

        return productsPanel;
    }

    private JPanel createUsersPanel() {
        JPanel usersPanel = new JPanel(new BorderLayout(10, 10));
        usersPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        usersPanel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        usersPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = { "ID", "Username", "Email", "Phone No" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable userTable = new JTable(model);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setFont(SUBTITLE_FONT);
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.setRowHeight(30);

        List<User> users = dbHelper.getUsers();
        for (User user : users) {
            model.addRow(new Object[] {
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone()
            });
        }

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        usersPanel.add(scrollPane, BorderLayout.CENTER);

        return usersPanel;
    }

    private JPanel createOrdersPanel() {
        JPanel ordersPanel = new JPanel(new BorderLayout(10, 10));
        ordersPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        ordersPanel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        ordersPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = { "Order ID", "Customer", "Date", "Amount", "Status", "Actions" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? JButton.class : Object.class;
            }
        };

        JTable orderTable = new JTable(model);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.setFont(SUBTITLE_FONT);
        orderTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        orderTable.setRowHeight(40);

        List<Order> orders = dbHelper.getOrders();
        for (Order order : orders) {
            model.addRow(new Object[] {
                    order.getId(),
                    order.getCustomerName(),
                    order.getOrderDate(),
                    order.getAmount(),
                    order.getStatus(),
                    createViewButton()
            });
        }

        orderTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        orderTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        ordersPanel.add(scrollPane, BorderLayout.CENTER);

        return ordersPanel;
    }

    private JButton createViewButton() {
        JButton button = new JButton("View");
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private JPanel createSummaryCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(color);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(0, 0, 0, 0.1f), 1),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void styleNavButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setForeground(new Color(80, 80, 80));
        button.setBackground(Color.WHITE);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(PRIMARY_COLOR);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(80, 80, 80));
            }
        });
    }

    private void styleAccentButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_COLOR);
        button.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR.darker(), 1),
                new EmptyBorder(8, 20, 8, 20)));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR.brighter());
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JButton) {
                return (JButton) value;
            }
            return this;
        }
    }

    //
    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value instanceof JButton) {
                button = (JButton) value;
                label = button.getText();
            }
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                JOptionPane.showMessageDialog(button, "Order details would be shown here");
            }
            isPushed = false;
            return label;
        }
    }

    private void showAddProductDialog() {
        JDialog dialog = new JDialog(mainApp, "Add New Product", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(550, 480);
        dialog.setLocationRelativeTo(mainApp);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();
        JTextArea descriptionArea = new JTextArea(3, 20);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        JLabel imageLabel = new JLabel("No Image Selected");
        JButton chooseImageButton = new JButton("Choose Image");

        final String[] imagePath = { null }; // For selected image path

        chooseImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Product Image");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image Files", "jpg", "jpeg", "png", "gif"));

            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePath[0] = selectedFile.getAbsolutePath();
                imageLabel.setText(selectedFile.getName());
            }
        });

        // Add fields to form
        formPanel.add(new JLabel("Product Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Stock Quantity:"));
        formPanel.add(stockField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionScroll);
        formPanel.add(new JLabel("Image:"));

        JPanel imageChooserPanel = new JPanel(new BorderLayout());
        imageChooserPanel.add(imageLabel, BorderLayout.CENTER);
        imageChooserPanel.add(chooseImageButton, BorderLayout.EAST);
        formPanel.add(imageChooserPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save Product");
        styleAccentButton(saveButton);
        styleNavButton(cancelButton);

        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                String description = descriptionArea.getText().trim();

                if (name.isEmpty())
                    throw new IllegalArgumentException("Product name cannot be empty");
                if (price <= 0)
                    throw new IllegalArgumentException("Price must be positive");
                if (stock < 0)
                    throw new IllegalArgumentException("Stock cannot be negative");

                // Automatically determine status
                String status = (stock > 0) ? "Available" : "Out of Stock";

                Product product = new Product(0, "", "", 0.0, "", 0, "");
                product.setName(name);
                product.setPrice(price);
                product.setStockQuantity(stock);
                product.setStatus(status);
                product.setDescription(description);
                product.setImagePath(imagePath[0]); // nullable

                if (dbHelper.addProduct(product)) {
                    JOptionPane.showMessageDialog(dialog, "Product added successfully!");
                    loadProductData(); // Refresh table
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add product", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for price and stock", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void editSelectedProduct() {
        JTable productTable = getProductTable(); // Helper method to get the table reference
        int selectedRow = productTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to edit",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) productTable.getValueAt(selectedRow, 0);
        Product product = dbHelper.getProductById(productId);

        if (product == null) {
            JOptionPane.showMessageDialog(this, "Product not found",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(mainApp, "Edit Product", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(mainApp);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField(product.getName());
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
        JTextField stockField = new JTextField(String.valueOf(product.getStockQuantity()));
        JComboBox<String> categoryCombo = new JComboBox<>(
                new String[] { "Electronics", "Clothing", "Home", "Books", "Other" });
        JTextArea descriptionArea = new JTextArea(product.getDescription(), 3, 20);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        // File chooser for optional image update
        JLabel imageLabel = new JLabel(product.getImagePath() != null ? product.getImagePath() : "No image selected");
        JButton imageButton = new JButton("Choose Image");
        final String[] selectedImagePath = { product.getImagePath() }; // initialize with existing

        imageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(dialog);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedImagePath[0] = selectedFile.getAbsolutePath();
                imageLabel.setText(selectedFile.getName());
            }
        });

        formPanel.add(new JLabel("Product Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Stock Quantity:"));
        formPanel.add(stockField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryCombo);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionScroll);
        formPanel.add(imageButton);
        formPanel.add(imageLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save Changes");
        styleAccentButton(saveButton);
        styleNavButton(cancelButton);

        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty())
                    throw new IllegalArgumentException("Product name cannot be empty");

                double price = Double.parseDouble(priceField.getText());
                if (price <= 0)
                    throw new IllegalArgumentException("Price must be positive");

                int stock = Integer.parseInt(stockField.getText());
                if (stock < 0)
                    throw new IllegalArgumentException("Stock cannot be negative");

                String category = (String) categoryCombo.getSelectedItem();
                String description = descriptionArea.getText().trim();

                // Auto-calculate status
                String status = (stock > 0) ? "In Stock" : "Out of Stock";

                // Update product object
                product.setName(name);
                product.setPrice(price);
                product.setStockQuantity(stock);
                product.setStatus(status);
                product.setDescription(description);
                product.setImagePath(selectedImagePath[0]);

                if (dbHelper.updateProduct(product)) {
                    JOptionPane.showMessageDialog(dialog, "Product updated successfully!");
                    loadProductData(); // Refresh table
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update product", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for price and stock",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelectedProduct() {
        JTable productTable = getProductTable();
        int selectedRow = productTable.getSelectedRow();

        // Check if a row is selected
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Retrieve product ID and name from the table
        int productId = (int) productTable.getValueAt(selectedRow, 0);
        String productName = (String) productTable.getValueAt(selectedRow, 1);

        // Confirm deletion with the user
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete \"" + productName + "\"?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        // If user confirms
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = dbHelper.deleteProduct(productId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully");
                loadProductData(); // Refresh table after deletion
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadProductData() {
        try {
            DefaultTableModel model = (DefaultTableModel) getProductTable().getModel();
            model.setRowCount(0); // Clear existing data

            List<Product> products = dbHelper.getProducts();
            for (Product product : products) {
                String status = product.getStockQuantity() == 0 ? "Out of Stock"
                        : (product.getStockQuantity() < 10 ? "Low Stock" : "In Stock");
                model.addRow(new Object[] {
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStockQuantity(),
                        status
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading products: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Helper method to get the product table reference
    private JTable getProductTable() {
        Component comp = tabbedPane.getComponentAt(1); // Products tab
        if (comp instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) comp;
            return (JTable) scrollPane.getViewport().getView();
        }
        return null;
    }

}