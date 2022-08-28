package com.jpmc.theater.utills;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class LocalDateProvider {

    public static LocalDateTime getLocalDateTime(LocalDate date,int hours,int minutes){
        //LocalDateTime is an Immutable class. Hence, every time it  gives a new DateTime object
        return LocalDateTime.of(date, LocalTime.of(hours, minutes));
    }

    public static LocalDate currentDate(){
        //LocalDate is immutable
        return LocalDate.now(ZoneId.of("America/New_York"));
    }
}
