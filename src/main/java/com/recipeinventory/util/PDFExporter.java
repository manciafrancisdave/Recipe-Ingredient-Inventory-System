package com.recipeinventory.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class PDFExporter {
    private PDFExporter() {
    }

    public static void export(File file, String title, String content) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(title);
        lines.add("");
        for (String line : content.split("\\R")) {
            lines.add(line);
        }
        StringBuilder stream = new StringBuilder("BT /F1 11 Tf 50 780 Td 14 TL\n");
        for (String line : lines) {
            stream.append("(").append(escape(line)).append(") Tj T*\n");
        }
        stream.append("ET");
        byte[] streamBytes = stream.toString().getBytes(StandardCharsets.ISO_8859_1);

        List<String> objects = new ArrayList<>();
        objects.add("<< /Type /Catalog /Pages 2 0 R >>");
        objects.add("<< /Type /Pages /Kids [3 0 R] /Count 1 >>");
        objects.add("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>");
        objects.add("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>");
        objects.add("<< /Length " + streamBytes.length + " >>\nstream\n" + stream + "\nendstream");

        StringBuilder pdf = new StringBuilder("%PDF-1.4\n");
        List<Integer> offsets = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            offsets.add(pdf.length());
            pdf.append(i + 1).append(" 0 obj\n").append(objects.get(i)).append("\nendobj\n");
        }
        int xref = pdf.length();
        pdf.append("xref\n0 ").append(objects.size() + 1).append("\n");
        pdf.append("0000000000 65535 f \n");
        for (int offset : offsets) {
            pdf.append(String.format("%010d 00000 n \n", offset));
        }
        pdf.append("trailer << /Size ").append(objects.size() + 1).append(" /Root 1 0 R >>\n");
        pdf.append("startxref\n").append(xref).append("\n%%EOF");

        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(pdf.toString().getBytes(StandardCharsets.ISO_8859_1));
        }
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }
}
