package com.recipeinventory.model;

import java.time.LocalDateTime;

public class Report {
    private int reportId;
    private String type;
    private LocalDateTime generatedDate;
    private int generatedBy;
    private String data;

    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDateTime generatedDate) { this.generatedDate = generatedDate; }
    public int getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(int generatedBy) { this.generatedBy = generatedBy; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}
