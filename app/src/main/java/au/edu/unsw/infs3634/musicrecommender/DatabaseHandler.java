package au.edu.unsw.infs3634.musicrecommender;

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
import java.util.List;

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
                + COLUMN_SONG_IMAGE + " BLOB"
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
    public boolean addSong(Song song, byte[] byteImage) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Need to instantiate a new instance of ContentValues to access values method
        ContentValues cv = new ContentValues();

        //Associate columns of the table with the actual value we pass in from the parameter
        cv.put(COLUMN_SONG_NAME, song.getSong());
        cv.put(COLUMN_SONG_SINGER, song.getSinger());
        cv.put(COLUMN_SONG_GENRE, song.getGenre());
        cv.put(COLUMN_SONG_RATING, song.getRating());
        cv.put(COLUMN_SONG_DESCRIPTION, song.getDescription());
        cv.put(COLUMN_SONG_IMAGE, song.getDescription());
        cv.put(COLUMN_SONG_IMAGE, byteImage);
        //Note, we do not need to associate ID with anything, because it is an AUTOINCREMENT column in our database

        //Insert the values into the database
        //insert returns a long, -1 means failed add while 1 means successful add
        long insert = db.insert(SONG_TABLE, null, cv);
        if (insert ==-1) {
            return false;
        } else {
            return true;
        }
    }


        // Method to return a list of all our Songs in our table from our Database
        public List<Song> getSongs() {
            List<Song> returnSongList = new ArrayList<Song>();
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
                    // Adding song to returnList
                    //Create a new song using these values
//                    Song song = new Song(songId, songName, songSinger, songGenre, songRating, songDescription);
//                    //Now we just add our new song to our return List
//                    returnSongList.add(song);

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
//
//        // code to update the single contact
//        public int updateContact(Contact contact) {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(KEY_NAME, contact.getName());
//            values.put(KEY_PH_NO, contact.getPhoneNumber());
//
//            // updating row
//            return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//                    new String[] { String.valueOf(contact.getID()) });
//        }
//
//        // Deleting single contact
//        public void deleteContact(Contact contact) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
//                    new String[] { String.valueOf(contact.getID()) });
//            db.close();
//        }
//
//        // Getting contacts Count
//        public int getContactsCount() {
//            String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(countQuery, null);
//            cursor.close();
//
//            // return count
//            return cursor.getCount();
//        }
//
//    }

    //    //Method to return a Song's values, based on the song's Id
//    Song getSong(int id) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        //Need to instantiate a Cursor
//        //A cursor is a result set from a query operation in SQLite
//        //Need to loop through the cursor items and process each line of the search result
//        Cursor cursor = sqLiteDatabase.query(tableSongs, new String[] {
//                keyId, keySong, keySinger, keyGenre, keyRating, keyDescription + " =?",
//                new String[] {
//                        String.valueOf(id)
//                }, null, null, null, null, null, null);
//        if (cursor!= null)
//            cursor.moveToFirst();
//
//        Song song = new Song(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getFloat(2), cursor.getString(), cursor.getString())
//
//        return song;
//    }

//        // code to get the single contact
//        Contact getContact(int id) {
//            SQLiteDatabase db = this.getReadableDatabase();
//
//            Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
//                            KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
//                    new String[] { String.valueOf(id) }, null, null, null, null);
//            if (cursor != null)
//                cursor.moveToFirst();
//
//            Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
//                    cursor.getString(1), cursor.getString(2));
//            // return contact
//            return contact;
//        }
//

}
