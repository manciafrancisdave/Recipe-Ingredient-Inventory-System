package com.recipeinventory.dao;

import com.recipeinventory.model.Report;
import com.recipeinventory.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportDAO {
    public int insert(Report report) throws SQLException {
        String sql = "INSERT INTO reports(type,generated_by,data) VALUES(?,?,CAST(? AS JSON))";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, report.getType());
            ps.setInt(2, report.getGeneratedBy());
            ps.setString(3, report.getData());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void update(Report report) throws SQLException {
        String sql = "UPDATE reports SET type=?, generated_by=?, data=CAST(? AS JSON) WHERE report_id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, report.getType());
            ps.setInt(2, report.getGeneratedBy());
            ps.setString(3, report.getData());
            ps.setInt(4, report.getReportId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM reports WHERE report_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Report getById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM reports WHERE report_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Report> getAll() throws SQLException {
        return search("");
    }

    public List<Report> search(String keyword) throws SQLException {
        List<Report> reports = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM reports WHERE type LIKE ? ORDER BY generated_date DESC")) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) reports.add(map(rs));
            }
        }
        return reports;
    }

    public Map<String, Object> dashboardStats() throws SQLException {
        Map<String, Object> stats = new LinkedHashMap<>();
        try (Connection con = DBConnection.getConnection()) {
            stats.put("Total users", scalar(con, "SELECT COUNT(*) FROM users"));
            stats.put("Total recipes", scalar(con, "SELECT COUNT(*) FROM recipes"));
            stats.put("Low stock ingredients", scalar(con, "SELECT COUNT(*) FROM inventory_items WHERE stock_qty < threshold"));
            stats.put("Expired ingredients", scalar(con, "SELECT COUNT(*) FROM inventory_items WHERE expiry_date < CURDATE()"));
            stats.put("Inventory cost", scalar(con, "SELECT COALESCE(SUM(inv.stock_qty * i.cost_per_unit),0) FROM inventory_items inv JOIN ingredients i ON inv.ingredient_id=i.ingredient_id"));
        }
        return stats;
    }

    public List<String[]> mostCookedRecipes() throws SQLException {
        return rows("SELECT title, cooked_count FROM recipes ORDER BY cooked_count DESC LIMIT 10");
    }

    public List<String[]> popularRecipes() throws SQLException {
        return rows("SELECT r.title, ROUND(COALESCE(AVG(rr.score),0),2) rating FROM recipes r LEFT JOIN recipe_ratings rr ON r.recipe_id=rr.recipe_id GROUP BY r.recipe_id ORDER BY rating DESC LIMIT 10");
    }

    public List<String[]> lowStockReport() throws SQLException {
        return rows("SELECT i.name, inv.stock_qty, inv.threshold FROM inventory_items inv JOIN ingredients i ON inv.ingredient_id=i.ingredient_id WHERE inv.stock_qty < inv.threshold");
    }

    public List<String[]> inventoryCostReport() throws SQLException {
        return rows("SELECT i.name, inv.stock_qty, i.cost_per_unit, (inv.stock_qty*i.cost_per_unit) total FROM inventory_items inv JOIN ingredients i ON inv.ingredient_id=i.ingredient_id ORDER BY total DESC");
    }

    public List<String[]> userActivityReport() throws SQLException {
        return rows("SELECT u.username, COUNT(DISTINCT r.recipe_id) recipes, COUNT(DISTINCT f.recipe_id) favorites, COUNT(DISTINCT rr.rating_id) ratings FROM users u LEFT JOIN recipes r ON u.user_id=r.created_by LEFT JOIN favorites f ON u.user_id=f.user_id LEFT JOIN recipe_ratings rr ON u.user_id=rr.user_id GROUP BY u.user_id");
    }

    private Object scalar(Connection con, String sql) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getObject(1) : 0;
        }
    }

    private List<String[]> rows(String sql) throws SQLException {
        List<String[]> data = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            int cols = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                String[] row = new String[cols];
                for (int i = 1; i <= cols; i++) row[i - 1] = String.valueOf(rs.getObject(i));
                data.add(row);
            }
        }
        return data;
    }

    private Report map(ResultSet rs) throws SQLException {
        Report report = new Report();
        report.setReportId(rs.getInt("report_id"));
        report.setType(rs.getString("type"));
        report.setGeneratedDate(rs.getTimestamp("generated_date").toLocalDateTime());
        report.setGeneratedBy(rs.getInt("generated_by"));
        report.setData(rs.getString("data"));
        return report;
    }
}
