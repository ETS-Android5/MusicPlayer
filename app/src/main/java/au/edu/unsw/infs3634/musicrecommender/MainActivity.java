package au.edu.unsw.infs3634.musicrecommender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //This attribute is used to track which song was clicked on by the user in the xml
    public static int intSong;

    //Declare a List, SongAdapter and RecyclerView
    //These 3 components are used to pass a list of data objects to our SongAdapter class
    public static ArrayList<Song> songsTemp = new ArrayList<>();
    private SongAdapter myAdapter;
    private RecyclerView myRecyclerView;







    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Upon creation of this Activity, we want to set the contentView to activity_main
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Need write and read external storage permissions in order to display images stored in android memory
        ActivityCompat.requestPermissions(this, new String[]
                { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE },
                PackageManager.PERMISSION_GRANTED);

        //Load the list of Songs into songsTemp
        songsTemp = Song.getSongs();

        //Instantiate our RecyclerView
        myRecyclerView = findViewById(R.id.recyclerView);
        myRecyclerView.setHasFixedSize(true);

        //Set the LayoutManager of the RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        myRecyclerView.setLayoutManager(layoutManager);

        //Need to instantiate DatabaseHandler so we can access databasehandler methods e.g. construct our database
        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
        for (Song song : songsTemp) {
            databaseHandler.addSong(song);
        }
        System.out.println("Songs Added");
        //we now have enough information to populate our table


        //Instantiate an onClickListener so that when a user clicks an item in our RecyclerView, user is taken to DetailActivity
        SongAdapter.ClickListener clickListener = new SongAdapter.ClickListener() {

            //When the user clicks on an item in our RecyclerView, it checks the songId
            //Then it takes the user to the Detail Activity, showing details for the clicked song
            @Override
            public void onSongClick(View view, int songId) {
                final Song song = songsTemp.get(songId);
                //Assign intSong as the songId so that when load the detail activity, we see the appropriate details
                intSong = songId;
                //Navigate user to DetailActivity via Intents
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        };

        //Instantiate a SongAdapter and pass in our ArrayList, and clickListner of songs as the parameter
        myAdapter = new SongAdapter(songsTemp, clickListener);
        //Link RecyclerView to Adapter
        myRecyclerView.setAdapter(myAdapter);
    }





    //Override the onCreate method for the Menu
    // @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //instantiate a menu inflater, and then assign our menu as menu_main so program can find the appropriate xml elements
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //Identify which element is our search item, and set a listener for the search, so that whenever text is changed or submitted our
        // .. filter() method applies
        SearchView searchView = (SearchView) menu.findItem(R.id.searchItem).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //Define behaviour when user presses enter
            @Override
            public boolean onQueryTextSubmit(String searchString) {
                myAdapter.getFilter().filter(searchString);
                return false;
            }

            //Define beheaviour when user enters text
            @Override
            public boolean onQueryTextChange(String searchString) {
                myAdapter.getFilter().filter(searchString);
                return false;
            }
        });

        return true;
    }





    //Implement onOptionItemSelected behaviour to define the behaviour when user clicks on Sort by Artist, or Song Name
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortSong:
                //If the id of the item was sortSong, we sort by Song
                myAdapter.sort(1);
                return true;
            case R.id.sortSinger:
                //If the id of the item was sortArtist, we sort by Artist
                myAdapter.sort(2);
                return true;
            default:
                //By default, if nothing is selected by user
                return super.onOptionsItemSelected(item);
        }
    }
}