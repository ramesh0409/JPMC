package com.jpmc.theater.services;

import com.jpmc.theater.TestBase;
import com.jpmc.theater.data.model.*;
import com.jpmc.theater.exception.ReservationException;
import com.jpmc.theater.utills.LocalDateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.Duration;
import java.time.LocalDate;

class ReservationManagerTest extends TestBase {

    ReservationManager reservationManager;
    ShowManager showManager;
    CustomerManager customerManager;
    MovieManager movieManager;
    PricingService pricingService;
    TheatreManager theatreManager;

    @BeforeAll
    void setUp() {
        reservationManager=reservationManager();
        showManager=reservationManager.getShowManager();
        customerManager=customerManager();
        movieManager=reservationManager.getMovieManager();
        pricingService=reservationManager.getPricingService();

    }

    @Test
    public void testReservationSuccess(){
        Reservation reservation = createTestReservation(4,0,5);
        Assertions.assertEquals("R1",reservation.getReservationId());
        Assertions.assertTrue(reservationManager.getReservationMap().size() ==1);

    }

    @Test
    public void testReservationCancelSuccess(){
        Object [] arr=createBase(4,0,20);
        String customerId= (String) arr[0];
        Integer showId= (Integer) arr[1];
        Integer numTick= (Integer) arr[2];
        Integer theatreCapacity= (Integer) arr[3];
        Reservation reservation = reservationManager.createReservation(customerId,showId,numTick,theatreCapacity);
        Assertions.assertEquals("R1",reservation.getReservationId());
        Assertions.assertTrue(reservationManager.getReservationMap().size() ==1);
        Show show=showManager.getShow(reservation.getShowId());
        Assertions.assertEquals(4,show.getReservedTicketCount());

        //Cancel Reservation
        reservationManager.cancelReservation(reservation.getReservationId());
        Assertions.assertTrue(reservation.isCancelled());
        Assertions.assertEquals(0,show.getReservedTicketCount());

    }

    @Test
    public void testReservationCancelMulti(){
        Object [] arr=createBase(4,0,20);
        String customerId= (String) arr[0];
        Integer showId= (Integer) arr[1];
        Integer numTick= (Integer) arr[2];
        Integer theatreCapacity= (Integer) arr[3];
        Reservation reservation = reservationManager.createReservation(customerId,showId,numTick,theatreCapacity);
        Assertions.assertEquals("R1",reservation.getReservationId());
        Assertions.assertTrue(reservationManager.getReservationMap().size() ==1);
        Show show=showManager.getShow(reservation.getShowId());
        Assertions.assertEquals(4,show.getReservedTicketCount());

        Reservation anotherResrvation = reservationManager.createReservation(customerId,showId,2,theatreCapacity);
        Assertions.assertEquals(6,show.getReservedTicketCount());
        //Cancel Reservation
        reservationManager.cancelReservation(reservation.getReservationId());
        Assertions.assertTrue(reservation.isCancelled());
        Assertions.assertFalse(anotherResrvation.isCancelled());
        Assertions.assertEquals(2,show.getReservedTicketCount());

    }

    @Test
    public void testReservationWithShowPreviousBooking(){
        Reservation reservation = createTestReservation(4,1,5);
        Assertions.assertEquals("R1",reservation.getReservationId());
        Assertions.assertTrue(reservationManager.getReservationMap().size() ==1);

    }
    @Test
    public void testReservationWithShowPreviousBookingException(){

        Executable executable=() -> createTestReservation(4,2,5);
        String expectedMsg="Reservation not successful for show-1 .Available tickets are 3 Requested tickets 4";
        assertException(ReservationException.class,executable,expectedMsg);

    }

    @Test
    public void testReservationException(){
        Executable executable=() -> createTestReservation(6,0,5);
        String expectedMsg="Reservation not successful for show-1 .Available tickets are 5 Requested tickets 6";
        assertException(ReservationException.class,executable,expectedMsg);
    }

    private Reservation createTestReservation(int numberOfTickets,int showReservedTicket,int theatreCapacity) {
        Object [] arr=createBase(numberOfTickets,showReservedTicket,theatreCapacity);
        String customerId= (String) arr[0];
        Integer showId= (Integer) arr[1];
        Integer numTick= (Integer) arr[2];
        Integer capacity= (Integer) arr[3];
        Reservation reservation = reservationManager.createReservation(customerId,showId,numTick,capacity);
        return reservation;
    }

    private Object[] createBase(int numberOfTickets,int showReservedTicket,int theatreCapacity){
        reservationManager.getReservationMap().clear();
        LocalDate currentDate= LocalDateProvider.currentDate();
        //Create movie
        movieManager.getMovieMap().clear();
        Movie movie=movieManager.addMovie("Spider-Man: No Way Home", Duration.ofMinutes(90));
        pricingService.setPrice(movie.getId(),11);
        //Create show
        showManager.getShowMap().clear();
        Show show=showManager.createShow(movie.getId(), currentDate,11,25);
        show.setReservedTicketCount(showReservedTicket);
        //Create customer data
        Customer customer=customerManager.createCustomer("ABC","ABC@gmail.com");
        // Create theatre
        Theater theater=new Theater("Th1","My Theatre",theatreCapacity);
        Object[] arr={customer.getId(),show.getSequenceOfTheDay(),numberOfTickets,theater.getCapacity()};
        return arr;
    }

}