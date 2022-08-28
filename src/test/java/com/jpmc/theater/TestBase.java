package com.jpmc.theater;

import com.jpmc.theater.exception.NoDataFoundException;
import com.jpmc.theater.services.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {

    protected void assertException(Class exception, Executable executable, String expectedErrorMessage) {
        assertEquals(expectedErrorMessage,assertThrows(exception, executable).getMessage());
    }
    protected void assertNoDataException(Executable executable, String expectedErrorMessage) {
        assertEquals(expectedErrorMessage,assertThrows(NoDataFoundException.class, executable).getMessage());
    }

    protected   MovieManager movieManager() {
        return new MovieManager();
    }

    protected ShowManager showManager() {
        return new ShowManager();
    }

    protected CustomerManager customerManager() {
        return new CustomerManager();
    }

    protected ReservationManager reservationManager() {
        return new ReservationManager(showManager(),movieManager(),pricingService());
    }

    protected PricingService pricingService(){
        return new PricingService();
    }

    protected TheatreManager theatreManager() {
        return new TheatreManager(movieManager(),showManager(),pricingService());
    }

}
