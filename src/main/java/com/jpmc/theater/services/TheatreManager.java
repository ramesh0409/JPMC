package com.jpmc.theater.services;

import com.jpmc.theater.data.model.Movie;
import com.jpmc.theater.data.model.Show;
import com.jpmc.theater.data.model.Theater;
import com.jpmc.theater.utills.IdGenerator;
import com.jpmc.theater.utills.LocalDateProvider;
import com.jpmc.theater.utills.MessageFormatter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TheatreManager {

    private final MovieManager movieManager;
    private final ShowManager showManager;
    private final PricingService pricingService;
    private final Map<String, Theater> theaterMap;

    public TheatreManager(MovieManager movieManager, ShowManager showManager, PricingService pricingService) {
        this.movieManager = movieManager;
        this.showManager = showManager;
        this.pricingService = pricingService;
        this.theaterMap = new HashMap<>();
    }

    public Theater createTheatre(String theatreName, int capacity) {
        String theatreId = IdGenerator.nextId(theaterMap, "Th");
        Theater theater = new Theater(theatreId, theatreName, capacity);
        theaterMap.put(theatreId, theater);
        return theater;
    }

    public Map<String, Theater> getTheaterMap() {
        return theaterMap;
    }

    public void printShowSchedule() {
        System.out.println("====================================================");
        for (Show show : showManager.getShowMap().values()) {
            Movie movie = movieManager.getMovieMap().get(show.getMovieId());

            String minutes = MessageFormatter.plural("minute", movie.getRunningTime().toMinutesPart());
            String hours = MessageFormatter.plural("hour", movie.getRunningTime().toHoursPart());
            BigDecimal ticketPrice = pricingService.getTicketPrice(movie);
            System.out.println(show.getSequenceOfTheDay() + " " + show.getShowStartTime() + " " + movie.getTitle() + " [ " + hours + " " + minutes + "] "
                    + ticketPrice);
        }
        System.out.println("====================================================");
    }

    public void addNewDiscount() {
        Function<Show, Double> seventhShowDiscount = (show -> show.getSequenceOfTheDay() == 7 ? 1 : 0.0);
        pricingService.addShowDiscountRule(seventhShowDiscount);

        BiFunction<Movie, Show, Double> oddTimeShowDiscount = (movie, show) -> showTimeDiscount(pricingService, movie, show);
        pricingService.addMovieRule(oddTimeShowDiscount);
    }

    private Double showTimeDiscount(PricingService pricingService, Movie movie, Show show) {
        //Show  starting time between 11 AM to 4PM
        Double discountPercentage = (show.getShowStartTime().getHour() >= 11 && show.getShowStartTime().getHour() < 16) ? 0.25 : 0;
        BigDecimal price = pricingService.getTicketPrice(movie);
        Double discountAmt = price.doubleValue() * discountPercentage;
        return discountAmt;
    }

    public void createShow(Theater theater) {
        //Create Movie
        Movie spiderMan = movieManager.addMovie("Spider-Man: No Way Home", Duration.ofMinutes(90));
        Movie turningRed = movieManager.addMovie("Turning Red", Duration.ofMinutes(85));
        Movie theBatMan = movieManager.addMovie("The Batman", Duration.ofMinutes(95));

        //Pricing service
        pricingService.setPrice(spiderMan.getId(), 12.5);
        pricingService.setPrice(turningRed.getId(), 11);
        pricingService.setPrice(theBatMan.getId(), 9);
        //Special discount
        pricingService.setSpecialDiscount(spiderMan.getId());

        //Create show for each movie
        LocalDate currentDate = LocalDateProvider.currentDate();
        showManager.createShow(turningRed.getId(), currentDate, 9, 0);
        showManager.createShow(spiderMan.getId(), currentDate, 11, 0);
        showManager.createShow(theBatMan.getId(), currentDate, 12, 50);
        showManager.createShow(turningRed.getId(), currentDate, 14, 30);
        showManager.createShow(spiderMan.getId(), currentDate, 16, 10);
        showManager.createShow(theBatMan.getId(), currentDate, 17, 50);
        showManager.createShow(turningRed.getId(), currentDate, 19, 30);
        showManager.createShow(spiderMan.getId(), currentDate, 21, 10);
        showManager.createShow(theBatMan.getId(), currentDate, 23, 0);

        //Add all show Id to the theatre
        theater.setCurrentShows(new HashSet<>(showManager.getShowMap().keySet()));

    }

    public MovieManager getMovieManager() {
        return movieManager;
    }

    public ShowManager getShowManager() {
        return showManager;
    }

    public PricingService getPricingService() {
        return pricingService;
    }
}
