package com.recipeinventory.ui;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;

public class AdminDashboardForm extends DashboardForm {
    public AdminDashboardForm() {
        super();
        setTitle("Admin Dashboard - Recipe Inventory System");
        getContentPane().removeAll();
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Recipes", new RecipeExplorerForm());
        tabs.addTab("Inventory", new InventoryForm(true));
        tabs.addTab("Users", new UserManagementForm());
        tabs.addTab("Reports", new ReportsForm());
        tabs.addTab("Meal Planner", new MealPlannerForm());
        tabs.addTab("Shopping List", new ShoppingListForm());
        add(tabs, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
