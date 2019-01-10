package com.example.bijesh.mp3.Lists;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.bijesh.mp3.R;
import com.example.bijesh.mp3.Song;
import com.example.bijesh.mp3.Adapters.SongAdapter;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.bijesh.mp3.Lists.SongList.songs;


public class ArtistList extends AppCompatActivity {


    private static final int
            MY_PERMISSIONS_REQUEST_READ = 1;
    ListView artistLV;
    private Cursor cursor;
    public static ArrayList<String> artists;
    private DrawerLayout mDrawerLayout;
    private Cursor musicCursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);
        artistLV = findViewById(R.id.artsit_lv);
        artists = new ArrayList<>();
        getArtist();
        Collections.sort(artists);
        artistLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Song> temp = new ArrayList<>();
                String albumSelected = artists.get(i);
                for (Song song: songs){
                    if (song.getArtist().equals(albumSelected)){
                        temp.add(song);
                    }
                }
                SongAdapter songAdt = new SongAdapter(getApplication(), temp);
                artistLV.setAdapter(songAdt);
            }
        });
        mDrawerLayout = findViewById(R.id.drawer_layout);
        //click on screen

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        if (menuItem.getItemId() == R.id.nav_song){
                            Intent intent = new Intent(ArtistList.this,SongList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_artist){
                            Intent intent = new Intent(ArtistList.this,ArtistList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_playlist){
                            Intent intent = new Intent(ArtistList.this,PlaylistList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_album){
                            Intent intent = new Intent(ArtistList.this,AlbumList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_genre){
                            Intent intent = new Intent(ArtistList.this,GenreList.class);
                            startActivity(intent);}
                        return true;
                    }
                });


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ);
        } else {
            displayArtist();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    displayArtist();

                } else {
                    Toast.makeText(this, "NO PERMISSION CANNOT CONTINUE!",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public void getArtist(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri =  MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int artistColumn = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                String thisAlbum= musicCursor.getString(artistColumn);
                artists.add(thisAlbum);}
            while (musicCursor.moveToNext());
        }
    }
    private void displayArtist() {
        cursor = getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, null,
                null, null, "Artist ASC");

        String[] from = {MediaStore.Audio.Artists.ARTIST,MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
        int[] to = {R.id.artist_name_tv,R.id.artist_no_song_tv};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.artist_layout, cursor, from, to, 0);

        artistLV.setAdapter(adapter);


    }


}
