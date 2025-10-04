package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JTabbedPane;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.controller.StatisticsController;
import com.pinguela.rentexpres.desktop.model.RentalStatsTableModel;
import com.pinguela.rentexpres.desktop.model.ReservationStatsTableModel;
import com.pinguela.rentexpres.service.StatisticsService;

public class StatisticsView extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTable tablaRental = new JTable(new RentalStatsTableModel());
    private final JTable tablaReservation = new JTable(new ReservationStatsTableModel());
    private final LineChartPanel graficaRental = new LineChartPanel();
    private final LineChartPanel graficaReservation = new LineChartPanel();
    private final StatisticsController controller;

    public StatisticsView(StatisticsService service) {
        setLayout(new BorderLayout(5,5));
        JPanel center = new JPanel(new GridLayout(2,1,0,10));

        JTabbedPane tabRental = new JTabbedPane();
        tabRental.addTab("Tabla", new JScrollPane(tablaRental));
        tabRental.addTab("Gráfico", graficaRental);
        center.add(tabRental);

        JTabbedPane tabReservation = new JTabbedPane();
        tabReservation.addTab("Tabla", new JScrollPane(tablaReservation));
        tabReservation.addTab("Gráfico", graficaReservation);
        center.add(tabReservation);

        add(center, BorderLayout.CENTER);

        controller = new StatisticsController(this, service);
        controller.cargar();
    }

    public JTable getTablaRental() {
        return tablaRental;
    }

    public JTable getTablaReservation() {
        return tablaReservation;
    }

    public LineChartPanel getGraficaRental() {
        return graficaRental;
    }

    public LineChartPanel getGraficaReservation() {
        return graficaReservation;
    }
}
