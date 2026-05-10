package com.recipeinventory.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InventoryItem {
    private int itemId;
    private int ingredientId;
    private String ingredientName;
    private String category;
    private String unit;
    private BigDecimal stockQty = BigDecimal.ZERO;
    private BigDecimal threshold = BigDecimal.ZERO;
    private LocalDate expiryDate;
    private String location;

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }
    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getStockQty() { return stockQty; }
    public void setStockQty(BigDecimal stockQty) { this.stockQty = stockQty; }
    public BigDecimal getThreshold() { return threshold; }
    public void setThreshold(BigDecimal threshold) { this.threshold = threshold; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public boolean isLowStock() { return stockQty.compareTo(threshold) < 0; }
    public boolean isExpired() { return expiryDate != null && expiryDate.isBefore(LocalDate.now()); }
}
