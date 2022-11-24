package com.example.fypchatbotapp.model;

public class Catering {
    String people;
    String time;
    String date;
    String delivery_charges;
    String username;
    String userphone;
    String event_location;
    String total_bill;
    public Catering(String people, String time, String date, String delivery_charges, String username, String userphone, String event_location, String total_bill, String type, String date1) {
        this.people = people;
        this.time = time;
        this.date = date;
        this.delivery_charges = delivery_charges;
        this.username = username;
        this.userphone = userphone;
        this.event_location = event_location;
        this.total_bill = total_bill;
        this.date = date1;
    }
    public Catering() {
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

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
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

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getTotal_bill() {
        return total_bill;
    }

    public void setTotal_bill(String total_bill) {
        this.total_bill = total_bill;
    }

}
