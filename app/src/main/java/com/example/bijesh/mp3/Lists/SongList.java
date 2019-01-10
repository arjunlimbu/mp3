package com.example.bijesh.mp3.Lists;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bijesh.mp3.Adapters.SongAdapter;
import com.example.bijesh.mp3.Album;
import com.example.bijesh.mp3.Genre;
import com.example.bijesh.mp3.MusicPlayer;
import com.example.bijesh.mp3.R;
import com.example.bijesh.mp3.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SongList extends AppCompatActivity {
    private static final int
            MY_PERMISSIONS_REQUEST_READ = 1;
    ListView songLV;
    private DrawerLayout mDrawerLayout;
    public static ArrayList<Song> songs;
    public static ArrayList<Album> albums;
    public static ArrayList<String> genres;
    public static ArrayList<Genre> songGenre;
    public LinearLayout songLayout;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_song_list);
        songLV = findViewById(R.id.song_lv);
        songLayout = findViewById(R.id.song_layout);
        songs = new ArrayList<>();
        albums = new ArrayList<>();
        genres = new ArrayList<>();
        songGenre =new ArrayList<>();
        getAlbumList();
        getSongList();
        getGenre();
        Collections.sort(songs, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getArtist().compareTo(b.getArtist());
            }
        });
        Collections.sort(genres);
        SongAdapter songAdt = new SongAdapter(this, songs);
        songLV.setAdapter(songAdt);

        songLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SongList.this, "score"+songs.get(i).getScore(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SongList.this, MusicPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putString("Title" , songs.get(i).getTitle());
                bundle.putString("Album",songs.get(i).getAlbum());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        songLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                songs.get(i).setScore(5);
                return false;
            }
        });
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        if (menuItem.getItemId() == R.id.nav_song){
                            Intent intent = new Intent(SongList.this,SongList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_artist){
                            Intent intent = new Intent(SongList.this,ArtistList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_playlist){
                            Intent intent = new Intent(SongList.this,PlaylistList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_album){
                            Intent intent = new Intent(SongList.this,AlbumList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_genre){
                            Intent intent = new Intent(SongList.this,GenreList.class);
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

                } else {
                    Toast.makeText(this, "NO PERMISSION CANNOT CONTINUE!",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

    }



    public void getSongList() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor !=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media.ALBUM);

            //add songs to list
            do {

                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum= musicCursor.getString(albumColumn);
                songs.add(new Song(thisId,thisTitle,thisArtist,thisAlbum));}
            while (musicCursor.moveToNext());
        }

    }

    public void getAlbumList() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, "Album ASC");
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int albumColumn = musicCursor.getColumnIndex(
                    MediaStore.Audio.Albums.ALBUM);
            int albumArtColumn = musicCursor.getColumnIndex(
                    MediaStore.Audio.Albums.ALBUM_ART);
            //add songs to list
            do {
                String thisAlbum= musicCursor.getString(albumColumn);
                String thisArt = musicCursor.getString(albumArtColumn);
                albums.add(new Album(thisAlbum,thisArt));}
            while (musicCursor.moveToNext());
        }
    }


    public void getGenre(){
        int index, indexG;
        long genreId;
        Uri uri;
        Cursor genrecursor;
        Cursor tempcursor;
        String[] proj1 = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};
        String[] proj2 = {MediaStore.Audio.Media.TITLE};

        genrecursor = managedQuery(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, proj1, null, null, null);
        if (genrecursor.moveToFirst()) {
            do {
                indexG = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
                genres.add(genrecursor.getString(indexG));
                index = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);
                genreId = Long.parseLong(genrecursor.getString(index));
                uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);

                tempcursor = managedQuery(uri, proj2, null,null,null);

                if (tempcursor.moveToFirst()) {
                    do {
                        index = tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                        songGenre.add(new Genre(tempcursor.getString(index), genrecursor.getString(indexG)));

                    } while(tempcursor.moveToNext());
                }
            } while(genrecursor.moveToNext());
        }
    }


    public  void  getSongInfo(long position){
        MediaMetadataRetriever mr = new MediaMetadataRetriever();
        Uri uri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, position);
        mr.setDataSource(getApplicationContext(), uri);
        String title = mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String album = mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String genre = mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);


    }

}
