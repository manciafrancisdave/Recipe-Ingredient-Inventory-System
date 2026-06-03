package com.recipeinventory.model;

public class CookingStep {
    private int stepId;
    private int recipeId;
    private int stepNumber;
    private String instruction;
    private int timeMinutes;
    private String stepType;

    public int getStepId() { return stepId; }
    public void setStepId(int stepId) { this.stepId = stepId; }
    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
    public int getStepNumber() { return stepNumber; }
    public void setStepNumber(int stepNumber) { this.stepNumber = stepNumber; }
    public String getInstruction() { return instruction; }
    public void setInstruction(String instruction) { this.instruction = instruction; }
    public int getTimeMinutes() { return timeMinutes; }
    public void setTimeMinutes(int timeMinutes) { this.timeMinutes = timeMinutes; }
    public String getStepType() { return stepType; }
    public void setStepType(String stepType) { this.stepType = stepType; }
}
