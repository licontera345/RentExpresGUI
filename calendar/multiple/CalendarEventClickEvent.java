package com.pinguela.rentexpres.desktop.calendar.multiple;

import java.awt.*;

public class CalendarEventClickEvent extends AWTEvent {

    private MultipleCalendarEvent calendarEvent;

    public CalendarEventClickEvent(Object source, MultipleCalendarEvent calendarEvent) {
        super(source, 0);
        this.calendarEvent = calendarEvent;
    }

    public MultipleCalendarEvent getMultipleCalendarEvent() {
        return  calendarEvent;
    }
}
