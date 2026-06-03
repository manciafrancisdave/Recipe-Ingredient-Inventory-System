package com.recipeinventory.ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.recipeinventory.model.Recipe;
import com.recipeinventory.service.RecipeService;
import com.recipeinventory.util.SessionManager;

public class FavoritesForm extends JPanel {
    private final RecipeService service = new RecipeService();
    private final JTable table = Ui.table();

    public FavoritesForm() {
        super(new BorderLayout(10, 10));
        setBackground(Ui.BACKGROUND);
        setBorder(Ui.pageBorder());
        JPanel top = Ui.toolbar();
        JButton refresh = Ui.primaryButton("Refresh");
        top.add(refresh);
        add(top, BorderLayout.NORTH);
        JPanel card = Ui.card();
        card.add(Ui.scrollPane(table));
        add(card, BorderLayout.CENTER);
        refresh.addActionListener(e -> load());
        load();
    }

    private void load() {
        try {
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Title", "Cuisine", "Difficulty", "Rating"}, 0);
            for (Recipe r : service.favorites(SessionManager.getCurrentUser().getUserId())) {
                model.addRow(new Object[]{r.getRecipeId(), r.getTitle(), r.getCuisine(), r.getDifficulty(), r.getAverageRating()});
            }
            table.setModel(model);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
