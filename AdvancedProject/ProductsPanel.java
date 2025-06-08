package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.List;

public class ProductsPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(255, 140, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font PRODUCT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private MainApplication mainApp;
    private List<Product> products;
    private JButton cartButton; // Make cartButton an instance variable so we can update it

    public ProductsPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(SECONDARY_COLOR);

        // Load products from database
        products = DatabaseHelper.getAllProducts();

        // Create and add navigation bar (similar to HomePanel)
        JPanel navBar = createNavBar();
        add(navBar, BorderLayout.NORTH);

        // Main products area
        JPanel productsPanel = createProductsPanel();
        add(new JScrollPane(productsPanel), BorderLayout.CENTER);
    }

    public JPanel createNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(10, 30, 10, 30)));

        // Logo and title
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(Color.WHITE);

        try {
            ImageIcon logoIcon = new ImageIcon(new URL("https://via.placeholder.com/40x40"));
            JLabel logo = new JLabel(logoIcon);
            logoPanel.add(logo);
        } catch (Exception e) {
            System.out.println("Logo not loaded");
        }

        JLabel title = new JLabel("BusyBuy");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY_COLOR);
        logoPanel.add(title);
        navPanel.add(logoPanel, BorderLayout.WEST);

        // Navigation buttons
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        navButtons.setBackground(Color.WHITE);

        // Home button
        JButton homeButton = new JButton("Home");
        styleNavButton(homeButton);
        homeButton.addActionListener(e -> {
            mainApp.showView(MainApplication.HOME_VIEW);
        });
        navButtons.add(homeButton);

        JButton productsButton = new JButton("Products");
        styleNavButton(productsButton);
        productsButton.addActionListener(e -> {
            mainApp.showView(MainApplication.PRODUCTS_VIEW);
        });
        navButtons.add(productsButton);

        // About button
        JButton aboutButton = new JButton("About");
        styleNavButton(aboutButton);
        aboutButton.addActionListener(e -> {
            mainApp.showView(MainApplication.ABOUT_VIEW);
        });
        navButtons.add(aboutButton);

        // Contact button
        JButton contactButton = new JButton("Contact");
        styleNavButton(contactButton);
        contactButton.addActionListener(e -> {
            mainApp.showView(MainApplication.CONTACT_VIEW);
        });
        navButtons.add(contactButton);

        // Cart button with item count
        cartButton = new JButton();
        updateCartButtonCount(); // Initialize the cart button with current count
        styleCartButton(cartButton);
        cartButton.addActionListener(e -> {
            if (UserSession.getInstance().isLoggedIn()) {
                mainApp.showView(MainApplication.CART_VIEW);
                System.out.println("the user logged in...");
            } else {
                JOptionPane.showMessageDialog(this, "Please login to view your cart");
                mainApp.showView(MainApplication.LOGIN_VIEW);
            }
        });
        navButtons.add(cartButton);

        // User actions
        JButton logoutButton = new JButton("Logout");
        styleAccentButton(logoutButton);
        logoutButton.addActionListener(e -> {
            UserSession.getInstance().logout();
            JOptionPane.showMessageDialog(this, "Logged out successfully.");
            mainApp.showView(MainApplication.HOME_VIEW);
        });
        navButtons.add(logoutButton);

        navPanel.add(navButtons, BorderLayout.EAST);

        return navPanel;
    }

    private void updateCartButtonCount() {
        int itemCount = 0;
        if (UserSession.getInstance().isLoggedIn()) {
            itemCount = UserSession.getInstance().getCart().getTotalItems();
        }
        cartButton.setText(String.format("Cart (%d)", itemCount));
    }

    private void styleCartButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(PRIMARY_COLOR);
        button.setBackground(Color.WHITE);
        button.setBorder(new CompoundBorder(
                new LineBorder(PRIMARY_COLOR, 1),
                new EmptyBorder(5, 15, 5, 15)));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(230, 245, 255));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
    }

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3, 20, 20)); // 3 columns, auto rows
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(SECONDARY_COLOR);

        if (products.isEmpty()) {
            panel.add(new JLabel("No products available", SwingConstants.CENTER));
            return panel;
        }

        for (Product product : products) {
            panel.add(createProductCard(product));
        }

        return panel;
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)));

        // Product Image
        JLabel imageLabel;
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(product.getImagePath()));
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
        } catch (Exception e) {
            imageLabel = new JLabel("Image not available");
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(imageLabel);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Product Name
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(TITLE_FONT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);

        // Product Description
        JTextArea descArea = new JTextArea(product.getDescription());
        descArea.setFont(PRODUCT_FONT);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(Color.WHITE);
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(descArea);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Product Price
        JLabel priceLabel = new JLabel(String.format("$%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(priceLabel);

        // Stock Status
        JLabel stockLabel = new JLabel(product.getStockQuantity() > 0 ? "In Stock" : "Out of Stock");
        stockLabel.setFont(PRODUCT_FONT);
        stockLabel.setForeground(product.getStockQuantity() > 0 ? Color.GREEN.darker() : Color.RED);
        stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(stockLabel);

        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Quantity Selector
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        quantityPanel.setBackground(Color.WHITE);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, product.getStockQuantity(), 1));

        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) quantitySpinner.getEditor();
        editor.getTextField().setColumns(3);

        quantityPanel.add(quantitySpinner);
        card.add(quantityPanel);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton addToCartButton = new JButton("Add to Cart");
        styleAccentButton(addToCartButton);
        addToCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addToCartButton.setEnabled(product.getStockQuantity() > 0);

        addToCartButton.addActionListener(e -> {
            if (!UserSession.getInstance().isLoggedIn()) {
                JOptionPane.showMessageDialog(this, "Please login to add items to cart");
                mainApp.showView(MainApplication.LOGIN_VIEW);
                return;
            } else {
                System.out.println("User logged in ");
            }

            int quantity = (int) quantitySpinner.getValue();
            UserSession.getInstance().getCart().addToCart(product, quantity);
            JOptionPane.showMessageDialog(card, quantity + " " + product.getName() + "(s) added to cart.");
            updateCartButtonCount(); // Update the cart count after adding items
        });

        card.add(addToCartButton);
        return card;
    }

    private void styleAccentButton(JButton button) {
        button.setFont(PRODUCT_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_COLOR);
        button.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 1),
                new EmptyBorder(8, 20, 8, 20)));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 165, 0));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }

    private void styleNavButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(new Color(80, 80, 80));
        button.setBackground(Color.WHITE);
        button.setBorder(new EmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(PRIMARY_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(80, 80, 80));
            }
        });
    }
}