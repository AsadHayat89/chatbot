package com.example.fypchatbotapp.model;

public class DealsFood {
    String name, quantity;
    public DealsFood() {
    }
    public DealsFood(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String food_item) {
        this.name = food_item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


}
