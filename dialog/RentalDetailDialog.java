package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.model.RentalStatusDTO;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.impl.CustomerServiceImpl;
import com.pinguela.rentexpres.service.impl.RentalStatusServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleServiceImpl;

import net.miginfocom.swing.MigLayout;

public class RentalDetailDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd");

	public RentalDetailDialog(Frame owner, RentalDTO a) {
		super(owner, "Detalle del Rental", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                JPanel content = new JPanel(
                                new MigLayout("wrap 4",
                                                "[right]10[grow,fill]20[right]10[grow,fill]",
                                                "[]10[]10[]10[]10[]10[]10[]10[]10[]10[]10[]20[]"));
		content.setBorder(new EmptyBorder(15, 20, 15, 20));
		CustomerDTO customer;
		RentalStatusDTO estado;
		VehicleDTO vehicle;

		try {
			customer = new CustomerServiceImpl().findById(a.getCustomerId());
		} catch (Exception e) {
			customer = new CustomerDTO();
			customer.setName("Desconocido");
			customer.setLastName("");
		}
		try {
			estado = new RentalStatusServiceImpl().findById(a.getRentalStatusId());
		} catch (Exception e) {
			estado = new RentalStatusDTO();
			estado.setStatusName("Desconocido");
		}
		try {
			vehicle = new VehicleServiceImpl().findById(a.getVehicleId());
		} catch (Exception e) {
			vehicle = new VehicleDTO();
			vehicle.setLicensePlate("Desconocida");
			vehicle.setMake("-");
			vehicle.setModel("-");
		}

		content.add(boldLabel("ID Rental:"));
		content.add(new JLabel(String.valueOf(a.getId())));

		content.add(boldLabel("ID Reservation:"));
		content.add(new JLabel(String.valueOf(a.getReservationId())));

		content.add(boldLabel("Vehicle:"));
		content.add(new JLabel(vehicle.getLicensePlate()));

		content.add(boldLabel("Make:"));
		content.add(new JLabel(vehicle.getMake()));

		content.add(boldLabel("Model:"));
		content.add(new JLabel(vehicle.getModel()));

		content.add(boldLabel("Customer:"));
		content.add(new JLabel(customer.getName() + " " + customer.getLastName()));

		content.add(boldLabel("Fecha Inicio:"));
		content.add(new JLabel(FMT.format(java.sql.Date.valueOf(a.getActualStartDate()))));

		content.add(boldLabel("Fecha Fin:"));
		content.add(new JLabel(FMT.format(java.sql.Date.valueOf(a.getActualEndDate()))));

		content.add(boldLabel("KM Inicio:"));
		content.add(new JLabel(String.valueOf(a.getStartKm())));

		content.add(boldLabel("KM Fin:"));
		content.add(new JLabel(String.valueOf(a.getEndKm())));

		content.add(boldLabel("Estado:"));
		content.add(new JLabel(estado.getStatusName()));

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		content.add(btnCerrar, "span, center");

		add(content);
		pack();
		setResizable(false);
		setLocationRelativeTo(owner);
	}

	private JLabel boldLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		return label;
	}
}
