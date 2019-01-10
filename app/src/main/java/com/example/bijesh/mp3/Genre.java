package com.example.bijesh.mp3;

public class Genre {
    private String name;
    private String genre;

    public Genre(String name , String genre){
        this.name = name;
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
