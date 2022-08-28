package com.jpmc.theater.services;

import com.jpmc.theater.TestBase;
import com.jpmc.theater.data.model.Movie;
import com.jpmc.theater.data.model.Show;
import com.jpmc.theater.data.model.Theater;
import com.jpmc.theater.utills.LocalDateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

class TheatreManagerTest extends TestBase {

    TheatreManager theatreManager;
    Theater theater;

    @BeforeEach
    void setUp() {
        theatreManager = theatreManager();
        theater = theatreManager.createTheatre("My Theatre", 100);
        //create initial data
        theatreManager.createShow(theater);
    }

    @Test
    public void testCreateShow() {

        Assertions.assertTrue(theatreManager.getTheaterMap().size() > 0);
        Assertions.assertEquals(9, theater.getCurrentShows().size());
        Show show = theatreManager.getShowManager().getShow(1);
        Movie movie = theatreManager.getMovieManager().getMovie(show.getMovieId());
        BigDecimal price = theatreManager.getPricingService().getTicketPrice(movie);
        Assertions.assertEquals("Turning Red", movie.getTitle());
        Assertions.assertEquals(BigDecimal.valueOf(11.0), price);
        Assertions.assertFalse(theatreManager.getPricingService().getSpecialDiscountMovies().contains(movie.getId()));
        //First Show discount
        BigDecimal discountedPrice = theatreManager.getPricingService().getDiscountTicketPrice(movie, show);
        Assertions.assertEquals(BigDecimal.valueOf(8.0), discountedPrice);
    }

    @Test
    public void testSpecialDiscountAndSecondShow_Max() {
        Show show = theatreManager.getShowManager().getShow(2);
        Movie movie = theatreManager.getMovieManager().getMovie(show.getMovieId());
        BigDecimal price = theatreManager.getPricingService().getTicketPrice(movie);
        Assertions.assertEquals("Spider-Man: No Way Home", movie.getTitle());
        Assertions.assertEquals(BigDecimal.valueOf(12.5), price);
        Assertions.assertTrue(theatreManager.getPricingService().getSpecialDiscountMovies().contains(movie.getId()));
        //20% discount price. Second show discount also applicable for this movie however we applied only the max amout that is 20%
        BigDecimal discountedPrice = theatreManager.getPricingService().getDiscountTicketPrice(movie, show);
        Assertions.assertEquals(BigDecimal.valueOf(10.0), discountedPrice);

    }

    @Test
    public void testSecondShowDiscount() {
        Show show = theatreManager.getShowManager().getShow(2);
        Movie movie = theatreManager.getMovieManager().getMovie(show.getMovieId());
        BigDecimal price = theatreManager.getPricingService().getTicketPrice(movie);
        Assertions.assertEquals("Spider-Man: No Way Home", movie.getTitle());
        Assertions.assertEquals(BigDecimal.valueOf(12.5), price);
        //Remove 20% discount
        theatreManager.getPricingService().removeSpecialDiscount(movie.getId());
        Assertions.assertFalse(theatreManager.getPricingService().getSpecialDiscountMovies().contains(movie.getId()));
        //20% discount is removed . Hence now highest $1 second show discount
        BigDecimal discountedPrice = theatreManager.getPricingService().getDiscountTicketPrice(movie, show);
        Assertions.assertEquals(BigDecimal.valueOf(11.5), discountedPrice);

    }

    @Test
    public void testNewDiscountRules_25_Percent() {
        Show show = theatreManager.getShowManager().getShow(2);
        Movie movie = theatreManager.getMovieManager().getMovie(show.getMovieId());
        BigDecimal price = theatreManager.getPricingService().getTicketPrice(movie);
        Assertions.assertEquals(BigDecimal.valueOf(12.5), price);
        Assertions.assertEquals(11, show.getShowStartTime().getHour());
        //20% discount
        BigDecimal discountedPrice = theatreManager.getPricingService().getDiscountTicketPrice(movie, show);
        Assertions.assertEquals(BigDecimal.valueOf(10.0), discountedPrice);
        //Now add new discounts
        theatreManager.addNewDiscount();
        //Show 2 starts 11 AM . Hence it is applicable for 25% discount
        discountedPrice = theatreManager.getPricingService().getDiscountTicketPrice(movie, show);
        Assertions.assertEquals(BigDecimal.valueOf(9.375), discountedPrice);

    }

    @Test
    public void testNewDiscountRules_25_Percent_11AM_4PM() {
        Show show = theatreManager.getShowManager().getShow(3);
        Movie movie = theatreManager.getMovieManager().getMovie(show.getMovieId());
        theatreManager.getPricingService().setPrice(movie.getId(), 20);
        show.setMovieId(movie.getId());
        BigDecimal price = theatreManager.getPricingService().getTicketPrice(movie);
        Assertions.assertEquals(BigDecimal.valueOf(20.0), price);
        BigDecimal discountPrice=theatreManager.getPricingService().getDiscountTicketPrice(movie,show);
        Assertions.assertEquals(discountPrice, price);
        //Add time discount
        theatreManager.addNewDiscount();
        //Set show time as 11 AM
        assertTimeBasedDiscount(show, movie,11,0,15.0);
        //Set show time 2.30 PM
        assertTimeBasedDiscount(show, movie,14,30,15.0);
        //Set show time 3.59 PM
        assertTimeBasedDiscount(show, movie,15,59,15.0);
        //Set show time 4 PM. No discount applied
        assertTimeBasedDiscount(show, movie,16,0,20.0);


    }

    private void assertTimeBasedDiscount(Show show, Movie movie,int hours,int minutes,double expected) {
        LocalDate currDate= LocalDateProvider.currentDate();
        LocalDateTime dateTime=LocalDateProvider.getLocalDateTime(currDate,hours,minutes);
        show.setShowStartTime(dateTime);
        BigDecimal discountPrice=theatreManager.getPricingService().getDiscountTicketPrice(movie, show);
        Assertions.assertEquals(BigDecimal.valueOf(expected), discountPrice);

    }

    @Test
    public void testNewDiscountRules_Show_7() {
        Show show = theatreManager.getShowManager().getShow(7);
        Movie movie = theatreManager.getMovieManager().getMovie(show.getMovieId());
        BigDecimal price = theatreManager.getPricingService().getTicketPrice(movie);
        Assertions.assertEquals(BigDecimal.valueOf(11.0), price);
        //No Discount
        BigDecimal discountedPrice = theatreManager.getPricingService().getDiscountTicketPrice(movie, show);
        Assertions.assertEquals(price, discountedPrice);
        //Now add new discounts
        theatreManager.addNewDiscount();
        //Show 7 $1 discount applied
        discountedPrice = theatreManager.getPricingService().getDiscountTicketPrice(movie, show);
        Assertions.assertEquals(BigDecimal.valueOf(10.0), discountedPrice);

    }

    @Test
    public void testPrintSchedule() {
        theatreManager.printShowSchedule();
        Assertions.assertTrue(true);

    }


}