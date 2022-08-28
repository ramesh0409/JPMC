package com.jpmc.theater.data.model;

import java.time.Duration;
import java.util.Objects;

public class Movie {

    private final String id;
    private final String title;
    private String description;
    private final Duration runningTime;

    public Movie(String id, String title, Duration runningTime) {
        this.id = id;
        this.title = title;
        this.runningTime = runningTime;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Duration getRunningTime() {
        return runningTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(getId(), movie.getId()) && Objects.equals(getTitle(), movie.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle());
    }
}
