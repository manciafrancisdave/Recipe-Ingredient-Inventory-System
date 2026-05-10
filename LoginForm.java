package com.recipeinventory.service;

import com.recipeinventory.dao.InventoryDAO;
import com.recipeinventory.model.InventoryItem;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class InventoryService {
    private final InventoryDAO inventoryDAO = new InventoryDAO();

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
    }

    public void deductStock(int ingredientId, BigDecimal qty) throws SQLException {
        requireQuantity(qty);
        inventoryDAO.deduct(ingredientId, qty);
    }

    private void requireQuantity(BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
    }
}
