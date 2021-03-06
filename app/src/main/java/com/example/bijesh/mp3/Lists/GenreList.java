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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.bijesh.mp3.Adapters.SongAdapter;
import com.example.bijesh.mp3.Genre;
import com.example.bijesh.mp3.R;
import com.example.bijesh.mp3.Song;

import java.util.ArrayList;

import static com.example.bijesh.mp3.Lists.SongList.genres;
import static com.example.bijesh.mp3.Lists.SongList.songGenre;
import static com.example.bijesh.mp3.Lists.SongList.songs;

public class GenreList extends AppCompatActivity {
    private static final int
            MY_PERMISSIONS_REQUEST_READ = 1;
    private ListView genreLV;
    private Cursor cursor;
    private DrawerLayout mDrawerLayout;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_list);


        genreLV = findViewById(R.id.genre_lv);
        genreLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Song> temp = new ArrayList<>();

                String genreSelected = genres.get(i);
                //every song name and genre
                for (Genre x: songGenre){
                    //if genre is the same
                    if (x.getGenre().equals(genreSelected)){
                        //every song
                        for (Song song: songs){
                            //song name is same this is same song
                            if (song.getTitle().equals(x.getName())){
                                temp.add(song);
                            }
                        }
                    }
                }
                SongAdapter songAdt = new SongAdapter(getApplication(), temp);
                genreLV.setAdapter(songAdt);
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
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        if (menuItem.getItemId() == R.id.nav_song){
                            Intent intent = new Intent(GenreList.this,SongList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_artist){
                            Intent intent = new Intent(GenreList.this,ArtistList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_playlist){
                            Intent intent = new Intent(GenreList.this,PlaylistList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_album){
                            Intent intent = new Intent(GenreList.this,AlbumList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_genre){
                            Intent intent = new Intent(GenreList.this,GenreList.class);
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
            displayGenres();
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
                    displayGenres();

                } else {
                    Toast.makeText(this, "NO PERMISSION CANNOT CONTINUE!",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

    }





    private void displayGenres() {
        cursor = getContentResolver().query(
                MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, null,
                null, null, null);


            String[] from = {MediaStore.Audio.Genres.NAME};
            int[] to = {R.id.genre_tv};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    R.layout.genre_layout, cursor, from, to, 0);

        genreLV.setAdapter(adapter);
    }
}
