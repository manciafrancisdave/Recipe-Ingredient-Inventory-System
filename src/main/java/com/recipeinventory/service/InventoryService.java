package com.recipeinventory.service;

import com.recipeinventory.dao.InventoryDAO;
import com.recipeinventory.dao.InventoryTransactionDAO;
import com.recipeinventory.dao.NotificationDAO;
import com.recipeinventory.model.InventoryItem;
import com.recipeinventory.model.InventoryTransaction;
import com.recipeinventory.model.Notification;
import com.recipeinventory.model.User;
import com.recipeinventory.util.SessionManager;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class InventoryService {
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final InventoryTransactionDAO transactionDAO = new InventoryTransactionDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    public List<InventoryItem> dashboard(String keyword) throws SQLException {
        return inventoryDAO.search(keyword == null ? "" : keyword);
    }

    public List<InventoryItem> lowStockAlerts() throws SQLException {
        return inventoryDAO.getLowStock();
    }

    public List<InventoryItem> expiredItems() throws SQLException {
        return inventoryDAO.getExpired();
    }

    public void restock(int ingredientId, BigDecimal qty) throws SQLException {
        requireQuantity(qty);
        inventoryDAO.restock(ingredientId, qty);
        transactionDAO.insert(ingredientId, currentUserId(), InventoryTransaction.RESTOCK, qty, "Manual restock");
    }

    public void deductStock(int ingredientId, BigDecimal qty) throws SQLException {
        requireQuantity(qty);
        inventoryDAO.deduct(ingredientId, qty);
        transactionDAO.insert(ingredientId, currentUserId(), InventoryTransaction.DEDUCT, qty, "Manual deduction");
        refreshLowStockAlerts();
    }

    /** Full inventory movement history, newest first. */
    public List<InventoryTransaction> transactionHistory() throws SQLException {
        return transactionDAO.getAll();
    }

    /**
     * Re-scans inventory and persists a notification for every item at or below
     * its threshold and for every expired item, de-duplicating against existing
     * unread alerts. Safe to call after any stock change.
     *
     * @return number of new notifications created
     */
    public int refreshLowStockAlerts() throws SQLException {
        int created = 0;
        for (InventoryItem item : inventoryDAO.getLowStock()) {
            String msg = "Low stock: " + item.getIngredientName() + " (" + item.getStockQty()
                    + " " + item.getUnit() + " left, threshold " + item.getThreshold() + ")";
            created += addSystemAlert(Notification.LOW_STOCK, msg);
        }
        for (InventoryItem item : inventoryDAO.getExpired()) {
            String msg = "Expired: " + item.getIngredientName() + " on " + item.getExpiryDate();
            created += addSystemAlert(Notification.EXPIRY, msg);
        }
        return created;
    }

    public List<Notification> notifications(int userId, boolean unreadOnly) throws SQLException {
        return notificationDAO.getForUser(userId, unreadOnly);
    }

    public void markNotificationRead(int notificationId) throws SQLException {
        notificationDAO.markRead(notificationId);
    }

    public void markAllNotificationsRead(int userId) throws SQLException {
        notificationDAO.markAllRead(userId);
    }

    private int addSystemAlert(String type, String message) throws SQLException {
        Notification n = new Notification();
        n.setUserId(null); // system-wide alert, visible to everyone
        n.setType(type);
        n.setMessage(message);
        return notificationDAO.insertIfAbsent(n) > 0 ? 1 : 0;
    }

    private Integer currentUserId() {
        User user = SessionManager.getCurrentUser();
        return user == null ? null : user.getUserId();
    }

    private void requireQuantity(BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
    }
}
