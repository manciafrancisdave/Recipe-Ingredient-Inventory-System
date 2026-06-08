package com.recipeinventory.ui;

import com.recipeinventory.dao.RecipeDAO;
import com.recipeinventory.dao.TagDAO;
import com.recipeinventory.model.Recipe;
import com.recipeinventory.model.Tag;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TagManagerForm extends JPanel {

    private final TagDAO tagDAO = new TagDAO();
    private final RecipeDAO recipeDAO = new RecipeDAO();

    private final JTable tagTable = Ui.table();
    private final JTable recipeTagTable = Ui.table();
    private final JComboBox<String> recipePicker = new JComboBox<>();

    private List<Tag> allTags;
    private List<Recipe> allRecipes;

    public TagManagerForm() {
        super(new BorderLayout(10, 10));
        setBackground(Ui.BACKGROUND);
        setBorder(Ui.pageBorder());

        add(buildTagSection(), BorderLayout.NORTH);
        add(buildAssignSection(), BorderLayout.CENTER);

        loadTags();
        loadRecipes();
    }

    private JPanel buildTagSection() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBackground(Ui.BACKGROUND);
        panel.setBorder(BorderFactory.createTitledBorder("All Tags"));

        JScrollPane scroll = new JScrollPane(tagTable);
        scroll.setPreferredSize(new Dimension(0, 160));
        panel.add(scroll, BorderLayout.CENTER);

        JButton addBtn = Ui.primaryButton("Add Tag");
        JButton deleteBtn = Ui.dangerButton("Delete Tag");
        addBtn.addActionListener(e -> addTag());
        deleteBtn.addActionListener(e -> deleteTag());

        JPanel toolbar = Ui.toolbar();
        toolbar.add(addBtn);
        toolbar.add(deleteBtn);
        panel.add(toolbar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildAssignSection() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBackground(Ui.BACKGROUND);
        panel.setBorder(BorderFactory.createTitledBorder("Assign Tags to Recipe"));

        JPanel top = Ui.toolbar();
        top.add(Ui.fieldLabel("Recipe"));
        top.add(recipePicker);
        JButton loadBtn = Ui.secondaryButton("Load Tags");
        top.add(loadBtn);
        panel.add(top, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(recipeTagTable);
        panel.add(scroll, BorderLayout.CENTER);

        JButton assignBtn = Ui.primaryButton("Assign Tag");
        JButton removeBtn = Ui.dangerButton("Remove Tag");
        assignBtn.addActionListener(e -> assignTag());
        removeBtn.addActionListener(e -> removeTag());
        loadBtn.addActionListener(e -> loadRecipeTags());

        JPanel toolbar = Ui.toolbar();
        toolbar.add(assignBtn);
        toolbar.add(removeBtn);
        panel.add(toolbar, BorderLayout.SOUTH);
        return panel;
    }

    private void loadTags() {
        try {
            allTags = tagDAO.getAll();
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Color"}, 0);
            for (Tag t : allTags) model.addRow(new Object[]{t.getTagId(), t.getName(), t.getColor()});
            tagTable.setModel(model);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void loadRecipes() {
        try {
            allRecipes = recipeDAO.getAll();
            recipePicker.removeAllItems();
            for (Recipe r : allRecipes) recipePicker.addItem(r.getRecipeId() + " - " + r.getTitle());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void loadRecipeTags() {
        int idx = recipePicker.getSelectedIndex();
        if (idx < 0) return;
        try {
            int recipeId = allRecipes.get(idx).getRecipeId();
            List<Tag> tags = tagDAO.getByRecipe(recipeId);
            DefaultTableModel model = new DefaultTableModel(new Object[]{"Tag ID", "Name", "Color"}, 0);
            for (Tag t : tags) model.addRow(new Object[]{t.getTagId(), t.getName(), t.getColor()});
            recipeTagTable.setModel(model);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void addTag() {
        JTextField name = Ui.searchField(16);
        JTextField color = Ui.searchField(10);
        color.setText("#117E74");
        Object[] form = {"Tag Name", name, "Color (hex)", color};
        if (JOptionPane.showConfirmDialog(this, form, "Add Tag", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        if (name.getText().isBlank()) { Ui.info(this, "Tag name cannot be empty."); return; }
        try {
            Tag tag = new Tag();
            tag.setName(name.getText().trim());
            tag.setColor(color.getText().trim());
            tagDAO.insert(tag);
            loadTags();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void deleteTag() {
        int row = tagTable.getSelectedRow();
        if (row < 0) { Ui.info(this, "Select a tag to delete."); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete selected tag?") != JOptionPane.OK_OPTION) return;
        try {
            int tagId = (int) tagTable.getValueAt(tagTable.convertRowIndexToModel(row), 0);
            tagDAO.delete(tagId);
            loadTags();
            loadRecipeTags();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void assignTag() {
        int recipeIdx = recipePicker.getSelectedIndex();
        if (recipeIdx < 0) { Ui.info(this, "Select a recipe first."); return; }
        if (allTags == null || allTags.isEmpty()) { Ui.info(this, "No tags available. Add a tag first."); return; }
        JComboBox<String> tagPicker = new JComboBox<>();
        for (Tag t : allTags) tagPicker.addItem(t.getTagId() + " - " + t.getName());
        if (JOptionPane.showConfirmDialog(this, new Object[]{"Tag", tagPicker}, "Assign Tag", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            int recipeId = allRecipes.get(recipeIdx).getRecipeId();
            int tagId = allTags.get(tagPicker.getSelectedIndex()).getTagId();
            tagDAO.addToRecipe(recipeId, tagId);
            loadRecipeTags();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void removeTag() {
        int recipeIdx = recipePicker.getSelectedIndex();
        int tagRow = recipeTagTable.getSelectedRow();
        if (recipeIdx < 0 || tagRow < 0) { Ui.info(this, "Select a recipe and a tag row to remove."); return; }
        try {
            int recipeId = allRecipes.get(recipeIdx).getRecipeId();
            int tagId = (int) recipeTagTable.getValueAt(recipeTagTable.convertRowIndexToModel(tagRow), 0);
            tagDAO.removeFromRecipe(recipeId, tagId);
            loadRecipeTags();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
