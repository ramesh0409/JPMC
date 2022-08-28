package com.jpmc.theater.services;

import com.jpmc.theater.TestBase;
import com.jpmc.theater.data.model.Show;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShowManagerTest extends TestBase {

    ShowManager showManager;
    LocalDate currentDate;
    @BeforeAll
    void setUp() {
        showManager=showManager();
        currentDate=LocalDate.now(ZoneId.of("America/New_York"));
    }

    @Test
    public void testRemoveException(){
        Executable executable=() -> showManager.removeShow(0);
        String expectedMsg="No Show found for the given input 0";
        assertNoDataException(executable,expectedMsg);
    }

    @Test
    public void testAddShpwSuccess(){
        showManager.getShowMap().clear();
        Show show=showManager.createShow("M1", currentDate,9,0);
        assertTrue(showManager.getShowMap().size() ==1);
        Assertions.assertEquals(1,show.getSequenceOfTheDay());
        String date=currentDate.toString();
        String time="09:00";
        String expectedDateTime=date+"T"+time;
        assertEquals(expectedDateTime,show.getShowStartTime().toString());
        assertTrue(showManager.getShowMap().size()>0);
    }

    @Test
    public void testRemoveSuccess(){
        showManager.getShowMap().clear();
        Show show=showManager.createShow("M1", currentDate,11,25);
        assertTrue(showManager.getShowMap().size() ==1);
        Show removedShow=showManager.removeShow(show.getSequenceOfTheDay());
        assertTrue(showManager.getShowMap().size() ==0);
        assertEquals(show,removedShow);
    }

    @Test
    public void testUpdateReservCount(){
        Show show=showManager.createShow("M1", currentDate,11,25);
        Assertions.assertEquals(0,show.getReservedTicketCount());
        showManager.updateShowReservedTicketCount(show,5);
        Assertions.assertEquals(5,show.getReservedTicketCount());
        showManager.updateShowReservedTicketCount(show,7);
        Assertions.assertEquals(12,show.getReservedTicketCount());
        //Cancel test
        showManager.updateShowReservedTicketCount(show,-3);
        Assertions.assertEquals(9,show.getReservedTicketCount());
    }

}