package com.example.veggiproject;

public class cart_item_model_class {
        String name, price, quantity, vid;


    public cart_item_model_class(String name, String price, String quantity, String vid) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.vid = vid;
    }
    public cart_item_model_class(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
