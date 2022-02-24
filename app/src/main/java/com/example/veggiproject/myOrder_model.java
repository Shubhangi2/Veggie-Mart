package com.example.veggiproject;

public class myOrder_model {
    String date, image, name, price, quantity, time, vid, status;

    public myOrder_model(){

    }

    public myOrder_model(String date, String image, String name, String price, String quantity, String time, String vid, String status) {
        this.date = date;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
        this.vid = vid;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
