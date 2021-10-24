package au.edu.unsw.infs3634.musicrecommender;

import java.util.ArrayList;

public class Song {

    //Declare attributes
    private int id;
    private String song;
    private String singer;
    private String genre;
    private Float rating;
    private String description;
    private int image;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    //Declare constructor
    public Song (int id, String song, String singer, String genre, float rating, String description, int image) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.genre = genre;
        this.rating = rating;
        this.description = description;
        this.image = image;
    }

    //Declare empty constructor
    public Song () {
    }



    //Declare getter and setters

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setName(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        songs.add(new Song(1, "Endless Motion", "Benjamin Tissot", "Electro",3.4f,  "Benjamin produced this hypnotic, but otherwise awesome electronic dance track.", R.drawable.image_1));
        songs.add(new Song(2, "Tech House vibes", "Alejandro Magaña", "Electronic",3.8f,  "Alejandro's 'Tech House vibes' is  relaxing, mellow and smooth electronic and house mashup.", R.drawable.image_2));
        songs.add(new Song(3, "C.B.P.D", "Arulo", "Hip Hop",4.2f,  "Produced in 2016, Arulo's 'C.B.P.D' is sad, dramatic alternative style hip-hop beat.", R.drawable.image_1));
        songs.add(new Song(4, "Dreams", "Benjamin Tissot", "Electro",3.9f,  "Sample Description - this will be changed later.", R.drawable.image_2));
        songs.add(new Song(5, "Happiness", "Benjamin Tissot", "Folk",4.1f,  "Sample Description - this will be changed later.", R.drawable.image_1));
        songs.add(new Song(6, "Complicated", "Arulo", "Hip Hop",4.2f,  "Sample Description - this will be changed later.", R.drawable.image_2));
        songs.add(new Song(7, "Sports Highlights", "Ahjay Stelino","Rock", 3.1f,  "Sample Description - this will be changed later.", R.drawable.image_1));
        songs.add(new Song(8, "Sneaky Snitch", "Kevin Macleod","Classical", 4.9f,  "Sample Description - this will be changed later.", R.drawable.image_2));
        return songs;
    }
}
