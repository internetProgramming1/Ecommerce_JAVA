package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class CreateUserPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(255, 140, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 35);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 20);

    private MainApplication mainApp;

    public CreateUserPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(SECONDARY_COLOR);

        // Navigation bar
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(10, 30, 10, 30)));

        JButton backButton = new JButton("← Back to Login");
        styleNavButton(backButton);
        backButton.addActionListener(e -> mainApp.showView(MainApplication.LOGIN_VIEW));
        navBar.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("BusyBuy");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(PRIMARY_COLOR);
        navBar.add(titleLabel, BorderLayout.EAST);

        add(navBar, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(SECONDARY_COLOR);
        formPanel.setBorder(new EmptyBorder(40, 80, 40, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel userTitle = new JLabel("Create New Account");
        userTitle.setFont(TITLE_FONT);
        userTitle.setForeground(PRIMARY_COLOR);
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(userTitle, gbc);

        // Full Name
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 10);
        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setFont(BUTTON_FONT);
        formPanel.add(fullNameLabel, gbc);

        gbc.gridx++;
        JTextField fullNameField = new JTextField(20);
        styleTextField(fullNameField);
        formPanel.add(fullNameField, gbc);

        // Email
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(BUTTON_FONT);
        formPanel.add(emailLabel, gbc);

        gbc.gridx++;
        JTextField emailField = new JTextField(20);
        styleTextField(emailField);
        formPanel.add(emailField, gbc);

        // Phone Number
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(BUTTON_FONT);
        formPanel.add(phoneLabel, gbc);

        gbc.gridx++;
        JTextField phoneField = new JTextField(20);
        styleTextField(phoneField);
        formPanel.add(phoneField, gbc);
        // Username Number
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel userLabel = new JLabel("UserName:");
        userLabel.setFont(BUTTON_FONT);
        formPanel.add(userLabel, gbc);

        gbc.gridx++;
        JTextField userField = new JTextField(20);
        styleTextField(userField);
        formPanel.add(userField, gbc);
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

        // Confirm Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(BUTTON_FONT);
        formPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx++;
        JPasswordField confirmPasswordField = new JPasswordField(20);
        styleTextField(confirmPasswordField);
        formPanel.add(confirmPasswordField, gbc);

        // Show password checkbox
        gbc.gridy++;
        gbc.gridx = 1;
        JCheckBox showPassword = new JCheckBox("Show Passwords");
        showPassword.setBackground(SECONDARY_COLOR);
        showPassword.setFont(SUBTITLE_FONT);
        showPassword.addActionListener(e -> {
            char echoChar = showPassword.isSelected() ? (char) 0 : '●';
            passwordField.setEchoChar(echoChar);
            confirmPasswordField.setEchoChar(echoChar);
        });
        formPanel.add(showPassword, gbc);

        // Error message
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(SUBTITLE_FONT);
        errorLabel.setForeground(Color.RED);
        formPanel.add(errorLabel, gbc);

        // Register Button
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 10, 0);
        JButton registerButton = new JButton("Create Account");
        styleAccentButton(registerButton);
        registerButton.addActionListener(e -> {
            UserDAO userDAO = new UserDAO();

            String username = userField.getText();
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            // Password length check (min 6 chars)
            if (password.length() < 6) {
                errorLabel.setText("Password must be at least 6 characters.");
                return;
            }
            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                    || confirmPassword.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
            } else if (!password.equals(confirmPassword)) {
                errorLabel.setText("Passwords do not match.");

            } else if (userDAO.isUserExists(username, email)) {
                errorLabel.setText("Username or Email already exists. Please choose another.");

            } else {
                User newUser = new User();
                newUser.setFullName(fullName);
                newUser.setEmail(email);
                newUser.setPhone(phone);
                newUser.setUsername(username);
                newUser.setPassword(UserDAO.hashPassword(password));

                userDAO.saveUser(newUser);

                errorLabel.setText(" ");
                fullNameField.setText(" ");
                emailField.setText(" ");
                phoneField.setText(" ");
                userField.setText(" ");
                passwordField.setText(" ");
                confirmPasswordField.setText(" ");

                JOptionPane.showMessageDialog(this, "User account created successfully!");
                mainApp.showView(MainApplication.PRODUCTS_VIEW);

            }
        });
        formPanel.add(registerButton, gbc);

        // Wrapper for centering
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
            public void mouseEntered(MouseEvent e) {
                button.setForeground(PRIMARY_COLOR);
            }

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
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 165, 0));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }

}
