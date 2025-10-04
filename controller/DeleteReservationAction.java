package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.desktop.util.LogUtils;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.ReservationService;

public class DeleteReservationAction implements ActionListener {
	private static final Logger logger = LogManager.getLogger(DeleteReservationAction.class);

	private final Supplier<ReservationDTO> reservationSupplier;
	private final Component parent;
        private final ReservationService reservationService;
        private final ActionCallback onRefresh;

        public DeleteReservationAction(Supplier<ReservationDTO> reservationSupplier, Component parent, ReservationService reservationService,
                        ActionCallback onRefresh) {
		this.reservationSupplier = reservationSupplier;
		this.parent = parent;
		this.reservationService = reservationService;
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ReservationDTO reservation = reservationSupplier.get();
		if (reservation == null) {
			SwingUtils.showWarning(parent, "Por favor, selecciona una reservation para delete.");
			return;
		}

		int id = reservation.getId();
		int resp = SwingUtils.showConfirm(parent, "Are you sure you want to delete la reservation con ID " + id + "?",
				"Confirm deletion");
		if (resp != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			boolean ok = reservationService.delete(id);
			if (ok) {
				SwingUtils.showInfo(parent, "Reservation eliminada correctamente.");
                                if (onRefresh != null)
                                        onRefresh.execute();
			} else {
				SwingUtils.showError(parent, "No se pudo delete la reservation.");
			}
                } catch (Exception ex) {
                        logger.error(LogUtils.buildMessage(DeleteReservationAction.class,
                                        "Error eliminando reservation id {}"), id, ex);
			SwingUtils.showError(parent, "Error al delete la reservation: " + ex.getMessage());
		}
	}
}
