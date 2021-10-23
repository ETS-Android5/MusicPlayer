package au.edu.unsw.infs3634.musicrecommender;

import java.util.ArrayList;

public class Song {

    //Declare attributes
    private String song;
    private String singer;
    private Float rating;
    private String genre;

    //Declare constructor
    public Song (String song, String singer, float rating, String genre) {
        this.song = song;
        this.singer = singer;
        this.rating = rating;
        this.genre = genre;
    }

    //Declare empty constructor
    public Song () {
    }

    //Declare getter and setters
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

    public static ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        songs.add(new Song("Endless Motion", "Benjamin Tissot", 3.4f, "Electro"));
        songs.add(new Song("Tech House vibes", "Alejandro Magaña", 3.8f, "Electronic"));
        songs.add(new Song("C.B.P.D", "Arulo", 4.2f, "Hip Hop"));
        songs.add(new Song("Dreams", "Benjamin Tissot", 3.9f, "Electro"));
        songs.add(new Song("Happiness", "Benjamin Tissot", 4.1f, "Folk"));
        songs.add(new Song("Complicated", "Arulo", 4.2f, "Hip Hop"));
        songs.add(new Song("Sports Highlights", "Ahjay Stelino", 3.1f, "Rock"));
        songs.add(new Song("Sneaky Snitch", "Kevin Macleod", 4.9f, "Classical"));
        return songs;
    }
}
