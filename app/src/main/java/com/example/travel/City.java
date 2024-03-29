package com.example.travel;

public class City {

    String name;
    String country;
    String description;
    String busTimetable;
    String metroTimetable;
    String tramTimetable;
    int img;

    public City(String name, String country, String description, String busTimetable, String metroTimetable, String tramTimetable, int img) {
        this.name = name;
        this.country = country;
        this.description = description;
        this.busTimetable = busTimetable;
        this.metroTimetable = metroTimetable;
        this.img = img;
        this.tramTimetable = tramTimetable;
    }

    public String getName() {
        return name;
    }

    public int getImg() {
        return img;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public String getBusTimetable() {
        return busTimetable;
    }

    public String getMetroTimetable() {
        return metroTimetable;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTramTimetable() {
        return tramTimetable;
    }
}

