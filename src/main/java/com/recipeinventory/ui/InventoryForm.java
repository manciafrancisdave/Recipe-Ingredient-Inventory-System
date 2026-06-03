package com.recipeinventory.ui;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.recipeinventory.model.InventoryItem;
import com.recipeinventory.model.InventoryTransaction;
import com.recipeinventory.model.Notification;
import com.recipeinventory.service.InventoryService;
import com.recipeinventory.util.SessionManager;

public class InventoryForm extends JPanel {
    private final InventoryService service = new InventoryService();
    private final JTable table = Ui.table();
    private final JTextField search = Ui.searchField(20);

    public InventoryForm(boolean admin) {
        super(new BorderLayout(10, 10));
        setBackground(Ui.BACKGROUND);
        setBorder(Ui.pageBorder());
        JPanel top = Ui.toolbar();
        JButton refresh = Ui.secondaryButton("Search");
        JButton restock = Ui.primaryButton("Restock");
        JButton deduct = Ui.secondaryButton("Deduct");
        JButton low = Ui.secondaryButton("Low Stock");
        JButton expired = Ui.secondaryButton("Expired");
        JButton history = Ui.secondaryButton("History");
        JButton alerts = Ui.secondaryButton("Notifications");
        JButton scan = Ui.secondaryButton("Scan Alerts");
        top.add(Ui.fieldLabel("Search"));
        top.add(search);
        top.add(refresh);
        top.add(restock);
        top.add(deduct);
        top.add(low);
        top.add(expired);
        top.add(history);
        top.add(alerts);
        top.add(scan);
        add(top, BorderLayout.NORTH);
        JPanel card = Ui.card();
        card.add(Ui.scrollPane(table));
        add(card, BorderLayout.CENTER);
        refresh.addActionListener(e -> load());
        restock.addActionListener(e -> changeStock(true));
        deduct.addActionListener(e -> changeStock(false));
        low.addActionListener(e -> loadLow());
        expired.addActionListener(e -> loadExpired());
        history.addActionListener(e -> loadHistory());
        alerts.addActionListener(e -> loadNotifications());
        scan.addActionListener(e -> scanAlerts());
        load();
    }

    private void load() {
        try {
            fill(service.dashboard(search.getText()));
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void loadLow() {
        try {
            fill(service.lowStockAlerts());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void loadExpired() {
        try {
            fill(service.expiredItems());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void loadHistory() {
        try {
            DefaultTableModel model = new DefaultTableModel(new Object[]{"Txn", "When", "Ingredient", "Type", "Quantity", "User", "Note"}, 0);
            for (InventoryTransaction t : service.transactionHistory()) {
                model.addRow(new Object[]{t.getTransactionId(), t.getCreatedAt(), t.getIngredientName(), t.getChangeType(), t.getQuantity(), t.getUsername() == null ? "-" : t.getUsername(), t.getNote()});
            }
            table.setModel(model);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void loadNotifications() {
        try {
            int userId = SessionManager.getCurrentUser() == null ? 0 : SessionManager.getCurrentUser().getUserId();
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "When", "Type", "Message", "Read"}, 0);
            for (Notification n : service.notifications(userId, false)) {
                model.addRow(new Object[]{n.getNotificationId(), n.getCreatedAt(), n.getType(), n.getMessage(), n.isRead() ? "Yes" : "No"});
            }
            table.setModel(model);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void scanAlerts() {
        try {
            int created = service.refreshLowStockAlerts();
            Ui.info(this, created == 0 ? "No new alerts. Inventory looks healthy." : created + " new alert(s) generated.");
            loadNotifications();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void fill(java.util.List<InventoryItem> items) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Item", "Ingredient ID", "Name", "Category", "Stock", "Unit", "Threshold", "Expiry", "Location", "Status"}, 0);
        for (InventoryItem i : items) {
            model.addRow(new Object[]{i.getItemId(), i.getIngredientId(), i.getIngredientName(), i.getCategory(), i.getStockQty(), i.getUnit(), i.getThreshold(), i.getExpiryDate(), i.getLocation(), i.isExpired() ? "Expired" : i.isLowStock() ? "Low" : "OK"});
        }
        table.setModel(model);
    }

    private void changeStock(boolean restock) {
        if (table.getSelectedRow() < 0) {
            Ui.info(this, "Select an inventory item.");
            return;
        }
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        int ingredientId = Integer.parseInt(table.getValueAt(row, 1).toString());
        String qty = JOptionPane.showInputDialog(this, "Quantity");
        if (qty == null) return;
        try {
            if (restock) service.restock(ingredientId, new BigDecimal(qty));
            else service.deductStock(ingredientId, new BigDecimal(qty));
            load();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
