package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.pinguela.rentexpres.model.ClienteDTO;

import net.miginfocom.swing.MigLayout;

public class ClienteDetailDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat FMT_OUT = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat FMT_IN = new SimpleDateFormat("yyyy-MM-dd");

	public ClienteDetailDialog(Frame owner, ClienteDTO c) {
		super(owner, "Detalle Cliente", true);
		setLayout(new MigLayout("wrap 2", "[right]10[300]", ""));

		add(new JLabel("ID:"));
		add(new JLabel(c.getId().toString()));
		add(new JLabel("Nombre:"));
		add(new JLabel(c.getNombre()));
		add(new JLabel("Apellidos:"));
		add(new JLabel(c.getApellido1() + " " + c.getApellido2()));

		String fNac = "";
		try {
			if (c.getFechaNacimiento() != null)
				fNac = FMT_OUT.format(FMT_IN.parse(c.getFechaNacimiento()));
		} catch (ParseException ignored) {
		}
		add(new JLabel("Fecha nac.:"));
		add(new JLabel(fNac));

		add(new JLabel("E-mail:"));
		add(new JLabel(c.getEmail()));
		add(new JLabel("Teléfono:"));
		add(new JLabel(c.getTelefono()));

		add(new JLabel("Dirección:"));
		add(new JLabel(c.getCalle() + " " + c.getNumero()));
		add(new JLabel("Localidad:"));
		add(new JLabel(c.getNombreLocalidad()));
		add(new JLabel("Provincia:"));
		add(new JLabel(c.getNombreProvincia()));

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(btnCerrar, "span,center");

		pack();
		setResizable(false);
		setLocationRelativeTo(owner);
	}
}
