package com.recipeinventory.service;

import com.recipeinventory.dao.RecipeDAO;
import com.recipeinventory.dao.ShoppingListDAO;
import com.recipeinventory.dao.InventoryDAO;
import com.recipeinventory.model.InventoryItem;
import com.recipeinventory.model.RecipeIngredient;
import com.recipeinventory.model.ShoppingList;
import com.recipeinventory.model.ShoppingListItem;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ShoppingListService {
    private final ShoppingListDAO shoppingListDAO = new ShoppingListDAO();
    private final RecipeDAO recipeDAO = new RecipeDAO();
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    public int generateFromRecipe(int userId, int recipeId) throws SQLException {
        ShoppingList list = new ShoppingList();
        list.setUserId(userId);
        list.setCreatedDate(LocalDate.now());
        list.setStatus("OPEN");
        int listId = shoppingListDAO.insert(list);
        for (RecipeIngredient needed : recipeDAO.getIngredients(recipeId)) {
            InventoryItem inventory = inventoryDAO.getByIngredientId(needed.getIngredientId());
            BigDecimal available = inventory == null ? BigDecimal.ZERO : inventory.getStockQty();
            if (available.compareTo(needed.getQuantity()) < 0) {
                ShoppingListItem item = new ShoppingListItem();
                item.setShoppingListId(listId);
                item.setIngredientId(needed.getIngredientId());
                item.setQuantity(needed.getQuantity().subtract(available));
                shoppingListDAO.insertItem(item);
            }
        }
        return listId;
    }

    public void addItem(ShoppingListItem item) throws SQLException {
        shoppingListDAO.insertItem(item);
    }

    public void removeItem(int itemId) throws SQLException {
        shoppingListDAO.deleteItem(itemId);
    }

    public void markComplete(int listId) throws SQLException {
        shoppingListDAO.markComplete(listId);
    }

    public List<ShoppingList> getLists(String keyword) throws SQLException {
        return shoppingListDAO.search(keyword == null ? "" : keyword);
    }

    public List<ShoppingListItem> getItems(int listId) throws SQLException {
        return shoppingListDAO.getItems(listId);
    }
}
