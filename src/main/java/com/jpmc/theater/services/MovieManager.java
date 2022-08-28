package com.jpmc.theater.services;


import com.jpmc.theater.data.model.Movie;
import com.jpmc.theater.exception.NoDataFoundException;
import com.jpmc.theater.utills.IdGenerator;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MovieManager {

    private final Map<String, Movie> movieMap;

    public MovieManager() {
        this.movieMap = new HashMap<>();
    }

    public Movie addMovie(String title, Duration runningTime) {
        String id = IdGenerator.nextId(movieMap, "M");
        Movie newMovie = new Movie(id, title, runningTime);
        movieMap.put(id, newMovie);
        return newMovie;

    }

    public Movie removeMovie(String id) {
        if (!movieMap.containsKey(id)) {
            NoDataFoundException.throwException("Movie", id);
        }
        return movieMap.remove(id);

    }

    public Movie getMovie(String movieId) {
        Movie movie = getMovieMap().get(movieId);
        if (movie == null) {
            NoDataFoundException.throwException("Movie", movieId);
        }
        return movie;
    }

    public Map<String, Movie> getMovieMap() {
        return movieMap;
    }
}
