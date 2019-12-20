package com.hong.fragement;

import java.util.HashMap;
import java.util.Map;

public class MovieObj implements Comparable<MovieObj> {
    private  String title;
    private  String imageUri;
    private  String summary;
    private Map<String, Integer> price = new HashMap<>();
    private boolean free;
    private String youtubeUri;
    private int rank;
    private String genre;

    public MovieObj(){}

    public MovieObj(String title, String imageUri, String summary, Map<String, Integer> price, boolean free, String youtubeUri, int rank, String genre) {
        this.title = title;
        this.imageUri = imageUri;
        this.summary = summary;
        this.price = price;
        this.free = free;
        this.youtubeUri = youtubeUri;
        this.rank = rank;
        this.genre = genre;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setPrice(Map<String, Integer> price) {
        this.price = price;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setFree(boolean free) { this.free = free; }

    public void setYoutubeUri(String youtubeUri) { this.youtubeUri = youtubeUri; }

    public void setRank(int rank) { this.rank = rank; }

    public String getTitle(){
        return title;
    }

    public String getImageUri(){
        return imageUri;
    }

    public String getSummary(){
        return summary;
    }

    public Map<String, Integer> getPrice(){
        return price;
    }

    public boolean isFree() {
        return free;
    }

    public String getYoutubeUri() { return youtubeUri; }

    public int getRank() { return rank; }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }

    @Override
    public int compareTo(MovieObj obj) {
        if (this.getRank()>obj.getRank()) return 1;
        else if (this.getRank()<obj.getRank()) return -1;
        else return 0;
    }
}
