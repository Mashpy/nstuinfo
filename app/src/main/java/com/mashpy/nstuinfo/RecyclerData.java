package com.mashpy.nstuinfo;


public class RecyclerData {
    private String title, genre, year, url, type;

    public RecyclerData() {
    }

    public RecyclerData(String title, String genre, String year, String url, String type) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.url = url;
        this.type = type;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
