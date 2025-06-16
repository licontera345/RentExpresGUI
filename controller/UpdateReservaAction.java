package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.dialog.ReservaEditDialog;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.ReservaService;

public class UpdateReservaAction implements ActionListener {
	private final Frame frame;
	private final Supplier<ReservaDTO> currentSupplier;
	private final ReservaService service;
	private final Runnable onRefresh;

	public UpdateReservaAction(Frame frame, Supplier<ReservaDTO> currentSupplier, ReservaService service,
			Runnable onRefresh) {
		this.frame = frame;
		this.currentSupplier = currentSupplier;
		this.service = service;
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ReservaDTO dto = currentSupplier.get();
		if (dto == null) {
			SwingUtils.showWarning(frame, "Selecciona una reserva para editar.");
			return;
		}

		var dlg = new ReservaEditDialog(frame, dto);
		dlg.setVisible(true);
		if (!dlg.isConfirmed())
			return;

		try {
			service.update(dlg.getReserva());
			if (onRefresh != null)
				onRefresh.run();
		} catch (RentexpresException ex) {
			JOptionPane.showMessageDialog(frame, "Error actualizando la reserva: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
