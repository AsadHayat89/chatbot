package com.example.fypchatbotapp.model;

import java.util.List;



public class FoodOrder {
    public String name, price, quantity;

    public FoodOrder(){

    }
    public FoodOrder(String name, String price, String quantity) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }




}
