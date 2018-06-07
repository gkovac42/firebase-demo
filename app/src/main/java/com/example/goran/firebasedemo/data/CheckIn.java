package com.example.goran.firebasedemo.data;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckIn {

    private double latitude;
    private double longitude;
    private Date date;

    public CheckIn() {
    }

    public CheckIn(double latitude, double longitude, Date date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public String getLocalizedDate() {
        DateFormat df = new SimpleDateFormat("dd-mm-yyyy, HH:mm", Locale.ENGLISH);
        return df.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return "Lat: " + df.format(latitude) + ", Lng: " + df.format(longitude);
    }
}
