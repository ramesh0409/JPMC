package com.jpmc.theater.services;

import com.jpmc.theater.data.model.Movie;
import com.jpmc.theater.data.model.Reservation;
import com.jpmc.theater.data.model.Show;
import com.jpmc.theater.exception.NoDataFoundException;
import com.jpmc.theater.exception.ReservationException;
import com.jpmc.theater.utills.IdGenerator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ReservationManager {

    private final Map<String, Reservation> reservationMap;
    private final ShowManager showManager;
    private final MovieManager movieManager;
    private final PricingService pricingService;

    public ReservationManager(ShowManager showManager, MovieManager movieManager, PricingService pricingService) {
        this.reservationMap = new HashMap<>();
        this.showManager = showManager;
        this.movieManager = movieManager;
        this.pricingService = pricingService;
    }

    public Reservation createReservation(String customerId, int showId, int numberOfTickets, int theatreCapacity){
        Show show = showManager.getShow(showId);
        Movie movie=movieManager.getMovie(show.getMovieId());
        if(!available(show,numberOfTickets,theatreCapacity)){
            ReservationException.throwReservationFull(showId);
        }

        BigDecimal totalTicketAmount=pricingService.totalTicketPrice(movie,show,numberOfTickets);
        Reservation reservation = getReservation(customerId, showId, numberOfTickets, totalTicketAmount);
        //Update show reserved ticket count
        showManager.updateShowReservedTicketCount(show,numberOfTickets);

        return reservation;
    }

    private Reservation getReservation(String customerId, int showId, int numberOfTickets, BigDecimal totalTicketAmount) {
        String reservationId= IdGenerator.nextId(reservationMap,"R");
        Reservation reservation=new Reservation(reservationId, customerId, showId, numberOfTickets, totalTicketAmount);
        reservationMap.put(reservationId,reservation);
        return reservation;
    }

    private boolean available(Show show, int numberOfTickets, int theatreCapacity) {
        if(show.getReservedTicketCount() == theatreCapacity){
            ReservationException.throwReservationFull(show.getSequenceOfTheDay());
        }else if(show.getReservedTicketCount()+numberOfTickets> theatreCapacity){
            int available=theatreCapacity-show.getReservedTicketCount();
            ReservationException.throwTicketsNotAvailable(show.getSequenceOfTheDay(),available,numberOfTickets);
        }
        return true;
    }


    public boolean cancelReservation(String reservationId){
        Reservation reservation=reservationMap.get(reservationId);
        if(reservation == null){
            NoDataFoundException.throwException("Reservation",reservationId);
        }

        Show show= showManager.getShow(reservation.getShowId());
        showManager.updateShowReservedTicketCount(show,-reservation.getNumberOfTickets());
        reservation.setCancelled(true);
        return true;
    }

    public Map<String, Reservation> getReservationMap() {
        return reservationMap;
    }

    public ShowManager getShowManager() {
        return showManager;
    }

    public MovieManager getMovieManager() {
        return movieManager;
    }

    public PricingService getPricingService() {
        return pricingService;
    }
}
