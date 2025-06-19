package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.model.EstadoReservaDTO;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.impl.ClienteServiceImpl;
import com.pinguela.rentexpres.service.impl.EstadoReservaServiceImpl;

import net.miginfocom.swing.MigLayout;

public class ReservaDetailDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd");

	public ReservaDetailDialog(Frame owner, ReservaDTO r) {
		super(owner, "Detalle Reserva", true);
		initComponents(r);
	}

	private void initComponents(ReservaDTO r) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setLayout(new MigLayout("wrap 4",
                                "[right]10[grow,fill]20[right]10[grow,fill]",
                                "[]10[]10[]10[]10[]20[]"));

		ClienteServiceImpl clienteService = new ClienteServiceImpl();
		EstadoReservaServiceImpl estadoService = new EstadoReservaServiceImpl();

		ClienteDTO cliente = null;
		EstadoReservaDTO estado = null;

		try {
			cliente = clienteService.findById(r.getIdCliente());
		} catch (Exception e) {
			cliente = new ClienteDTO();
			cliente.setNombre("Desconocido");
			cliente.setApellido1("");
		}

		try {
			estado = estadoService.findById(r.getIdEstadoReserva());
		} catch (Exception e) {
			estado = new EstadoReservaDTO();
			estado.setNombreEstado("Desconocido");
		}

		add(new JLabel("ID Reserva:"));
		add(new JLabel(r.getId().toString()));

		add(new JLabel("Vehículo ID:"));
		add(new JLabel(r.getIdVehiculo().toString()));

		add(new JLabel("Cliente:"));
		add(new JLabel(cliente.getNombre() + " " + cliente.getApellido1()));

		add(new JLabel("Fecha Inicio:"));
		add(new JLabel(FMT.format(java.sql.Date.valueOf(r.getFechaInicio()))));

		add(new JLabel("Fecha Fin:"));
		add(new JLabel(FMT.format(java.sql.Date.valueOf(r.getFechaFin()))));

		add(new JLabel("Estado:"));
		add(new JLabel(estado.getNombreEstado()));

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(btnCerrar, "span, center");

		pack();
		setResizable(false);
		setLocationRelativeTo(getOwner());
	}
}
