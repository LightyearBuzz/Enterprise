package com.github.lightyear;

//class Item описывает объект, который предназначен для объектного представления данных из базы.
//База данных хранит иинформацию о товаре на складе в виде записи данного типа:
// id  |  title          |    quantity    |  price      | location
// 2   |  WiloStar RS    |    3           |  5657.0     | A2
public class Item {
    protected int id;
    protected String title;
    protected int quantity;
    protected float price;
    protected String location;

    public Item() {

    }

    public Item(int id) {
        this.id = id;
    }

    public Item(String title, int quantity, float price, String location) {
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.location = location;
    }

    public Item(int id, String title, int quantity, float price, String location) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.location = location;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}