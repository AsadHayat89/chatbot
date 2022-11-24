package com.example.fypchatbotapp.model;

public class EventCatering {

    public String name, price;

    public EventCatering(){

    }

    public EventCatering(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
