package com.recipeinventory.ui;

import com.recipeinventory.model.Role;
import com.recipeinventory.service.AuthService;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterForm extends JPanel {
    public RegisterForm(LoginForm frame, AuthService authService) {
        super(new GridBagLayout());
        setBackground(Ui.BACKGROUND);
        JPanel form = Ui.card();
        form.setLayout(new GridBagLayout());
        form.setBorder(javax.swing.BorderFactory.createEmptyBorder(36, 44, 36, 44));
        JTextField username = Ui.searchField(22);
        JTextField email = Ui.searchField(22);
        JPasswordField password = Ui.passwordField(22);
        JButton back = Ui.secondaryButton("Back");
        JButton register = Ui.primaryButton("Create Account");

        GridBagConstraints full = Ui.gbc(0, 0);
        full.gridwidth = 2;
        full.insets = new java.awt.Insets(8, 6, 2, 6);
        form.add(Ui.title("Create Account"), full);

        full = Ui.gbc(0, 1);
        full.gridwidth = 2;
        full.insets = new java.awt.Insets(0, 6, 22, 6);
        form.add(Ui.subtitle("New accounts are created as USER only."), full);

        form.add(Ui.fieldLabel("Username"), Ui.gbc(0, 2));
        form.add(username, Ui.gbc(1, 2));
        form.add(Ui.fieldLabel("Email"), Ui.gbc(0, 3));
        form.add(email, Ui.gbc(1, 3));
        form.add(Ui.fieldLabel("Password"), Ui.gbc(0, 4));
        form.add(password, Ui.gbc(1, 4));
        form.add(back, Ui.gbc(0, 5));
        form.add(register, Ui.gbc(1, 5));
        add(form);

        back.addActionListener(e -> frame.showLogin());
        register.addActionListener(e -> {
            try {
                authService.register(username.getText(), email.getText(), new String(password.getPassword()), Role.USER);
                Ui.info(this, "Account created. You can login now.");
                frame.showLogin();
            } catch (Exception ex) {
                Ui.error(this, ex);
            }
        });
    }
}
