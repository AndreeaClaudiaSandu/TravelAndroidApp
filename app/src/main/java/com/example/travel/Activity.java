package com.example.travel;

public class Activity {

    private String name;
    private String description;
    private String price;
    private String city;
    private String country;
    private int image;

    public Activity(String denumire, String descriere, String pret, String oras, String tara, int image) {
        this.name = denumire;
        this.description = descriere;
        this.price = pret;
        this.city = oras;
        this.country = tara;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public int getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }
}
