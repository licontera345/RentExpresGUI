package com.pinguela.rentexpres.model;

/** DTO con informacion agregada de reservas por mes. */
public class ReservaStatsDTO extends ValueObject {

    private int year;
    private int month;
    private int totalReservas;

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

    public int getTotalReservas() {
        return totalReservas;
    }

    public void setTotalReservas(int totalReservas) {
        this.totalReservas = totalReservas;
    }
}
