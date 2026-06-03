package com.recipeinventory.model;

import java.time.LocalDateTime;

public class Admin extends User {
    private int adminLevel = 1;

    public Admin() {
        setRole(Role.ADMIN);
    }

    public Admin(int userId, String username, String email, String password, LocalDateTime createdAt, int adminLevel) {
        super(userId, username, email, password, Role.ADMIN, createdAt);
        this.adminLevel = adminLevel;
    }

    public int getAdminLevel() { return adminLevel; }
    public void setAdminLevel(int adminLevel) { this.adminLevel = adminLevel; }
}
