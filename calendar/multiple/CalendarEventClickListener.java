package com.pinguela.rentexpres.desktop.calendar.multiple;

import java.util.EventListener;

public interface CalendarEventClickListener extends EventListener {
    // Event dispatch methods
    void calendarEventClick(CalendarEventClickEvent e);
}
