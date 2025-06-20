package com.pinguela.rentexpres.desktop.controller;

import java.util.List;

import javax.swing.SwingUtilities;

import com.pinguela.rentexpres.desktop.model.AlquilerStatsTableModel;
import com.pinguela.rentexpres.desktop.model.ReservaStatsTableModel;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.view.EstadisticasView;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerStatsDTO;
import com.pinguela.rentexpres.model.ReservaStatsDTO;
import com.pinguela.rentexpres.service.EstadisticaService;

public class EstadisticasController {

    private final EstadisticasView view;
    private final EstadisticaService service;

    public EstadisticasController(EstadisticasView view, EstadisticaService service) {
        this.view = view;
        this.service = service;
    }

    public void cargar() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final List<AlquilerStatsDTO> aStats = service.getAlquileresMensuales();
                    final List<ReservaStatsDTO> rStats = service.getReservasMensuales();
                    SwingUtils.invokeLater(new ActionCallback() {
                        @Override
                        public void execute() {
                            ((AlquilerStatsTableModel) view.getTablaAlquiler().getModel()).setData(aStats);
                            ((ReservaStatsTableModel) view.getTablaReserva().getModel()).setData(rStats);
                            view.getGraficaAlquiler().setData(toValuesA(aStats), toLabelsA(aStats));
                            view.getGraficaReserva().setData(toValuesR(rStats), toLabelsR(rStats));
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

    private List<Integer> toValuesA(List<AlquilerStatsDTO> list) {
        java.util.List<Integer> vals = new java.util.ArrayList<>();
        if (list != null) {
            for (AlquilerStatsDTO dto : list) {
                vals.add(dto.getTotalAlquileres());
            }
        }
        return vals;
    }

    private List<String> toLabelsA(List<AlquilerStatsDTO> list) {
        java.util.List<String> labels = new java.util.ArrayList<>();
        if (list != null) {
            for (AlquilerStatsDTO dto : list) {
                labels.add(dto.getMonth() + "/" + dto.getYear());
            }
        }
        return labels;
    }

    private List<Integer> toValuesR(List<ReservaStatsDTO> list) {
        java.util.List<Integer> vals = new java.util.ArrayList<>();
        if (list != null) {
            for (ReservaStatsDTO dto : list) {
                vals.add(dto.getTotalReservas());
            }
        }
        return vals;
    }

    private List<String> toLabelsR(List<ReservaStatsDTO> list) {
        java.util.List<String> labels = new java.util.ArrayList<>();
        if (list != null) {
            for (ReservaStatsDTO dto : list) {
                labels.add(dto.getMonth() + "/" + dto.getYear());
            }
        }
        return labels;
    }
}
