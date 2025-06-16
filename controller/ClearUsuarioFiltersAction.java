package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class ClearUsuarioFiltersAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private final JTextField txtNombre;
	private final JTextField txtApellido1;
	private final JTextField txtApellido2;
	private final JTextField txtEmail;
	private final JTextField txtUsuario;
	private final JComboBox<?> cmbTipoUsuario;

	public ClearUsuarioFiltersAction(JTextField txtNombre, JTextField txtApellido1, JTextField txtApellido2,
			JTextField txtEmail, JTextField txtUsuario, JComboBox<?> cmbTipoUsuario) {
		super("Limpiar Filtros");
		this.txtNombre = txtNombre;
		this.txtApellido1 = txtApellido1;
		this.txtApellido2 = txtApellido2;
		this.txtEmail = txtEmail;
		this.txtUsuario = txtUsuario;
		this.cmbTipoUsuario = cmbTipoUsuario;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			txtNombre.setText("");
			txtApellido1.setText("");
			txtApellido2.setText("");
			txtEmail.setText("");
			txtUsuario.setText("");
			cmbTipoUsuario.setSelectedIndex(-1);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al limpiar filtros: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
