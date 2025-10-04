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

import com.pinguela.rentexpres.model.CustomerDTO;

import net.miginfocom.swing.MigLayout;

public class CustomerDetailDialog extends StyledDialog {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat FMT_OUT = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat FMT_IN = new SimpleDateFormat("yyyy-MM-dd");

        public CustomerDetailDialog(Frame owner, CustomerDTO c) {
                super(owner, "Detalle Customer", true);
                JPanel content = createContentPanel();
                content.setLayout(new MigLayout("wrap 2", "[right]10[300]", ""));

                content.add(new JLabel("ID:"));
                content.add(new JLabel(c.getId().toString()));
                content.add(new JLabel("Name:"));
                content.add(new JLabel(c.getName()));
                content.add(new JLabel("Apellidos:"));
                content.add(new JLabel(c.getLastName() + " " + c.getSecondLastName()));

		String fNac = "";
		try {
			if (c.getBirthDate() != null)
				fNac = FMT_OUT.format(FMT_IN.parse(c.getBirthDate()));
		} catch (ParseException ignored) {
		}
                content.add(new JLabel("Fecha nac.:"));
                content.add(new JLabel(fNac));

                content.add(new JLabel("E-mail:"));
                content.add(new JLabel(c.getEmail()));
                content.add(new JLabel("Teléfono:"));
                content.add(new JLabel(c.getPhone()));

                content.add(new JLabel("Dirección:"));
                content.add(new JLabel(c.getStreet() + " " + c.getStreetNumber()));
                content.add(new JLabel("City:"));
                content.add(new JLabel(c.getCityName()));
                content.add(new JLabel("Province:"));
                content.add(new JLabel(c.getProvinceName()));

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
