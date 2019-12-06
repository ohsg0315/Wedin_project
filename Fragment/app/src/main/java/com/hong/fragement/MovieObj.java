package com.hong.fragement;

import java.util.HashMap;
import java.util.Map;

public class MovieObj {
    private  String title;
    private  String imageUri;
    private  String summary;
    private Map<String, Integer> price = new HashMap<>();

    public MovieObj(){}

    public MovieObj(String imageUri, Map<String, Integer> price, String summary, String title ){
        this.title = title;
        this.imageUri = imageUri;
        this.summary = summary;
        this.price = price;
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
}
