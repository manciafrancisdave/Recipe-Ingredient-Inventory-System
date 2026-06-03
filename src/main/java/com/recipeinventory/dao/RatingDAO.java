package com.recipeinventory.dao;

import com.recipeinventory.model.RecipeRating;
import com.recipeinventory.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO {
    public int insert(RecipeRating rating) throws SQLException {
        String sql = "INSERT INTO recipe_ratings(recipe_id,user_id,score,comment,created_date) VALUES(?,?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE score=VALUES(score), comment=VALUES(comment), created_date=VALUES(created_date)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, rating.getRecipeId());
            ps.setInt(2, rating.getUserId());
            ps.setInt(3, rating.getScore());
            ps.setString(4, rating.getComment());
            ps.setDate(5, Date.valueOf(rating.getCreatedDate()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void update(RecipeRating rating) throws SQLException {
        String sql = "UPDATE recipe_ratings SET score=?, comment=?, created_date=? WHERE rating_id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rating.getScore());
            ps.setString(2, rating.getComment());
            ps.setDate(3, Date.valueOf(rating.getCreatedDate()));
            ps.setInt(4, rating.getRatingId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM recipe_ratings WHERE rating_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public RecipeRating getById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM recipe_ratings WHERE rating_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<RecipeRating> getAll() throws SQLException {
        return search("");
    }

    public List<RecipeRating> search(String keyword) throws SQLException {
        String sql = "SELECT rr.* FROM recipe_ratings rr JOIN recipes r ON rr.recipe_id=r.recipe_id WHERE r.title LIKE ? ORDER BY created_date DESC";
        List<RecipeRating> ratings = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ratings.add(map(rs));
            }
        }
        return ratings;
    }

    private RecipeRating map(ResultSet rs) throws SQLException {
        RecipeRating rating = new RecipeRating();
        rating.setRatingId(rs.getInt("rating_id"));
        rating.setRecipeId(rs.getInt("recipe_id"));
        rating.setUserId(rs.getInt("user_id"));
        rating.setScore(rs.getInt("score"));
        rating.setComment(rs.getString("comment"));
        rating.setCreatedDate(rs.getDate("created_date").toLocalDate());
        return rating;
    }
}
