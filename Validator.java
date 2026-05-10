package com.recipeinventory.ui;

import com.recipeinventory.model.Role;
import com.recipeinventory.service.AuthService;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterForm extends JPanel {
    public RegisterForm(LoginForm frame, AuthService authService) {
        super(new GridBagLayout());
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));
        JTextField username = new JTextField(22);
        JTextField email = new JTextField(22);
        JPasswordField password = new JPasswordField(22);
        JButton back = new JButton("Back");
        JButton register = Ui.primaryButton("Create Account");

        form.add(Ui.title("Create Account"), Ui.gbc(0, 0));
        form.add(new JLabel("Username"), Ui.gbc(0, 1));
        form.add(username, Ui.gbc(1, 1));
        form.add(new JLabel("Email"), Ui.gbc(0, 2));
        form.add(email, Ui.gbc(1, 2));
        form.add(new JLabel("Password"), Ui.gbc(0, 3));
        form.add(password, Ui.gbc(1, 3));
        form.add(back, Ui.gbc(0, 4));
        form.add(register, Ui.gbc(1, 4));
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
