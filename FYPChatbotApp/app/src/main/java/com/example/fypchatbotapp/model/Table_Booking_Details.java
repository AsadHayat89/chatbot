package com.example.fypchatbotapp.model;

public class Table_Booking_Details {
    private String adv_booking_time, max_people;
    public Table_Booking_Details()
    {

    }
    public Table_Booking_Details(String adv_booking_time, String max_people) {
        this.adv_booking_time = adv_booking_time;
        this.max_people = max_people;
    }

    public String getAdv_booking_time() {
        return adv_booking_time;
    }

    public void setAdv_booking_time(String adv_booking_time) {
        this.adv_booking_time = adv_booking_time;
    }

    public String getMax_people() {
        return max_people;
    }

    public void setMax_people(String max_people) {
        this.max_people = max_people;
    }
}
