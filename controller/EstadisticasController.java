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
}
