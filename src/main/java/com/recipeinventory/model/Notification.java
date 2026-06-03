package com.recipeinventory.model;

import java.time.LocalDateTime;

/**
 * A persisted alert shown to a user, e.g. low stock or expired ingredient
 * warnings (functional requirement 12: "Generate alerts").
 *
 * <p>A {@code null} {@link #getUserId() userId} denotes a system-wide alert
 * visible to every user.</p>
 */
public class Notification {
    /** Triggered when stock quantity falls to or below its threshold. */
    public static final String LOW_STOCK = "LOW_STOCK";
    /** Triggered when an inventory item has passed its expiry date. */
    public static final String EXPIRY = "EXPIRY";
    /** General system message. */
    public static final String SYSTEM = "SYSTEM";

    private int notificationId;
    private Integer userId;
    private String type = SYSTEM;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;

    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
