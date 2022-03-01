package au.syd.project.erwinmaysonq.musicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    //We instantiate variables so we don't need to manually retype these each time we are creating SQL statements
    private static final int dbVersion = 1;
    private static final String SONG_DB = "SONGS.DB";
    private static final String SONG_TABLE = "SONG_TABLE";
    private static final String COLUMN_SONG_ID = "SONG_ID";
    private static final String COLUMN_SONG_NAME = "SONG_NAME";
    private static final String COLUMN_SONG_SINGER = "SONG_SINGER";
    private static final String COLUMN_SONG_GENRE = "SONG_GENRE";
    private static final String COLUMN_SONG_RATING = "SONG_RATING";
    private static final String COLUMN_SONG_DESCRIPTION = "SONG_DESCRIPTION";
    private static final String COLUMN_SONG_IMAGE = "SONG_IMAGE";
    private static final String COLUMN_SONG_PLAYS = "SONG_PLAYS";


    //For the song art
    String stringFilePath = Environment.getExternalStorageDirectory().getPath();
    Bitmap bitmap = BitmapFactory.decodeFile(stringFilePath);

    //Constructor for DatabaseHandler
    public DatabaseHandler(@Nullable Context context) {
        super(context, SONG_DB, null, dbVersion);
    }


    //Create the tables of our database in SQLite
    //This method is only called the first time our database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Define the SQL query to create a new table with the following columns
        //Make sure there are spaces between String segments, and no missing commas
        String createTableSongs = "CREATE TABLE " + SONG_TABLE
                + " ("
                + COLUMN_SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SONG_NAME + " TEXT,"
                + COLUMN_SONG_SINGER + " TEXT, "
                + COLUMN_SONG_GENRE + " TEXT, "
                + COLUMN_SONG_RATING + " FLOAT, "
                + COLUMN_SONG_DESCRIPTION + " TEXT, "
                + COLUMN_SONG_IMAGE + " BLOB, "
                + COLUMN_SONG_PLAYS + " INTEGER"
                + ")";

        //Execute the SQL query to create our tables
        db.execSQL(createTableSongs);
    }

    //Whenever a database version is changed, this method will prevent existing apps users' apps from breaking when you change the database in some way
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionOld, int versionNew) {
        //Drop the older table when upgrading, if it exists
        //Then recreate it
        db.execSQL("DROP TABLE IF EXISTS " + SONG_TABLE);
        onCreate(db);
    }

    //Declare the methods to insert a Song into the database
    //This will be looped for each song in the ArrayList songs
    public boolean addSong(String song, String singer, String genre, Float rating, String description, byte[] albumImg, int plays) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Need to instantiate a new instance of ContentValues to access values method
        ContentValues cv = new ContentValues();

        //Associate columns of the table with the actual value we pass in from the parameter
        cv.put(COLUMN_SONG_NAME, song);
        cv.put(COLUMN_SONG_SINGER, singer);
        cv.put(COLUMN_SONG_GENRE, genre);
        cv.put(COLUMN_SONG_RATING, rating);
        cv.put(COLUMN_SONG_DESCRIPTION, description);
        cv.put(COLUMN_SONG_IMAGE, albumImg);
        cv.put(COLUMN_SONG_PLAYS, plays);

        //Note, we do not need to associate ID with anything, because it is an AUTOINCREMENT column in our database

        //Insert the values into the database
        //insert returns a long, -1 means failed add while 1 means successful add
        long insert = db.insert(SONG_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Method to return a list of all our Songs in our table from our Database
    public ArrayList<Song> getSongs() {
        ArrayList<Song> returnSongList = new ArrayList<Song>();
        // SQL statement to return all rows from our database
        String querySelect = "SELECT  * FROM " + SONG_TABLE;

        //Note: we use getReadableDatabase() instead of getWriteable because writeable will lock up the Database from other processes - can bottleneck other processes
        SQLiteDatabase db = this.getReadableDatabase();

        //We now execute our query and store our results in a Cursor - a cursor is used to store a resultSet
        //Selection args is used when we are using Prepared Statements; so leave as null otherwise
        Cursor cursor = db.rawQuery(querySelect, null);
        //we check if we returned any results from our query
        //moveToFirst() returns true if Java is able to go to the first row and there are results
        //If there are results in our cursor, loop through the cursor (Result Set) and create new Song objects, and add them to our songList
        if (cursor.moveToFirst()) {
            do {
                //Assign values of Song variables as the columns in our cursor resultSet
                //Note we need to know what are the column numbers of our resultSet
                int songId = cursor.getInt(0);
                String songName = cursor.getString(1);
                String songSinger = cursor.getString(2);
                String songGenre = cursor.getString(3);
                float songRating = cursor.getFloat(4);
                String songDescription = cursor.getString(5);
                int songPlays = cursor.getInt(7);

                // Adding song to returnList
                //Create a new song using these values
                //We leave the image and musicFile as 0 because we assume that user cannot change the image or audio file
                Song song = new Song(songId, songName, songSinger, songGenre, songRating, songDescription, 0, 0, songPlays);
                //Now we just add our new song to our return List
                returnSongList.add(song);

                //Loop through adding in Songs until there is no more rows in our Cursor (Result set) - important to use moveToNext()
                //moveToNext means while there is still a row to move next to
                //Using moveToFirst() will result in an infinite loop, so do not use that
            } while (cursor.moveToNext());

            //If our query returned no results:
        } else {
            //If there is no results in our query, do nothing
            ;
        }
        //When we are done, we just need to close the connection to the cursor and database so other resources can use it
        cursor.close();
        db.close();

        //Our method must return a list...
        return returnSongList;
    }

    //Method to update the rating of a song in the database
    public void updateRating(int id, Float rating) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SONG_RATING, rating);

        //Update the rating of the Song, where the COLUMN_SONG_ID is equal to the parameter id
        db.update(SONG_TABLE, cv,COLUMN_SONG_ID + "= ?", new String[] {Integer.toString(id)});
    }

    //Method to update play count of a song in the database
    public void updatePlays(int id, int plays) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SONG_PLAYS, plays);

        //Update the rating of the Song, where the COLUMN_SONG_ID is equal to the parameter id
        db.update(SONG_TABLE, cv,COLUMN_SONG_ID + "= ?", new String[] {Integer.toString(id)});
    }



    //Refresh the data
    //Only 2 variables are tracked/modifiable so we just refresh them whenever something changes
    //songsDatabase is the database's returned songs whilst songsList is what we want to change i.e.g songsTemp
    public void refreshData(ArrayList<Song> songsDatabase, ArrayList<Song> songsList) {
        int i = 0;
        for (Song song : songsDatabase) {

            //Get the rating in the database of each song
            int dbPlays = song.getPlays();
            float dbRating = song.getRating();

            //Set the rating of each song in songsTemp as the rating of each song in songsDatabase
            songsList.get(i).setPlays(dbPlays);
            songsList.get(i).setRating(dbRating);
            //Increment by 1
            i += 1;
        }
    }
}