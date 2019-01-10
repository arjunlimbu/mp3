package com.example.bijesh.mp3.Lists;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
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
import com.example.bijesh.mp3.MusicPlayer;
import com.example.bijesh.mp3.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.bijesh.mp3.Lists.AlbumList.partialSongs;
import static com.example.bijesh.mp3.Lists.SongList.albums;


public class PartialList extends AppCompatActivity {

    private ListView songLV;
    private LinearLayout songLayout;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        songLV = findViewById(R.id.song_lv);
        songLayout = findViewById(R.id.song_layout);

        SongAdapter songAdt = new SongAdapter(getApplication(), partialSongs);
        songLV.setAdapter(songAdt);


        songLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PartialList.this, MusicPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putString("Title" , partialSongs.get(i).getTitle());
                bundle.putString("Album",partialSongs.get(i).getAlbum());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        songLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                partialSongs.get(i).setScore(5);
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
                            Intent intent = new Intent(PartialList.this,SongList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_artist){
                            Intent intent = new Intent(PartialList.this,ArtistList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_playlist){
                            Intent intent = new Intent(PartialList.this,PlaylistList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_album){
                            Intent intent = new Intent(PartialList.this,AlbumList.class);
                            startActivity(intent);}
                        if (menuItem.getItemId() == R.id.nav_genre){
                            Intent intent = new Intent(PartialList.this,GenreList.class);
                            startActivity(intent);}
                        return true;
                    }
                });
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

    //gets the album art and sets its using the album name
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void getArt(String albumName){
        //loop all albums
        for (Album album: albums){
            //compare the all album names to find the album name given from bundle
            if (album.getAlbum().equals(albumName)){
                /**
                 * turn the album art value in to bitmap and set the image view
                 * convert to drawable to set the background image
                 */
                Bitmap bm = BitmapFactory.decodeFile(album.getArt());
                Drawable drawable = new BitmapDrawable(bm);
                drawable.setAlpha(150);
                songLayout.setBackground(drawable);
            }
        }
    }
}
