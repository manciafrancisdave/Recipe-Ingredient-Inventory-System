package com.recipeinventory.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public final class CSVExporter {
    private CSVExporter() {
    }

    public static void export(File file, List<String[]> rows) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            for (String[] row : rows) {
                for (int i = 0; i < row.length; i++) {
                    if (i > 0) {
                        writer.write(",");
                    }
                    writer.write("\"" + row[i].replace("\"", "\"\"") + "\"");
                }
                writer.write(System.lineSeparator());
            }
        }
    }
}
