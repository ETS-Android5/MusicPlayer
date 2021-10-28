package au.edu.unsw.infs3634.musicrecommender;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity {

    //Initialize components needed to play Songs
    ImageView btnRewind, btnPlay, btnFastForward, btnPause;
    TextView playerPosition, playerDuration, mRating, mPlays;
    SeekBar seeker;
    RatingBar ratingBar;

    //Need instance of MediaPlayer and Handlers and Runnables to stop and start songs
    Runnable runnable;
    Handler handler = new Handler();

    //Static means there can be only one instance of each variable
    public static MediaPlayer mediaPlayer, mediaPlayerCurrent;

    //For adjusting rating of Song
    ImageButton btnBack, btnSearch;
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
        ratingBar = findViewById(R.id.ratingSong);

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

        //Seeker and rewind disabled by default
        seeker.setEnabled(false);
        btnRewind.setEnabled(false);

        //Remember ID of which song we're playing so we can check if we clicked the same song twice from MainActivity
        Song.setCurrentSongId(MainActivity.songsTemp.get(MainActivity.intSong).getId());
        //When user first clicks on on any Song, it is disabled until they click on Play, Fast Forward or Rewind



        //Do a check to see if user is playing music currently, and clicked on the same song twice then we need to hide the Play button
        //And also ensure our seeker is in the original spot!
        if ((Song.isPlaying() == true && Song.getPlayingSongId() == Song.getCurrentSongId())) {
            btnPlay.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
            //Enable our seeker and rewind button, if user is going back to same song they are already playing.
            seeker.setEnabled(true);
            btnRewind.setEnabled(true);

            seeker.setMax(mediaPlayerCurrent.getDuration());
            handler.postDelayed(runnable, 0);
        } else if ((Song.isPlaying() == false && Song.getPlayingSongId() == Song.getCurrentSongId())) {
            //If the song was paused, but user clicked on the same song, then hide the pause button, and show the play button
            btnPlay.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.GONE);

            seeker.setEnabled(true);
            btnRewind.setEnabled(true);

            seeker.setMax(mediaPlayerCurrent.getDuration());
            handler.postDelayed(runnable, 0);
        } else {
            ;
        }












        //Pressing play will start the media player and hide the play button but shows the pause button
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Immediately enable the Seeker and Rewind Buttons
                seeker.setEnabled(true);
                btnRewind.setEnabled(true);

                //If the user is not playing a Song (i.e. the first time they load app) we assign our mediaPlayerCurrent to the one we create always
                if (Song.isPlaying() == false && Song.getPlayingSongId() != Song.getCurrentSongId() ) {
                    mediaPlayerCurrent = mediaPlayer;

                    //Otherwise, if a song is playing we need to stop the existing instance before assigning it, but only if it is a different song
                } else if (Song.isPlaying() && Song.getPlayingSongId() != Song.getCurrentSongId()) {
                    mediaPlayerCurrent.stop();
                    mediaPlayerCurrent.release();
                    mediaPlayerCurrent = mediaPlayer;

                    //Otherwise, if the user is playing a Song, and it is the same song they clicked play on previously AND it has been paused
                    //...we just let it continue playing
                } else if (Song.isPlaying() == false && Song.getPlayingSongId() == Song.getCurrentSongId() ) {
                    mediaPlayerCurrent.start();
                } else {
                    ;
                }

                //Regardless, we let the player continue running at the end
                mediaPlayerCurrent.start();
                //The user has already played the song
                Song.setIsPlaying(true);

                //Remember which song is playing so we can compare Ids
                Song.setPlayingSongId(Song.getCurrentSongId());

                //Show the pause button but hide the play button
                btnPause.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);

                //Assign maximum values on the as the duration of the music file
                seeker.setMax(mediaPlayerCurrent.getDuration());
                handler.postDelayed(runnable, 0);
            }
        });



        //Clicking on pause will pause the media player
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the play button but hide the pause button
                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);

                //Set song is playing to false
                Song.setIsPlaying(false);

                //Ensure our seeker and rewind button are enabled so they function properly
                seeker.setEnabled(true);
                btnRewind.setEnabled(true);

                //Pause the media player and stop the handler
                mediaPlayerCurrent.pause();
            }
        });



        //When the user presses fast forward, the music is moved forward by 10 seconds
        btnFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //First Check if the user is looking at a different song, or the same song while music is playing
                //If its a different song, first release the existing song so they don't loop
                //Then, assign mediaPlayerCurrent to the mediaPlayer for the new song we want to play
                //If the user is not playing a Song (i.e. the first time they load app) we assign our mediaPlayerCurrent to the one we create always
                if (Song.isPlaying() == false) {
                    mediaPlayerCurrent = mediaPlayer;
                } else if (Song.isPlaying() && Song.getPlayingSongId() != Song.getCurrentSongId()) {
                    //Otherwise, if a song is playing we need to stop the existing instance before assigning it, but only if it is a different song
                    mediaPlayerCurrent.stop();
                    mediaPlayerCurrent.release();
                    mediaPlayerCurrent = mediaPlayer;
                    //Otherwise, if the user is playing a Song, and it is the same song they clicked Play on previously...we just let it continue playing
                } else {
                    ;
                }

                //Immediately enable our seeker and rewind buttons
                seeker.setEnabled(true);
                btnRewind.setEnabled(true);

                //Get current position and duration of the mediaPlayer
                int position = mediaPlayerCurrent.getCurrentPosition();
                    //Get the duration of the media playe
                    //If the current position of the media player is not at the end, fast forward the song by 5 seconds
                int d = mediaPlayerCurrent.getDuration();
                if (position != d) {
                    //Note we are using milliseconds
                    position += 5000;
                    mediaPlayerCurrent.start();
                    //The user is playing a song
                    Song.setIsPlaying(true);
                    //seek to 5 seconds ahead of current time and change the playerPosition's text to 5 seconds ahead
                    mediaPlayerCurrent.seekTo(position);
                    playerPosition.setText(convertFormat(position));

                    //Remember which song is playing so we can compare Ids
                    Song.setPlayingSongId(Song.getCurrentSongId());

                    //Show the pause button but hide the play button
                    btnPause.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.GONE);

                    //Assign maximum values on the as the duration of the music file
                    seeker.setMax(mediaPlayerCurrent.getDuration());
                    handler.postDelayed(runnable, 0);
                }
            }
        });



        //Pressing rewind will take the user back by 5 seconds in the song
        btnRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the song is a different song, it will be at 0, so disable the btnRewind Button
                if (Song.getPlayingSongId() != Song.getCurrentSongId()) {
                    btnRewind.setEnabled(false);
                } else if (Song.getCurrentSongId() == Song.getCurrentSongId()) {
                    //Otherwise if it is the same song
                    btnRewind.setEnabled(true);
                } else {
                    //Otherwise user has not clicked Play on any song yet
                    btnRewind.setEnabled(false);
                }

                //Get current position
                int position = mediaPlayerCurrent.getCurrentPosition();



                //If the song is at least 5 seconds in, we can rewind back by 5 seconds
                if (position > 5000) {
                    position -= 5000;

                    //Set the current progress on seekBar and change the TextView to the position of the player
                    //Show the pause button but hide the play button
                    btnPause.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.GONE);
                    mediaPlayerCurrent.start();
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
                    //If playing same song, seek to new position
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

                // Retrieve the current number of plays of the currently selected song
                int newPlays = MainActivity.songsTemp.get(MainActivity.intSong).getPlays() + 1;

                //The id of our database starts from 1, so we add 1
                int id = MainActivity.intSong + 1;

                //Update play count in database by 1
                databaseHandler.updatePlays(id, newPlays);
                //Update songsTemp with the new Plays
                databaseHandler.refreshData(databaseHandler.getSongs(), MainActivity.songsTemp);
                //Update the textView with the new amount
                int playCount = MainActivity.songsTemp.get(MainActivity.intSong).getPlays();
                mPlays.setText(String.valueOf(playCount) + " Plays");
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
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);

        //Initialize image
        ImageView imageView = findViewById(R.id.imgAlbum);

        //Initialize string values
        String song = MainActivity.songsTemp.get(MainActivity.intSong).getSong();
        String singer = MainActivity.songsTemp.get(MainActivity.intSong).getSinger();
        String genre = MainActivity.songsTemp.get(MainActivity.intSong).getGenre();
        String rating = String.valueOf(MainActivity.songsTemp.get(MainActivity.intSong).getRating());
        String description = MainActivity.songsTemp.get(MainActivity.intSong).getDescription();
        String plays = String.valueOf(MainActivity.songsTemp.get(MainActivity.intSong).getPlays());


        //Set the text of the TextViews for Song, Singer, Genre and Rating
        //Note: this can be simplified by creating new Strings to store those values, instead of having to use getters and setters each time
        mSong.setText(song);
        mSinger.setText(singer);
        mGenre.setText(genre);
        mRating.setText(rating);
        ratingBar.setRating(Float.parseFloat(rating));
        mPlays.setText(plays + " Plays");
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
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Float newRating = Float.parseFloat(String.valueOf(ratingBar.getRating()));
                //Next, obtain the currentId
                int id = MainActivity.intSong + 1;
                //Then execute the updateRating method
                databaseHandler.updateRating(id, newRating);
                databaseHandler.refreshData(databaseHandler.getSongs(), MainActivity.songsTemp);
                ratingBar.setRating(newRating);
                mRating.setText(String.valueOf(newRating));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
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
}