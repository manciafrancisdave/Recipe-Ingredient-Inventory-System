package com.recipeinventory.ui;

import com.recipeinventory.model.InventoryItem;
import com.recipeinventory.service.InventoryService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class InventoryForm extends JPanel {
    private final InventoryService service = new InventoryService();
    private final JTable table = Ui.table();
    private final JTextField search = new JTextField(20);

    public InventoryForm(boolean admin) {
        super(new BorderLayout(10, 10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Search");
        JButton restock = Ui.primaryButton("Restock");
        JButton deduct = new JButton("Deduct");
        JButton low = new JButton("Low Stock");
        JButton expired = new JButton("Expired");
        top.add(new JLabel("Search"));
        top.add(search);
        top.add(refresh);
        top.add(restock);
        top.add(deduct);
        top.add(low);
        top.add(expired);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        refresh.addActionListener(e -> load());
        restock.addActionListener(e -> changeStock(true));
        deduct.addActionListener(e -> changeStock(false));
        low.addActionListener(e -> loadLow());
        expired.addActionListener(e -> loadExpired());
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
