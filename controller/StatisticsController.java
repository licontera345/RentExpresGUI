package com.pinguela.rentexpres.desktop.controller;

import java.util.List;

import javax.swing.SwingUtilities;

import com.pinguela.rentexpres.desktop.model.RentalStatsTableModel;
import com.pinguela.rentexpres.desktop.model.ReservationStatsTableModel;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.view.StatisticsView;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalStatsDTO;
import com.pinguela.rentexpres.model.ReservationStatsDTO;
import com.pinguela.rentexpres.service.StatisticsService;

public class StatisticsController {

    private final StatisticsView view;
    private final StatisticsService service;

    public StatisticsController(StatisticsView view, StatisticsService service) {
        this.view = view;
        this.service = service;
    }

    public void cargar() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final List<RentalStatsDTO> aStats = service.getRentalsMensuales();
                    final List<ReservationStatsDTO> rStats = service.getReservationsMensuales();
                    SwingUtils.invokeLater(new ActionCallback() {
                        @Override
                        public void execute() {
                            ((RentalStatsTableModel) view.getTablaRental().getModel()).setData(aStats);
                            ((ReservationStatsTableModel) view.getTablaReservation().getModel()).setData(rStats);
                            view.getGraficaRental().setData(toValuesA(aStats), toLabelsA(aStats));
                            view.getGraficaReservation().setData(toValuesR(rStats), toLabelsR(rStats));
                        }
                    });
                } catch (final RentexpresException ex) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            SwingUtils.showError(view, "Error cargando estadísticas: " + ex.getMessage());
                        }
                    });
                }
            }
        }.start();
    }

    private List<Integer> toValuesA(List<RentalStatsDTO> list) {
        java.util.List<Integer> vals = new java.util.ArrayList<>();
        if (list != null) {
            for (RentalStatsDTO dto : list) {
                vals.add(dto.getTotalRentals());
            }
        }
        return vals;
    }

    private List<String> toLabelsA(List<RentalStatsDTO> list) {
        java.util.List<String> labels = new java.util.ArrayList<>();
        if (list != null) {
            for (RentalStatsDTO dto : list) {
                labels.add(dto.getMonth() + "/" + dto.getYear());
            }
        }
        return labels;
    }

    private List<Integer> toValuesR(List<ReservationStatsDTO> list) {
        java.util.List<Integer> vals = new java.util.ArrayList<>();
        if (list != null) {
            for (ReservationStatsDTO dto : list) {
                vals.add(dto.getTotalReservations());
            }
        }
        return vals;
    }

    private List<String> toLabelsR(List<ReservationStatsDTO> list) {
        java.util.List<String> labels = new java.util.ArrayList<>();
        if (list != null) {
            for (ReservationStatsDTO dto : list) {
                labels.add(dto.getMonth() + "/" + dto.getYear());
            }
        }
        return labels;
    }
}
