package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Frame;

import com.pinguela.rentexpres.model.EstadoReservaDTO;
import com.pinguela.rentexpres.model.ReservaDTO;

public class ReservaEditDialog extends ReservaCreateDialog {
	private static final long serialVersionUID = 1L;
	private final ReservaDTO reserva;

	public ReservaEditDialog(Frame owner, ReservaDTO r) {
		super(owner);
		this.reserva = r;
		setTitle("Editar Reserva");
		btnCrear.setText("Guardar");
		precargarDatos();
	}

	private void precargarDatos() {
		super.setIdUsuario(reserva.getIdUsuario());
		txtVeh.setText(reserva.getIdVehiculo().toString());
		txtCli.setText(reserva.getIdCliente().toString());
		dcInicio.setDate(java.sql.Date.valueOf(reserva.getFechaInicio()));
		dcFin.setDate(java.sql.Date.valueOf(reserva.getFechaFin()));
		// Seleccionar estado existente
		for (int i = 0; i < cmbEst.getItemCount(); i++) {
			if (cmbEst.getItemAt(i).getId().equals(reserva.getIdEstadoReserva())) {
				cmbEst.setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	protected void onCrear() {
		if (!validar())
			return;
		reserva.setIdVehiculo(Integer.parseInt(txtVeh.getText().trim()));
		reserva.setIdCliente(Integer.parseInt(txtCli.getText().trim()));
		reserva.setFechaInicio(new java.text.SimpleDateFormat("yyyy-MM-dd").format(dcInicio.getDate()));
		reserva.setFechaFin(new java.text.SimpleDateFormat("yyyy-MM-dd").format(dcFin.getDate()));
		reserva.setIdEstadoReserva(((EstadoReservaDTO) cmbEst.getSelectedItem()).getId());
		confirmed = true;
		dispose();
	}

	@Override
	public ReservaDTO getReserva() {
		return reserva;
	}
}
