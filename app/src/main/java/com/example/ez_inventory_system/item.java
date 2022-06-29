package com.example.ez_inventory_system;

public class item {
    private int id;
    private String quantity;
    private String name;
    private String price;
    private byte[] image;

    public item(String name, String price, byte[] image, int id,String quantity) {

        this.name = name;
        this.price = price;
        this.image = image;
        this.id = id;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

