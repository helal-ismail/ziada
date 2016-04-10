package com.trianglz.ziadashow;


import java.util.ArrayList;

public class SongItem {

    String title;
    String artist;
    String image;
    String playlist;
    public SongItem() {
    }

    public SongItem(String title, String image, String artist,String playlist){

        this.title = title;
        this.image = image;
        this.artist = artist;
this.playlist=playlist;
    }
    public String getList(){return playlist;}
    public void setList(String playlist) {this.playlist = playlist;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getArtist() {return artist;}

    public void setArtist(String artist) {this.artist = artist;}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
