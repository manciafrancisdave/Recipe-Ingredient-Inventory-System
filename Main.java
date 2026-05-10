package com.recipeinventory.dao;

import com.recipeinventory.model.InventoryItem;
import com.recipeinventory.util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    public int insert(InventoryItem item) throws SQLException {
        String sql = "INSERT INTO inventory_items(ingredient_id,stock_qty,threshold,expiry_date,location) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getIngredientId());
            ps.setBigDecimal(2, item.getStockQty());
            ps.setBigDecimal(3, item.getThreshold());
            ps.setDate(4, item.getExpiryDate() == null ? null : Date.valueOf(item.getExpiryDate()));
            ps.setString(5, item.getLocation());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void update(InventoryItem item) throws SQLException {
        String sql = "UPDATE inventory_items SET ingredient_id=?, stock_qty=?, threshold=?, expiry_date=?, location=? WHERE item_id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, item.getIngredientId());
            ps.setBigDecimal(2, item.getStockQty());
            ps.setBigDecimal(3, item.getThreshold());
            ps.setDate(4, item.getExpiryDate() == null ? null : Date.valueOf(item.getExpiryDate()));
            ps.setString(5, item.getLocation());
            ps.setInt(6, item.getItemId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM inventory_items WHERE item_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public InventoryItem getById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(baseSql() + " WHERE inv.item_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public InventoryItem getByIngredientId(int ingredientId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(baseSql() + " WHERE inv.ingredient_id=?")) {
            ps.setInt(1, ingredientId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<InventoryItem> getAll() throws SQLException {
        return search("");
    }

    public List<InventoryItem> search(String keyword) throws SQLException {
        String sql = baseSql() + " WHERE i.name LIKE ? OR i.category LIKE ? OR inv.location LIKE ? ORDER BY i.name";
        List<InventoryItem> items = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String q = "%" + keyword + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setString(3, q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(map(rs));
            }
        }
        return items;
    }

    public List<InventoryItem> getLowStock() throws SQLException {
        return query(baseSql() + " WHERE inv.stock_qty < inv.threshold ORDER BY i.name");
    }

    public List<InventoryItem> getExpired() throws SQLException {
        return query(baseSql() + " WHERE inv.expiry_date < CURDATE() ORDER BY inv.expiry_date");
    }

    public void restock(int ingredientId, BigDecimal qty) throws SQLException {
        changeStock(ingredientId, qty);
    }

    public void deduct(int ingredientId, BigDecimal qty) throws SQLException {
        changeStock(ingredientId, qty.negate());
    }

    private void changeStock(int ingredientId, BigDecimal delta) throws SQLException {
        String sql = "UPDATE inventory_items SET stock_qty=GREATEST(0, stock_qty + ?) WHERE ingredient_id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, delta);
            ps.setInt(2, ingredientId);
            ps.executeUpdate();
        }
    }

    private List<InventoryItem> query(String sql) throws SQLException {
        List<InventoryItem> items = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) items.add(map(rs));
        }
        return items;
    }

    private String baseSql() {
        return "SELECT inv.*, i.name ingredient_name, i.category, i.unit FROM inventory_items inv JOIN ingredients i ON inv.ingredient_id=i.ingredient_id";
    }

    private InventoryItem map(ResultSet rs) throws SQLException {
        InventoryItem item = new InventoryItem();
        item.setItemId(rs.getInt("item_id"));
        item.setIngredientId(rs.getInt("ingredient_id"));
        item.setIngredientName(rs.getString("ingredient_name"));
        item.setCategory(rs.getString("category"));
        item.setUnit(rs.getString("unit"));
        item.setStockQty(rs.getBigDecimal("stock_qty"));
        item.setThreshold(rs.getBigDecimal("threshold"));
        Date expiry = rs.getDate("expiry_date");
        item.setExpiryDate(expiry == null ? null : expiry.toLocalDate());
        item.setLocation(rs.getString("location"));
        return item;
    }
}
