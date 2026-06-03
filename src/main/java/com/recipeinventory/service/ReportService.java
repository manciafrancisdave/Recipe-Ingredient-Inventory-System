package com.recipeinventory.service;

import com.recipeinventory.dao.ReportDAO;
import com.recipeinventory.util.CSVExporter;
import com.recipeinventory.util.PDFExporter;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportService {
    private final ReportDAO reportDAO = new ReportDAO();

    public String generateDashboardReport() throws SQLException {
        StringBuilder report = new StringBuilder("Recipe Inventory Analytics\n\n");
        for (Map.Entry<String, Object> entry : reportDAO.dashboardStats().entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        report.append("\nMost cooked recipes\n");
        appendRows(report, reportDAO.mostCookedRecipes());
        report.append("\nRecipe popularity\n");
        appendRows(report, reportDAO.popularRecipes());
        report.append("\nLow stock report\n");
        appendRows(report, reportDAO.lowStockReport());
        report.append("\nInventory cost report\n");
        appendRows(report, reportDAO.inventoryCostReport());
        report.append("\nUser activity report\n");
        appendRows(report, reportDAO.userActivityReport());
        return report.toString();
    }

    public void exportPdf(File file) throws SQLException, IOException {
        PDFExporter.export(file, "Recipe Inventory Report", generateDashboardReport());
    }

    public void exportCsv(File file) throws SQLException, IOException {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Section", "Value 1", "Value 2", "Value 3", "Value 4"});
        for (String[] row : reportDAO.inventoryCostReport()) {
            rows.add(prepend("Inventory Cost", row));
        }
        for (String[] row : reportDAO.lowStockReport()) {
            rows.add(prepend("Low Stock", row));
        }
        CSVExporter.export(file, rows);
    }

    private void appendRows(StringBuilder builder, List<String[]> rows) {
        if (rows.isEmpty()) {
            builder.append("No data\n");
            return;
        }
        for (String[] row : rows) {
            builder.append("- ").append(String.join(" | ", row)).append("\n");
        }
    }

    private String[] prepend(String label, String[] row) {
        String[] result = new String[row.length + 1];
        result[0] = label;
        System.arraycopy(row, 0, result, 1, row.length);
        return result;
    }
}
