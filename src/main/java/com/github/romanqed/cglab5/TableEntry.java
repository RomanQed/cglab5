package com.github.romanqed.cglab5;

public class TableEntry {
    private final Integer id;
    private String x;
    private String y;

    public TableEntry(int id) {
        this.id = id;
        this.x = "";
        this.y = "";
    }

    public Integer getId() {
        return id;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}
