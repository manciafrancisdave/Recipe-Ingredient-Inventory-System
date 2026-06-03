package com.recipeinventory.model;

import java.math.BigDecimal;

public class ShoppingListItem {
    private int itemId;
    private int shoppingListId;
    private int ingredientId;
    private String ingredientName;
    private BigDecimal quantity = BigDecimal.ONE;
    private boolean purchased;

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getShoppingListId() { return shoppingListId; }
    public void setShoppingListId(int shoppingListId) { this.shoppingListId = shoppingListId; }
    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }
    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public boolean isPurchased() { return purchased; }
    public void setPurchased(boolean purchased) { this.purchased = purchased; }
}
