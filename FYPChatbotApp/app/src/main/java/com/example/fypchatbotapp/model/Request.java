package com.example.fypchatbotapp.model;

import java.util.ArrayList;
import java.util.List;

public class Request {

    String total_price, useraddress, username, userphone;

    List<FoodOrder> food_items;

    public Request(){

    }

    public List<FoodOrder> getFood_items() {
        return food_items;
    }

    public void setFood_items(ArrayList<FoodOrder> food_items) {
        this.food_items = food_items;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
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

    public Request(String total_price, String useraddress, String username, String userphone,List<FoodOrder> food_items) {
        this.total_price = total_price;
        this.useraddress = useraddress;
        this.username = username;
        this.userphone = userphone;
        this.food_items = food_items;
    }


}
