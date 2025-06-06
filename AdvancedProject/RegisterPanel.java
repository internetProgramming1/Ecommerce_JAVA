package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 20);
    private MainApplication mainApp;

    public RegisterPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(SECONDARY_COLOR);

        // Navigation bar (Back to Login)
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(new EmptyBorder(10, 30, 10, 30));

        JButton backButton = new JButton("← Back to Login");
        backButton.setFont(BUTTON_FONT);
        backButton.setForeground(PRIMARY_COLOR);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(new EmptyBorder(5, 10, 5, 10));
        backButton.addActionListener(e -> mainApp.showView(MainApplication.LOGIN_VIEW));

        navBar.add(backButton);
        add(navBar, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(SECONDARY_COLOR);
        formPanel.setBorder(new EmptyBorder(40, 80, 40, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 35));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(titleLabel, gbc);

        // Username label and field
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 10);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(LABEL_FONT);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(LABEL_FONT);
        formPanel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(LABEL_FONT);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(LABEL_FONT);
        formPanel.add(passwordField, gbc);

        // Error label
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(errorLabel, gbc);

        // Register button
        gbc.gridy++;
        JButton registerButton = new JButton("Register");
        registerButton.setFont(BUTTON_FONT);
        registerButton.setBackground(PRIMARY_COLOR);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(new EmptyBorder(12, 25, 12, 25));
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formPanel.add(registerButton, gbc);

        // Register button logic
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill all fields.");
            } else {
                boolean success = UserService.registerUser(username, password);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Registration successful! Please log in.");
                    mainApp.showView(MainApplication.LOGIN_VIEW);
                } else {
                    errorLabel.setText("Username already exists. Try another.");
                }
            }
        });

        // Center wrapper
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(SECONDARY_COLOR);
        wrapper.add(formPanel);

        add(wrapper, BorderLayout.CENTER);
    }
}
