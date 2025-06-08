package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ContactPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 153);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 16);

    private MainApplication mainApp;

    public ContactPanel(MainApplication mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // soft background

        // Navigation bar (same style as LoginPanel)
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(10, 30, 10, 30)));

        JButton backButton = new JButton("← Back to Home");
        styleNavButton(backButton);
        backButton.addActionListener(e -> mainApp.showView(MainApplication.HOME_VIEW));
        navBar.add(backButton, BorderLayout.WEST);

        add(navBar, BorderLayout.NORTH);

        // Center content box
        JPanel contentBox = new JPanel();
        contentBox.setBackground(Color.WHITE);
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(20, 30, 20, 30)));

        JLabel title = new JLabel("📞 Contact Us");
        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY_COLOR);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel email = new JLabel("Email: contact@busybuy.com");
        JLabel phone = new JLabel("Phone: +251 912 345 678");
        JLabel address = new JLabel("Address: AASTU, Addis Ababa");

        for (JLabel label : new JLabel[] { email, phone, address }) {
            label.setFont(TEXT_FONT);
            label.setBorder(new EmptyBorder(10, 0, 0, 0));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        contentBox.add(title);
        contentBox.add(email);
        contentBox.add(phone);
        contentBox.add(address);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(245, 245, 245));
        wrapper.add(contentBox);

        add(wrapper, BorderLayout.CENTER);
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
