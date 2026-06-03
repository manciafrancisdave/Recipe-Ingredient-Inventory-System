package com.recipeinventory.dao;

import com.recipeinventory.model.InventoryTransaction;
import com.recipeinventory.util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC data access for the {@code inventory_transactions} audit table.
 */
public class InventoryTransactionDAO {

    /**
     * Appends a stock-movement record.
     *
     * @param ingredientId affected ingredient
     * @param userId       user who caused the change, may be {@code null}
     * @param changeType   one of {@link InventoryTransaction#RESTOCK},
     *                     {@link InventoryTransaction#DEDUCT},
     *                     {@link InventoryTransaction#COOK}
     * @param quantity     positive magnitude of the change
     * @param note         optional human-readable context
     * @return generated transaction id
     */
    public int insert(int ingredientId, Integer userId, String changeType, BigDecimal quantity, String note) throws SQLException {
        String sql = "INSERT INTO inventory_transactions(ingredient_id,user_id,change_type,quantity,note) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ingredientId);
            if (userId == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, userId);
            ps.setString(3, changeType);
            ps.setBigDecimal(4, quantity);
            ps.setString(5, note);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<InventoryTransaction> getAll() throws SQLException {
        return query(baseSql() + " ORDER BY t.created_at DESC, t.transaction_id DESC", null);
    }

    public List<InventoryTransaction> getByIngredient(int ingredientId) throws SQLException {
        return query(baseSql() + " WHERE t.ingredient_id=? ORDER BY t.created_at DESC, t.transaction_id DESC", ingredientId);
    }

    private List<InventoryTransaction> query(String sql, Integer ingredientId) throws SQLException {
        List<InventoryTransaction> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (ingredientId != null) ps.setInt(1, ingredientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    private String baseSql() {
        return "SELECT t.*, i.name ingredient_name, u.username FROM inventory_transactions t "
                + "JOIN ingredients i ON t.ingredient_id=i.ingredient_id "
                + "LEFT JOIN users u ON t.user_id=u.user_id";
    }

    private InventoryTransaction map(ResultSet rs) throws SQLException {
        InventoryTransaction t = new InventoryTransaction();
        t.setTransactionId(rs.getInt("transaction_id"));
        t.setIngredientId(rs.getInt("ingredient_id"));
        t.setIngredientName(rs.getString("ingredient_name"));
        int uid = rs.getInt("user_id");
        t.setUserId(rs.wasNull() ? null : uid);
        t.setUsername(rs.getString("username"));
        t.setChangeType(rs.getString("change_type"));
        t.setQuantity(rs.getBigDecimal("quantity"));
        t.setNote(rs.getString("note"));
        Timestamp ts = rs.getTimestamp("created_at");
        t.setCreatedAt(ts == null ? null : ts.toLocalDateTime());
        return t;
    }
}
