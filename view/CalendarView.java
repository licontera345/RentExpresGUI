package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.pinguela.rentexpres.desktop.calendar.CalendarEvent;
import com.pinguela.rentexpres.desktop.calendar.WeekCalendar;
import com.pinguela.rentexpres.desktop.dialog.RentalDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ReservationDetailDialog;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.RentalService;
import com.pinguela.rentexpres.service.ReservationService;
import com.pinguela.rentexpres.service.impl.RentalServiceImpl;
import com.pinguela.rentexpres.service.impl.ReservationServiceImpl;

/**
 * Weekly calendar view showing reservations and rentals using the SwingCalendar
 * component provided by the professor.
 */
public class CalendarView extends JPanel {
    private static final long serialVersionUID = 1L;

    private final WeekCalendar calendar;
    private final ReservationService reservationService = new ReservationServiceImpl();
    private final RentalService rentalService = new RentalServiceImpl();
    private final Map<CalendarEvent, ReservationDTO> reservationMap = new HashMap<>();
    private final Map<CalendarEvent, RentalDTO> rentalMap = new HashMap<>();

    public CalendarView() {
        setLayout(new BorderLayout());
        calendar = new WeekCalendar(loadEvents());
        add(calendar, BorderLayout.CENTER);

        calendar.addCalendarEventClickListener(e -> {
            CalendarEvent evt = e.getCalendarEvent();
            if (reservationMap.containsKey(evt)) {
                new ReservationDetailDialog(
                        (java.awt.Frame) SwingUtilities.getWindowAncestor(CalendarView.this),
                        reservationMap.get(evt)).setVisible(true);
            } else if (rentalMap.containsKey(evt)) {
                new RentalDetailDialog(
                        (java.awt.Frame) SwingUtilities.getWindowAncestor(CalendarView.this),
                        rentalMap.get(evt)).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        CalendarView.this,
                        evt.getText(),
                        "Evento",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private ArrayList<CalendarEvent> loadEvents() {
        ArrayList<CalendarEvent> events = new ArrayList<>();
        try {
            List<ReservationDTO> reservations = reservationService.findAll();
            for (ReservationDTO r : reservations) {
                LocalDate start = LocalDate.parse(r.getStartDate());
                LocalDate end = LocalDate.parse(r.getEndDate());
                for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                    CalendarEvent evt = new CalendarEvent(d,
                            LocalTime.of(9, 0),
                            LocalTime.of(10, 0),
                            "Reservation " + r.getId());
                    events.add(evt);
                    reservationMap.put(evt, r);
                }
            }
            List<RentalDTO> rentals = rentalService.findAll();
            for (RentalDTO a : rentals) {
                LocalDate start = LocalDate.parse(a.getActualStartDate());
                LocalDate end = LocalDate.parse(a.getActualEndDate());
                for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                    CalendarEvent evt = new CalendarEvent(d,
                            LocalTime.of(10, 0),
                            LocalTime.of(11, 0),
                            "Rental " + a.getId(),
                            java.awt.Color.CYAN);
                    events.add(evt);
                    rentalMap.put(evt, a);
                }
            }
        } catch (Exception ex) {
            // In case of errors just print stack trace; calendar will show no events
            ex.printStackTrace();
        }
        return events;
    }
}
