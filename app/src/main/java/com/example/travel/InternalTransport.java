package com.example.travel;

public class InternalTransport {

    String city;
    String type;
    String description;
    String pret;
    String link;

    public InternalTransport(String city, String type, String description, String pret, String link) {
        this.city = city;
        this.type = type;
        this.description = description;
        this.pret = pret;
        this.link = link;
    }

    public String getCity() {
        return city;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getPret() {
        return pret;
    }

    public String getLink() {
        return link;
    }
}
