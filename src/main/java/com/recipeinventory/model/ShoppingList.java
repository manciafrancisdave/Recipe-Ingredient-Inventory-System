package com.recipeinventory.model;

import java.time.LocalDate;

public class ShoppingList {
    private int listId;
    private int userId;
    private LocalDate createdDate;
    private String status;

    public int getListId() { return listId; }
    public void setListId(int listId) { this.listId = listId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
