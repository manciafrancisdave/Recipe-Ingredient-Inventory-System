package com.recipeinventory.model;

public class Tag {
    private int tagId;
    private String name;
    private String color;

    public int getTagId() { return tagId; }
    public void setTagId(int tagId) { this.tagId = tagId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    @Override public String toString() { return name; }
}
