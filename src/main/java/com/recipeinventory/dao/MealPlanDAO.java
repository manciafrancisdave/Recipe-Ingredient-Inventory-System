package com.recipeinventory.dao;

import com.recipeinventory.model.MealPlan;
import com.recipeinventory.model.MealPlanRecipe;
import com.recipeinventory.model.MealType;
import com.recipeinventory.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealPlanDAO {
    public int insert(MealPlan plan) throws SQLException {
        String sql = "INSERT INTO meal_plans(user_id,start_date,end_date) VALUES(?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, plan.getUserId());
            ps.setDate(2, Date.valueOf(plan.getStartDate()));
            ps.setDate(3, Date.valueOf(plan.getEndDate()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void update(MealPlan plan) throws SQLException {
        String sql = "UPDATE meal_plans SET user_id=?, start_date=?, end_date=? WHERE plan_id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, plan.getUserId());
            ps.setDate(2, Date.valueOf(plan.getStartDate()));
            ps.setDate(3, Date.valueOf(plan.getEndDate()));
            ps.setInt(4, plan.getPlanId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM meal_plans WHERE plan_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public MealPlan getById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM meal_plans WHERE plan_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapPlan(rs) : null;
            }
        }
    }

    public List<MealPlan> getAll() throws SQLException {
        return search("");
    }

    public List<MealPlan> search(String keyword) throws SQLException {
        String sql = "SELECT mp.* FROM meal_plans mp JOIN users u ON mp.user_id=u.user_id WHERE u.username LIKE ? ORDER BY start_date DESC";
        List<MealPlan> plans = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) plans.add(mapPlan(rs));
            }
        }
        return plans;
    }

    public void addMeal(MealPlanRecipe meal) throws SQLException {
        String sql = "INSERT INTO meal_plan_recipes(meal_plan_id,recipe_id,meal_date,meal_type) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, meal.getMealPlanId());
            ps.setInt(2, meal.getRecipeId());
            ps.setDate(3, Date.valueOf(meal.getMealDate()));
            ps.setString(4, meal.getMealType().name());
            ps.executeUpdate();
        }
    }

    public void removeMeal(int mealPlanRecipeId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM meal_plan_recipes WHERE meal_plan_recipe_id=?")) {
            ps.setInt(1, mealPlanRecipeId);
            ps.executeUpdate();
        }
    }

    public List<MealPlanRecipe> getMeals(int planId) throws SQLException {
        String sql = "SELECT mpr.*, r.title recipe_title FROM meal_plan_recipes mpr JOIN recipes r ON mpr.recipe_id=r.recipe_id WHERE meal_plan_id=? ORDER BY meal_date, meal_type";
        List<MealPlanRecipe> meals = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, planId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MealPlanRecipe meal = new MealPlanRecipe();
                    meal.setMealPlanRecipeId(rs.getInt("meal_plan_recipe_id"));
                    meal.setMealPlanId(rs.getInt("meal_plan_id"));
                    meal.setRecipeId(rs.getInt("recipe_id"));
                    meal.setRecipeTitle(rs.getString("recipe_title"));
                    meal.setMealDate(rs.getDate("meal_date").toLocalDate());
                    meal.setMealType(MealType.valueOf(rs.getString("meal_type")));
                    meals.add(meal);
                }
            }
        }
        return meals;
    }

    private MealPlan mapPlan(ResultSet rs) throws SQLException {
        MealPlan plan = new MealPlan();
        plan.setPlanId(rs.getInt("plan_id"));
        plan.setUserId(rs.getInt("user_id"));
        plan.setStartDate(rs.getDate("start_date").toLocalDate());
        plan.setEndDate(rs.getDate("end_date").toLocalDate());
        return plan;
    }
}
