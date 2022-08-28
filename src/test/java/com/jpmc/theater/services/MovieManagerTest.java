package com.jpmc.theater.services;

import com.jpmc.theater.TestBase;
import com.jpmc.theater.data.model.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.Duration;

class MovieManagerTest extends TestBase {

    MovieManager movieManager;

    @BeforeAll
    void setUp() {
        movieManager= movieManager();
    }

    @Test
    void testRemoveMovieException() {
        Executable executable=() -> movieManager.removeMovie("InvalidMovieId");
        String expectedErrorMessage="No Movie found for the given input InvalidMovieId";
        assertNoDataException(executable, expectedErrorMessage);
    }

    @Test
    public void testAddMovie(){
        movieManager.getMovieMap().clear();
        Movie movie=movieManager.addMovie("Spider-Man: No Way Home", Duration.ofMinutes(90));
        Assertions.assertTrue(movieManager.getMovieMap().size() ==1);
        Assertions.assertEquals("M1",movie.getId());
        Assertions.assertEquals("Spider-Man: No Way Home",movie.getTitle());
    }

    @Test
    void testRemoveMovieSuccess() {
        movieManager.getMovieMap().clear();
        Movie movie=movieManager.addMovie("Spider-Man: No Way Home", Duration.ofMinutes(90));
        Assertions.assertTrue(movieManager.getMovieMap().size() ==1);
        Movie removedMovie=movieManager.removeMovie(movie.getId());
        Assertions.assertTrue(movieManager.getMovieMap().size() ==0);
        Assertions.assertEquals(movie,removedMovie);
    }

}