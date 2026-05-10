package com.recipeinventory.ui;

import com.recipeinventory.model.User;
import com.recipeinventory.service.AuthService;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {
    public LoginPanel(LoginForm frame, AuthService authService) {
        super(new GridBagLayout());
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));
        JTextField email = new JTextField("admin@gmail.com", 22);
        JPasswordField password = new JPasswordField("admin123", 22);
        JButton login = Ui.primaryButton("Login");
        JButton register = new JButton("Register");

        form.add(Ui.title("Recipe & Ingredient Inventory"), Ui.gbc(0, 0));
        form.add(new JLabel("Email"), Ui.gbc(0, 1));
        form.add(email, Ui.gbc(1, 1));
        form.add(new JLabel("Password"), Ui.gbc(0, 2));
        form.add(password, Ui.gbc(1, 2));
        form.add(register, Ui.gbc(0, 3));
        form.add(login, Ui.gbc(1, 3));
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
