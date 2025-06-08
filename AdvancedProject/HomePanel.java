package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.imageio.ImageIO;
import java.net.URL;

public class HomePanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(255, 140, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private MainApplication mainApp;

    public HomePanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(SECONDARY_COLOR);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // Create and add components (same as before)
        JPanel navBar = createNavBar();
        add(navBar, BorderLayout.NORTH);

        JPanel heroPanel = createHeroPanel();
        add(heroPanel, BorderLayout.CENTER);

        JPanel featuresPanel = createFeaturesPanel();
        add(featuresPanel, BorderLayout.SOUTH);
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
            ImageIcon logoIcon = new ImageIcon(new URL(""));
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

        // Home button - stays on current view but could refresh
        JButton homeButton = new JButton("Home");
        styleNavButton(homeButton);
        homeButton.addActionListener(e -> {
            mainApp.showView(MainApplication.HOME_VIEW);
        });
        navButtons.add(homeButton);

        // Products button - would show products view
        JButton productsButton = new JButton("Products");
        styleNavButton(productsButton);
        productsButton.addActionListener(e -> {
            mainApp.showView(MainApplication.PRODUCTS_VIEW);
            ;
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

        // User actions
        JButton loginButton = new JButton("Login");
        styleAccentButton(loginButton);
        loginButton.addActionListener(e -> {
            mainApp.showView(MainApplication.LOGIN_VIEW);
        });
        navButtons.add(loginButton);

        navPanel.add(navButtons, BorderLayout.EAST);

        return navPanel;
    }

    private JPanel createHeroPanel() {
        JPanel heroPanel = new JPanel(new BorderLayout());
        heroPanel.setBackground(SECONDARY_COLOR);
        heroPanel.setBorder(new EmptyBorder(10, 100, 10, 100));
        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setBackground(SECONDARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);

        JLabel noteLabel = new JLabel();
        noteLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridy = 0; // First row
        textPanel.add(noteLabel, gbc);

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome to BusyBuy!");
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(PRIMARY_COLOR);
        gbc.gridy = 1; // Second row
        gbc.insets = new Insets(0, 0, 20, 0); // More space below title
        textPanel.add(welcomeLabel, gbc);

        // Subtitle
        JLabel subtitle = new JLabel(
                "<html><div style='width: 400px;'>Your premier destination for quality products. "
                        + "Shop with confidence with our secure payment system and fast delivery options.</div>"
                        + "<div style='color:#FF8C00; font-size:16px;'>Your Number 1 Choice!</div></html>");
        subtitle.setFont(SUBTITLE_FONT);
        subtitle.setForeground(new Color(100, 100, 100));
        gbc.gridy = 2; // Third row
        gbc.insets = new Insets(0, 0, 20, 0); // Space below subtitle
        textPanel.add(subtitle, gbc);

        // Shop Now Button
        JButton shopNowButton = new JButton("Shop Now →");
        styleAccentButton(shopNowButton); // Apply styling
        gbc.gridy = 3; // Fourth row
        gbc.insets = new Insets(10, 0, 0, 0); // Space above button

        textPanel.add(shopNowButton, gbc);

        // Right side - Hero image
        JLabel imageLabel = null;
        try {
            java.net.URL imageUrl = getClass().getClassLoader().getResource("Images/bag.png");
            if (imageUrl != null) {
                ImageIcon heroImage = new ImageIcon(imageUrl);

                Image scaledImage = heroImage.getImage().getScaledInstance(500, -1, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(scaledImage));
            } else {
                System.err.println("Image not found: Images/bag.png");
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }

        // Add components to the heroPanel
        heroPanel.add(textPanel, BorderLayout.WEST);

        if (imageLabel != null) {
            heroPanel.add(imageLabel, BorderLayout.EAST);
        } else {
            // If image is not found, you might want to center the text panel or
            // add a placeholder. Here, I'm just centering it as a fallback.
            heroPanel.add(textPanel, BorderLayout.CENTER);
            System.out.println("Image not found, textPanel moved to CENTER.");
        }

        return heroPanel;
    }

    private JPanel createFeaturesPanel() {
        JPanel featuresPanel = new JPanel();
        featuresPanel.setBackground(Color.WHITE);
        featuresPanel.setBorder(new EmptyBorder(40, 100, 60, 100));
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));

        // Section title
        JLabel featuresTitle = new JLabel("Our Services");
        featuresTitle.setFont(TITLE_FONT);
        featuresTitle.setForeground(PRIMARY_COLOR);
        featuresTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        featuresPanel.add(featuresTitle);

        featuresPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Features grid
        JPanel featuresGrid = new JPanel(new GridLayout(1, 4, 30, 0));
        featuresGrid.setBackground(Color.WHITE);
        featuresGrid.setMaximumSize(new Dimension(1000, 200));

        String[] features = {
                "Secure Payments",
                "Fast Delivery",
                "24/7 Support",
                "Easy Returns"
        };

        String[] icons = {
                "🔒", "🚚", "📞", "🔄"
        };

        String[] descriptions = {
                "<html><center>Bank-level encryption keeps your transactions safe and secure at all times</center></html>",
                "<html><center>Get your orders delivered within 2-3 business days with our express shipping</center></html>",
                "<html><center>Our customer service team is always available to assist you with any questions</center></html>",
                "<html><center>30-day hassle-free return policy on all products, no questions asked</center></html>"
        };

        for (int i = 0; i < features.length; i++) {
            JPanel featureCard = new JPanel();
            featureCard.setBackground(Color.WHITE);
            featureCard.setBorder(new CompoundBorder(
                    new MatteBorder(1, 1, 1, 1, new Color(240, 240, 240)),
                    new EmptyBorder(20, 20, 20, 20)));
            featureCard.setLayout(new BoxLayout(featureCard, BoxLayout.Y_AXIS));

            JLabel iconLabel = new JLabel(icons[i], SwingConstants.CENTER);
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            featureCard.add(iconLabel);

            featureCard.add(Box.createRigidArea(new Dimension(0, 15)));

            JLabel titleLabel = new JLabel(features[i]);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            featureCard.add(titleLabel);

            featureCard.add(Box.createRigidArea(new Dimension(0, 10)));

            JLabel descLabel = new JLabel(descriptions[i]);
            descLabel.setFont(SUBTITLE_FONT);
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            featureCard.add(descLabel);

            featuresGrid.add(featureCard);
        }

        featuresPanel.add(featuresGrid);
        featuresPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Call to action
        JPanel ctaPanel = new JPanel();
        ctaPanel.setBackground(Color.WHITE);
        ctaPanel.setLayout(new BoxLayout(ctaPanel, BoxLayout.Y_AXIS));

        JLabel ctaTitle = new JLabel("Ready to start shopping?");
        ctaTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        ctaTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        ctaPanel.add(ctaTitle);

        ctaPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton registerButton = new JButton("Create Account");
        styleAccentButton(registerButton);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(200, 45));
        registerButton.addActionListener(e -> {
            mainApp.showView(MainApplication.CREATE_USER_VIEW);
        });
        ctaPanel.add(registerButton);

        featuresPanel.add(ctaPanel);

        return featuresPanel;
    }

    private void styleNavButton(JButton button) {
        button.setFont(BUTTON_FONT);
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

    private void styleAccentButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.BLACK);
        button.setBackground(ACCENT_COLOR);
        button.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 1),
                new EmptyBorder(12, 25, 12, 25)));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 165, 0)); // Lighter orange
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }

}