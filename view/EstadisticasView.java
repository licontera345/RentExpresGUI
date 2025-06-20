package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JTabbedPane;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.controller.EstadisticasController;
import com.pinguela.rentexpres.desktop.model.AlquilerStatsTableModel;
import com.pinguela.rentexpres.desktop.model.ReservaStatsTableModel;
import com.pinguela.rentexpres.service.EstadisticaService;

public class EstadisticasView extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTable tablaAlquiler = new JTable(new AlquilerStatsTableModel());
    private final JTable tablaReserva = new JTable(new ReservaStatsTableModel());
    private final LineChartPanel graficaAlquiler = new LineChartPanel();
    private final LineChartPanel graficaReserva = new LineChartPanel();
    private final EstadisticasController controller;

    public EstadisticasView(EstadisticaService service) {
        setLayout(new BorderLayout(5,5));
        JPanel center = new JPanel(new GridLayout(2,1,0,10));

        JTabbedPane tabAlquiler = new JTabbedPane();
        tabAlquiler.addTab("Tabla", new JScrollPane(tablaAlquiler));
        tabAlquiler.addTab("Gráfico", graficaAlquiler);
        center.add(tabAlquiler);

        JTabbedPane tabReserva = new JTabbedPane();
        tabReserva.addTab("Tabla", new JScrollPane(tablaReserva));
        tabReserva.addTab("Gráfico", graficaReserva);
        center.add(tabReserva);

        add(center, BorderLayout.CENTER);

        controller = new EstadisticasController(this, service);
        controller.cargar();
    }

    public JTable getTablaAlquiler() {
        return tablaAlquiler;
    }

    public JTable getTablaReserva() {
        return tablaReserva;
    }

    public LineChartPanel getGraficaAlquiler() {
        return graficaAlquiler;
    }

    public LineChartPanel getGraficaReserva() {
        return graficaReserva;
    }
}
