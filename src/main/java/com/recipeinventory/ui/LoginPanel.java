package com.recipeinventory.ui;

import com.recipeinventory.model.User;
import com.recipeinventory.service.AuthService;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {
    public LoginPanel(LoginForm frame, AuthService authService) {
        super(new GridBagLayout());
        setBackground(Ui.BACKGROUND);
        JPanel form = Ui.card();
        form.setLayout(new GridBagLayout());
        form.setBorder(javax.swing.BorderFactory.createEmptyBorder(36, 44, 36, 44));
        JTextField email = Ui.searchField(24);
        JPasswordField password = Ui.passwordField(22);
        JButton login = Ui.primaryButton("Login");
        JButton register = Ui.secondaryButton("Register");

        GridBagConstraints full = Ui.gbc(0, 0);
        full.gridwidth = 2;
        full.insets = new java.awt.Insets(8, 6, 2, 6);
        form.add(Ui.title("Recipe Inventory"), full);

        full = Ui.gbc(0, 1);
        full.gridwidth = 2;
        full.insets = new java.awt.Insets(0, 6, 22, 6);
        form.add(Ui.subtitle("Sign in to your kitchen workspace"), full);

        JLabel emailLabel = Ui.fieldLabel("Email or Username");
        JLabel passwordLabel = Ui.fieldLabel("Password");
        form.add(emailLabel, Ui.gbc(0, 2));
        form.add(email, Ui.gbc(1, 2));
        form.add(passwordLabel, Ui.gbc(0, 3));
        form.add(password, Ui.gbc(1, 3));
        form.add(register, Ui.gbc(0, 4));
        form.add(login, Ui.gbc(1, 4));
        add(form);

        login.addActionListener(e -> {
            try {
                User user = authService.login(email.getText(), new String(password.getPassword()));
                frame.dispose();
                if (user.isAdmin()) {
                    new AdminDashboardForm().setVisible(true);
                } else {
                    new DashboardForm().setVisible(true);
                }
            } catch (Exception ex) {
                Ui.error(this, ex);
            }
        });
        register.addActionListener(e -> frame.showRegister());
    }
}
