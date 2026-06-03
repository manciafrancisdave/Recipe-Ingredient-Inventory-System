package com.recipeinventory.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public final class Ui {
    static final Color BACKGROUND = new Color(245, 247, 250);
    static final Color SURFACE = Color.WHITE;
    static final Color HEADER = new Color(25, 35, 48);
    static final Color ACCENT = new Color(17, 126, 116);
    static final Color ACCENT_DARK = new Color(13, 94, 87);
    static final Color ACCENT_SOFT = new Color(221, 244, 241);
    static final Color BORDER = new Color(219, 226, 232);
    static final Color TEXT_MUTED = new Color(93, 106, 120);
    static final Color WARNING = new Color(196, 122, 0);
    static final Color DANGER = new Color(190, 65, 65);

    private static final Font UI_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font UI_FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    private Ui() {
    }

    public static void installTheme() {
        UIManager.put("Label.font", UI_FONT);
        UIManager.put("Button.font", UI_FONT_BOLD);
        UIManager.put("TextField.font", UI_FONT);
        UIManager.put("PasswordField.font", UI_FONT);
        UIManager.put("TextArea.font", UI_FONT);
        UIManager.put("Table.font", UI_FONT);
        UIManager.put("TableHeader.font", UI_FONT_BOLD);
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("TabbedPane.font", UI_FONT_BOLD);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabInsets", new Insets(12, 18, 12, 18));
    }

    static JButton primaryButton(String text) {
        return new ModernButton(text, ACCENT, new Color(20, 145, 134), ACCENT_DARK, Color.WHITE);
    }

    static JButton secondaryButton(String text) {
        return new ModernButton(text, SURFACE, new Color(238, 246, 245), BORDER, HEADER);
    }

    static JButton dangerButton(String text) {
        return new ModernButton(text, DANGER, new Color(211, 79, 79), new Color(162, 48, 48), Color.WHITE);
    }

    static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UI_FONT.deriveFont(Font.BOLD, 28f));
        label.setForeground(HEADER);
        return label;
    }

    static JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UI_FONT.deriveFont(Font.BOLD, 16f));
        label.setForeground(HEADER);
        return label;
    }

    static JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UI_FONT);
        label.setForeground(TEXT_MUTED);
        return label;
    }

    static JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UI_FONT_BOLD);
        label.setForeground(TEXT_MUTED);
        return label;
    }

    static JTable table() {
        JTable table = new JTable();
        table.setRowHeight(36);
        table.setFont(UI_FONT);
        table.setForeground(HEADER);
        table.setBackground(SURFACE);
        table.setGridColor(new Color(238, 242, 245));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(ACCENT_SOFT);
        table.setSelectionForeground(HEADER);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, selected, false, row, column);
                if (!selected) {
                    c.setBackground(row % 2 == 0 ? SURFACE : new Color(249, 251, 253));
                }
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return c;
            }
        });
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        table.getTableHeader().setBackground(new Color(238, 243, 247));
        table.getTableHeader().setForeground(HEADER);
        table.getTableHeader().setBorder(new MatteLineBorder(BORDER, 0, 0, 1, 0));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, selected, focus, row, column);
                label.setFont(UI_FONT_BOLD);
                label.setForeground(HEADER);
                label.setBackground(new Color(238, 243, 247));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBorder(new EmptyBorder(0, 12, 0, 12));
                return label;
            }
        });
        return table;
    }

    static JPanel page() {
        JPanel panel = new JPanel(new java.awt.BorderLayout(12, 12));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(14, 16, 16, 16));
        return panel;
    }

    static JPanel toolbar() {
        JPanel panel = new RoundedPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 8), SURFACE, BORDER, 14);
        panel.setBorder(new EmptyBorder(8, 10, 8, 10));
        return panel;
    }

    static JPanel card() {
        JPanel panel = new RoundedPanel(new java.awt.BorderLayout(), SURFACE, BORDER, 18);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        return panel;
    }

    static JTextField searchField(int columns) {
        JTextField field = new JTextField(columns);
        styleTextComponent(field);
        return field;
    }

    static JPasswordField passwordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        styleTextComponent(field);
        return field;
    }

    static JTextArea textArea() {
        JTextArea area = new JTextArea();
        area.setFont(UI_FONT);
        area.setForeground(HEADER);
        area.setBackground(SURFACE);
        area.setCaretColor(ACCENT_DARK);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(12, 12, 12, 12));
        return area;
    }

    static JScrollPane scrollPane(Component component) {
        JScrollPane pane = new JScrollPane(component);
        pane.setBorder(BorderFactory.createEmptyBorder());
        pane.getViewport().setBackground(SURFACE);
        return pane;
    }

    static JTabbedPane tabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BACKGROUND);
        tabs.setForeground(HEADER);
        tabs.setFont(UI_FONT_BOLD);
        tabs.setBorder(BorderFactory.createEmptyBorder());
        return tabs;
    }

    static Border pageBorder() {
        return new EmptyBorder(18, 18, 18, 18);
    }

    static JPanel appHeader(String title, String subtitle, Runnable logoutAction) {
        JPanel header = new JPanel(new java.awt.BorderLayout(16, 0));
        header.setBackground(HEADER);
        header.setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
        text.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UI_FONT.deriveFont(Font.BOLD, 22f));
        titleLabel.setForeground(Color.WHITE);
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(UI_FONT);
        subtitleLabel.setForeground(new Color(202, 211, 221));
        text.add(titleLabel);
        text.add(subtitleLabel);

        JButton logout = secondaryButton("Logout");
        logout.addActionListener(e -> logoutAction.run());
        header.add(text, java.awt.BorderLayout.WEST);
        header.add(logout, java.awt.BorderLayout.EAST);
        return header;
    }

    static void styleTextComponent(JTextField field) {
        field.setFont(UI_FONT);
        field.setForeground(HEADER);
        field.setCaretColor(ACCENT_DARK);
        field.setBackground(SURFACE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(BORDER, 12),
                new EmptyBorder(9, 11, 9, 11)));
    }

    static GridBagConstraints gbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = x == 1 ? 1 : 0;
        return gbc;
    }

    static void error(Component parent, Exception ex) {
        JOptionPane.showMessageDialog(parent, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message);
    }

    private static final class ModernButton extends JButton {
        private static final long serialVersionUID = 1L;
        private final Color base;
        private final Color hover;
        private final Color stroke;
        private boolean hovered;

        private ModernButton(String text, Color base, Color hover, Color stroke, Color foreground) {
            super(text);
            this.base = base;
            this.hover = hover;
            this.stroke = stroke;
            setFont(UI_FONT_BOLD);
            setForeground(foreground);
            setBorder(new EmptyBorder(9, 16, 9, 16));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fill = getModel().isPressed() ? stroke : hovered ? hover : base;
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.setColor(stroke);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static final class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final Color fill;
        private final Color stroke;
        private final int radius;

        private RoundedPanel(LayoutManager layout, Color fill, Color stroke, int radius) {
            super(layout);
            this.fill = fill;
            this.stroke = stroke;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.setColor(stroke);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static final class RoundedBorder extends AbstractBorder {
        private static final long serialVersionUID = 1L;
        private final Color color;
        private final int radius;

        private RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    private static final class MatteLineBorder extends AbstractBorder {
        private static final long serialVersionUID = 1L;
        private final Color color;
        private final int top;
        private final int left;
        private final int bottom;
        private final int right;

        private MatteLineBorder(Color color, int top, int left, int bottom, int right) {
            this.color = color;
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(top, left, bottom, right);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = top;
            insets.left = left;
            insets.bottom = bottom;
            insets.right = right;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            if (top > 0) {
                g.fillRect(x, y, width, top);
            }
            if (left > 0) {
                g.fillRect(x, y, left, height);
            }
            if (bottom > 0) {
                g.fillRect(x, y + height - bottom, width, bottom);
            }
            if (right > 0) {
                g.fillRect(x + width - right, y, right, height);
            }
        }
    }
}
