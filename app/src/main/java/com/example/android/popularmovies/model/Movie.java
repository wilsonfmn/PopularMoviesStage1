package com.example.android.popularmovies.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by willf on 25/03/2018.
 */

public class Movie implements Serializable {

    private int movieId;
    private String originalTitle;
    private String title;
    private String overview;
    private String posterPath;
    private double popularity;
    private double voteAVG;
    private int voteCount;
    private String releaseDate;
    private Drawable fakeMoviePoster;

    public int getMovieId() {
        return this.movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w185/" + this.posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getPopularity() {
        return this.popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVoteAVG() {
        return this.voteAVG;
    }

    public void setVoteAVG(double voteAVG) {
        this.voteAVG = voteAVG;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Drawable getFakeMoviePoster() {
        return this.fakeMoviePoster;
    }

    public void setFakeMoviePoster(Drawable fakePoster) {
        this.fakeMoviePoster = fakePoster;
    }

}
