package Ecommerce_JAVA.AdvancedProject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.List;

class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());

        // Summary cards at top
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        summaryPanel.add(createSummaryCard("Total Products", "120", Color.BLUE));
        summaryPanel.add(createSummaryCard("Low Stock", "15", Color.ORANGE));
        summaryPanel.add(createSummaryCard("Registered Users", "85", Color.GREEN));
        summaryPanel.add(createSummaryCard("Pending Orders", "7", Color.RED));

        add(summaryPanel, BorderLayout.NORTH);

        // Charts panel
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2));
        chartsPanel.add(createStockChart());
        chartsPanel.add(createUserActivityChart());

        add(chartsPanel, BorderLayout.CENTER);
    }

    private JPanel createSummaryCard(String title, String value, Color color) {
        // Implementation for summary cards
    }

    private JPanel createStockChart() {
        // Implementation for stock chart
    }

    private JPanel createUserActivityChart() {
        // Implementation for user activity chart
    }
}