package com.recipeinventory.dao;

import com.recipeinventory.model.Tag;
import com.recipeinventory.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {

    public List<Tag> getAll() throws SQLException {
        List<Tag> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM tags ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Tag> getByRecipe(int recipeId) throws SQLException {
        String sql = "SELECT t.* FROM tags t JOIN recipe_tags rt ON t.tag_id=rt.tag_id WHERE rt.recipe_id=? ORDER BY t.name";
        List<Tag> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recipeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public int insert(Tag tag) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO tags(name,color) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tag.getName());
            ps.setString(2, tag.getColor());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void delete(int tagId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM tags WHERE tag_id=?")) {
            ps.setInt(1, tagId);
            ps.executeUpdate();
        }
    }

    public void addToRecipe(int recipeId, int tagId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT IGNORE INTO recipe_tags(recipe_id,tag_id) VALUES(?,?)")) {
            ps.setInt(1, recipeId);
            ps.setInt(2, tagId);
            ps.executeUpdate();
        }
    }

    public void removeFromRecipe(int recipeId, int tagId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM recipe_tags WHERE recipe_id=? AND tag_id=?")) {
            ps.setInt(1, recipeId);
            ps.setInt(2, tagId);
            ps.executeUpdate();
        }
    }

    private Tag map(ResultSet rs) throws SQLException {
        Tag tag = new Tag();
        tag.setTagId(rs.getInt("tag_id"));
        tag.setName(rs.getString("name"));
        tag.setColor(rs.getString("color"));
        return tag;
    }
}
