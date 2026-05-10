package com.recipeinventory.model;

import java.time.LocalDate;

public class MealPlanRecipe {
    private int mealPlanRecipeId;
    private int mealPlanId;
    private int recipeId;
    private String recipeTitle;
    private LocalDate mealDate;
    private MealType mealType;

    public int getMealPlanRecipeId() { return mealPlanRecipeId; }
    public void setMealPlanRecipeId(int mealPlanRecipeId) { this.mealPlanRecipeId = mealPlanRecipeId; }
    public int getMealPlanId() { return mealPlanId; }
    public void setMealPlanId(int mealPlanId) { this.mealPlanId = mealPlanId; }
    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
    public String getRecipeTitle() { return recipeTitle; }
    public void setRecipeTitle(String recipeTitle) { this.recipeTitle = recipeTitle; }
    public LocalDate getMealDate() { return mealDate; }
    public void setMealDate(LocalDate mealDate) { this.mealDate = mealDate; }
    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }
}
