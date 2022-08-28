package com.jpmc.theater.exception;

public class ReservationException extends RuntimeException{

    public ReservationException(String message) {
        super(message);
    }

    public static void throwReservationFull(int showId){
        String msg=String.format("Reservation for the Show %s is Full",showId);
        throw new ReservationException(msg);
    }

    public static void throwTicketsNotAvailable(int showId,int availableTickets,int requested){
        String msg=String.format("Reservation not successful for show-%s .Available tickets are %s Requested tickets %s",showId,availableTickets,requested);
        throw new ReservationException(msg);
    }
}
