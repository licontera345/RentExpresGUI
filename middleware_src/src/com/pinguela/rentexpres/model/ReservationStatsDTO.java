package com.pinguela.rentexpres.model;

/** DTO con informacion agregada de reservations por mes. */
public class ReservationStatsDTO extends ValueObject {

    private int year;
    private int month;
    private int totalReservations;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(int totalReservations) {
        this.totalReservations = totalReservations;
    }
}
