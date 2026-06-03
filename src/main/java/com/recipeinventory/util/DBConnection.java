package com.recipeinventory.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/recipe_management_system?useSSL=false&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private static final String URL = setting("recipe.db.url", "RECIPE_DB_URL", DEFAULT_URL);
    private static final String USER = setting("recipe.db.user", "RECIPE_DB_USER", DEFAULT_USER);
    private static final String PASSWORD = setting("recipe.db.password", "RECIPE_DB_PASSWORD", DEFAULT_PASSWORD);

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            throw new SQLException("""
                    Cannot connect to MySQL.

                    Please check:
                    1. MySQL Server is running.
                    2. MySQL is using port 3306.
                    3. Database recipe_management_system exists.
                    4. Username/password in DBConnection.java are correct.

                    Current settings:
                    URL: """ + URL + """

                    User: """ + USER + """

                    Original error: """ + ex.getMessage(), ex);
        }
    }

    private static String setting(String propertyName, String envName, String defaultValue) {
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        return defaultValue;
    }
}
