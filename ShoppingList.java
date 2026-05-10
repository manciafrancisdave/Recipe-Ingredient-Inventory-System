package com.recipeinventory.model;

import java.time.LocalDate;

public class MealPlan {
    private int planId;
    private int userId;
    private LocalDate startDate;
    private LocalDate endDate;

    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
