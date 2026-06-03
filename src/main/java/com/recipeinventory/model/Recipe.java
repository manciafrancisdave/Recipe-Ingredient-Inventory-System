package com.recipeinventory.model;

import java.time.LocalDateTime;

public class Recipe {
    private int recipeId;
    private String title;
    private String description;
    private String cuisine;
    private String difficulty;
    private int servings;
    private int cookingTime;
    private String imagePath;
    private int createdBy;
    private int cookedCount;
    private LocalDateTime createdAt;
    private double averageRating;

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }
    public int getCookingTime() { return cookingTime; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
    public int getCookedCount() { return cookedCount; }
    public void setCookedCount(int cookedCount) { this.cookedCount = cookedCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    @Override public String toString() { return title; }
}
