package com.example.goran.firebasedemo.data.model;

import java.util.Date;

public class CheckIn {

    private double lat;
    private double lng;
    private String address;
    private Date date;

    public CheckIn() {
    }

    public CheckIn(double lat, double lng, Date date) {
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
