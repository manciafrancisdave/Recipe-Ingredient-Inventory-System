package com.recipeinventory.service;

import com.recipeinventory.dao.UserDAO;
import com.recipeinventory.model.Role;
import com.recipeinventory.model.User;
import com.recipeinventory.util.PasswordHasher;
import com.recipeinventory.util.SessionManager;
import com.recipeinventory.util.Validator;
import java.sql.SQLException;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User login(String email, String password) throws SQLException {
        Validator.requireEmail(email);
        Validator.requireText(password, "Password");
        User user = userDAO.findByEmail(email);
        if (user == null || !PasswordHasher.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        SessionManager.login(user);
        return user;
    }

    public int register(String username, String email, String password, Role role) throws SQLException {
        Validator.requireText(username, "Username");
        Validator.requireEmail(email);
        Validator.requireText(password, "Password");
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must contain at least 6 characters.");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PasswordHasher.hash(password));
        user.setRole(role == null ? Role.USER : role);
        return userDAO.insert(user);
    }

    public void logout() {
        SessionManager.logout();
    }
}
