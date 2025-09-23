package com.furkanoffice.clock.model;

public class SaatDilimi {
    private String name;
    private long dilim;
    String date;
    int id;

    public SaatDilimi(String name, String date, long dilim, int id) {
        this.name = name;
        this.dilim = dilim;
        this.date = date;
        this.id = id;
    }

    public String toString() {
        return name + " | " + date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDilim() {
        return dilim;
    }

    public String getDate() {
        return date;
    }

    public void setDilim(long dilim) {
        this.dilim = dilim;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
