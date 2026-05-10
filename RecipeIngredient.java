package com.recipeinventory.model;

import java.util.ArrayList;
import java.util.List;

public class CookabilityResult {
    private final List<String> availableIngredients = new ArrayList<>();
    private final List<String> missingIngredients = new ArrayList<>();

    public List<String> getAvailableIngredients() { return availableIngredients; }
    public List<String> getMissingIngredients() { return missingIngredients; }
    public boolean canCook() { return missingIngredients.isEmpty(); }
}
