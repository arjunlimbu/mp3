package com.example.bijesh.mp3;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import static com.example.bijesh.mp3.Lists.SongList.albums;
import static com.example.bijesh.mp3.Lists.SongList.songs;

public class MusicPlayer extends AppCompatActivity {

    Button playBt, rewindBt , forwardBt;
    TextView songNameText , albumNameText;
    ImageView albumCover;
    MediaPlayer mp;
    String[] audioList;
    LinearLayout musicPlayerLayout;
    int nextSongPos  , preSongPos =1;
    private String title, albumName;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        //define all the layouts
        playBt = findViewById(R.id.playBt);
        rewindBt = findViewById(R.id.rewindBt);
        forwardBt = findViewById(R.id.forwardBt);
        songNameText = findViewById(R.id.songNameText);
        albumNameText = findViewById(R.id.albumNameText);
        albumCover = findViewById(R.id.AlbumCover);
        musicPlayerLayout = findViewById(R.id.music_player_layout);

        //set list of paths
        audioList = getAudioList();

        //get the info from songList from bundle
        final Intent intent = getIntent();
        final Bundle extraBundle = intent.getExtras();
        assert extraBundle != null;
        //gets title and album of song and sets the tex views as the values
        title = extraBundle.getString("Title");
        albumName = extraBundle.getString("Album");
        songNameText.setText(title);
        albumNameText.setText(albumName);
        getArt(albumName);

        mp = new MediaPlayer();

        playBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mp.isPlaying()){
                    try {
                        //start the music change button pic to the pause
                        mp.start();
                        playBt.setBackgroundResource(R.drawable.pausebtn);
                        /**
                         * playSong needs a path to play a song
                         * getPosition gets the position of the song in the list
                         * using the position value to get the path of the song
                         */
                        playSong(audioList[getPosition()]);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    //pause the music change button pic to play
                    mp.pause();
                    playBt.setBackgroundResource(R.drawable.playbtn);

                }
            }
        });

        forwardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (getPosition() < audioList.length) {
                        mp.start();
                        //need to set teh position to +1 so can do multiple forwards
                        playSong(audioList[nextSongPos]);
                        playBt.setBackgroundResource(R.drawable.pausebtn);
                        //need to change the information of art, title, artist
                        changeSongInfo(nextSongPos);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        rewindBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (getPosition() < audioList.length) {
                        mp.start();
                        //need to set the position to -1 so can do rewind
                        playSong(audioList[preSongPos]);
                        playBt.setBackgroundResource(R.drawable.pausebtn);
                        //need to change the information of art, title, artist
                        changeSongInfo(preSongPos);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //gets list of the path to the songs
    private String[] getAudioList() {
        final Cursor mCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA }, null, null,
                "Artist ASC");

        int count = mCursor.getCount();

        String[] mAudioPath = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                mAudioPath[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                i++;
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return mAudioPath;
    }

    //plays the songs, this requires a path
    private void playSong(String path) throws IllegalArgumentException,
            IllegalStateException, IOException {
        Log.d("ringtone", "playSong :: " + path);
        mp.reset();
        mp.setDataSource(path);
        //mp.setLooping(true);
        mp.prepare();
        mp.start();
    }

    //get the right position on list gets the right song
    private int getPosition(){
        int pos=0;
        //loop all songs
        for (Song song: songs){
            //compare the all song title to find the song title given from bundle
            if (song.getTitle().equals(songNameText.getText().toString())){
                //for the next , pre song
                if (nextSongPos< songs.size()-1) {
                    nextSongPos = pos+1;
                }
                if (preSongPos>0){
                    preSongPos = pos-1;
                }
                return pos;
            }
            //increment to track the position in the list of songs
            pos ++;
        }
        return pos;
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
                albumCover.setImageBitmap(bm);
                musicPlayerLayout.setBackground(drawable);
            }
        }
    }


    //sets the value of the new song
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void changeSongInfo(int pos)
    {
            Song nextSong =songs.get(pos);
            title = nextSong.getTitle();
            albumName = nextSong.getAlbum();
            songNameText.setText(title);
            albumNameText.setText(albumName);
            String nextSongAlbum = nextSong.getAlbum();
            getArt(nextSongAlbum);
    }




}
