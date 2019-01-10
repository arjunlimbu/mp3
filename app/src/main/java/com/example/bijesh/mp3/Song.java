package com.example.bijesh.mp3;

public class Song {
    private long id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private Integer score = 0;

    public Song(long songID, String songTitle, String songArtist, String songAlbum) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        album = songAlbum;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbum() {return album;}
    public String getGenre() {return genre;}
    public Integer getScore() {return score;}

    public void setScore(Integer score) {this.score = score;}
    public void setGenre(String genre) {this.genre = genre;}
}