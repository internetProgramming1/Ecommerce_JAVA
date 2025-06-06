package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CartPage extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(255, 140, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font PRODUCT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private MainApplication mainApp;
    private int currentUserId;
    private JPanel cartItemsPanel;
    private JLabel totalLabel;

    public CartPage(MainApplication mainApp, int userId) {
        this.mainApp = mainApp;
        this.currentUserId = userId;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(SECONDARY_COLOR);

        // Navigation bar
        JPanel navBar = createNavBar();
        add(navBar, BorderLayout.NORTH);

        // Main cart content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(SECONDARY_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        // Title
        JLabel titleLabel = new JLabel("Your Shopping Cart");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Cart items scroll pane
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(SECONDARY_COLOR);

        refreshCartItems();

        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(null);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Checkout panel
        JPanel checkoutPanel = createCheckoutPanel();
        contentPanel.add(checkoutPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(10, 30, 10, 30)));

        // Back button
        JButton backButton = new JButton("← Continue Shopping");
        styleNavButton(backButton);
        backButton.addActionListener(e -> mainApp.showView(MainApplication.PRODUCTS_VIEW));
        navPanel.add(backButton, BorderLayout.WEST);

        // Title
        JLabel title = new JLabel("BusyBuy Cart", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY_COLOR);
        navPanel.add(title, BorderLayout.CENTER);

        return navPanel;
    }

    public void refreshCartItems() {
        cartItemsPanel.removeAll();

        if (currentUserId == -1) {
            JLabel message = new JLabel("Please login to view your cart");
            message.setFont(PRODUCT_FONT);
            message.setAlignmentX(Component.CENTER_ALIGNMENT);
            cartItemsPanel.add(message);
            return;
        }

        List<CartItem> cartItems = DatabaseHelper.getCartItems(currentUserId);

        if (cartItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your cart is empty");
            emptyLabel.setFont(PRODUCT_FONT);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cartItemsPanel.add(emptyLabel);
        } else {
            for (CartItem item : cartItems) {
                cartItemsPanel.add(createCartItemPanel(item));
                cartItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();

        if (totalLabel != null) {
            updateTotal();
        }
    }

    private JPanel createCartItemPanel(CartItem item) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
                new EmptyBorder(10, 10, 10, 10)));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Product image
        JLabel imageLabel;
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(item.getImagePath()));
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
        } catch (Exception e) {
            imageLabel = new JLabel("No Image");
        }
        itemPanel.add(imageLabel);

        itemPanel.add(Box.createRigidArea(new Dimension(15, 0)));

        // Product info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(item.getProductName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoPanel.add(nameLabel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel priceLabel = new JLabel(String.format("$%.2f", item.getPrice()));
        priceLabel.setFont(PRODUCT_FONT);
        infoPanel.add(priceLabel);

        itemPanel.add(infoPanel);
        itemPanel.add(Box.createHorizontalGlue());

        // Quantity controls
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));
        quantityPanel.setBackground(Color.WHITE);

        JButton decreaseButton = new JButton("-");
        styleQuantityButton(decreaseButton);
        decreaseButton.addActionListener(e -> {
            DatabaseHelper.updateCartItemQuantity(item.getId(), item.getQuantity() - 1);
            refreshCartItems();
            mainApp.updateCartView();
        });
        quantityPanel.add(decreaseButton);

        JLabel quantityLabel = new JLabel(String.valueOf(item.getQuantity()));
        quantityLabel.setFont(PRODUCT_FONT);
        quantityLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        quantityPanel.add(quantityLabel);

        JButton increaseButton = new JButton("+");
        styleQuantityButton(increaseButton);
        increaseButton.addActionListener(e -> {
            DatabaseHelper.updateCartItemQuantity(item.getId(), item.getQuantity() + 1);
            refreshCartItems();
            mainApp.updateCartView();
        });
        quantityPanel.add(increaseButton);

        itemPanel.add(quantityPanel);
        itemPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        // Total price for this item
        JLabel itemTotalLabel = new JLabel(String.format("$%.2f", item.getTotalPrice()));
        itemTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        itemPanel.add(itemTotalLabel);

        itemPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        // Remove button
        JButton removeButton = new JButton("×");
        styleRemoveButton(removeButton);
        removeButton.addActionListener(e -> {
            DatabaseHelper.removeFromCart(item.getId());
            refreshCartItems();
            mainApp.updateCartView();
        });
        itemPanel.add(removeButton);

        return itemPanel;
    }

    private JPanel createCheckoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Total display
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(SECONDARY_COLOR);

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalPanel.add(totalLabel);

        panel.add(totalPanel, BorderLayout.CENTER);

        // Checkout button
        JButton checkoutButton = new JButton("Proceed to Checkout");
        styleAccentButton(checkoutButton);
        checkoutButton.addActionListener(e -> {
            if (currentUserId == -1) {
                JOptionPane.showMessageDialog(this, "Please login to proceed to checkout");
                mainApp.showView(MainApplication.LOGIN_VIEW);
                return;
            }
            JOptionPane.showMessageDialog(this, "Checkout functionality would be implemented here");
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(SECONDARY_COLOR);
        buttonPanel.add(checkoutButton);
        panel.add(buttonPanel, BorderLayout.EAST);

        updateTotal();
        return panel;
    }

    private void updateTotal() {
        if (currentUserId != -1) {
            double total = DatabaseHelper.getCartTotal(currentUserId);
            totalLabel.setText(String.format("Total: $%.2f", total));
        } else {
            totalLabel.setText("Total: $0.00");
        }
    }

    private void styleNavButton(JButton button) {
        button.setFont(PRODUCT_FONT);
        button.setForeground(PRIMARY_COLOR);
        button.setBackground(Color.WHITE);
        button.setBorder(new EmptyBorder(5, 5, 5, 5));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
    }

    private void styleQuantityButton(JButton button) {
        button.setFont(PRODUCT_FONT);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);

        button.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(2, 8, 2, 8)));

        button.setFocusPainted(false);
    }

    private void styleRemoveButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.RED);
        button.setBackground(Color.WHITE);
        button.setBorder(new EmptyBorder(2, 8, 2, 8));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
    }

    private void styleAccentButton(JButton button) {
        button.setFont(PRODUCT_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_COLOR);
        button.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 1),
                new EmptyBorder(10, 25, 10, 25)));
        button.setFocusPainted(false);
    }
}