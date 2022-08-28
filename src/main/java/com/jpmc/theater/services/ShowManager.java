package com.jpmc.theater.services;

import com.jpmc.theater.data.model.Show;
import com.jpmc.theater.exception.NoDataFoundException;
import com.jpmc.theater.utills.LocalDateProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ShowManager {

    private final Map<Integer, Show> showMap;

    public ShowManager() {
        showMap = new HashMap<>();
    }

    public Show createShow(String movieId, LocalDate showDate, int hours, int minutes) {
        int showId = showMap.size() + 1;
        LocalDateTime showStartTime = LocalDateProvider.getLocalDateTime(showDate, hours, minutes);
        Show show = new Show(showId, movieId, showStartTime);
        showMap.put(showId, show);
        return show;
    }

    public Show removeShow(int showNumber) {
        if (!showMap.containsKey(showNumber)) {
            NoDataFoundException.throwException("Show", showNumber);
        }
        return showMap.remove(showNumber);
    }


    public void updateShowReservedTicketCount(Show show, int numberOfTickets){
        show.setReservedTicketCount(show.getReservedTicketCount()+numberOfTickets);
    }

    public Show getShow(int showId) {
        Show show=getShowMap().get(showId);
        if(show == null){
            NoDataFoundException.throwException("Show",showId);
        }
        return show;
    }

    public Map<Integer, Show> getShowMap() {
        return showMap;
    }

}
