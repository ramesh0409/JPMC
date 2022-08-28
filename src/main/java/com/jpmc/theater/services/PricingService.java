package com.jpmc.theater.services;

import com.jpmc.theater.data.model.Movie;
import com.jpmc.theater.data.model.Show;
import com.jpmc.theater.exception.NoDataFoundException;
import com.jpmc.theater.utills.IdGenerator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PricingService {

    private final Set<String> specialDiscountMovies;
    private final Map<String, BigDecimal> movieTicketPriceMap;
    private final Map<String, Function<Show, Double>> showRuleSetMap;
    private final Map<String, BiFunction<Movie,Show, Double>> movieRuleMap;
    private static final String SHOW_RULE_ID_PREFIX = "RuleShow";
    private static final String MOVIE_RULE_ID_PREFIX = "RuleM";


    public PricingService() {
        specialDiscountMovies = new HashSet<>();
        movieTicketPriceMap = new HashMap<>();
        showRuleSetMap = new HashMap<>();
        movieRuleMap = new HashMap<>();
        initRule();
    }

    public void initRule() {
        Function<Show, Double> firstShowDiscount = (show) -> show.getSequenceOfTheDay() == 1 ? 3.0 : 0.0;
        Function<Show, Double> secondShowDiscount = (show) -> show.getSequenceOfTheDay() == 2 ? 1.0 : 0.0;

        addShowDiscountRule(firstShowDiscount);
        addShowDiscountRule(secondShowDiscount);
        BiFunction<Movie,Show, Double> specialDiscount = this::twentyPercentDiscount;
        addMovieRule(specialDiscount);

    }

    private Double twentyPercentDiscount(Movie movie,Show show) {
        Double discount= specialDiscountMovies.contains(movie.getId()) ? 0.2: 0.0;
        BigDecimal price= movieTicketPriceMap.getOrDefault(movie.getId(),BigDecimal.ZERO);
        return price.doubleValue()*discount;
    }

    public BigDecimal getDiscountTicketPrice(Movie movie, Show show) {
        BigDecimal ticketPrice = getTicketPrice(movie);
        BigDecimal movieDiscount = applyMovieRules(movie,show);
        BigDecimal showDiscount = applyShowRules(show);
        BigDecimal discount=movieDiscount.max(showDiscount);
        return ticketPrice.subtract(discount);
    }

    public BigDecimal totalTicketPrice(Movie movie, Show show, int numberOfTickets) {
        BigDecimal ticketPrice = getDiscountTicketPrice(movie, show);
        return ticketPrice.multiply(BigDecimal.valueOf(numberOfTickets));
    }

    private BigDecimal applyShowRules(Show show) {
        return showRuleSetMap.values()
                .stream()
                .map(rule -> BigDecimal.valueOf(rule.apply(show)))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal applyMovieRules(Movie movie,Show show) {
        return movieRuleMap.values()
                .stream()
                .map(rule -> BigDecimal.valueOf(rule.apply(movie,show)))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getTicketPrice(Movie movie) {
        BigDecimal ticketPrice = movieTicketPriceMap.get(movie.getId());
        if (ticketPrice == null) {
            NoDataFoundException.throwException("PricingService Movie map", movie.getId());
        }
        return ticketPrice;

    }

    public void setPrice(String movieId, double ticketPrice) {
        movieTicketPriceMap.put(movieId, BigDecimal.valueOf(ticketPrice));
    }

    public void addShowDiscountRule(Function<Show, Double> rule) {
        String showRuleId = IdGenerator.nextId(showRuleSetMap, SHOW_RULE_ID_PREFIX);
        showRuleSetMap.put(showRuleId, rule);
    }

    public void removeShowDiscountRule(String ruleId) {
        showRuleSetMap.remove(ruleId);
    }

    public void addMovieRule(BiFunction<Movie,Show, Double> rule) {
        String movieRuleId = IdGenerator.nextId(movieRuleMap, MOVIE_RULE_ID_PREFIX);
        movieRuleMap.put(movieRuleId, rule);
    }

    public void removeMovieRule(String ruleId) {
        movieRuleMap.remove(ruleId);
    }

    public void setSpecialDiscount(String movieId) {
        specialDiscountMovies.add(movieId);
    }

    public boolean  removeSpecialDiscount(String movieId) {
        return  specialDiscountMovies.remove(movieId);
    }

    public Set<String> getSpecialDiscountMovies() {
        return specialDiscountMovies;
    }

    public Map<String, BigDecimal> getMovieTicketPriceMap() {
        return movieTicketPriceMap;
    }

    public Map<String, Function<Show, Double>> getShowRuleSetMap() {
        return showRuleSetMap;
    }

    public Map<String, BiFunction<Movie,Show, Double>> getMovieRuleMap() {
        return movieRuleMap;
    }
}
