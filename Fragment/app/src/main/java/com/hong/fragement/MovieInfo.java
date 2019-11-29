package com.hong.fragement;


// 영화검색, 무료영화관, TOP100 등 리스트뷰로 띄울 영화정보 객체를 위한 클래스
public class MovieInfo {
    private String title; //영화제목
    private String poster; //영화포스터
    private String story; //영화줄거리
    private int price; //영화가격
    private String genre1; //영화장르1
    private String genre2; //영화장르2
    private String genre3; //영화장르3

    public MovieInfo() {
    }

    // 생성자
    public MovieInfo(String title, String poster, String story, int price, String genre1, String genre2, String genre3) {
        this.title = title;
        this.poster = poster;
        this.story = story;
        this.price = price;
        this.genre1 = genre1;
        this.genre2 = genre2;
        this.genre3 = genre3;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getPoster() { return poster; }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getStory() { return story; }

    public int getPrice() { return price; }

    public String getGenre1() { return genre1; }

    public String getGenre2() { return genre2; }

    public String getGenre3() { return genre3; }





    public void setStory(String story) { this.story = story; }

    public void setPrice(int price) { this.price = price; }

    public void setGenre1(String genre1) {
        this.genre1 = genre1;
    }

    public void setGenre2(String genre2) {
        this.genre2 = genre2;
    }

    public void setGenre3(String genre3) {
        this.genre3 = genre3;
    }
}
