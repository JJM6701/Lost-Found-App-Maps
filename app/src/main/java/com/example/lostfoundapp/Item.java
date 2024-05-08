package com.example.lostfoundapp;

public class Item {
    private int id;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private boolean isLost;
    public Item(String name, String phone, String description, String date, String location, boolean isLost) {
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.isLost = isLost;
    }
    public Item(int id, String name, String phone, String description, String date, String location, boolean isLost) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.isLost = isLost;
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
    public String getPhone() {
        return phone;
    }
    public String getDescription() {
        return description;
    }
    public String getDate() {
        return date;
    }
    public String getLocation() {
        return location;
    }
    public boolean isLost() {
        return isLost;
    }
}