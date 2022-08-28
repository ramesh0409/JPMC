package com.jpmc.theater.main;

import com.jpmc.theater.data.model.Customer;
import com.jpmc.theater.data.model.Reservation;
import com.jpmc.theater.data.model.Theater;
import com.jpmc.theater.services.*;

public class TheatreMain {

    public static void main(String[] args) {
        ShowManager showManager=new ShowManager();
        MovieManager movieManager=new MovieManager();
        PricingService pricingService= new PricingService();

        //Create Theatre manager
        TheatreManager theatreManager=new TheatreManager(movieManager,showManager,pricingService);
        Theater theater=theatreManager.createTheatre("My Theatre",100);
        //create initial data
        theatreManager.createShow(theater);
        //Print
        theatreManager.printShowSchedule();

        //Create customer
        CustomerManager customerManager=new CustomerManager();
        Customer customer=customerManager.createCustomer("ABC","ABC@gmail.com");

        //Reservation
        ReservationManager reservationManager=new ReservationManager(showManager, movieManager, pricingService);
        //First show discount added
        System.out.println("Fist show discount $3");
        reservation(reservationManager, theater, customer,1,5);
        //Max discount Spider man move has special discount 20% as well as show discount $1
        System.out.println("Max of Second show $2 and 20% special discount");
        reservation(reservationManager, theater, customer,2,5);
        //Add new discount
        theatreManager.addNewDiscount();
        //Between 11 AM to 4PM 25% discount applied
        System.out.println("Show time between 11 AM to 4PM. 25%");
        reservation(reservationManager, theater, customer,3,5);

        //Show 7 discount
        System.out.println("Show 7 Discount");
        reservation(reservationManager, theater, customer,7,5);


    }

    private static void reservation(ReservationManager reservationManager,Theater theater, Customer customer,int showId,int tickets) {
        Reservation reservation=reservationManager.createReservation(customer.getId(),showId,tickets, theater.getCapacity());
        System.out.println(reservation);
    }

}
