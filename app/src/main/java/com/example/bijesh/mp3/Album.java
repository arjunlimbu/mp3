package com.example.bijesh.mp3;

import android.widget.ImageView;

public class Album {
    private String album;
    private String art;

    public Album(String album,String art ){
        this.album = album;
        this.art = art;
    }

    public void setAlbum(String album) { this.album = album; }

    public void setArt(String art) { this.art = art; }

    public String getAlbum() { return album; }

    public String getArt() { return art; }
}
