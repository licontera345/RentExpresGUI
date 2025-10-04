package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.model.ReservationStatusDTO;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.impl.CustomerServiceImpl;
import com.pinguela.rentexpres.service.impl.ReservationStatusServiceImpl;

import net.miginfocom.swing.MigLayout;

public class ReservationDetailDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd");

	public ReservationDetailDialog(Frame owner, ReservationDTO r) {
		super(owner, "Detalle Reservation", true);
		initComponents(r);
	}

	private void initComponents(ReservationDTO r) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setLayout(new MigLayout("wrap 4",
                                "[right]10[grow,fill]20[right]10[grow,fill]",
                                "[]10[]10[]10[]10[]20[]"));

		CustomerServiceImpl customerService = new CustomerServiceImpl();
		ReservationStatusServiceImpl estadoService = new ReservationStatusServiceImpl();

		CustomerDTO customer = null;
		ReservationStatusDTO estado = null;

		try {
			customer = customerService.findById(r.getCustomerId());
		} catch (Exception e) {
			customer = new CustomerDTO();
			customer.setName("Desconocido");
			customer.setLastName("");
		}

		try {
			estado = estadoService.findById(r.getReservationIdStatus());
		} catch (Exception e) {
			estado = new ReservationStatusDTO();
			estado.setStatusName("Desconocido");
		}

		add(new JLabel("ID Reservation:"));
		add(new JLabel(r.getId().toString()));

		add(new JLabel("Vehicle ID:"));
		add(new JLabel(r.getVehicleId().toString()));

		add(new JLabel("Customer:"));
		add(new JLabel(customer.getName() + " " + customer.getLastName()));

		add(new JLabel("Fecha Inicio:"));
		add(new JLabel(FMT.format(java.sql.Date.valueOf(r.getStartDate()))));

		add(new JLabel("Fecha Fin:"));
		add(new JLabel(FMT.format(java.sql.Date.valueOf(r.getEndDate()))));

		add(new JLabel("Estado:"));
		add(new JLabel(estado.getStatusName()));

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
