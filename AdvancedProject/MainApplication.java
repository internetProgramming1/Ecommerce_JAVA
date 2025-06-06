package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;

import java.awt.*;

public class MainApplication extends JFrame {
    private static MainApplication instance;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // View constants
    public static final String HOME_VIEW = "HOME";
    public static final String LOGIN_VIEW = "LOGIN";
    public static final String ADMIN_VIEW = "ADMIN";
    public static final String CREATE_ADMIN_VIEW = "CREATE_ADMIN";
    public static final String CREATE_USER_VIEW = "CREATE_USER";

    private MainApplication() {
        super("BusyBuy - Premium E-Commerce Platform");
        initializeUI();
    }

    public static MainApplication getInstance() {
        if (instance == null) {
            instance = new MainApplication();
        }
        return instance;
    }

    private void initializeUI() {
        // Setup card layout for all views
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create and add all views
        cardPanel.add(new HomePanel(this), HOME_VIEW);
        cardPanel.add(new LoginPanel(this), LOGIN_VIEW);
        cardPanel.add(new AdminPanel(this), ADMIN_VIEW);
        cardPanel.add(new CreateAdminPanel(this), CREATE_ADMIN_VIEW);
        cardPanel.add(new CreateUserPanel(this), CREATE_USER_VIEW);

        setContentPane(cardPanel);
        setupFrame();
    }

    public void showView(String viewName) {
        cardLayout.show(cardPanel, viewName);
    }

    private void setupFrame() {
        setSize(1600, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                MainApplication.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}