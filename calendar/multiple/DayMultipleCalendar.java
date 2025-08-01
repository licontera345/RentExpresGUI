package com.pinguela.rentexpres.desktop.calendar.multiple;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import com.pinguela.rentexpres.desktop.calendar.CalendarEvent;

public class DayMultipleCalendar extends MultipleCalendar {

    private LocalDate calDate;

    public DayMultipleCalendar(ArrayList<MultipleCalendarEvent> events) {
        super(events);
        calDate = LocalDate.now();
    }

    @Override
    protected boolean dateInRange(LocalDate date) {
        return calDate.equals(date);
    }

    @Override
    protected LocalDate getDateFromDay(DayOfWeek day) {
        return calDate;
    }

    @Override
    protected int numDaysToShow() {
        return 1;
    }

    @Override
    protected DayOfWeek getStartDay() {
        return calDate.getDayOfWeek();
    }

    @Override
    protected DayOfWeek getEndDay() {
        return calDate.getDayOfWeek();
    }

    @Override
    protected void setRangeToToday() {
        calDate = LocalDate.now();
    }

    @Override
    protected double dayToPixel(DayOfWeek dayOfWeek) {
        return TIME_COL_WIDTH;
    }

    public void nextDay() {
        calDate = calDate.plusDays(1);
        repaint();
    }

    public void prevDay() {
        calDate = calDate.minusDays(1);
        repaint();
    }

}
