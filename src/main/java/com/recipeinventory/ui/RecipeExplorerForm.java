package com.recipeinventory.ui;

import com.recipeinventory.model.CookabilityResult;
import com.recipeinventory.model.CookingStep;
import com.recipeinventory.model.Ingredient;
import com.recipeinventory.model.Recipe;
import com.recipeinventory.model.RecipeIngredient;
import com.recipeinventory.service.RecipeService;
import com.recipeinventory.service.ShoppingListService;
import com.recipeinventory.util.SessionManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class RecipeExplorerForm extends JPanel {
    private final RecipeService recipeService = new RecipeService();
    private final ShoppingListService shoppingListService = new ShoppingListService();
    private final JTable table = Ui.table();
    private final JTextField search = Ui.searchField(22);

    public RecipeExplorerForm() {
        super(new BorderLayout(10, 10));
        setBackground(Ui.BACKGROUND);
        setBorder(Ui.pageBorder());
        JPanel top = Ui.toolbar();
        JButton refresh = Ui.secondaryButton("Search");
        JButton add = Ui.primaryButton("Add");
        JButton edit = Ui.secondaryButton("Edit");
        JButton delete = Ui.dangerButton("Delete");
        JButton details = Ui.secondaryButton("Details");
        JButton scale = Ui.secondaryButton("Scale");
        JButton cook = Ui.secondaryButton("Cook");
        JButton favorite = Ui.secondaryButton("Favorite");
        JButton shopping = Ui.secondaryButton("Shopping List");
        top.add(Ui.fieldLabel("Search"));
        top.add(search);
        for (JButton b : List.of(refresh, add, edit, delete, details, scale, cook, favorite, shopping)) top.add(b);
        add(top, BorderLayout.NORTH);
        JPanel card = Ui.card();
        card.add(Ui.scrollPane(table));
        add(card, BorderLayout.CENTER);

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

        // --- Header fields ---
        JTextField title = Ui.searchField(22);
        title.setText(target.getTitle());
        JTextField cuisine = Ui.searchField(16);
        cuisine.setText(target.getCuisine());
        JTextField difficulty = Ui.searchField(12);
        difficulty.setText(target.getDifficulty() == null ? "Easy" : target.getDifficulty());
        JSpinner servings = new JSpinner(new SpinnerNumberModel(target.getServings() == 0 ? 2 : target.getServings(), 1, 100, 1));
        JSpinner time = new JSpinner(new SpinnerNumberModel(target.getCookingTime(), 0, 1000, 5));
        JTextField image = Ui.searchField(22);
        image.setText(target.getImagePath());
        JButton imagePick = Ui.secondaryButton("Choose Image");
        JTextArea description = new JTextArea(3, 22);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setText(target.getDescription() == null ? "" : target.getDescription());

        JPanel header = new JPanel(new java.awt.GridBagLayout());
        header.setBackground(Ui.SURFACE);
        header.add(Ui.fieldLabel("Title"), Ui.gbc(0, 0)); header.add(title, Ui.gbc(1, 0));
        header.add(Ui.fieldLabel("Cuisine"), Ui.gbc(0, 1)); header.add(cuisine, Ui.gbc(1, 1));
        header.add(Ui.fieldLabel("Difficulty"), Ui.gbc(0, 2)); header.add(difficulty, Ui.gbc(1, 2));
        header.add(Ui.fieldLabel("Servings"), Ui.gbc(0, 3)); header.add(servings, Ui.gbc(1, 3));
        header.add(Ui.fieldLabel("Cooking Time"), Ui.gbc(0, 4)); header.add(time, Ui.gbc(1, 4));
        header.add(Ui.fieldLabel("Image Path"), Ui.gbc(0, 5)); header.add(image, Ui.gbc(1, 5));
        header.add(imagePick, Ui.gbc(1, 6));
        header.add(Ui.fieldLabel("Description"), Ui.gbc(0, 7)); header.add(new JScrollPane(description), Ui.gbc(1, 7));
        imagePick.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                image.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        // --- Working copies of ingredients and steps ---
        List<RecipeIngredient> workingIngredients = new ArrayList<>();
        List<CookingStep> workingSteps = new ArrayList<>();
        List<Ingredient> catalog = new ArrayList<>();
        try {
            catalog.addAll(recipeService.availableIngredients());
            if (target.getRecipeId() != 0) {
                workingIngredients.addAll(recipeService.getIngredients(target.getRecipeId()));
                workingSteps.addAll(recipeService.getSteps(target.getRecipeId()));
            }
        } catch (Exception ex) {
            Ui.error(this, ex);
            return;
        }

        DefaultTableModel ingredientModel = new DefaultTableModel(new Object[]{"Ingredient", "Quantity", "Unit", "Optional"}, 0);
        JTable ingredientTable = Ui.table();
        ingredientTable.setModel(ingredientModel);
        refreshIngredientRows(ingredientModel, workingIngredients);

        DefaultTableModel stepModel = new DefaultTableModel(new Object[]{"#", "Instruction", "Minutes", "Type"}, 0);
        JTable stepTable = Ui.table();
        stepTable.setModel(stepModel);
        refreshStepRows(stepModel, workingSteps);

        JButton addIngredient = Ui.secondaryButton("Add Ingredient");
        JButton removeIngredient = Ui.dangerButton("Remove");
        addIngredient.addActionListener(e -> {
            if (catalog.isEmpty()) { Ui.info(this, "No ingredients in catalog. Add some in the Ingredients screen first."); return; }
            addIngredientRow(catalog, workingIngredients);
            refreshIngredientRows(ingredientModel, workingIngredients);
        });
        removeIngredient.addActionListener(e -> {
            int row = ingredientTable.getSelectedRow();
            if (row < 0) { Ui.info(this, "Select an ingredient row to remove."); return; }
            workingIngredients.remove(ingredientTable.convertRowIndexToModel(row));
            refreshIngredientRows(ingredientModel, workingIngredients);
        });

        JButton addStep = Ui.secondaryButton("Add Step");
        JButton removeStep = Ui.dangerButton("Remove");
        addStep.addActionListener(e -> {
            addStepRow(workingSteps);
            refreshStepRows(stepModel, workingSteps);
        });
        removeStep.addActionListener(e -> {
            int row = stepTable.getSelectedRow();
            if (row < 0) { Ui.info(this, "Select a step row to remove."); return; }
            workingSteps.remove(stepTable.convertRowIndexToModel(row));
            refreshStepRows(stepModel, workingSteps);
        });

        JPanel ingredientSection = section("Ingredients", ingredientTable, addIngredient, removeIngredient);
        JPanel stepSection = section("Cooking Steps", stepTable, addStep, removeStep);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Ui.SURFACE);
        panel.add(header);
        panel.add(Box.createVerticalStrut(8));
        panel.add(ingredientSection);
        panel.add(Box.createVerticalStrut(8));
        panel.add(stepSection);
        panel.setPreferredSize(new Dimension(640, 600));

        if (JOptionPane.showConfirmDialog(this, new JScrollPane(panel), "Recipe", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            target.setTitle(title.getText());
            target.setDescription(description.getText());
            target.setCuisine(cuisine.getText());
            target.setDifficulty(difficulty.getText());
            target.setServings((Integer) servings.getValue());
            target.setCookingTime((Integer) time.getValue());
            target.setImagePath(image.getText());
            target.setCreatedBy(SessionManager.getCurrentUser().getUserId());
            int recipeId = recipeService.saveRecipe(target);
            recipeService.saveIngredients(recipeId, workingIngredients);
            recipeService.saveSteps(recipeId, workingSteps);
            load();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    /** Builds a titled card holding a scrollable table and its action buttons. */
    private JPanel section(String title, JTable table, JButton add, JButton remove) {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBackground(Ui.SURFACE);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(600, 120));
        panel.add(scroll, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        buttons.setBackground(Ui.SURFACE);
        buttons.add(add);
        buttons.add(remove);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshIngredientRows(DefaultTableModel model, List<RecipeIngredient> items) {
        model.setRowCount(0);
        for (RecipeIngredient i : items) {
            model.addRow(new Object[]{i.getIngredientName(), i.getQuantity(), i.getUnit(), i.isOptional() ? "Yes" : "No"});
        }
    }

    private void refreshStepRows(DefaultTableModel model, List<CookingStep> steps) {
        model.setRowCount(0);
        int n = 1;
        for (CookingStep s : steps) {
            model.addRow(new Object[]{n++, s.getInstruction(), s.getTimeMinutes(), s.getStepType()});
        }
    }

    /** Prompts for an ingredient/quantity/unit/optional and appends it to the list. */
    private void addIngredientRow(List<Ingredient> catalog, List<RecipeIngredient> target) {
        JComboBox<String> picker = new JComboBox<>();
        for (Ingredient ing : catalog) picker.addItem(ing.getName());
        JSpinner qty = new JSpinner(new SpinnerNumberModel(1.0, 0.01, 100000.0, 1.0));
        JTextField unit = Ui.searchField(8);
        JCheckBox optional = new JCheckBox("Optional");
        // Default the unit to the chosen ingredient's catalog unit for convenience.
        Runnable syncUnit = () -> {
            int idx = picker.getSelectedIndex();
            if (idx >= 0) unit.setText(catalog.get(idx).getUnit());
        };
        syncUnit.run();
        picker.addActionListener(e -> syncUnit.run());
        Object[] form = {"Ingredient", picker, "Quantity", qty, "Unit", unit, optional};
        if (JOptionPane.showConfirmDialog(this, form, "Add Ingredient", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        int idx = picker.getSelectedIndex();
        if (idx < 0) return;
        Ingredient chosen = catalog.get(idx);
        RecipeIngredient item = new RecipeIngredient();
        item.setIngredientId(chosen.getIngredientId());
        item.setIngredientName(chosen.getName());
        item.setQuantity(BigDecimal.valueOf(((Number) qty.getValue()).doubleValue()));
        item.setUnit(unit.getText().isBlank() ? chosen.getUnit() : unit.getText());
        item.setOptional(optional.isSelected());
        target.add(item);
    }

    /** Prompts for a cooking step and appends it to the list. */
    private void addStepRow(List<CookingStep> target) {
        JTextArea instruction = new JTextArea(3, 24);
        instruction.setLineWrap(true);
        instruction.setWrapStyleWord(true);
        JSpinner minutes = new JSpinner(new SpinnerNumberModel(5, 0, 1000, 1));
        JTextField type = Ui.searchField(12);
        Object[] form = {"Instruction", new JScrollPane(instruction), "Minutes", minutes, "Step Type", type};
        if (JOptionPane.showConfirmDialog(this, form, "Add Cooking Step", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        if (instruction.getText().isBlank()) { Ui.info(this, "Step instruction cannot be empty."); return; }
        CookingStep step = new CookingStep();
        step.setInstruction(instruction.getText());
        step.setTimeMinutes((Integer) minutes.getValue());
        step.setStepType(type.getText().isBlank() ? "Prep" : type.getText());
        target.add(step);
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
            List<RecipeIngredient> ingredients = recipeService.getIngredients(recipe.getRecipeId());
            if (ingredients.isEmpty()) {
                Ui.info(this, "\"" + recipe.getTitle() + "\" has no ingredients yet.\n"
                        + "Use Edit to add ingredients before cooking.");
                return;
            }
            CookabilityResult result = recipeService.checkCookability(recipe.getRecipeId());
            if (!result.canCook()) {
                Ui.info(this, "Cannot cook \"" + recipe.getTitle() + "\".\nMissing:\n"
                        + String.join("\n", result.getMissingIngredients()));
                return;
            }
            StringBuilder summary = new StringBuilder("Cooked \"").append(recipe.getTitle())
                    .append("\". Deducted from inventory:\n");
            for (RecipeIngredient i : ingredients) {
                if (!i.isOptional()) {
                    summary.append("- ").append(i.getIngredientName()).append(": ")
                            .append(i.getQuantity()).append(" ").append(i.getUnit()).append("\n");
                }
            }
            recipeService.cookRecipe(recipe.getRecipeId());
            Ui.info(this, summary.toString());
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
