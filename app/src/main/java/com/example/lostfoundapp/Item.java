package com.example.lostfoundapp;

public class Item {
    private int id;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private boolean isLost;
    private double latitude;
    private double longitude;

    public Item() {
    }

    public Item(int id, String name, String phone, String description, String date, String location, boolean isLost, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.isLost = isLost;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Item(String name, String phone, String description, String date, String location, boolean isLost, double latitude, double longitude) {
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.isLost = isLost;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}