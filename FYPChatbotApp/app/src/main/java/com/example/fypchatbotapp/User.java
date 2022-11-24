package com.example.fypchatbotapp;

public class User {
    private String name, password, email, phone;

    public User() {
    }

    public User(String Name, String Password, String Email, String Phone)
    {
        name = Name;
        password = Password;
        email = Email;
        phone = Phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }



}
