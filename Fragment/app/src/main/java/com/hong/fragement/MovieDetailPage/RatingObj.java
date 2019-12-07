package com.hong.fragement.MovieDetailPage;

public class RatingObj {
    private String id;
    private String review;
    private float score;

    public RatingObj() {
    }

    public RatingObj(String id, String review, float score) {
        this.id = id;
        this.review = review;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
