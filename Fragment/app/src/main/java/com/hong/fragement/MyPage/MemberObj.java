package com.hong.fragement.MyPage;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class MemberObj implements Parcelable {
    private String name;
    private String year;
    private String month;
    private String day;
    private ArrayList<String> genre;
    private String email;
    private String type;

    public MemberObj() {
    }

    public MemberObj(String email, String name, String year, String month, String day, ArrayList<String> genre, String type) {
        this.email = email;
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.genre = genre;
        this.type = type;
    }

    protected MemberObj(Parcel in) {
        name = in.readString();
        year = in.readString();
        month = in.readString();
        day = in.readString();
        genre = in.createStringArrayList();
        email = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(year);
        dest.writeString(month);
        dest.writeString(day);
        dest.writeStringList(genre);
        dest.writeString(email);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MemberObj> CREATOR = new Creator<MemberObj>() {
        @Override
        public MemberObj createFromParcel(Parcel in) {
            return new MemberObj(in);
        }

        @Override
        public MemberObj[] newArray(int size) {
            return new MemberObj[size];
        }
    };

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

    public String getEmail() {return email;}

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

    public String getType() {return type; }
}
