package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import Ecommerce_JAVA.AdvancedProject.Session;
import Ecommerce_JAVA.AdvancedProject.User;
import Ecommerce_JAVA.AdvancedProject.UserService;

public class LoginPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(255, 140, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 35);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 20);

    private static final Font BUTTON2_FONT = new Font("Segoe UI", Font.PLAIN, 15);
    private MainApplication mainApp;

    public LoginPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(SECONDARY_COLOR);

        // Navigation Bar
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(10, 30, 10, 30)));

        JButton backButton = new JButton("← Back to Home");
        styleNavButton(backButton);
        backButton.addActionListener(e -> mainApp.showView(MainApplication.HOME_VIEW));
        navBar.add(backButton, BorderLayout.WEST);

        JLabel logoLabel = new JLabel("BusyBuy");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logoLabel.setForeground(PRIMARY_COLOR);

        // Create a panel for right-aligned items
        JPanel rightNavPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightNavPanel.setBackground(Color.WHITE);
        rightNavPanel.add(logoLabel);

        // Add admin button to the right nav panel
        JButton adminButton = new JButton("Admin Login");
        styleNavButton(adminButton);
        adminButton.addActionListener(e -> mainApp.showView(MainApplication.ADMIN_VIEW));
        rightNavPanel.add(adminButton);

        navBar.add(rightNavPanel, BorderLayout.CENTER);

        add(navBar, BorderLayout.NORTH);

        // Main content (rest of the form remains the same, just remove the admin button
        // part)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(SECONDARY_COLOR);
        formPanel.setBorder(new EmptyBorder(40, 80, 40, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title
        JLabel titleLabel = new JLabel("User Login");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(titleLabel, gbc);

        // Username
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 10);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(BUTTON_FONT);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx++;
        JTextField usernameField = new JTextField(20);
        styleTextField(usernameField);
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(BUTTON_FONT);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx++;
        JPasswordField passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        formPanel.add(passwordField, gbc);

        // Show password checkbox
        gbc.gridy++;
        gbc.gridx = 1;
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(SECONDARY_COLOR);
        showPassword.setFont(SUBTITLE_FONT);
        showPassword.addActionListener(e -> passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '●'));
        formPanel.add(showPassword, gbc);

        // Error label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(SUBTITLE_FONT);
        errorLabel.setForeground(Color.RED);
        formPanel.add(errorLabel, gbc);

        // User register button
        gbc.gridy++;
        JButton registerUserButton = new JButton("Don't have an account? Create New Account");
        OtherButton(registerUserButton);
        registerUserButton.addActionListener(e -> mainApp.showView(MainApplication.CREATE_USER_VIEW));
        formPanel.add(registerUserButton, gbc);

        // Login button
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 10, 0);
        JButton loginButton = new JButton("Login");
        styleAccentButton(loginButton);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter both username and password.");
            } else {
                boolean loggedIn = UserService.login(username, password);
                if (loggedIn) {
                    User user = UserService.getUserByUsername(username);
                    Session.setCurrentUser(user);
                    errorLabel.setText(" ");
                    mainApp.showView(MainApplication.HOME_VIEW);
                } else {
                    errorLabel.setText("Invalid username or password.");
                }
            }
        });

        formPanel.add(loginButton, gbc);

        // Centering wrapper
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(SECONDARY_COLOR);
        wrapper.add(formPanel);

        add(wrapper, BorderLayout.CENTER);
    }

    private void styleTextField(JTextField field) {
        field.setFont(BUTTON_FONT);
        field.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                new EmptyBorder(5, 5, 5, 5)));
        field.setBackground(SECONDARY_COLOR);
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

    private void OtherButton(JButton button) {
        button.setFont(BUTTON2_FONT);
        button.setForeground(PRIMARY_COLOR);
        button.setBackground(Color.WHITE);
        button.setBorder(new EmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(80, 80, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(PRIMARY_COLOR);
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
                button.setBackground(new Color(255, 165, 0));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }
}
