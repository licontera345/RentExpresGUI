package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.dialog.ReservationEditDialog;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.ReservationService;

public class UpdateReservationAction implements ActionListener {
	private final Frame frame;
	private final Supplier<ReservationDTO> currentSupplier;
	private final ReservationService service;
        private final ActionCallback onRefresh;

        public UpdateReservationAction(Frame frame, Supplier<ReservationDTO> currentSupplier, ReservationService service,
                        ActionCallback onRefresh) {
		this.frame = frame;
		this.currentSupplier = currentSupplier;
		this.service = service;
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ReservationDTO dto = currentSupplier.get();
		if (dto == null) {
			SwingUtils.showWarning(frame, "Selecciona una reservation para editar.");
			return;
		}

                ReservationEditDialog dlg = new ReservationEditDialog(frame, dto);
		dlg.setVisible(true);
		if (!dlg.isConfirmed())
			return;

		try {
			service.update(dlg.getReservation());
                        if (onRefresh != null)
                                onRefresh.execute();
		} catch (RentexpresException ex) {
			JOptionPane.showMessageDialog(frame, "Error actualizando la reservation: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
