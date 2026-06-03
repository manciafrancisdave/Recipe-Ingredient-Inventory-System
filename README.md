# Recipe & Ingredient Inventory Management System

Desktop-based Java Swing application using MVC, JDBC, and MySQL localhost. It can be run with plain Java; NetBeans is optional.

## Technology Stack

- Java SE 17
- Java command line, VS Code, IntelliJ IDEA, Eclipse, or NetBeans
- Java Swing GUI
- MySQL Localhost
- JDBC PreparedStatement
- Maven
- Built-in PDF and CSV export utilities

## Main Modules

- Authentication: register, login, logout, session handling, role-based access
- User: profile records, favorites, user role
- Admin: user management, recipe deletion, inventory view, reports
- Recipe: add, edit, delete, search, filter, scale, cookability, image path support
- Recipe Ingredient: recipe-to-ingredient quantities
- Ingredient: database-backed ingredients
- Inventory: stock tracking, restock, deduct, low stock, expired detection
- Inventory Transactions: full audit history of every restock, deduct, and cook deduction
- Notifications: persisted low-stock and expiry alerts, scannable and markable as read
- Cooking Steps: stored in `cooking_steps`
- Shopping Lists: generate missing ingredients from recipes, complete lists
- Meal Planning: weekly plans with breakfast, lunch, dinner, snack
- Tags: recipe tags and colors
- Ratings: score and review comments
- Reports: totals, most cooked, low stock, inventory cost, user activity, popularity

## Run Without NetBeans

This folder already contains a runnable jar:

```text
target/RecipeInventoryMVCMySQL-1.0.0.jar
```

From PowerShell in this project folder, run:

```powershell
.\run-app.ps1
```

Or double-click/run:

```bat
run-app.bat
```

You can also run the jar directly:

```powershell
java -jar target\RecipeInventoryMVCMySQL-1.0.0.jar
```

If you edit source code, rebuild from PowerShell:

```powershell
.\build-app.ps1
```

Then run again:

```powershell
.\run-app.ps1
```

If Maven is installed, `build-app.ps1` uses `mvn clean package`. If Maven is not installed, it uses the local Java 17 JDK tools and updates the existing runnable jar in `target`.

## Database Setup

1. Open MySQL Workbench, phpMyAdmin, or the MySQL command line.
2. Run:

```sql
SOURCE C:/Users/franc/Downloads/RecipeInventoryMVCMySQL-gui-builder-source/database/schema.sql;
```

Or open `database/schema.sql` and execute the whole file.

From the MySQL command line, you can also run:

```powershell
cmd /c "mysql -u root < database\schema.sql"
```

If your root account has a password:

```powershell
cmd /c "mysql -u root -p < database\schema.sql"
```

To add the expanded recipe catalog without resetting your database, run:

```powershell
cmd /c "mysql -u root recipe_management_system < database\add_more_dishes.sql"
```

If your root account has a password:

```powershell
cmd /c "mysql -u root -p recipe_management_system < database\add_more_dishes.sql"
```

Or open `database/add_more_dishes.sql` in MySQL Workbench/phpMyAdmin and execute the whole file.

Default database:

```text
recipe_management_system
```

Default MySQL connection:

```java
jdbc:mysql://localhost:3306/recipe_management_system
user: root
password: empty
```

If your MySQL root password is not empty, you do not need to edit Java source. Run:

```powershell
.\run-app.ps1 -DbPassword "your_mysql_password"
```

For a different database URL or user:

```powershell
.\run-app.ps1 -DbUrl "jdbc:mysql://localhost:3306/recipe_management_system?useSSL=false&serverTimezone=UTC" -DbUser "root" -DbPassword "your_mysql_password"
```

You can also use environment variables:

```powershell
$env:RECIPE_DB_URL = "jdbc:mysql://localhost:3306/recipe_management_system?useSSL=false&serverTimezone=UTC"
$env:RECIPE_DB_USER = "root"
$env:RECIPE_DB_PASSWORD = "your_mysql_password"
.\run-app.ps1
```

## Sample Accounts

Admin:

```text
Email: admin@gmail.com
Password: admin123
```

Sample users:

```text
maria@example.com / password
juan@example.com / password
```

## Optional NetBeans Run Guide

1. Install NetBeans IDE.
2. Open NetBeans.
3. Choose **File > Open Project**.
4. Select:

```text
C:\Users\franc\Downloads\RecipeInventoryMVCMySQL-gui-builder-source
```

5. Wait for Maven dependencies to download.
6. Right-click the project and choose **Run**.

## Required Libraries

Maven downloads this automatically:

- MySQL Connector/J

## Project Architecture

```text
com.recipeinventory.model    Entity/model classes
com.recipeinventory.dao      JDBC DAO classes
com.recipeinventory.service  Business logic classes
com.recipeinventory.ui       Swing forms
com.recipeinventory.util     Database, session, validation, export utilities
```

## Screenshots

Place screenshots in the `screenshots` folder after running the app.
