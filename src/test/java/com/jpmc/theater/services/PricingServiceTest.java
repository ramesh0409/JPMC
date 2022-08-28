package com.jpmc.theater.services;

import com.jpmc.theater.TestBase;
import com.jpmc.theater.data.model.Movie;
import com.jpmc.theater.data.model.Show;
import com.jpmc.theater.utills.LocalDateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingServiceTest extends TestBase {

    PricingService pricingService;
    MovieManager movieManager;
    ShowManager showManager;

    @BeforeAll
    void setUp() {
        pricingService=pricingService();
        movieManager=movieManager();
        showManager=showManager();
    }

    @Test
    public void testInit(){
        Map<String, Function<Show,Double>> showRuleSetMap=pricingService.getShowRuleSetMap();
        Assertions.assertTrue(showRuleSetMap.size() ==2);
        Assertions.assertTrue(showRuleSetMap.containsKey("RuleShow1"));
        Assertions.assertTrue(showRuleSetMap.containsKey("RuleShow2"));
        //Movie rule
        Map<String, BiFunction<Movie,Show,Double>> movieRuleMap=pricingService.getMovieRuleMap();
        Assertions.assertTrue(movieRuleMap.size() ==1);
        Assertions.assertTrue(movieRuleMap.containsKey("RuleM1"));
    }

    @Test
    public void testSetTicketPrice(){
        movieManager.getMovieMap().clear();
        Movie movie=movieManager.addMovie("Spider-Man: No Way Home", Duration.ofMinutes(90));
        pricingService.setPrice(movie.getId(),11.25);
        BigDecimal price=pricingService.getTicketPrice(movie);
        Assertions.assertEquals(BigDecimal.valueOf(11.25),price);

    }

    @Test
    public void testFirstShowDiscount(){
        pricingService.getSpecialDiscountMovies().clear();
        movieManager.getMovieMap().clear();
        Movie movie=movieManager.addMovie("Spider-Man: No Way Home", Duration.ofMinutes(90));
        LocalDate currentDate= LocalDateProvider.currentDate();
        showManager.getShowMap().clear();
        Show firstShow=showManager.createShow(movie.getId(), currentDate,9,0);

        //Set movie ticket price
        pricingService.setPrice(movie.getId(),20);
        BigDecimal price=pricingService.getTicketPrice(movie);
        BigDecimal discountPrice =pricingService.getDiscountTicketPrice(movie,firstShow);
        //First show discount $3 Dollar applied
        assertEquals(BigDecimal.valueOf(20.0),price);
        assertEquals(BigDecimal.valueOf(17.0),discountPrice);

    }

    @Test
    public void testSecondShowDiscount(){
        pricingService.getSpecialDiscountMovies().clear();
        movieManager.getMovieMap().clear();
        Movie movie=movieManager.addMovie("Spider-Man: No Way Home", Duration.ofMinutes(90));
        LocalDate currentDate= LocalDateProvider.currentDate();
        showManager.getShowMap().clear();
        Show firstShow=showManager.createShow("dummy", currentDate,9,0);
        //Add to second show
        Show secondShow=showManager.createShow(movie.getId(), currentDate,9,0);
        //Set movie ticket price
        pricingService.setPrice(movie.getId(),20);
        BigDecimal price=pricingService.getTicketPrice(movie);
        BigDecimal discountPrice =pricingService.getDiscountTicketPrice(movie,secondShow);
        //Second show   discount $1 Dollar applied
        assertEquals(BigDecimal.valueOf(20.0),price);
        assertEquals(BigDecimal.valueOf(19.0),discountPrice);

    }

    @Test
    public void testSprcialDiscount(){
        pricingService.getSpecialDiscountMovies().clear();
        movieManager.getMovieMap().clear();
        Movie movie=movieManager.addMovie("Spider-Man: No Way Home", Duration.ofMinutes(90));
        LocalDate currentDate= LocalDateProvider.currentDate();
        showManager.getShowMap().clear();
        Show firstShow=showManager.createShow(movie.getId(), currentDate,9,0);
        //Add to second show
        Show secondShow=showManager.createShow(movie.getId(), currentDate,9,0);
        //Third show which has no discount
        Show thirdShow=showManager.createShow(movie.getId(), currentDate,9,0);
        //Set movie ticket price
        pricingService.setPrice(movie.getId(),20);
        BigDecimal price=pricingService.getTicketPrice(movie);
        BigDecimal discountPrice =pricingService.getDiscountTicketPrice(movie,thirdShow);
        //FNo Show discount applied
        assertEquals(BigDecimal.valueOf(20.0),price);
        assertEquals(price,discountPrice);

        //Set special discount 20%
        pricingService.setSpecialDiscount(movie.getId());
        discountPrice =pricingService.getDiscountTicketPrice(movie,thirdShow);
        assertEquals(BigDecimal.valueOf(16.0),discountPrice);

    }

    @Test
    public void testMaxDiscount(){
        pricingService.getSpecialDiscountMovies().clear();
        movieManager.getMovieMap().clear();
        Movie movie=movieManager.addMovie("Spider-Man: No Way Home", Duration.ofMinutes(90));
        LocalDate currentDate= LocalDateProvider.currentDate();
        showManager.getShowMap().clear();
        Show firstShow=showManager.createShow(movie.getId(), currentDate,9,0);

        //Set movie ticket price
        pricingService.setPrice(movie.getId(),20);
        BigDecimal price=pricingService.getTicketPrice(movie);
        BigDecimal discountPrice =pricingService.getDiscountTicketPrice(movie,firstShow);
        //Fist show discount $3 applied
        assertEquals(BigDecimal.valueOf(20.0),price);
        assertEquals(BigDecimal.valueOf(17.0),discountPrice);

        //Set special discount 20%
        pricingService.setSpecialDiscount(movie.getId());
        discountPrice =pricingService.getDiscountTicketPrice(movie,firstShow);
        //Fist show $3  special dis $4 . Hence max of two discounts  $4 applied
        assertEquals(BigDecimal.valueOf(16.0),discountPrice);

    }
}