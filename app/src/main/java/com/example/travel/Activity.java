package com.example.travel;

public class Activity {

    private String denumire;
    private String descriere;
    private String oras;
    private String tara;
    private int image;

    public Activity(String denumire, String descriere, String oras, String tara, int image) {
        this.denumire = denumire;
        this.descriere = descriere;
        this.oras = oras;
        this.tara = tara;
        this.image = image;
    }

    public String getDenumire() {
        return denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public String getOras() {
        return oras;
    }

    public String getTara() {
        return tara;
    }

    public int getImage() {
        return image;
    }
}
