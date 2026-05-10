package com.recipeinventory.ui;

import com.recipeinventory.model.Recipe;
import com.recipeinventory.service.RecipeService;
import com.recipeinventory.util.SessionManager;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FavoritesForm extends JPanel {
    private final RecipeService service = new RecipeService();
    private final JTable table = Ui.table();

    public FavoritesForm() {
        super(new BorderLayout());
        JButton refresh = Ui.primaryButton("Refresh");
        add(refresh, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
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
