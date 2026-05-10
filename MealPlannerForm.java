package com.recipeinventory.service;

import com.recipeinventory.dao.InventoryDAO;
import com.recipeinventory.dao.RecipeDAO;
import com.recipeinventory.model.CookabilityResult;
import com.recipeinventory.model.InventoryItem;
import com.recipeinventory.model.Recipe;
import com.recipeinventory.model.RecipeIngredient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeService {
    private final RecipeDAO recipeDAO = new RecipeDAO();
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    public List<Recipe> search(String keyword) throws SQLException {
        return recipeDAO.search(keyword == null ? "" : keyword);
    }

    public List<Recipe> filter(String cuisine, String difficulty, String tag, String ingredient, int maxCookingTime) throws SQLException {
        return recipeDAO.filter(nvl(cuisine), nvl(difficulty), nvl(tag), nvl(ingredient), maxCookingTime);
    }

    public int saveRecipe(Recipe recipe) throws SQLException {
        validate(recipe);
        if (recipe.getRecipeId() == 0) {
            return recipeDAO.insert(recipe);
        }
        recipeDAO.update(recipe);
        return recipe.getRecipeId();
    }

    public void deleteRecipe(int recipeId) throws SQLException {
        recipeDAO.delete(recipeId);
    }

    public Recipe getRecipe(int recipeId) throws SQLException {
        return recipeDAO.getById(recipeId);
    }

    public List<RecipeIngredient> getIngredients(int recipeId) throws SQLException {
        return recipeDAO.getIngredients(recipeId);
    }

    public List<RecipeIngredient> scaleRecipe(int recipeId, int targetServings) throws SQLException {
        Recipe recipe = recipeDAO.getById(recipeId);
        if (recipe == null || targetServings <= 0) {
            throw new IllegalArgumentException("Valid recipe and servings are required.");
        }
        BigDecimal factor = BigDecimal.valueOf(targetServings)
                .divide(BigDecimal.valueOf(recipe.getServings()), 4, RoundingMode.HALF_UP);
        List<RecipeIngredient> scaled = new ArrayList<>();
        for (RecipeIngredient item : recipeDAO.getIngredients(recipeId)) {
            RecipeIngredient copy = new RecipeIngredient();
            copy.setIngredientName(item.getIngredientName());
            copy.setIngredientId(item.getIngredientId());
            copy.setQuantity(item.getQuantity().multiply(factor).setScale(2, RoundingMode.HALF_UP));
            copy.setUnit(item.getUnit());
            copy.setOptional(item.isOptional());
            scaled.add(copy);
        }
        return scaled;
    }

    public CookabilityResult checkCookability(int recipeId) throws SQLException {
        CookabilityResult result = new CookabilityResult();
        for (RecipeIngredient needed : recipeDAO.getIngredients(recipeId)) {
            InventoryItem item = inventoryDAO.getByIngredientId(needed.getIngredientId());
            BigDecimal available = item == null ? BigDecimal.ZERO : item.getStockQty();
            if (available.compareTo(needed.getQuantity()) >= 0 || needed.isOptional()) {
                result.getAvailableIngredients().add(needed.getIngredientName() + " available: " + available + " " + needed.getUnit());
            } else {
                BigDecimal missing = needed.getQuantity().subtract(available);
                result.getMissingIngredients().add(needed.getIngredientName() + " missing: " + missing + " " + needed.getUnit());
            }
        }
        return result;
    }

    public void cookRecipe(int recipeId) throws SQLException {
        CookabilityResult result = checkCookability(recipeId);
        if (!result.canCook()) {
            throw new IllegalStateException("Missing ingredients: " + String.join(", ", result.getMissingIngredients()));
        }
        for (RecipeIngredient ingredient : recipeDAO.getIngredients(recipeId)) {
            if (!ingredient.isOptional()) {
                inventoryDAO.deduct(ingredient.getIngredientId(), ingredient.getQuantity());
            }
        }
        recipeDAO.incrementCookedCount(recipeId);
    }

    public void addFavorite(int userId, int recipeId) throws SQLException {
        recipeDAO.addFavorite(userId, recipeId);
    }

    public void removeFavorite(int userId, int recipeId) throws SQLException {
        recipeDAO.removeFavorite(userId, recipeId);
    }

    public List<Recipe> favorites(int userId) throws SQLException {
        return recipeDAO.getFavorites(userId);
    }

    private void validate(Recipe recipe) {
        if (recipe.getTitle() == null || recipe.getTitle().isBlank()) {
            throw new IllegalArgumentException("Recipe title is required.");
        }
        if (recipe.getServings() <= 0) {
            throw new IllegalArgumentException("Servings must be greater than zero.");
        }
    }

    private String nvl(String value) {
        return value == null ? "" : value;
    }
}
