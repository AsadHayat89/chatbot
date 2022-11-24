package com.example.fypchatbotapp;

public class Food {
    private String Name, Image, Price, MenuId, Description;

    public Food() {
    }

    public Food(String name, String image, String price, String menuId, String description) {
        Name = name;
        Image = image;
        Price = price;
        MenuId = menuId;
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getDescription(){return Description;}

    public void setDescription(String description){Description = description;}
}
