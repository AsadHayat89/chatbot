package com.example.fypchatbotapp.model;

import java.util.List;

public class TableBooking {
    String date, people, time, total_price, username, userphone;

    public TableBooking() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public TableBooking(String date, String people, String time, String total_price, String username, String userphone) {
        this.date = date;
        this.people = people;
        this.time = time;
        this.total_price = total_price;
        this.username = username;
        this.userphone = userphone;
    }
}
