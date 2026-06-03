package com.recipeinventory.ui;

import com.recipeinventory.dao.UserDAO;
import com.recipeinventory.model.User;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class UserManagementForm extends JPanel {
    private final UserDAO userDAO = new UserDAO();
    private final JTable table = Ui.table();
    private final JTextField search = Ui.searchField(20);

    public UserManagementForm() {
        super(new BorderLayout(10, 10));
        setBackground(Ui.BACKGROUND);
        setBorder(Ui.pageBorder());
        JPanel top = Ui.toolbar();
        JButton refresh = Ui.secondaryButton("Search");
        JButton delete = Ui.dangerButton("Delete User");
        top.add(Ui.fieldLabel("Search"));
        top.add(search);
        top.add(refresh);
        top.add(delete);
        add(top, BorderLayout.NORTH);
        JPanel card = Ui.card();
        card.add(Ui.scrollPane(table));
        add(card, BorderLayout.CENTER);
        refresh.addActionListener(e -> load());
        delete.addActionListener(e -> delete());
        load();
    }

    private void load() {
        try {
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Username", "Email", "Role", "Created"}, 0);
            for (User user : userDAO.search(search.getText())) {
                model.addRow(new Object[]{user.getUserId(), user.getUsername(), user.getEmail(), user.getRole(), user.getCreatedAt()});
            }
            table.setModel(model);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void delete() {
        if (table.getSelectedRow() < 0) {
            Ui.info(this, "Select a user.");
            return;
        }
        try {
            int id = Integer.parseInt(table.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0).toString());
            userDAO.delete(id);
            load();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
