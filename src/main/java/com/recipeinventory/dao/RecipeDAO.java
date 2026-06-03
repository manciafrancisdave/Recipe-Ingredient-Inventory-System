package com.recipeinventory.dao;

import com.recipeinventory.model.CookingStep;
import com.recipeinventory.model.Recipe;
import com.recipeinventory.model.RecipeIngredient;
import com.recipeinventory.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    public int insert(Recipe recipe) throws SQLException {
        String sql = "INSERT INTO recipes(title,description,cuisine,difficulty,servings,cooking_time,image_path,created_by) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindRecipe(ps, recipe);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void update(Recipe recipe) throws SQLException {
        String sql = "UPDATE recipes SET title=?,description=?,cuisine=?,difficulty=?,servings=?,cooking_time=?,image_path=?,created_by=? WHERE recipe_id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            bindRecipe(ps, recipe);
            ps.setInt(9, recipe.getRecipeId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM recipes WHERE recipe_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Recipe getById(int id) throws SQLException {
        String sql = "SELECT r.*, COALESCE(AVG(rr.score),0) avg_rating FROM recipes r LEFT JOIN recipe_ratings rr ON r.recipe_id=rr.recipe_id WHERE r.recipe_id=? GROUP BY r.recipe_id";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Recipe> getAll() throws SQLException {
        return search("");
    }

    public List<Recipe> search(String keyword) throws SQLException {
        String sql = "SELECT r.*, COALESCE(AVG(rr.score),0) avg_rating FROM recipes r LEFT JOIN recipe_ratings rr ON r.recipe_id=rr.recipe_id "
                + "WHERE r.title LIKE ? OR r.cuisine LIKE ? OR r.difficulty LIKE ? GROUP BY r.recipe_id ORDER BY r.created_at DESC";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String q = "%" + keyword + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setString(3, q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) recipes.add(map(rs));
            }
        }
        return recipes;
    }

    public List<Recipe> filter(String cuisine, String difficulty, String tag, String ingredient, int maxCookingTime) throws SQLException {
        String sql = "SELECT DISTINCT r.*, COALESCE(AVG(rr.score),0) avg_rating FROM recipes r "
                + "LEFT JOIN recipe_ratings rr ON r.recipe_id=rr.recipe_id "
                + "LEFT JOIN recipe_tags rt ON r.recipe_id=rt.recipe_id LEFT JOIN tags t ON rt.tag_id=t.tag_id "
                + "LEFT JOIN recipe_ingredients ri ON r.recipe_id=ri.recipe_id LEFT JOIN ingredients i ON ri.ingredient_id=i.ingredient_id "
                + "WHERE (?='' OR r.cuisine=?) AND (?='' OR r.difficulty=?) AND (?='' OR t.name=?) AND (?='' OR i.name LIKE ?) "
                + "AND (?=0 OR r.cooking_time<=?) GROUP BY r.recipe_id ORDER BY r.title";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cuisine); ps.setString(2, cuisine);
            ps.setString(3, difficulty); ps.setString(4, difficulty);
            ps.setString(5, tag); ps.setString(6, tag);
            ps.setString(7, ingredient); ps.setString(8, "%" + ingredient + "%");
            ps.setInt(9, maxCookingTime); ps.setInt(10, maxCookingTime);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) recipes.add(map(rs));
            }
        }
        return recipes;
    }

    public void insertIngredient(RecipeIngredient item) throws SQLException {
        String sql = "INSERT INTO recipe_ingredients(recipe_id,ingredient_id,quantity,unit,is_optional) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, item.getRecipeId());
            ps.setInt(2, item.getIngredientId());
            ps.setBigDecimal(3, item.getQuantity());
            ps.setString(4, item.getUnit());
            ps.setBoolean(5, item.isOptional());
            ps.executeUpdate();
        }
    }

    public List<RecipeIngredient> getIngredients(int recipeId) throws SQLException {
        String sql = "SELECT ri.*, i.name ingredient_name FROM recipe_ingredients ri JOIN ingredients i ON ri.ingredient_id=i.ingredient_id WHERE recipe_id=?";
        List<RecipeIngredient> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recipeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RecipeIngredient item = new RecipeIngredient();
                    item.setRecipeIngredientId(rs.getInt("recipe_ingredient_id"));
                    item.setRecipeId(rs.getInt("recipe_id"));
                    item.setIngredientId(rs.getInt("ingredient_id"));
                    item.setIngredientName(rs.getString("ingredient_name"));
                    item.setQuantity(rs.getBigDecimal("quantity"));
                    item.setUnit(rs.getString("unit"));
                    item.setOptional(rs.getBoolean("is_optional"));
                    list.add(item);
                }
            }
        }
        return list;
    }

    public void clearIngredients(int recipeId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM recipe_ingredients WHERE recipe_id=?")) {
            ps.setInt(1, recipeId);
            ps.executeUpdate();
        }
    }

    public void insertStep(CookingStep step) throws SQLException {
        String sql = "INSERT INTO cooking_steps(recipe_id,step_number,instruction,time_minutes,step_type) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, step.getRecipeId());
            ps.setInt(2, step.getStepNumber());
            ps.setString(3, step.getInstruction());
            ps.setInt(4, step.getTimeMinutes());
            ps.setString(5, step.getStepType());
            ps.executeUpdate();
        }
    }

    public List<CookingStep> getSteps(int recipeId) throws SQLException {
        List<CookingStep> steps = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM cooking_steps WHERE recipe_id=? ORDER BY step_number")) {
            ps.setInt(1, recipeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CookingStep step = new CookingStep();
                    step.setStepId(rs.getInt("step_id"));
                    step.setRecipeId(rs.getInt("recipe_id"));
                    step.setStepNumber(rs.getInt("step_number"));
                    step.setInstruction(rs.getString("instruction"));
                    step.setTimeMinutes(rs.getInt("time_minutes"));
                    step.setStepType(rs.getString("step_type"));
                    steps.add(step);
                }
            }
        }
        return steps;
    }

    public void clearSteps(int recipeId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM cooking_steps WHERE recipe_id=?")) {
            ps.setInt(1, recipeId);
            ps.executeUpdate();
        }
    }

    public void addFavorite(int userId, int recipeId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT IGNORE INTO favorites(user_id,recipe_id) VALUES(?,?)")) {
            ps.setInt(1, userId);
            ps.setInt(2, recipeId);
            ps.executeUpdate();
        }
    }

    public void removeFavorite(int userId, int recipeId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM favorites WHERE user_id=? AND recipe_id=?")) {
            ps.setInt(1, userId);
            ps.setInt(2, recipeId);
            ps.executeUpdate();
        }
    }

    public List<Recipe> getFavorites(int userId) throws SQLException {
        String sql = "SELECT r.*, COALESCE(AVG(rr.score),0) avg_rating FROM favorites f JOIN recipes r ON f.recipe_id=r.recipe_id "
                + "LEFT JOIN recipe_ratings rr ON r.recipe_id=rr.recipe_id WHERE f.user_id=? GROUP BY r.recipe_id";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) recipes.add(map(rs));
            }
        }
        return recipes;
    }

    public void incrementCookedCount(int recipeId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE recipes SET cooked_count=cooked_count+1 WHERE recipe_id=?")) {
            ps.setInt(1, recipeId);
            ps.executeUpdate();
        }
    }

    private void bindRecipe(PreparedStatement ps, Recipe recipe) throws SQLException {
        ps.setString(1, recipe.getTitle());
        ps.setString(2, recipe.getDescription());
        ps.setString(3, recipe.getCuisine());
        ps.setString(4, recipe.getDifficulty());
        ps.setInt(5, recipe.getServings());
        ps.setInt(6, recipe.getCookingTime());
        ps.setString(7, recipe.getImagePath());
        ps.setInt(8, recipe.getCreatedBy());
    }

    private Recipe map(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(rs.getInt("recipe_id"));
        recipe.setTitle(rs.getString("title"));
        recipe.setDescription(rs.getString("description"));
        recipe.setCuisine(rs.getString("cuisine"));
        recipe.setDifficulty(rs.getString("difficulty"));
        recipe.setServings(rs.getInt("servings"));
        recipe.setCookingTime(rs.getInt("cooking_time"));
        recipe.setImagePath(rs.getString("image_path"));
        recipe.setCreatedBy(rs.getInt("created_by"));
        recipe.setCookedCount(rs.getInt("cooked_count"));
        recipe.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        recipe.setAverageRating(rs.getDouble("avg_rating"));
        return recipe;
    }
}
