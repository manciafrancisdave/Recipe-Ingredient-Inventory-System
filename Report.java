package com.recipeinventory.model;

import java.math.BigDecimal;

public class Ingredient {
    private int ingredientId;
    private String name;
    private String category;
    private String unit;
    private BigDecimal costPerUnit = BigDecimal.ZERO;

    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getCostPerUnit() { return costPerUnit; }
    public void setCostPerUnit(BigDecimal costPerUnit) { this.costPerUnit = costPerUnit; }
    @Override public String toString() { return name + " (" + unit + ")"; }
}
