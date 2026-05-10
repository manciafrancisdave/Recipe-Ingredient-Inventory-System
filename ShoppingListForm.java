package com.recipeinventory.ui;

import com.recipeinventory.util.SessionManager;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

public class DashboardForm extends JFrame {
    public DashboardForm() {
        super("User Dashboard - Recipe Inventory System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setJMenuBar(menuBar());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Recipes", new RecipeExplorerForm());
        tabs.addTab("Meal Planner", new MealPlannerForm());
        tabs.addTab("Shopping List", new ShoppingListForm());
        tabs.addTab("Inventory", new InventoryForm(false));
        tabs.addTab("Favorites", new FavoritesForm());
        add(tabs, BorderLayout.CENTER);
    }

    protected JMenuBar menuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu account = new JMenu("Account");
        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(e -> {
            SessionManager.logout();
            dispose();
            new LoginForm().setVisible(true);
        });
        account.add(logout);
        bar.add(account);
        return bar;
    }
}
