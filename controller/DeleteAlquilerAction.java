package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.service.AlquilerService;

public class DeleteAlquilerAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(DeleteAlquilerAction.class);

	private final Supplier<AlquilerDTO> alquilerSupplier;
	private final Component parent;
	private final AlquilerService alquilerService;
        private final ActionCallback onRefresh;

        public DeleteAlquilerAction(Supplier<AlquilerDTO> alquilerSupplier, Component parent,
                        AlquilerService alquilerService, ActionCallback onRefresh) {
		this.alquilerSupplier = alquilerSupplier;
		this.parent = parent;
		this.alquilerService = alquilerService;
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AlquilerDTO alquiler = alquilerSupplier.get();
		if (alquiler == null) {
			SwingUtils.showWarning(parent, "Por favor, selecciona una alquiler para eliminar.");
			return;
		}

		int id = alquiler.getId();
		int resp = SwingUtils.showConfirm(parent, "¿Estás seguro de eliminar la alquiler con ID " + id + "?",
				"Confirmar eliminación");
		if (resp != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			boolean ok = alquilerService.delete(id);
			if (ok) {
				SwingUtils.showInfo(parent, "alquiler eliminada correctamente.");
                                if (onRefresh != null)
                                        onRefresh.execute();
			} else {
				SwingUtils.showError(parent, "No se pudo eliminar la alquiler.");
			}
		} catch (Exception ex) {
			logger.error("Error eliminando alquiler id {}", id, ex);
			SwingUtils.showError(parent, "Error al eliminar la alquiler: " + ex.getMessage());
		}
	}
}
