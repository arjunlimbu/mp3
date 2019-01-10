package com.example.bijesh.mp3.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bijesh.mp3.R;
import com.example.bijesh.mp3.Song;

import java.util.ArrayList;


public class SongAdapter extends BaseAdapter {
    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    public SongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate
                (R.layout.song_layout, parent, false);
        //get title and artist views
        TextView songView = songLay.findViewById(R.id.song_title_tv);
        TextView artistView = songLay.findViewById(R.id.artist_tv);
        //get song using position
        Song currSong = songs.get(position);
        if (currSong.getTitle().contains("b")){
            songLay.setBackgroundColor(Color.YELLOW);
        }
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        //set position as tag
        songLay.setTag(position);
        return songLay;
    }

}
