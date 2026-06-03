package com.recipeinventory.model;

import java.math.BigDecimal;

public class RecipeIngredient {
    private int recipeIngredientId;
    private int recipeId;
    private int ingredientId;
    private String ingredientName;
    private BigDecimal quantity = BigDecimal.ONE;
    private String unit;
    private boolean optional;

    public int getRecipeIngredientId() { return recipeIngredientId; }
    public void setRecipeIngredientId(int recipeIngredientId) { this.recipeIngredientId = recipeIngredientId; }
    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }
    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public boolean isOptional() { return optional; }
    public void setOptional(boolean optional) { this.optional = optional; }
}
