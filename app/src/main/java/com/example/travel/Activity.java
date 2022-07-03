package com.example.travel;

public class Activity {

    private String name;
    private String description;
    private String price;
    private String location;
    private int image;

    public Activity(String denumire, String descriere, String pret, String location, int image) {
        this.name = denumire;
        this.description = descriere;
        this.price = pret;
        this.location = location;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public int getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }
}
