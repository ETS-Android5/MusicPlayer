package au.edu.unsw.infs3634.musicrecommender;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //NOTE: This section must always go first. This links your Java class to the XML you describe
        //If you do not include this, will not be able to find any R.ids (will return Null Pointer Exception)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //Create and initialize variables for each TextView that holds data on each country
        TextView mSong = findViewById(R.id.tvSong);
        TextView mSinger = findViewById(R.id.tvSinger);
        TextView mGenre = findViewById(R.id.tvGenre);
        TextView mRating = findViewById(R.id.tvRating);
        TextView mDescription = findViewById(R.id.tvDescription);

        //Initialize button for integration with browser
        ImageButton mSearch = findViewById(R.id.btnSearch);

        //Initialize string values
        String song = MainActivity.songsTemp.get(MainActivity.intSong).getSong();
        String singer = MainActivity.songsTemp.get(MainActivity.intSong).getSinger();
        String genre = MainActivity.songsTemp.get(MainActivity.intSong).getSong();
        String rating = String.valueOf(MainActivity.songsTemp.get(MainActivity.intSong).getRating());
        String description = MainActivity.songsTemp.get(MainActivity.intSong).getDescription();

        //Set the text of the TextViews for Song, Singer, Genre and Rating
        //Note: this can be simplified by creating new Strings to store those values, instead of having to use getters and setters each time
        mSong.setText(song);
        mSinger.setText(singer);
        mGenre.setText(genre);
        mRating.setText(rating);
        mDescription.setText(description);

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
    }
}