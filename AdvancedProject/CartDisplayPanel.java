package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.List;
import Ecommerce_JAVA.AdvancedProject.Cart.CartItem;

public class CartDisplayPanel extends JPanel {
    // Constants for styling
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(255, 140, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font PRODUCT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private MainApplication mainApp;
    private JPanel cartItemsPanel;
    private JLabel totalLabel;
    private JTextArea addressTextArea;
    private DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");

    public CartDisplayPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(SECONDARY_COLOR);

        // Navigation bar
        JPanel navBar = createNavBar();
        add(navBar, BorderLayout.NORTH);

        // Main content area
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Your Shopping Cart");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(SECONDARY_COLOR);

        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel checkoutPanel = createCheckoutPanel();
        contentPanel.add(checkoutPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        refreshCartItems();
    }

    private JPanel createNavBar() {
        return new ProductsPanel(mainApp).createNavBar();
    }

    private JPanel createCheckoutPanel() {
        System.out.println("CartDisplayPanel initialized. LoggedIn = " + UserSession.getInstance().isLoggedIn());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                new EmptyBorder(20, 0, 0, 0)));
        panel.setBackground(Color.WHITE);

        // Create address panel
        JPanel addressPanel = new JPanel(new BorderLayout());
        addressPanel.setBackground(Color.WHITE);
        addressPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel addressLabel = new JLabel("Delivery Address:");
        addressLabel.setFont(PRODUCT_FONT);
        addressPanel.add(addressLabel, BorderLayout.NORTH);

        addressTextArea = new JTextArea(3, 20);
        addressTextArea.setLineWrap(true);
        addressTextArea.setWrapStyleWord(true);
        addressTextArea.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 5, 5, 5)));
        JScrollPane addressScrollPane = new JScrollPane(addressTextArea);
        addressPanel.add(addressScrollPane, BorderLayout.CENTER);

        panel.add(addressPanel, BorderLayout.NORTH);

        // Create total and checkout panel
        JPanel totalCheckoutPanel = new JPanel(new BorderLayout());
        totalCheckoutPanel.setBackground(Color.WHITE);

        totalLabel = new JLabel("Total: $0.00", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalCheckoutPanel.add(totalLabel, BorderLayout.CENTER);

        JButton checkoutButton = new JButton("Proceed to Checkout");
        checkoutButton.setFont(PRODUCT_FONT);
        checkoutButton.setForeground(Color.BLACK);
        checkoutButton.setBackground(ACCENT_COLOR);
        checkoutButton.setBorder(new EmptyBorder(10, 30, 10, 30));
        checkoutButton.addActionListener(e -> proceedToCheckout());
        totalCheckoutPanel.add(checkoutButton, BorderLayout.EAST);

        panel.add(totalCheckoutPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshCartItems() {
        // cartItemsPanel.removeAll();

        // if (!UserSession.getInstance().isLoggedIn()) {
        // cartItemsPanel.add(new JLabel("Please login to view your cart",
        // JLabel.CENTER));
        // totalLabel.setText("Total: $0.00");
        // revalidateAndRepaint();
        // return;
        // }

        List<CartItem> cartItems = UserSession.getInstance().getCart().getItems();

        if (cartItems.isEmpty()) {
            cartItemsPanel.add(new JLabel("Your cart is empty", JLabel.CENTER));
            totalLabel.setText("Total: $0.00");
            revalidateAndRepaint();
            return;
        }

        double total = 0;

        for (CartItem item : cartItems) {
            cartItemsPanel.add(createCartItemPanel(item));
            cartItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        totalLabel.setText("Total: " + currencyFormat.format(total));
        revalidateAndRepaint();
    }

    private void revalidateAndRepaint() {
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private JPanel createCartItemPanel(CartItem item) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
                new EmptyBorder(10, 10, 10, 10)));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(item.getProduct().getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoPanel.add(nameLabel, BorderLayout.NORTH);

        JLabel priceLabel = new JLabel(
                currencyFormat.format(item.getProduct().getPrice()) + " x " +
                        item.getQuantity() + " = " +
                        currencyFormat.format(item.getProduct().getPrice() * item.getQuantity()));
        priceLabel.setFont(PRODUCT_FONT);
        infoPanel.add(priceLabel, BorderLayout.CENTER);

        panel.add(infoPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);

        JButton decreaseButton = new JButton("-");
        decreaseButton.addActionListener(e -> updateQuantity(item, -1));
        controlPanel.add(decreaseButton);

        JLabel quantityLabel = new JLabel(String.valueOf(item.getQuantity()));
        quantityLabel.setFont(PRODUCT_FONT);
        quantityLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        controlPanel.add(quantityLabel);

        JButton increaseButton = new JButton("+");
        increaseButton.addActionListener(e -> updateQuantity(item, 1));
        controlPanel.add(increaseButton);

        JButton removeButton = new JButton("Remove");
        removeButton.setForeground(Color.RED);
        removeButton.setBorder(new EmptyBorder(5, 10, 5, 10));
        removeButton.addActionListener(e -> removeItem(item));
        controlPanel.add(removeButton);

        panel.add(controlPanel, BorderLayout.EAST);

        return panel;
    }

    private void updateQuantity(CartItem item, int change) {
        int newQuantity = item.getQuantity() + change;

        if (newQuantity <= 0) {
            removeItem(item);
            return;
        }

        if (newQuantity > item.getProduct().getStockQuantity()) {
            JOptionPane.showMessageDialog(this,
                    "Cannot add more than available stock",
                    "Stock Limit", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserSession.getInstance().getCart().updateQuantity(item.getProduct(), newQuantity);
        refreshCartItems();
    }

    private void removeItem(Cart.CartItem item) {
        UserSession.getInstance().getCart().removeFromCart(item.getProduct());
        refreshCartItems();
    }

    private void proceedToCheckout() {
        if (!UserSession.getInstance().isLoggedIn()) {
            JOptionPane.showMessageDialog(this,
                    "Please login to proceed to checkout",
                    "Authentication Required", JOptionPane.INFORMATION_MESSAGE);
            mainApp.showView(MainApplication.LOGIN_VIEW);
            return;
        }

        if (UserSession.getInstance().getCart().getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Your cart is empty",
                    "No Items", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String deliveryAddress = addressTextArea.getText().trim();
        if (deliveryAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a delivery address",
                    "Address Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirm your order with the following address?\n\n" + deliveryAddress,
                "Checkout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = DatabaseHelper.saveOrder(
                    UserSession.getInstance().getUsername(),
                    UserSession.getInstance().getCart().getItems(),
                    calculateTotal(),
                    deliveryAddress); // Pass the address to the saveOrder method

            if (success) {
                JOptionPane.showMessageDialog(this, "Order placed successfully!");
                UserSession.getInstance().getCart().clear();
                mainApp.showView(MainApplication.HOME_VIEW);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to place order. Please try again.",
                        "Checkout Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private double calculateTotal() {
        return UserSession.getInstance().getCart().getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}