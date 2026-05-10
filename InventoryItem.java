package com.recipeinventory.dao;

import com.recipeinventory.model.ShoppingList;
import com.recipeinventory.model.ShoppingListItem;
import com.recipeinventory.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListDAO {
    public int insert(ShoppingList list) throws SQLException {
        String sql = "INSERT INTO shopping_lists(user_id,created_date,status) VALUES(?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, list.getUserId());
            ps.setDate(2, Date.valueOf(list.getCreatedDate()));
            ps.setString(3, list.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void update(ShoppingList list) throws SQLException {
        String sql = "UPDATE shopping_lists SET user_id=?, created_date=?, status=? WHERE list_id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, list.getUserId());
            ps.setDate(2, Date.valueOf(list.getCreatedDate()));
            ps.setString(3, list.getStatus());
            ps.setInt(4, list.getListId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM shopping_lists WHERE list_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public ShoppingList getById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM shopping_lists WHERE list_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapList(rs) : null;
            }
        }
    }

    public List<ShoppingList> getAll() throws SQLException {
        return search("");
    }

    public List<ShoppingList> search(String keyword) throws SQLException {
        String sql = "SELECT sl.* FROM shopping_lists sl JOIN users u ON sl.user_id=u.user_id WHERE u.username LIKE ? OR sl.status LIKE ? ORDER BY sl.created_date DESC";
        List<ShoppingList> lists = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String q = "%" + keyword + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lists.add(mapList(rs));
            }
        }
        return lists;
    }

    public void insertItem(ShoppingListItem item) throws SQLException {
        String sql = "INSERT INTO shopping_list_items(shopping_list_id,ingredient_id,quantity,purchased) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, item.getShoppingListId());
            ps.setInt(2, item.getIngredientId());
            ps.setBigDecimal(3, item.getQuantity());
            ps.setBoolean(4, item.isPurchased());
            ps.executeUpdate();
        }
    }

    public void deleteItem(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM shopping_list_items WHERE item_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void markComplete(int listId) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE shopping_lists SET status='COMPLETED' WHERE list_id=?")) {
                ps.setInt(1, listId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("UPDATE shopping_list_items SET purchased=TRUE WHERE shopping_list_id=?")) {
                ps.setInt(1, listId);
                ps.executeUpdate();
            }
        }
    }

    public List<ShoppingListItem> getItems(int listId) throws SQLException {
        String sql = "SELECT sli.*, i.name ingredient_name FROM shopping_list_items sli JOIN ingredients i ON sli.ingredient_id=i.ingredient_id WHERE shopping_list_id=?";
        List<ShoppingListItem> items = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, listId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShoppingListItem item = new ShoppingListItem();
                    item.setItemId(rs.getInt("item_id"));
                    item.setShoppingListId(rs.getInt("shopping_list_id"));
                    item.setIngredientId(rs.getInt("ingredient_id"));
                    item.setIngredientName(rs.getString("ingredient_name"));
                    item.setQuantity(rs.getBigDecimal("quantity"));
                    item.setPurchased(rs.getBoolean("purchased"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    private ShoppingList mapList(ResultSet rs) throws SQLException {
        ShoppingList list = new ShoppingList();
        list.setListId(rs.getInt("list_id"));
        list.setUserId(rs.getInt("user_id"));
        list.setCreatedDate(rs.getDate("created_date").toLocalDate());
        list.setStatus(rs.getString("status"));
        return list;
    }
}
