package com.example.fypchatbotapp.model;

import com.example.fypchatbotapp.FoodDeliveryDetails;

public class Food_Delivery_Details {
    private String delivery_area_limit, delivery_price, delivery_time_max, delivery_time_min;

    public Food_Delivery_Details()
    {

    }
    public Food_Delivery_Details(String delivery_area_limit, String delivery_price, String delivery_time_max, String delivery_time_min) {
        this.delivery_area_limit = delivery_area_limit;
        this.delivery_price = delivery_price;
        this.delivery_time_max = delivery_time_max;
        this.delivery_time_min = delivery_time_min;
    }

    public String getDelivery_area_limit() {
        return delivery_area_limit;
    }

    public void setDelivery_area_limit(String delivery_area_limit) {
        this.delivery_area_limit = delivery_area_limit;
    }

    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String delivery_price) {
        this.delivery_price = delivery_price;
    }

    public String getDelivery_time_max() {
        return delivery_time_max;
    }

    public void setDelivery_time_max(String delivery_time_max) {
        this.delivery_time_max = delivery_time_max;
    }

    public String getDelivery_time_min() {
        return delivery_time_min;
    }

    public void setDelivery_time_min(String delivery_time_min) {
        this.delivery_time_min = delivery_time_min;
    }
}
