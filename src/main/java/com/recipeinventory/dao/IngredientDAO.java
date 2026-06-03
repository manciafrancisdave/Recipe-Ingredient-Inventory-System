package com.recipeinventory.dao;

import com.recipeinventory.model.Ingredient;
import com.recipeinventory.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO {
    public int insert(Ingredient ingredient) throws SQLException {
        String sql = "INSERT INTO ingredients(name,category,unit,cost_per_unit) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ingredient.getName());
            ps.setString(2, ingredient.getCategory());
            ps.setString(3, ingredient.getUnit());
            ps.setBigDecimal(4, ingredient.getCostPerUnit());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void update(Ingredient ingredient) throws SQLException {
        String sql = "UPDATE ingredients SET name=?, category=?, unit=?, cost_per_unit=? WHERE ingredient_id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ingredient.getName());
            ps.setString(2, ingredient.getCategory());
            ps.setString(3, ingredient.getUnit());
            ps.setBigDecimal(4, ingredient.getCostPerUnit());
            ps.setInt(5, ingredient.getIngredientId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM ingredients WHERE ingredient_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Ingredient getById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM ingredients WHERE ingredient_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Ingredient> getAll() throws SQLException {
        return search("");
    }

    public List<Ingredient> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM ingredients WHERE name LIKE ? OR category LIKE ? ORDER BY name";
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String q = "%" + keyword + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ingredients.add(map(rs));
            }
        }
        return ingredients;
    }

    private Ingredient map(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(rs.getInt("ingredient_id"));
        ingredient.setName(rs.getString("name"));
        ingredient.setCategory(rs.getString("category"));
        ingredient.setUnit(rs.getString("unit"));
        ingredient.setCostPerUnit(rs.getBigDecimal("cost_per_unit"));
        return ingredient;
    }
}
