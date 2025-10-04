package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Frame;

import com.pinguela.rentexpres.model.ReservationStatusDTO;
import com.pinguela.rentexpres.model.ReservationDTO;

public class ReservationEditDialog extends ReservationCreateDialog {
	private static final long serialVersionUID = 1L;
	private final ReservationDTO reservation;

	public ReservationEditDialog(Frame owner, ReservationDTO r) {
		super(owner);
		this.reservation = r;
		setTitle("Editar Reservation");
		btnCrear.setText("Guardar");
		precargarDatos();
	}

	private void precargarDatos() {
		super.setUserId(reservation.getUserId());
		txtVeh.setText(reservation.getVehicleId().toString());
		txtCli.setText(reservation.getCustomerId().toString());
		dcInicio.setDate(java.sql.Date.valueOf(reservation.getStartDate()));
		dcFin.setDate(java.sql.Date.valueOf(reservation.getEndDate()));
		// Seleccionar estado existente
		for (int i = 0; i < cmbEst.getItemCount(); i++) {
			if (cmbEst.getItemAt(i).getId().equals(reservation.getReservationIdStatus())) {
				cmbEst.setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	protected void onCrear() {
		if (!validar())
			return;
		reservation.setVehicleId(Integer.parseInt(txtVeh.getText().trim()));
		reservation.setCustomerId(Integer.parseInt(txtCli.getText().trim()));
		reservation.setStartDate(new java.text.SimpleDateFormat("yyyy-MM-dd").format(dcInicio.getDate()));
		reservation.setEndDate(new java.text.SimpleDateFormat("yyyy-MM-dd").format(dcFin.getDate()));
		reservation.setReservationIdStatus(((ReservationStatusDTO) cmbEst.getSelectedItem()).getId());
		confirmed = true;
		dispose();
	}

	@Override
	public ReservationDTO getReservation() {
		return reservation;
	}
}
