package com.example.fypchatbotapp.model;

public class Event_Booking_Details {
    private String adv_booking_time, decor_rate_balloons, extra_decor, hall_area, hall_price_per_hour, max_people, song_rate_per_hour, stage_decor_price;

    public Event_Booking_Details()
    {

    }
    public Event_Booking_Details(String adv_booking_time, String decor_rate_balloons, String extra_decor, String hall_area, String hall_price_per_hour, String max_people, String song_rate_per_hour, String stage_decor_price) {
        this.adv_booking_time = adv_booking_time;
        this.decor_rate_balloons = decor_rate_balloons;
        this.extra_decor = extra_decor;
        this.hall_area = hall_area;
        this.hall_price_per_hour = hall_price_per_hour;
        this.max_people = max_people;
        this.song_rate_per_hour = song_rate_per_hour;
        this.stage_decor_price = stage_decor_price;
    }

    public String getAdv_booking_time() {
        return adv_booking_time;
    }

    public void setAdv_booking_time(String adv_booking_time) {
        this.adv_booking_time = adv_booking_time;
    }

    public String getDecor_rate_balloons() {
        return decor_rate_balloons;
    }

    public void setDecor_rate_balloons(String decor_rate_balloons) {
        this.decor_rate_balloons = decor_rate_balloons;
    }

    public String getExtra_decor() {
        return extra_decor;
    }

    public void setExtra_decor(String extra_decor) {
        this.extra_decor = extra_decor;
    }

    public String getHall_area() {
        return hall_area;
    }

    public void setHall_area(String hall_area) {
        this.hall_area = hall_area;
    }

    public String getHall_price_per_hour() {
        return hall_price_per_hour;
    }

    public void setHall_price_per_hour(String hall_price_per_hour) {
        this.hall_price_per_hour = hall_price_per_hour;
    }

    public String getMax_people() {
        return max_people;
    }

    public void setMax_people(String max_people) {
        this.max_people = max_people;
    }

    public String getSong_rate_per_hour() {
        return song_rate_per_hour;
    }

    public void setSong_rate_per_hour(String song_rate_per_hour) {
        this.song_rate_per_hour = song_rate_per_hour;
    }

    public String getStage_decor_price() {
        return stage_decor_price;
    }

    public void setStage_decor_price(String stage_decor_price) {
        this.stage_decor_price = stage_decor_price;
    }


}
