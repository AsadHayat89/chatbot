package com.example.fypchatbotapp.model;

import java.util.List;

public class Deals { ;
    String cat_id, deal_name, deal_price, deal_description;
    public Deals() {
    }



    public Deals(String cat_id, String deal_name, String deal_price, String deal_description) {
        this.cat_id = cat_id;
        this.deal_name = deal_name;
        this.deal_price = deal_price;
        this.deal_description = deal_description;
    }

    public String getDeal_id() {
        return cat_id;
    }

    public void setDeal_id(String deal_id) {
        this.cat_id = deal_id;
    }

    public String getDeal_name() {
        return deal_name;
    }

    public void setDeal_name(String deal_name) {
        this.deal_name = deal_name;
    }

    public String getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(String deal_price) {
        this.deal_price = deal_price;
    }

    public String getDeal_description() {
        return deal_description;
    }

    public void setDeal_description(String deal_description) {
        this.deal_description = deal_description;
    }

}
