package com.example.travel;

public class ExternalTransport {

    String airport;
    String city;
    String type;
    String description;
    String price;
    String time;
    String link;
    String departureStation;
    String arrivalStation;
    String frequency;

    public ExternalTransport(String city, String airport,  String type, String description, String departureStation, String arrivalStation, String time, String frequency , String price, String link) {
        this.airport = airport;
        this.city = city;
        this.type = type;
        this.description = description;
        this.price = price;
        this.time = time;
        this.link = link;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.frequency = frequency;
    }

    public String getAirport() {
        return airport;
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

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public String getLink() {
        return link;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public String getFrequency() {
        return frequency;
    }
}
