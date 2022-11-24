package com.example.fypchatbotapp.model;

import java.util.List;

public class Orders {

    String delivery_charges, total_price, useraddress, username, userphone;

    public Orders(){

    }

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
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

    public Orders(String delivery_charges, String total_price, String useraddress, String username, String userphone) {
        this.delivery_charges = delivery_charges;
        this.total_price = total_price;
        this.useraddress = useraddress;
        this.username = username;
        this.userphone = userphone;
    }


}
