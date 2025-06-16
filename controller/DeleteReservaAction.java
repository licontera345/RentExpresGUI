package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.ReservaService;

public class DeleteReservaAction implements ActionListener {
	private static final Logger logger = LogManager.getLogger(DeleteReservaAction.class);

	private final Supplier<ReservaDTO> reservaSupplier;
	private final Component parent;
        private final ReservaService reservaService;
        private final ActionCallback onRefresh;

        public DeleteReservaAction(Supplier<ReservaDTO> reservaSupplier, Component parent, ReservaService reservaService,
                        ActionCallback onRefresh) {
		this.reservaSupplier = reservaSupplier;
		this.parent = parent;
		this.reservaService = reservaService;
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ReservaDTO reserva = reservaSupplier.get();
		if (reserva == null) {
			SwingUtils.showWarning(parent, "Por favor, selecciona una reserva para eliminar.");
			return;
		}

		int id = reserva.getId();
		int resp = SwingUtils.showConfirm(parent, "¿Estás seguro de eliminar la reserva con ID " + id + "?",
				"Confirmar eliminación");
		if (resp != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			boolean ok = reservaService.delete(id);
			if (ok) {
				SwingUtils.showInfo(parent, "Reserva eliminada correctamente.");
                                if (onRefresh != null)
                                        onRefresh.execute();
			} else {
				SwingUtils.showError(parent, "No se pudo eliminar la reserva.");
			}
		} catch (Exception ex) {
			logger.error("Error eliminando reserva id {}", id, ex);
			SwingUtils.showError(parent, "Error al eliminar la reserva: " + ex.getMessage());
		}
	}
}
