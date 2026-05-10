package com.recipeinventory.ui;

import com.recipeinventory.dao.RatingDAO;
import com.recipeinventory.model.CookingStep;
import com.recipeinventory.model.Recipe;
import com.recipeinventory.model.RecipeIngredient;
import com.recipeinventory.model.RecipeRating;
import com.recipeinventory.service.RecipeService;
import com.recipeinventory.util.SessionManager;
import java.awt.BorderLayout;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

public class RecipeDetailsForm extends JFrame {
    private final int recipeId;
    private final RecipeService recipeService = new RecipeService();
    private final RatingDAO ratingDAO = new RatingDAO();
    private final JTextArea details = new JTextArea();

    public RecipeDetailsForm(int recipeId) {
        super("Recipe Details");
        this.recipeId = recipeId;
        setSize(700, 520);
        setLocationRelativeTo(null);
        details.setEditable(false);
        JButton rate = Ui.primaryButton("Rate & Review");
        add(new JScrollPane(details), BorderLayout.CENTER);
        add(rate, BorderLayout.SOUTH);
        rate.addActionListener(e -> rateRecipe());
        load();
    }

    private void load() {
        try {
            Recipe recipe = recipeService.getRecipe(recipeId);
            StringBuilder text = new StringBuilder();
            text.append(recipe.getTitle()).append("\n")
                    .append(recipe.getCuisine()).append(" | ").append(recipe.getDifficulty())
                    .append(" | Servings: ").append(recipe.getServings())
                    .append(" | Time: ").append(recipe.getCookingTime()).append(" min\n\n")
                    .append(recipe.getDescription() == null ? "" : recipe.getDescription()).append("\n\nIngredients\n");
            for (RecipeIngredient item : recipeService.getIngredients(recipeId)) {
                text.append("- ").append(item.getIngredientName()).append(": ").append(item.getQuantity()).append(" ").append(item.getUnit());
                if (item.isOptional()) text.append(" optional");
                text.append("\n");
            }
            text.append("\nCooking steps are stored in the cooking_steps table and can be expanded in DAO/service code.\n");
            details.setText(text.toString());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void rateRecipe() {
        JSpinner score = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));
        JTextArea comment = new JTextArea(4, 24);
        Object[] form = {"Score", score, "Comment", new JScrollPane(comment)};
        if (JOptionPane.showConfirmDialog(this, form, "Rate Recipe", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            RecipeRating rating = new RecipeRating();
            rating.setRecipeId(recipeId);
            rating.setUserId(SessionManager.getCurrentUser().getUserId());
            rating.setScore((Integer) score.getValue());
            rating.setComment(comment.getText());
            rating.setCreatedDate(LocalDate.now());
            ratingDAO.insert(rating);
            Ui.info(this, "Review saved.");
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
