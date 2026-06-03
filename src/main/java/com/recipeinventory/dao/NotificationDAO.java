package com.recipeinventory.dao;

import com.recipeinventory.model.Notification;
import com.recipeinventory.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC data access for the {@code notifications} table.
 */
public class NotificationDAO {

    public int insert(Notification n) throws SQLException {
        String sql = "INSERT INTO notifications(user_id,type,message,is_read) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (n.getUserId() == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, n.getUserId());
            ps.setString(2, n.getType());
            ps.setString(3, n.getMessage());
            ps.setBoolean(4, n.isRead());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Inserts a notification only when an identical unread message of the same
     * type does not already exist for the user, so repeated low-stock scans do
     * not flood the table with duplicates.
     *
     * @return the new notification id, or 0 if a duplicate already existed
     */
    public int insertIfAbsent(Notification n) throws SQLException {
        String check = "SELECT notification_id FROM notifications WHERE type=? AND message=? AND is_read=FALSE "
                + "AND ((user_id IS NULL AND ? IS NULL) OR user_id=?) LIMIT 1";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(check)) {
            ps.setString(1, n.getType());
            ps.setString(2, n.getMessage());
            if (n.getUserId() == null) { ps.setNull(3, Types.INTEGER); ps.setNull(4, Types.INTEGER); }
            else { ps.setInt(3, n.getUserId()); ps.setInt(4, n.getUserId()); }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return 0;
            }
        }
        return insert(n);
    }

    /** Returns notifications targeted at the given user plus system-wide ones. */
    public List<Notification> getForUser(int userId, boolean unreadOnly) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE (user_id IS NULL OR user_id=?)"
                + (unreadOnly ? " AND is_read=FALSE" : "")
                + " ORDER BY created_at DESC, notification_id DESC";
        List<Notification> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public void markRead(int notificationId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE notifications SET is_read=TRUE WHERE notification_id=?")) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        }
    }

    public void markAllRead(int userId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE notifications SET is_read=TRUE WHERE user_id IS NULL OR user_id=?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public void delete(int notificationId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM notifications WHERE notification_id=?")) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        }
    }

    private Notification map(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setNotificationId(rs.getInt("notification_id"));
        int uid = rs.getInt("user_id");
        n.setUserId(rs.wasNull() ? null : uid);
        n.setType(rs.getString("type"));
        n.setMessage(rs.getString("message"));
        n.setRead(rs.getBoolean("is_read"));
        Timestamp ts = rs.getTimestamp("created_at");
        n.setCreatedAt(ts == null ? null : ts.toLocalDateTime());
        return n;
    }
}
