package com.recipeinventory.ui.builder;

import com.recipeinventory.ui.InventoryForm;
import com.recipeinventory.ui.MealPlannerForm;
import com.recipeinventory.ui.RecipeExplorerForm;
import com.recipeinventory.ui.ReportsForm;
import com.recipeinventory.ui.ShoppingListForm;
import com.recipeinventory.ui.UserManagementForm;
import com.recipeinventory.util.SessionManager;

public class MainBuilderDashboard extends javax.swing.JFrame {
    private final boolean admin;

    public MainBuilderDashboard(boolean admin) {
        this.admin = admin;
        initComponents();
        setLocationRelativeTo(null);
        loadRuntimePanels();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rootPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        subtitleLabel = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        contentTabs = new javax.swing.JTabbedPane();
        recipeDesignPanel = new javax.swing.JPanel();
        recipeToolbarPanel = new javax.swing.JPanel();
        recipeSearchField = new javax.swing.JTextField();
        recipeAddButton = new javax.swing.JButton();
        recipeEditButton = new javax.swing.JButton();
        recipeDeleteButton = new javax.swing.JButton();
        recipeScrollPane = new javax.swing.JScrollPane();
        recipeTable = new javax.swing.JTable();
        inventoryDesignPanel = new javax.swing.JPanel();
        inventoryToolbarPanel = new javax.swing.JPanel();
        inventorySearchField = new javax.swing.JTextField();
        restockButton = new javax.swing.JButton();
        deductButton = new javax.swing.JButton();
        inventoryScrollPane = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        mealDesignPanel = new javax.swing.JPanel();
        mealToolbarPanel = new javax.swing.JPanel();
        createPlanButton = new javax.swing.JButton();
        addMealButton = new javax.swing.JButton();
        mealScrollPane = new javax.swing.JScrollPane();
        mealTable = new javax.swing.JTable();
        shoppingDesignPanel = new javax.swing.JPanel();
        shoppingToolbarPanel = new javax.swing.JPanel();
        shoppingSearchField = new javax.swing.JTextField();
        completeListButton = new javax.swing.JButton();
        shoppingScrollPane = new javax.swing.JScrollPane();
        shoppingTable = new javax.swing.JTable();
        adminDesignPanel = new javax.swing.JPanel();
        adminToolbarPanel = new javax.swing.JPanel();
        userSearchField = new javax.swing.JTextField();
        deleteUserButton = new javax.swing.JButton();
        exportReportButton = new javax.swing.JButton();
        adminScrollPane = new javax.swing.JScrollPane();
        adminTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Recipe Inventory Dashboard");

        rootPanel.setBackground(new java.awt.Color(244, 247, 246));
        rootPanel.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(31, 46, 43));

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 20));
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setText("Recipe & Ingredient Inventory Management System");

        subtitleLabel.setForeground(new java.awt.Color(221, 232, 228));
        subtitleLabel.setText("Dashboard");

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel)
                    .addComponent(subtitleLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 418, Short.MAX_VALUE)
                .addComponent(logoutButton)
                .addGap(20, 20, 20))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(logoutButton)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subtitleLabel)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        rootPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        recipeDesignPanel.setBackground(new java.awt.Color(244, 247, 246));
        recipeDesignPanel.setLayout(new java.awt.BorderLayout(8, 8));

        recipeToolbarPanel.setBackground(new java.awt.Color(255, 255, 255));
        recipeSearchField.setText("Search recipes");
        recipeAddButton.setText("Add");
        recipeEditButton.setText("Edit");
        recipeDeleteButton.setText("Delete");
        recipeToolbarPanel.add(recipeSearchField);
        recipeToolbarPanel.add(recipeAddButton);
        recipeToolbarPanel.add(recipeEditButton);
        recipeToolbarPanel.add(recipeDeleteButton);
        recipeDesignPanel.add(recipeToolbarPanel, java.awt.BorderLayout.NORTH);

        recipeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Title", "Cuisine", "Difficulty", "Rating"}
        ));
        recipeScrollPane.setViewportView(recipeTable);
        recipeDesignPanel.add(recipeScrollPane, java.awt.BorderLayout.CENTER);
        contentTabs.addTab("Recipes", recipeDesignPanel);

        inventoryDesignPanel.setBackground(new java.awt.Color(244, 247, 246));
        inventoryDesignPanel.setLayout(new java.awt.BorderLayout(8, 8));
        inventoryToolbarPanel.setBackground(new java.awt.Color(255, 255, 255));
        inventorySearchField.setText("Search inventory");
        restockButton.setText("Restock");
        deductButton.setText("Deduct");
        inventoryToolbarPanel.add(inventorySearchField);
        inventoryToolbarPanel.add(restockButton);
        inventoryToolbarPanel.add(deductButton);
        inventoryDesignPanel.add(inventoryToolbarPanel, java.awt.BorderLayout.NORTH);
        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Item", "Ingredient", "Stock", "Threshold", "Status"}
        ));
        inventoryScrollPane.setViewportView(inventoryTable);
        inventoryDesignPanel.add(inventoryScrollPane, java.awt.BorderLayout.CENTER);
        contentTabs.addTab("Inventory", inventoryDesignPanel);

        mealDesignPanel.setBackground(new java.awt.Color(244, 247, 246));
        mealDesignPanel.setLayout(new java.awt.BorderLayout(8, 8));
        mealToolbarPanel.setBackground(new java.awt.Color(255, 255, 255));
        createPlanButton.setText("Create Weekly Plan");
        addMealButton.setText("Add Meal");
        mealToolbarPanel.add(createPlanButton);
        mealToolbarPanel.add(addMealButton);
        mealDesignPanel.add(mealToolbarPanel, java.awt.BorderLayout.NORTH);
        mealTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Date", "Meal Type", "Recipe"}
        ));
        mealScrollPane.setViewportView(mealTable);
        mealDesignPanel.add(mealScrollPane, java.awt.BorderLayout.CENTER);
        contentTabs.addTab("Meal Planner", mealDesignPanel);

        shoppingDesignPanel.setBackground(new java.awt.Color(244, 247, 246));
        shoppingDesignPanel.setLayout(new java.awt.BorderLayout(8, 8));
        shoppingToolbarPanel.setBackground(new java.awt.Color(255, 255, 255));
        shoppingSearchField.setText("Search shopping list");
        completeListButton.setText("Mark Complete");
        shoppingToolbarPanel.add(shoppingSearchField);
        shoppingToolbarPanel.add(completeListButton);
        shoppingDesignPanel.add(shoppingToolbarPanel, java.awt.BorderLayout.NORTH);
        shoppingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"List", "Date", "Item", "Quantity", "Status"}
        ));
        shoppingScrollPane.setViewportView(shoppingTable);
        shoppingDesignPanel.add(shoppingScrollPane, java.awt.BorderLayout.CENTER);
        contentTabs.addTab("Shopping List", shoppingDesignPanel);

        adminDesignPanel.setBackground(new java.awt.Color(244, 247, 246));
        adminDesignPanel.setLayout(new java.awt.BorderLayout(8, 8));
        adminToolbarPanel.setBackground(new java.awt.Color(255, 255, 255));
        userSearchField.setText("Search users");
        deleteUserButton.setText("Delete User");
        exportReportButton.setText("Export Report");
        adminToolbarPanel.add(userSearchField);
        adminToolbarPanel.add(deleteUserButton);
        adminToolbarPanel.add(exportReportButton);
        adminDesignPanel.add(adminToolbarPanel, java.awt.BorderLayout.NORTH);
        adminTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Username", "Email", "Role"}
        ));
        adminScrollPane.setViewportView(adminTable);
        adminDesignPanel.add(adminScrollPane, java.awt.BorderLayout.CENTER);
        contentTabs.addTab("Admin", adminDesignPanel);

        rootPanel.add(contentTabs, java.awt.BorderLayout.CENTER);

        getContentPane().add(rootPanel, java.awt.BorderLayout.CENTER);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        SessionManager.logout();
        dispose();
        new LoginBuilderForm().setVisible(true);
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void loadRuntimePanels() {
        contentTabs.removeAll();
        contentTabs.addTab("Recipes", new RecipeExplorerForm());
        contentTabs.addTab("Inventory", new InventoryForm(admin));
        contentTabs.addTab("Meal Planner", new MealPlannerForm());
        contentTabs.addTab("Shopping List", new ShoppingListForm());
        if (admin) {
            contentTabs.addTab("Users", new UserManagementForm());
            contentTabs.addTab("Reports", new ReportsForm());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane adminScrollPane;
    private javax.swing.JTable adminTable;
    private javax.swing.JPanel adminDesignPanel;
    private javax.swing.JPanel adminToolbarPanel;
    private javax.swing.JButton addMealButton;
    private javax.swing.JButton completeListButton;
    private javax.swing.JTabbedPane contentTabs;
    private javax.swing.JButton createPlanButton;
    private javax.swing.JButton deductButton;
    private javax.swing.JButton deleteUserButton;
    private javax.swing.JButton exportReportButton;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel inventoryDesignPanel;
    private javax.swing.JScrollPane inventoryScrollPane;
    private javax.swing.JTextField inventorySearchField;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JPanel inventoryToolbarPanel;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel mealDesignPanel;
    private javax.swing.JScrollPane mealScrollPane;
    private javax.swing.JTable mealTable;
    private javax.swing.JPanel mealToolbarPanel;
    private javax.swing.JButton recipeAddButton;
    private javax.swing.JButton recipeDeleteButton;
    private javax.swing.JPanel recipeDesignPanel;
    private javax.swing.JButton recipeEditButton;
    private javax.swing.JScrollPane recipeScrollPane;
    private javax.swing.JTextField recipeSearchField;
    private javax.swing.JTable recipeTable;
    private javax.swing.JPanel recipeToolbarPanel;
    private javax.swing.JButton restockButton;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JPanel shoppingDesignPanel;
    private javax.swing.JScrollPane shoppingScrollPane;
    private javax.swing.JTextField shoppingSearchField;
    private javax.swing.JTable shoppingTable;
    private javax.swing.JPanel shoppingToolbarPanel;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField userSearchField;
    // End of variables declaration//GEN-END:variables
}
