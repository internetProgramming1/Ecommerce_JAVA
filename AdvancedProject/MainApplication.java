package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import java.awt.*;

public class MainApplication extends JFrame {
    public static final String HOME_VIEW = "HOME";
    public static final String LOGIN_VIEW = "LOGIN";
    public static final String CREATE_USER_VIEW = "CREATE_USER";
    public static final String ADMIN_VIEW = "ADMIN";

    private CardLayout layout;
    private JPanel cards;
    public static final String ADMIN_DASHBOARD = "AdminDashboard";

    public MainApplication() {
        setTitle("E-commerce App");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        layout = new CardLayout();
        cards = new JPanel(layout);

        cards.add(new HomePanel(this), HOME_VIEW);
        cards.add(new LoginPanel(this), LOGIN_VIEW);
        cards.add(new CreateUserPanel(this), CREATE_USER_VIEW);
        // cards.add(new AdminLoginPanel(this), ADMIN_VIEW); // implement later

        add(cards);
        layout.show(cards, LOGIN_VIEW);
        setVisible(true);
    }

    public void showView(String name) {
        layout.show(cards, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApplication::new);
    }
}
