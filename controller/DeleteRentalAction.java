package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.desktop.util.LogUtils;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.service.RentalService;

public class DeleteRentalAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(DeleteRentalAction.class);

	private final Supplier<RentalDTO> rentalSupplier;
	private final Component parent;
	private final RentalService rentalService;
        private final ActionCallback onRefresh;

        public DeleteRentalAction(Supplier<RentalDTO> rentalSupplier, Component parent,
                        RentalService rentalService, ActionCallback onRefresh) {
		this.rentalSupplier = rentalSupplier;
		this.parent = parent;
		this.rentalService = rentalService;
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		RentalDTO rental = rentalSupplier.get();
		if (rental == null) {
			SwingUtils.showWarning(parent, "Por favor, selecciona una rental para delete.");
			return;
		}

		int id = rental.getId();
		int resp = SwingUtils.showConfirm(parent, "Are you sure you want to delete la rental con ID " + id + "?",
				"Confirm deletion");
		if (resp != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			boolean ok = rentalService.delete(id);
			if (ok) {
				SwingUtils.showInfo(parent, "rental eliminada correctamente.");
                                if (onRefresh != null)
                                        onRefresh.execute();
			} else {
				SwingUtils.showError(parent, "No se pudo delete la rental.");
			}
                } catch (Exception ex) {
                        logger.error(LogUtils.buildMessage(DeleteRentalAction.class,
                                        "Error eliminando rental id {}"), id, ex);
			SwingUtils.showError(parent, "Error al delete la rental: " + ex.getMessage());
		}
	}
}
