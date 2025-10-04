package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.pinguela.rentexpres.model.RentalDTO;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

public class RentalEditDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final RentalDTO rental;
	private boolean confirmed = false;

	private final JDateChooser dcInicio = new JDateChooser();
	private final JDateChooser dcFin = new JDateChooser();
	private final JSpinner spnKmInicio = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spnKmFin = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JTextField txtCosteTotal = new JTextField();

	public RentalEditDialog(Frame owner, RentalDTO a) {
		super(owner, "Editar Rental", true);
		this.rental = a;
		init();
	}

	private void init() {
		setLayout(new MigLayout("wrap 2", "[right]10[grow]"));

		dcInicio.setDateFormatString("yyyy-MM-dd");
		dcFin.setDateFormatString("yyyy-MM-dd");
		dcInicio.setDate(java.sql.Date.valueOf(rental.getActualStartDate()));
		dcFin.setDate(java.sql.Date.valueOf(rental.getActualEndDate()));
		spnKmInicio.setValue(rental.getStartKm());
		spnKmFin.setValue(rental.getEndKm());
		txtCosteTotal.setText(String.valueOf(rental.getTotalCost()));

		add(new JLabel("Fecha Inicio:"));
		add(dcInicio);
		add(new JLabel("Fecha Fin:"));
		add(dcFin);
		add(new JLabel("KM Inicio:"));
		add(spnKmInicio);
		add(new JLabel("KM Fin:"));
		add(spnKmFin);
		add(new JLabel("Coste Total:"));
		add(txtCosteTotal);

		JButton btnGuardar = new JButton("Guardar");
		JButton btnCancelar = new JButton("Cancelar");
		btnGuardar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onGuardar();
			}
		});
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(btnGuardar, "split 2");
		add(btnCancelar);

		pack();
		setLocationRelativeTo(getOwner());
	}

	private void onGuardar() {
		rental.setActualStartDate(format(dcInicio.getDate()));
		rental.setActualEndDate(format(dcFin.getDate()));
		rental.setStartKm(((Number) spnKmInicio.getValue()).intValue());
		rental.setEndKm(((Number) spnKmFin.getValue()).intValue());
		rental.setTotalCost(Integer.parseInt(txtCosteTotal.getText().trim()));
		confirmed = true;
		dispose();
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public RentalDTO getRental() {
		return rental;
	}

	private String format(Date d) {
		return d == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(d);
	}
}
