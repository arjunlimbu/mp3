package com.example.bijesh.mp3.Lists;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.bijesh.mp3.Album;
import com.example.bijesh.mp3.MusicPlayer;
import com.example.bijesh.mp3.R;
import com.example.bijesh.mp3.Song;
import com.example.bijesh.mp3.Adapters.SongAdapter;

import java.util.ArrayList;

import static com.example.bijesh.mp3.Lists.SongList.albums;
import static com.example.bijesh.mp3.Lists.SongList.songs;


public class AlbumList extends AppCompatActivity {


    private static final int
            MY_PERMISSIONS_REQUEST_READ = 1;
    GridView albumGV;
    private Cursor cursor;
    private DrawerLayout mDrawerLayout;
    private Cursor musicCursor;
    public static ArrayList<Song> partialSongs;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        albumGV = findViewById(R.id.album_gv);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        //click on screen
        albumGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                    partialSongs = new ArrayList<>();
                    String albumSelected = albums.get(position).getAlbum();

                    for (Song song: songs){
                        if (song.getAlbum().equals(albumSelected)){
                            partialSongs.add(song);
                        }
                    }
                    Intent intent = new Intent(AlbumList.this, PartialList.class);
                    startActivity(intent);
//                    SongAdapter songAdt = new SongAdapter(getApplication(), partialSongs);
//
//                    albumGV.setAdapter(songAdt);
                    }
        });
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
                        Intent intent = new Intent(AlbumList.this,SongList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_artist){
                            Intent intent = new Intent(AlbumList.this,ArtistList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_playlist){
                            Intent intent = new Intent(AlbumList.this,PlaylistList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_album){
                            Intent intent = new Intent(AlbumList.this,AlbumList.class);
                            startActivity(intent);}
                            if (menuItem.getItemId() == R.id.nav_genre){
                            Intent intent = new Intent(AlbumList.this,GenreList.class);
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
            displayAlbums();

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
                    displayAlbums();

                } else {
                    Toast.makeText(this, "NO PERMISSION CANNOT CONTINUE!",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

    }



    private void displayAlbums() {
        cursor = getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
                null, null, "Album ASC");

        String[] from = {MediaStore.Audio.Albums.ALBUM_ART};
        int[] to = {R.id.album_art};
        LinearLayout songLayout = findViewById(R.id.song_layout);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.album_layout, cursor, from, to, 0);
        albumGV.setAdapter(adapter);

    }
}
