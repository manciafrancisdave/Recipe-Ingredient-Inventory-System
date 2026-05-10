package com.recipeinventory.ui;

import com.recipeinventory.model.CookabilityResult;
import com.recipeinventory.model.Recipe;
import com.recipeinventory.model.RecipeIngredient;
import com.recipeinventory.service.RecipeService;
import com.recipeinventory.service.ShoppingListService;
import com.recipeinventory.util.SessionManager;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class RecipeExplorerForm extends JPanel {
    private final RecipeService recipeService = new RecipeService();
    private final ShoppingListService shoppingListService = new ShoppingListService();
    private final JTable table = Ui.table();
    private final JTextField search = new JTextField(22);

    public RecipeExplorerForm() {
        super(new BorderLayout(10, 10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Search");
        JButton add = Ui.primaryButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton details = new JButton("Details");
        JButton scale = new JButton("Scale");
        JButton cook = new JButton("Cook");
        JButton favorite = new JButton("Favorite");
        JButton shopping = new JButton("Shopping List");
        top.add(new JLabel("Search"));
        top.add(search);
        for (JButton b : List.of(refresh, add, edit, delete, details, scale, cook, favorite, shopping)) top.add(b);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.addActionListener(e -> load());
        add.addActionListener(e -> editRecipe(null));
        edit.addActionListener(e -> editRecipe(selectedRecipe()));
        delete.addActionListener(e -> deleteRecipe());
        details.addActionListener(e -> showDetails());
        scale.addActionListener(e -> scaleRecipe());
        cook.addActionListener(e -> cookRecipe());
        favorite.addActionListener(e -> addFavorite());
        shopping.addActionListener(e -> generateShoppingList());
        load();
    }

    private void load() {
        try {
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Title", "Cuisine", "Difficulty", "Servings", "Time", "Rating", "Cooked"}, 0);
            for (Recipe r : recipeService.search(search.getText())) {
                model.addRow(new Object[]{r.getRecipeId(), r.getTitle(), r.getCuisine(), r.getDifficulty(), r.getServings(), r.getCookingTime(), String.format("%.1f", r.getAverageRating()), r.getCookedCount()});
            }
            table.setModel(model);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void editRecipe(Recipe recipe) {
        Recipe target = recipe == null ? new Recipe() : recipe;
        JTextField title = new JTextField(target.getTitle(), 22);
        JTextField cuisine = new JTextField(target.getCuisine(), 16);
        JTextField difficulty = new JTextField(target.getDifficulty() == null ? "Easy" : target.getDifficulty(), 12);
        JSpinner servings = new JSpinner(new SpinnerNumberModel(target.getServings() == 0 ? 2 : target.getServings(), 1, 100, 1));
        JSpinner time = new JSpinner(new SpinnerNumberModel(target.getCookingTime(), 0, 1000, 5));
        JTextField image = new JTextField(target.getImagePath(), 22);
        JButton imagePick = new JButton("Upload Image");
        JPanel panel = new JPanel(new java.awt.GridBagLayout());
        panel.add(new JLabel("Title"), Ui.gbc(0, 0)); panel.add(title, Ui.gbc(1, 0));
        panel.add(new JLabel("Cuisine"), Ui.gbc(0, 1)); panel.add(cuisine, Ui.gbc(1, 1));
        panel.add(new JLabel("Difficulty"), Ui.gbc(0, 2)); panel.add(difficulty, Ui.gbc(1, 2));
        panel.add(new JLabel("Servings"), Ui.gbc(0, 3)); panel.add(servings, Ui.gbc(1, 3));
        panel.add(new JLabel("Cooking Time"), Ui.gbc(0, 4)); panel.add(time, Ui.gbc(1, 4));
        panel.add(new JLabel("Image Path"), Ui.gbc(0, 5)); panel.add(image, Ui.gbc(1, 5));
        panel.add(imagePick, Ui.gbc(1, 6));
        imagePick.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                image.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        if (JOptionPane.showConfirmDialog(this, panel, "Recipe", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            target.setTitle(title.getText());
            target.setCuisine(cuisine.getText());
            target.setDifficulty(difficulty.getText());
            target.setServings((Integer) servings.getValue());
            target.setCookingTime((Integer) time.getValue());
            target.setImagePath(image.getText());
            target.setCreatedBy(SessionManager.getCurrentUser().getUserId());
            recipeService.saveRecipe(target);
            load();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void showDetails() {
        Recipe recipe = selectedRecipe();
        if (recipe != null) new RecipeDetailsForm(recipe.getRecipeId()).setVisible(true);
    }

    private void scaleRecipe() {
        Recipe recipe = selectedRecipe();
        if (recipe == null) return;
        String value = JOptionPane.showInputDialog(this, "Target servings", recipe.getServings());
        if (value == null) return;
        try {
            List<RecipeIngredient> scaled = recipeService.scaleRecipe(recipe.getRecipeId(), Integer.parseInt(value));
            StringBuilder text = new StringBuilder();
            for (RecipeIngredient item : scaled) text.append(item.getIngredientName()).append(": ").append(item.getQuantity()).append(" ").append(item.getUnit()).append("\n");
            Ui.info(this, text.toString());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void cookRecipe() {
        Recipe recipe = selectedRecipe();
        if (recipe == null) return;
        try {
            CookabilityResult result = recipeService.checkCookability(recipe.getRecipeId());
            if (!result.canCook()) {
                Ui.info(this, "Missing:\n" + String.join("\n", result.getMissingIngredients()));
                return;
            }
            recipeService.cookRecipe(recipe.getRecipeId());
            Ui.info(this, "Inventory auto-deducted.");
            load();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void addFavorite() {
        Recipe recipe = selectedRecipe();
        if (recipe == null) return;
        try {
            recipeService.addFavorite(SessionManager.getCurrentUser().getUserId(), recipe.getRecipeId());
            Ui.info(this, "Saved to favorites.");
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void generateShoppingList() {
        Recipe recipe = selectedRecipe();
        if (recipe == null) return;
        try {
            int listId = shoppingListService.generateFromRecipe(SessionManager.getCurrentUser().getUserId(), recipe.getRecipeId());
            Ui.info(this, "Shopping list generated: #" + listId);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void deleteRecipe() {
        Recipe recipe = selectedRecipe();
        if (recipe == null) return;
        if (JOptionPane.showConfirmDialog(this, "Delete selected recipe?") != JOptionPane.OK_OPTION) return;
        try {
            recipeService.deleteRecipe(recipe.getRecipeId());
            load();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private Recipe selectedRecipe() {
        if (table.getSelectedRow() < 0) {
            Ui.info(this, "Select a recipe first.");
            return null;
        }
        try {
            int id = Integer.parseInt(table.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0).toString());
            return recipeService.getRecipe(id);
        } catch (Exception ex) {
            Ui.error(this, ex);
            return null;
        }
    }
}
