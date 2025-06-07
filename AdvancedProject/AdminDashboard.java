package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import javax.swing.table.TableCellRenderer;

public class AdminDashboard extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(255, 140, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private MainApplication mainApp;
    private JTabbedPane tabbedPane;

    public AdminDashboard(MainApplication mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(SECONDARY_COLOR);

        // Create navigation bar
        JPanel navBar = createNavBar();
        add(navBar, BorderLayout.NORTH);

        // Create tabbed content area
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add tabs
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

        // Logo and title
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("BusyBuy Admin");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY_COLOR);
        logoPanel.add(title);
        navPanel.add(logoPanel, BorderLayout.WEST);

        // Navigation buttons
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        navButtons.setBackground(Color.WHITE);

        // Logout button
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

        // Summary cards
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        summaryPanel.setBackground(SECONDARY_COLOR);

        summaryPanel.add(createSummaryCard("Total Products", "1,240", PRIMARY_COLOR));
        summaryPanel.add(createSummaryCard("Low Stock", "42", new Color(220, 20, 60)));
        summaryPanel.add(createSummaryCard("Active Users", "856", new Color(34, 139, 34)));
        summaryPanel.add(createSummaryCard("Pending Orders", "27", ACCENT_COLOR));

        dashboardPanel.add(summaryPanel, BorderLayout.NORTH);

        // Charts and recent activity
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        contentPanel.setBackground(SECONDARY_COLOR);

        // Sales chart panel
        JPanel salesPanel = new JPanel(new BorderLayout());
        salesPanel.setBackground(Color.WHITE);
        salesPanel.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel salesTitle = new JLabel("Sales Overview");
        salesTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        salesTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        salesPanel.add(salesTitle, BorderLayout.NORTH);

        // Placeholder for chart
        JLabel chartPlaceholder = new JLabel("Sales chart would be displayed here", SwingConstants.CENTER);
        chartPlaceholder.setForeground(new Color(150, 150, 150));
        salesPanel.add(chartPlaceholder, BorderLayout.CENTER);

        // Recent activity panel
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel activityTitle = new JLabel("Recent Activity");
        activityTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        activityTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        activityPanel.add(activityTitle, BorderLayout.NORTH);

        // Recent activity list
        String[] activities = {
                "New order #10045 received",
                "User 'john_doe' registered",
                "Product 'Wireless Headphones' stock updated",
                "Order #10042 shipped",
                "Product 'Smart Watch' added to catalog"
        };

        JList<String> activityList = new JList<>(activities);
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

        // Panel title
        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        productsPanel.add(titleLabel, BorderLayout.NORTH);

        // Toolbar
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

        // Product table
        String[] columns = { "ID", "Name", "Category", "Price", "Stock", "Status" };
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

        // Sample data - in real app, load from database
        model.addRow(new Object[] { 1001, "Wireless Headphones", "Electronics", 99.99, 45, "In Stock" });
        model.addRow(new Object[] { 1002, "Smart Watch", "Electronics", 199.99, 3, "Low Stock" });
        model.addRow(new Object[] { 1003, "Running Shoes", "Sports", 79.99, 0, "Out of Stock" });
        model.addRow(new Object[] { 1004, "Coffee Maker", "Home", 49.99, 12, "In Stock" });

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        productsPanel.add(scrollPane, BorderLayout.CENTER);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(SECONDARY_COLOR);
        searchPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JTextField searchField = new JTextField(25);
        searchField.setFont(SUBTITLE_FONT);

        JButton searchButton = new JButton("Search");
        styleAccentButton(searchButton);
        searchButton.addActionListener(e -> searchProducts(searchField.getText()));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        productsPanel.add(searchPanel, BorderLayout.SOUTH);

        return productsPanel;
    }

    private JPanel createUsersPanel() {
        JPanel usersPanel = new JPanel(new BorderLayout(10, 10));
        usersPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        usersPanel.setBackground(SECONDARY_COLOR);

        // Panel title
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        usersPanel.add(titleLabel, BorderLayout.NORTH);

        // User table
        String[] columns = { "ID", "Username", "Email", "Role", "Joined", "Status" };
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

        // Sample data - in real app, load from database
        model.addRow(new Object[] { 2001, "admin", "admin@busybuy.com", "Administrator", "2023-01-15", "Active" });
        model.addRow(new Object[] { 2002, "john_doe", "john@example.com", "Customer", "2023-05-22", "Active" });
        model.addRow(new Object[] { 2003, "jane_smith", "jane@example.com", "Customer", "2023-06-10", "Inactive" });

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        usersPanel.add(scrollPane, BorderLayout.CENTER);

        return usersPanel;
    }

    private JPanel createOrdersPanel() {
        JPanel ordersPanel = new JPanel(new BorderLayout(10, 10));
        ordersPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        ordersPanel.setBackground(SECONDARY_COLOR);

        // Panel title
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        ordersPanel.add(titleLabel, BorderLayout.NORTH);

        // Order table
        String[] columns = { "Order ID", "Customer", "Date", "Amount", "Status", "Actions" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only actions column is editable
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

        // Sample data - in real app, load from database
        model.addRow(new Object[] { 10045, "John Doe", "2023-06-15", 149.98, "Processing", createViewButton() });
        model.addRow(new Object[] { 10044, "Jane Smith", "2023-06-14", 79.99, "Shipped", createViewButton() });
        model.addRow(new Object[] { 10043, "Robert Johnson", "2023-06-12", 199.99, "Delivered", createViewButton() });

        // Add button renderer and editor for actions column
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

    // Button renderer for the orders table
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

    // Button editor for the orders table
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
                // Handle button click action here
                JOptionPane.showMessageDialog(button, "Order details would be shown here");
            }
            isPushed = false;
            return label;
        }
    }

    // Methods for product management (would connect to database in real app)
    private void showAddProductDialog() {
        JOptionPane.showMessageDialog(this, "Add product dialog would appear here");
    }

    private void editSelectedProduct() {
        JOptionPane.showMessageDialog(this, "Edit product dialog would appear here");
    }

    private void deleteSelectedProduct() {
        JOptionPane.showMessageDialog(this, "Delete product confirmation would appear here");
    }

    private void loadProductData() {
        JOptionPane.showMessageDialog(this, "Product data would be refreshed from database");
    }

    private void searchProducts(String query) {
        JOptionPane.showMessageDialog(this, "Would search for: " + query);
    }
}