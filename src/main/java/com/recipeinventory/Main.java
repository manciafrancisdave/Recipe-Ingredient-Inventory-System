package com.recipeinventory;

import com.recipeinventory.ui.LoginForm;
import com.recipeinventory.ui.Ui;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            Ui.installTheme();
            new LoginForm().setVisible(true);
        });
    }
}
