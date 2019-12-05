package com.hong.fragement.MyPage;

import java.io.Serializable;
import java.util.ArrayList;

public class MemberObj {
    private String name;
    private String year;
    private String month;
    private String day;
    private ArrayList<String> genre;

    public MemberObj() {
    }

    public MemberObj(String name, String year, String month, String day, ArrayList<String> genre) {
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.genre = genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }
}
