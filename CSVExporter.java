package com.recipeinventory.ui;

import com.recipeinventory.service.AuthService;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LoginForm extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final AuthService authService = new AuthService();

    public LoginForm() {
        super("Recipe & Ingredient Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);
        root.setLayout(cards);
        root.add(new LoginPanel(this, authService), "login");
        root.add(new RegisterForm(this, authService), "register");
        add(root, BorderLayout.CENTER);
    }

    void showRegister() {
        cards.show(root, "register");
    }

    void showLogin() {
        cards.show(root, "login");
    }

    JPanel centeredPanel() {
        return new JPanel(new GridBagLayout());
    }
}
