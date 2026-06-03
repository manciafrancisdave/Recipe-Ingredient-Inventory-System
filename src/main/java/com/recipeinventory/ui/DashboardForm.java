package com.recipeinventory.ui;

import com.recipeinventory.util.SessionManager;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class DashboardForm extends JFrame {
    public DashboardForm() {
        this(false);
    }

    protected DashboardForm(boolean admin) {
        super((admin ? "Admin" : "User") + " Dashboard - Recipe Inventory System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new java.awt.Dimension(980, 650));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Ui.BACKGROUND);
        getContentPane().setLayout(new BorderLayout());

        String user = SessionManager.getCurrentUser() == null
                ? "Local workspace"
                : SessionManager.getCurrentUser().getEmail();
        add(Ui.appHeader("Recipe Inventory", (admin ? "Admin workspace" : "Kitchen workspace") + " | " + user, this::logout), BorderLayout.NORTH);
        add(createTabs(admin), BorderLayout.CENTER);
    }

    private JTabbedPane createTabs(boolean admin) {
        JTabbedPane tabs = Ui.tabs();
        tabs.addTab("Recipes", new RecipeExplorerForm());
        tabs.addTab("Inventory", new InventoryForm(admin));
        if (admin) {
            tabs.addTab("Users", new UserManagementForm());
            tabs.addTab("Reports", new ReportsForm());
        } else {
            tabs.addTab("Favorites", new FavoritesForm());
        }
        tabs.addTab("Meal Planner", new MealPlannerForm());
        tabs.addTab("Shopping List", new ShoppingListForm());
        return tabs;
    }

    private void logout() {
        SessionManager.logout();
        dispose();
        new LoginForm().setVisible(true);
    }
}
