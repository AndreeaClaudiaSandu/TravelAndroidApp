package com.example.travel;

public class Attraction {

    String name;
    String city;
    String description;
    String location;
    String timetable;
    String price;
    String visitTime;
    String station;
    String link;
    int image;

    public Attraction(String name, String city, String description, String location, String timetable, String price, String visitTime, String station, String link, int image) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.location = location;
        this.timetable = timetable;
        this.price = price;
        this.visitTime = visitTime;
        this.station = station;
        this.link = link;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getTimetable() {
        return timetable;
    }

    public String getPrice() {
        return price;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public String getStation() {
        return station;
    }

    public String getLink() {
        return link;
    }

    public int getImage() {
        return image;
    }
}
