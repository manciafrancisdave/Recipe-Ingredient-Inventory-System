package com.recipeinventory.ui;

import com.recipeinventory.dao.RecipeDAO;
import com.recipeinventory.model.MealPlan;
import com.recipeinventory.model.MealPlanRecipe;
import com.recipeinventory.model.MealType;
import com.recipeinventory.model.Recipe;
import com.recipeinventory.service.MealPlanService;
import com.recipeinventory.util.SessionManager;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MealPlannerForm extends JPanel {
    private final MealPlanService service = new MealPlanService();
    private final RecipeDAO recipeDAO = new RecipeDAO();
    private final JTable plansTable = Ui.table();
    private final JTable mealsTable = Ui.table();

    public MealPlannerForm() {
        super(new BorderLayout(10, 10));
        JButton create = Ui.primaryButton("Create Weekly Plan");
        JButton addMeal = new JButton("Add Meal");
        JButton viewMeals = new JButton("View Meals");
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(create);
        top.add(addMeal);
        top.add(viewMeals);
        add(top, BorderLayout.NORTH);
        JPanel center = new JPanel(new java.awt.GridLayout(1, 2, 10, 10));
        center.add(new JScrollPane(plansTable));
        center.add(new JScrollPane(mealsTable));
        add(center, BorderLayout.CENTER);
        create.addActionListener(e -> createPlan());
        addMeal.addActionListener(e -> addMeal());
        viewMeals.addActionListener(e -> loadMeals());
        loadPlans();
    }

    private void loadPlans() {
        try {
            DefaultTableModel model = new DefaultTableModel(new Object[]{"Plan", "User", "Start", "End"}, 0);
            for (MealPlan plan : service.getPlans("")) {
                model.addRow(new Object[]{plan.getPlanId(), plan.getUserId(), plan.getStartDate(), plan.getEndDate()});
            }
            plansTable.setModel(model);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void createPlan() {
        try {
            service.createWeeklyPlan(SessionManager.getCurrentUser().getUserId(), LocalDate.now());
            loadPlans();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void addMeal() {
        Integer planId = selectedPlanId();
        if (planId == null) return;
        try {
            JComboBox<Recipe> recipes = new JComboBox<>(recipeDAO.getAll().toArray(new Recipe[0]));
            JComboBox<MealType> types = new JComboBox<>(MealType.values());
            JTextField date = new JTextField(LocalDate.now().toString());
            Object[] form = {"Date YYYY-MM-DD", date, "Meal Type", types, "Recipe", recipes};
            if (javax.swing.JOptionPane.showConfirmDialog(this, form, "Add Meal", javax.swing.JOptionPane.OK_CANCEL_OPTION) != javax.swing.JOptionPane.OK_OPTION) return;
            Recipe recipe = (Recipe) recipes.getSelectedItem();
            MealPlanRecipe meal = new MealPlanRecipe();
            meal.setMealPlanId(planId);
            meal.setRecipeId(recipe.getRecipeId());
            meal.setMealDate(LocalDate.parse(date.getText()));
            meal.setMealType((MealType) types.getSelectedItem());
            service.addMeal(meal);
            loadMeals();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void loadMeals() {
        Integer planId = selectedPlanId();
        if (planId == null) return;
        try {
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Date", "Type", "Recipe"}, 0);
            for (MealPlanRecipe meal : service.getMeals(planId)) {
                model.addRow(new Object[]{meal.getMealPlanRecipeId(), meal.getMealDate(), meal.getMealType(), meal.getRecipeTitle()});
            }
            mealsTable.setModel(model);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private Integer selectedPlanId() {
        if (plansTable.getSelectedRow() < 0) {
            Ui.info(this, "Select a meal plan.");
            return null;
        }
        return Integer.parseInt(plansTable.getValueAt(plansTable.convertRowIndexToModel(plansTable.getSelectedRow()), 0).toString());
    }
}
