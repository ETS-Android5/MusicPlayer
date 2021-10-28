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
    private int musicFile;
    private int plays;
    public static boolean playing = false;


    //To check if user clicked on the same song which is currently playing music
    public static int currentSongId;
    public static int playingSongId;

    //Declare constructor
    public Song (int id, String song, String singer, String genre, float rating, String description, int image, int musicFile, int plays) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.genre = genre;
        this.rating = rating;
        this.description = description;
        this.image = image;
        this.musicFile = musicFile;
        this.plays = plays;
    }

    //Declare empty constructor
    public Song () {
    }


//    //A boolean stored in primary memory
//    //When the user clicks on play the first time of a song, set to true
//    //Set to false when the user leaves the detail activity and tries to play a different song
//    public static boolean alreadyPlayed = false;



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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getMusicFile() {
        return musicFile;
    }

    public void setMusicFile(int musicFile) {
        this.musicFile = musicFile;
    }


    public int getPlays() {
        return plays;
    }

    public void setPlays(int plays) {
        this.plays = plays;
    }


    public static boolean isPlaying() {
        return playing;
    }

    public static void setIsPlaying(boolean playing) {
        Song.playing = playing;
    }

    public static int getCurrentSongId() {
        return currentSongId;
    }

    public static void setCurrentSongId(int currentSongId) {
        Song.currentSongId = currentSongId;
    }

    public static int getPlayingSongId() {
        return playingSongId;
    }

    public static void setPlayingSongId(int playingSongId) {
        Song.playingSongId = playingSongId;
    }


    public static ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        songs.add(new Song(1, "Endless Motion", "Benjamin Tissot", "Electro",3.4f,  "'Endless Motion' is a good, moderate paced song suitable for electro fans who want balanced, but catchy modern tunes.", R.drawable.img1, R.raw.song01, 0));
        songs.add(new Song(2, "Sunny", "Benjamin Tissot", "Acoustic",3.2f,  "Tissot's 'Sunny' incorporates uplifting melodies and is a great song to listen to during the summer holidays when travelling.", R.drawable.img2, R.raw.song02, 0));
        songs.add(new Song(3, "Tech House Vibes", "Alejandro Magaña", "Electronic",3.8f,  "If you feel like relaxing, Alejandro's 'Tech House Vibes' is great at getting listeners to experience what living in a modern home feels like.", R.drawable.img3, R.raw.song03, 0));
        songs.add(new Song(4, "C.B.P.D", "Arulo", "Hip Hop",4.2f,  "'C.B.P.D' is a a great hip hop beat produced and composed by Arulo, that is both catchy to listen to and a great for hip hop dancers.", R.drawable.img4, R.raw.song04, 0));
        songs.add(new Song(5, "Dreams", "Benjamin Tissot", "Electro",3.9f,  "A chill and mesmerizing electro beat by Tissot that incorporates a variety of electronic instruments.", R.drawable.img5, R.raw.song05,0));
        songs.add(new Song(6, "Happiness", "Benjamin Tissot", "Folk",4.1f,  "'Happiness' incorporates a variety of acoustic instruments to create an uplifting beat with calming melodies.", R.drawable.img6, R.raw.song06, 0));
        songs.add(new Song(7, "Complicated", "Arulo", "Hip Hop",4.2f,  "Hip-hop mashup 'Complicated' was composed in 2016 by artist Arulo as a soothing, but engaging hip hop beat.", R.drawable.img7, R.raw.song07, 0));
        songs.add(new Song(8, "Sports Highlights", "Ahjay Stelino","Rock", 3.1f,  "Ahjay Stelino composed 'Sports Highlights', which features energetic, exciting rock and roll style beats.", R.drawable.img8, R.raw.song08, 0));
        songs.add(new Song(9, "Sneaky Snitch", "Kevin Macleod","Classical", 4.9f,  "Macleod's 'Sneaky Snitch' has been used widely around the world as a popular song used by content creators due to its humorous elements.", R.drawable.img9, R.raw.song09, 0));
        songs.add(new Song(10, "The Jazz Piano", "Benjamin Tissot","Jazz", 4.9f,  "Benjamin Tissot's 'The Jazz Piano' is a free jazz soundtrack that has a catchy melody that is easy to get addicted to.", R.drawable.img1013, R.raw.song10, 0));
        songs.add(new Song(11, "Rumble", "Benjamin Tissot","Rock", 3.9f,  "'Rumble' is Tissot's second rock and roll beat, with a much subtler beat and melody, featuring drum and bass instrumentals.", R.drawable.img1013, R.raw.song11, 0));
        songs.add(new Song(12, "A New Beginning", "Benjamin Tissot","Pop Rock", 3.9f,  "'A New Beginning' was produced by Tissot, and offers an uplifting, motivational and inspiring tune, with a strong focus on piano.", R.drawable.img1013, R.raw.song12, 0));
        songs.add(new Song(13, "High Octane", "Benjamin Tissot","Rock", 3.9f,  "Tissot's 'High Octane' is a high-action, rock and roll style song, suitable for use in action movies or active workouts for gym-goers.", R.drawable.img1013, R.raw.song13, 0));
        songs.add(new Song(14, "About You", "de Pablos","Electronic", 4.2f,  "de Pablos' 'About You' is a catchy electronic beat, popular with younger listeners, and is free to download from his SoundCloud platform.", R.drawable.img14, R.raw.song14, 0));
        songs.add(new Song(15, "JIGLR", "Gang","Electronic", 4.9f,  "JIGLR's 'Gang' is a catchy electronic beat, falling into the contemporary 'Trap' category, which is popular amongst millenials.", R.drawable.img15, R.raw.song15, 0));
        return songs;
    }
}
