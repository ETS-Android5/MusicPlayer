package au.edu.unsw.infs3634.musicrecommender;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity {

    //Initialize components needed to play Songs
    ImageView btnRewind, btnPlay, btnFastForward, btnPause;
    TextView playerPosition, playerDuration, mRating, mPlays;
    SeekBar seeker;

    //Need instance of MediaPlayer and Handlers and Runnables to stop and start songs
    Runnable runnable;
    Handler handler = new Handler();

    //Static means there can be only one instance of each variable
    public static MediaPlayer mediaPlayer, mediaPlayerCurrent;

    //For adjusting rating of Song
    Button btnSave;
    EditText txtRating;

    //Need an instance of DatabaseHandler in order to update database values for each Song
    DatabaseHandler databaseHandler = new DatabaseHandler(DetailActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //NOTE: This section must always go first. This links your Java class to the XML you describe
        //If you do not include this, will not be able to find any R.ids (will return Null Pointer Exception)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);




        //================================================SECTION FOR MEDIA PLAYER==============================================
        //Initialize the player variables as required
        playerPosition = findViewById(R.id.tvPlayerPosition);
        playerDuration = findViewById(R.id.tvPlayerDuration);
        seeker = findViewById(R.id.seeker);
        btnPlay = findViewById(R.id.btnPlay);
        btnRewind = findViewById(R.id.btnRewind);
        btnFastForward = findViewById(R.id.btnFastForward);
        btnPause = findViewById(R.id.btnPause);

        //Need to create a new mediaPlayer always whenever we view Detail Activity
        mediaPlayer = MediaPlayer.create(this, MainActivity.songsTemp.get(MainActivity.intSong).getMusicFile());

        //Convert the duration of the mediaPlayer into a String format so it can be displayed in TextViews
        //Then set the duration TextView as the time remaining
        int d = mediaPlayer.getDuration();
        String duration = convertFormat(d);
        playerDuration.setText(duration);

        //Initialize runnable so that the player can be run or stopped as required
        runnable = new Runnable() {
            //Method to run the the player
            @Override
            public void run() {
                //Set the progress on the seeker as the current position on the media player
                seeker.setProgress(mediaPlayerCurrent.getCurrentPosition());
                //Handler post delay for 0.5 second
                handler.postDelayed(this, 500);
            }
        };

        //Remember ID of which song we're playing so we can check if we clicked the same song twice from MainActivity
        Song.setCurrentSongId(MainActivity.songsTemp.get(MainActivity.intSong).getId());


        //Do a check to see if user is playing music currently, and clicked on the same song twice then we need to hide the Play button
        //And also ensure our seeker is in the original spot!
        if ((Song.isPlaying() == true && Song.getPreviousSongId() == Song.getCurrentSongId())) {
            btnPlay.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
            seeker.setMax(mediaPlayerCurrent.getDuration());
            handler.postDelayed(runnable, 0);
        }











        //Pressing play will start the media player and hide the play button but shows the pause button
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the user is not playing a Song (i.e. the first time they load app) we assign our mediaPlayerCurrent to the one we create always
                if (Song.isPlaying() == false) {
                    mediaPlayerCurrent = mediaPlayer;
                } else if (Song.isPlaying() && Song.getPreviousSongId() != Song.getCurrentSongId()) {
                    //Otherwise, if a song is playing we need to stop the existing instance before assigning it, but only if it is a different song
                    mediaPlayerCurrent.stop();
                    mediaPlayerCurrent.release();
                    mediaPlayerCurrent = mediaPlayer;
                    //Otherwise, if the user is playing a Song, and it is the same song they clicked Play on previously...we just let it continue playing
                } else {
                    ;
                }

                mediaPlayerCurrent.start();

                //The user has already played the song
                Song.setIsPlaying(true);

                //Show the pause button but hide the play button
                btnPause.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);

                //Assign maximum values on the as the duration of the music file
                seeker.setMax(mediaPlayerCurrent.getDuration());
                handler.postDelayed(runnable, 0);

                // Retrieve the current number of plays of the currently selected song
                int newPlays = MainActivity.songsTemp.get(MainActivity.intSong).getPlays() + 1;

                //The id of our database starts from 1, so we add 1
                int id = MainActivity.intSong + 1;

                //Update play count in database by 1
                databaseHandler.updatePlays(id, newPlays);
                //Update songsTemp with the new Plays
                databaseHandler.refreshData(databaseHandler.getSongs(), MainActivity.songsTemp);
                //Update the textView with the new amount
                mPlays.setText(String.valueOf(MainActivity.songsTemp.get(MainActivity.intSong).getPlays()));
            }
        });



        //Clicking on pause will pause the media player
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the play button but hide the pause button
                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);

                //Pause the media player and stop the handler
                mediaPlayerCurrent.pause();
            }
        });



        //When the user presses fast forward, the music is moved forward by 10 seconds
        btnFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position and duration of the mediaPlayer
                int position = mediaPlayerCurrent.getCurrentPosition();

                //Get the duration of the media player
                //If the current position of the media player is not at the end, fast forward the song by 5 seconds
                int d = mediaPlayerCurrent.getDuration();
                if (mediaPlayerCurrent.isPlaying() && position != d) {
                    //Note we are using milliseconds
                    position += 5000;

                    //seek to 5 seconds ahead of current time and change the playerPosition's text to 5 seconds ahead
                    mediaPlayerCurrent.seekTo(position);
                    playerPosition.setText(convertFormat(position));
                }
            }
        });



        //Pressing rewind will take the user back by 5 seconds in the song
        btnRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position
                int position = mediaPlayerCurrent.getCurrentPosition();

                //If the song is at least 5 seconds in, we can rewind back by 5 seconds
                if (mediaPlayerCurrent.isPlaying() && position > 5000) {
                    position -=  5000;

                    //Set the current progress on seekBar and change the TextView to the position of the player
                    mediaPlayerCurrent.seekTo(position);
                    playerPosition.setText(convertFormat(position));

                }
            }
        });



        //Whenever the user adjusts the seeker, adjust both the song time and the TextView to the adjusted time
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int newPosition, boolean seekerChanged) {
                //Check if the seeker position has been changed by the user
                //If so, adjust media player to new position
                if (seekerChanged) {
                    mediaPlayerCurrent.seekTo(newPosition);
                }
                //Change textView to reflect new position
                playerPosition.setText(convertFormat(mediaPlayerCurrent.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ;
            }
        });




        //When the song finishes playing, stop playing the song, and hide the pause button
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);

                //Set to false otherwise app will crash
                Song.setIsPlaying(false);

                //Reset the position of the media player to 0
                mediaPlayer.seekTo(0);
            }
        });






        //=====================================================SECTION FOR OTHER XML ELEMENTS =======================================================

        //Create and initialize variables for each TextView that holds data on each country
        TextView mSong = findViewById(R.id.tvSong);
        TextView mSinger = findViewById(R.id.tvSinger);
        TextView mGenre = findViewById(R.id.tvGenre);
        mRating = findViewById(R.id.tvRating);
        mPlays = findViewById(R.id.tvPlays);
        TextView mDescription = findViewById(R.id.tvDescription);

        //Initialize button for integration with browser
        ImageButton mSearch = findViewById(R.id.btnSearch);

        //Initialize save button to save changes to rating;
        btnSave = findViewById(R.id.btnSave);
        txtRating = findViewById(R.id.txtRating);

        //Initialize image
        ImageView imageView = findViewById(R.id.imgAlbum);

        //Initialize string values
        String song = MainActivity.songsTemp.get(MainActivity.intSong).getSong();
        String singer = MainActivity.songsTemp.get(MainActivity.intSong).getSinger();
        String genre = MainActivity.songsTemp.get(MainActivity.intSong).getSong();
        String rating = String.valueOf(MainActivity.songsTemp.get(MainActivity.intSong).getRating());
        String description = MainActivity.songsTemp.get(MainActivity.intSong).getDescription();
        String plays = String.valueOf(MainActivity.songsTemp.get(MainActivity.intSong).getPlays());


        //Set the text of the TextViews for Song, Singer, Genre and Rating
        //Note: this can be simplified by creating new Strings to store those values, instead of having to use getters and setters each time
        mSong.setText(song);
        mSinger.setText(singer);
        mGenre.setText(genre);
        mRating.setText(rating);
        mPlays.setText(plays);
        mDescription.setText(description);
        imageView.setImageResource(MainActivity.songsTemp.get(MainActivity.intSong).getImage());

        //Set an OnClickListener to take user to Google search for Artist and Song when user clicks on Button Search
        mSearch.setOnClickListener(new View.OnClickListener() {
            //Override the onClick method
            @Override
            public void onClick(View view) {
                //Specify a new intent to take user to browser search
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.au/search?q=" +
                        singer + " " + song));
                startActivity(intent);
            }
        });



        //Set an OnClickListener that changes the rating of the current Song in the database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When user clicks save, we change that song's rating in the database
                //First obtain the text entered in the EditText
                String s = txtRating.getText().toString();
                System.out.println(s);
                Float newRating = Float.parseFloat(s);
                //Next, obtain the currentId
                int id = MainActivity.intSong + 1;
                //Then execute the updateRating method
                databaseHandler.updateRating(id, newRating);
                databaseHandler.refreshData(databaseHandler.getSongs(), MainActivity.songsTemp);
                mRating.setText(String.valueOf(MainActivity.songsTemp.get(MainActivity.intSong).getRating()));
            }
        });
    }

    //For Media Player
    //Converts the duration of the song into a 00:00 format, using MILLISECONDS as a time unit
    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format(
                "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //When user presses back, the previous songId will be set to what we just clicked on before
        Song.setPreviousSongId(Song.getCurrentSongId());
    }
}