package com.example.fypchatbotapp.model;

public class GeneralDetails {
    private String restr_address, restr_closing_time, restr_name, restr_opening_time, restr_sitting_area, tables;
    public GeneralDetails()
    {

    }
    public GeneralDetails(String restr_address, String restr_closing_time, String restr_name, String restr_opening_time, String restr_sitting_area, String tables) {
        this.restr_address = restr_address;
        this.restr_closing_time = restr_closing_time;
        this.restr_name = restr_name;
        this.restr_opening_time = restr_opening_time;
        this.restr_sitting_area = restr_sitting_area;
        this.tables = tables;
    }

    public String getRestr_address() {
        return restr_address;
    }

    public void setRestr_address(String restr_address) {
        this.restr_address = restr_address;
    }

    public String getRestr_closing_time() {
        return restr_closing_time;
    }

    public void setRestr_closing_time(String restr_closing_time) {
        this.restr_closing_time = restr_closing_time;
    }

    public String getRestr_name() {
        return restr_name;
    }

    public void setRestr_name(String restr_name) {
        this.restr_name = restr_name;
    }

    public String getRestr_opening_time() {
        return restr_opening_time;
    }

    public void setRestr_opening_time(String restr_opening_time) {
        this.restr_opening_time = restr_opening_time;
    }

    public String getRestr_sitting_area() {
        return restr_sitting_area;
    }

    public void setRestr_sitting_area(String restr_sitting_area) {
        this.restr_sitting_area = restr_sitting_area;
    }

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }
}
