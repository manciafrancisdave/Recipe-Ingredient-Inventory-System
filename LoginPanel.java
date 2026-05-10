package com.recipeinventory.service;

import com.recipeinventory.dao.MealPlanDAO;
import com.recipeinventory.model.MealPlan;
import com.recipeinventory.model.MealPlanRecipe;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MealPlanService {
    private final MealPlanDAO mealPlanDAO = new MealPlanDAO();

    public int createWeeklyPlan(int userId, LocalDate startDate) throws SQLException {
        MealPlan plan = new MealPlan();
        plan.setUserId(userId);
        plan.setStartDate(startDate);
        plan.setEndDate(startDate.plusDays(6));
        return mealPlanDAO.insert(plan);
    }

    public void addMeal(MealPlanRecipe meal) throws SQLException {
        mealPlanDAO.addMeal(meal);
    }

    public void removeMeal(int mealPlanRecipeId) throws SQLException {
        mealPlanDAO.removeMeal(mealPlanRecipeId);
    }

    public List<MealPlan> getPlans(String keyword) throws SQLException {
        return mealPlanDAO.search(keyword == null ? "" : keyword);
    }

    public List<MealPlanRecipe> getMeals(int planId) throws SQLException {
        return mealPlanDAO.getMeals(planId);
    }
}
