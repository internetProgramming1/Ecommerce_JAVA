package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AboutPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 35);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 18);

    private MainApplication mainApp;

    public AboutPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(SECONDARY_COLOR);

        // Top Navigation Bar
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

        JPanel rightNavPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightNavPanel.setBackground(Color.WHITE);
        rightNavPanel.add(logoLabel);

        navBar.add(rightNavPanel, BorderLayout.CENTER);

        add(navBar, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentWrapper = new JPanel(new GridBagLayout());
        contentWrapper.setBackground(SECONDARY_COLOR);

        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setBackground(Color.WHITE);
        contentBox.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 30, 30, 30)));
        contentBox.setPreferredSize(new Dimension(800, 500)); // Wider box

        // Title
        JLabel titleLabel = new JLabel("About BusyBuy");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentBox.add(titleLabel);

        contentBox.add(Box.createRigidArea(new Dimension(0, 20)));

        // Description
        JTextArea aboutText = new JTextArea(
                "BusyBuy is a Java-based e-commerce desktop application developed for educational and practical purposes.\n\n"
                        + "Features:\n"
                        + "- Clean and responsive user interface using Swing\n"
                        + "- User login and registration\n"
                        + "- Admin dashboard for managing products\n"
                        + "- Product browsing and shopping functionality\n\n"
                        + "Purpose:\n"
                        + "BusyBuy was created as a semester project to demonstrate object-oriented programming, "
                        + "Java GUI development\n\n"
                        + "Technologies Used:\n"
                        + "- Java (Swing, AWT)\n"
                        + "- MySQL (JDBC)\n"
                        + "This app aims to simulate real-world e-commerce systems while offering maintainable and scalable code structure.");
        aboutText.setFont(BODY_FONT);
        aboutText.setEditable(false);
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        aboutText.setOpaque(false);
        aboutText.setBorder(null);
        contentBox.add(aboutText);

        contentWrapper.add(contentBox);
        add(contentWrapper, BorderLayout.CENTER);
    }

    private void styleNavButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setForeground(new Color(80, 80, 80));
        button.setBackground(Color.WHITE);
        button.setBorder(new EmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(PRIMARY_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(80, 80, 80));
            }
        });
    }
}
