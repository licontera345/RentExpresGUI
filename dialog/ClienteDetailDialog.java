package com.pinguela.rentexpres.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.pinguela.rentexpres.desktop.dialog.StyledDialog;

import com.pinguela.rentexpres.model.ClienteDTO;

import net.miginfocom.swing.MigLayout;

public class ClienteDetailDialog extends StyledDialog {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat FMT_OUT = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat FMT_IN = new SimpleDateFormat("yyyy-MM-dd");

        public ClienteDetailDialog(Frame owner, ClienteDTO c) {
                super(owner, "Detalle Cliente", true);
                JPanel content = createContentPanel();
                content.setLayout(new MigLayout("wrap 2", "[right]10[300]", ""));

                content.add(new JLabel("ID:"));
                content.add(new JLabel(c.getId().toString()));
                content.add(new JLabel("Nombre:"));
                content.add(new JLabel(c.getNombre()));
                content.add(new JLabel("Apellidos:"));
                content.add(new JLabel(c.getApellido1() + " " + c.getApellido2()));

		String fNac = "";
		try {
			if (c.getFechaNacimiento() != null)
				fNac = FMT_OUT.format(FMT_IN.parse(c.getFechaNacimiento()));
		} catch (ParseException ignored) {
		}
                content.add(new JLabel("Fecha nac.:"));
                content.add(new JLabel(fNac));

                content.add(new JLabel("E-mail:"));
                content.add(new JLabel(c.getEmail()));
                content.add(new JLabel("Teléfono:"));
                content.add(new JLabel(c.getTelefono()));

                content.add(new JLabel("Dirección:"));
                content.add(new JLabel(c.getCalle() + " " + c.getNumero()));
                content.add(new JLabel("Localidad:"));
                content.add(new JLabel(c.getNombreLocalidad()));
                content.add(new JLabel("Provincia:"));
                content.add(new JLabel(c.getNombreProvincia()));

                JButton btnCerrar = new JButton("Cerrar");
                styleCancel(btnCerrar);
                btnCerrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
                content.add(btnCerrar, "span,center");

                getContentPane().setLayout(new BorderLayout());
                getContentPane().add(content, BorderLayout.CENTER);

		pack();
		setResizable(false);
		setLocationRelativeTo(owner);
	}
}
