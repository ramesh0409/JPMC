package com.jpmc.theater.utills;

import com.jpmc.theater.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateProviderTest extends TestBase {

    @Test
    public void testLocalDateTime() {
        LocalDate localDate = LocalDateProvider.currentDate();
        LocalDateTime localDateTime = LocalDateProvider.getLocalDateTime(localDate, 16, 30);
        String expectedDate = localDate.toString();
        String time = "16:30";
        String expectedDateTime = expectedDate + "T" + time;
        Assertions.assertEquals(expectedDateTime, localDateTime.toString());
    }

    @Test
    public void testImmutable() {
        LocalDate localDate = LocalDateProvider.currentDate();
        LocalDate anotherLocalDate=LocalDateProvider.currentDate();
        //LocalDate  is Immutable
        Assertions.assertFalse(anotherLocalDate==localDate);
        Assertions.assertEquals(anotherLocalDate,anotherLocalDate);
        //LocalDateTime  is Immutable
        LocalDateTime localDateTime = LocalDateProvider.getLocalDateTime(localDate, 16, 30);
        LocalDateTime anotherLocalDateTime = LocalDateProvider.getLocalDateTime(localDate, 16, 30);
        Assertions.assertFalse(anotherLocalDateTime==localDateTime);
        Assertions.assertEquals(anotherLocalDateTime,localDateTime);

    }
}
