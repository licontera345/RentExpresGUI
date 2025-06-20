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
import com.pinguela.rentexpres.desktop.dialog.AlquilerDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ReservaDetailDialog;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.AlquilerService;
import com.pinguela.rentexpres.service.ReservaService;
import com.pinguela.rentexpres.service.impl.AlquilerServiceImpl;
import com.pinguela.rentexpres.service.impl.ReservaServiceImpl;

/**
 * Weekly calendar view showing reservas and alquileres using the SwingCalendar
 * component provided by the professor.
 */
public class CalendarView extends JPanel {
    private static final long serialVersionUID = 1L;

    private final WeekCalendar calendar;
    private final ReservaService reservaService = new ReservaServiceImpl();
    private final AlquilerService alquilerService = new AlquilerServiceImpl();
    private final Map<CalendarEvent, ReservaDTO> reservaMap = new HashMap<>();
    private final Map<CalendarEvent, AlquilerDTO> alquilerMap = new HashMap<>();

    public CalendarView() {
        setLayout(new BorderLayout());
        calendar = new WeekCalendar(loadEvents());
        add(calendar, BorderLayout.CENTER);

        calendar.addCalendarEventClickListener(e -> {
            CalendarEvent evt = e.getCalendarEvent();
            if (reservaMap.containsKey(evt)) {
                new ReservaDetailDialog(
                        (java.awt.Frame) SwingUtilities.getWindowAncestor(CalendarView.this),
                        reservaMap.get(evt)).setVisible(true);
            } else if (alquilerMap.containsKey(evt)) {
                new AlquilerDetailDialog(
                        (java.awt.Frame) SwingUtilities.getWindowAncestor(CalendarView.this),
                        alquilerMap.get(evt)).setVisible(true);
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
            List<ReservaDTO> reservas = reservaService.findAll();
            for (ReservaDTO r : reservas) {
                LocalDate start = LocalDate.parse(r.getFechaInicio());
                LocalDate end = LocalDate.parse(r.getFechaFin());
                for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                    CalendarEvent evt = new CalendarEvent(d,
                            LocalTime.of(9, 0),
                            LocalTime.of(10, 0),
                            "Reserva " + r.getId());
                    events.add(evt);
                    reservaMap.put(evt, r);
                }
            }
            List<AlquilerDTO> alquileres = alquilerService.findAll();
            for (AlquilerDTO a : alquileres) {
                LocalDate start = LocalDate.parse(a.getFechaInicioEfectivo());
                LocalDate end = LocalDate.parse(a.getFechaFinEfectivo());
                for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                    CalendarEvent evt = new CalendarEvent(d,
                            LocalTime.of(10, 0),
                            LocalTime.of(11, 0),
                            "Alquiler " + a.getId(),
                            java.awt.Color.CYAN);
                    events.add(evt);
                    alquilerMap.put(evt, a);
                }
            }
        } catch (Exception ex) {
            // In case of errors just print stack trace; calendar will show no events
            ex.printStackTrace();
        }
        return events;
    }
}
