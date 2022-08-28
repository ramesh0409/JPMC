package com.jpmc.theater.data.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Show {

    private final int sequenceOfTheDay;
    private String movieId;
    private LocalDateTime showStartTime;
    private int reservedTicketCount;

    public Show(int sequenceOfTheDay, String movieId, LocalDateTime showStartTime) {
        this.sequenceOfTheDay = sequenceOfTheDay;
        this.movieId = movieId;
        this.showStartTime = showStartTime;
        reservedTicketCount = 0;
    }

    public int getSequenceOfTheDay() {
        return sequenceOfTheDay;
    }

    public String getMovieId() {
        return movieId;
    }

    public LocalDateTime getShowStartTime() {
        return showStartTime;
    }

    public int getReservedTicketCount() {
        return reservedTicketCount;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setShowStartTime(LocalDateTime showStartTime) {
        this.showStartTime = showStartTime;
    }

    public void setReservedTicketCount(int reservedTicketCount) {
        this.reservedTicketCount = reservedTicketCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Show)) return false;
        Show show = (Show) o;
        return getSequenceOfTheDay() == show.getSequenceOfTheDay() && getReservedTicketCount() == show.getReservedTicketCount() && Objects.equals(getMovieId(), show.getMovieId()) && Objects.equals(getShowStartTime(), show.getShowStartTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSequenceOfTheDay(), getMovieId(), getShowStartTime(), getReservedTicketCount());
    }
}
