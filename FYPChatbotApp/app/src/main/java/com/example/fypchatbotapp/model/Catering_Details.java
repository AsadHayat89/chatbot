package com.example.fypchatbotapp.model;

public class Catering_Details {
    private String adv_booking_time, delivery_charges;
    public Catering_Details()
    {

    }
    public Catering_Details(String adv_booking_time, String delivery_charges) {
        this.adv_booking_time = adv_booking_time;
        this.delivery_charges = delivery_charges;
    }

    public String getAdv_booking_time() {
        return adv_booking_time;
    }

    public void setAdv_booking_time(String adv_booking_time) {
        this.adv_booking_time = adv_booking_time;
    }

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
    }
}
