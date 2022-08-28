package com.jpmc.theater.data.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Reservation {

    private final String reservationId;
    private final String customerId;
    private final int showId;
    private final int numberOfTickets;
    private boolean paid;
    private boolean cancelled;
    private final BigDecimal totalTicketAmount;

    public Reservation(String reservationId, String customerId, int showId, int numberOfTickets, BigDecimal totalTicketAmount) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.showId = showId;
        this.numberOfTickets = numberOfTickets;
        this.totalTicketAmount = totalTicketAmount;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getShowId() {
        return showId;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public boolean isPaid() {
        return paid;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public BigDecimal getTotalTicketAmount() {
        return totalTicketAmount;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return getShowId() == that.getShowId() && Objects.equals(getReservationId(), that.getReservationId()) && Objects.equals(getCustomerId(), that.getCustomerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReservationId(), getCustomerId(), getShowId());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", showId=" + showId +
                ", numberOfTickets=" + numberOfTickets +
                ", paid=" + paid +
                ", cancelled=" + cancelled +
                ", totalTicketAmount=" + totalTicketAmount +
                '}';
    }
}
