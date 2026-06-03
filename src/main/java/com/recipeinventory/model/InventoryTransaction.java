package com.recipeinventory.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable-style record of a single stock movement against an ingredient.
 *
 * <p>Every restock, deduction or cook operation appends one row so the system
 * keeps a full audit trail of inventory changes (functional requirement 11:
 * "Store transaction history").</p>
 */
public class InventoryTransaction {
    /** Stock was added (manual restock / purchase). */
    public static final String RESTOCK = "RESTOCK";
    /** Stock was manually removed. */
    public static final String DEDUCT = "DEDUCT";
    /** Stock was consumed automatically by cooking a recipe. */
    public static final String COOK = "COOK";

    private int transactionId;
    private int ingredientId;
    private String ingredientName;
    private Integer userId;
    private String username;
    private String changeType;
    private BigDecimal quantity = BigDecimal.ZERO;
    private String note;
    private LocalDateTime createdAt;

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }
    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
