package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class ClearUserFiltersAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private final JTextField txtName;
	private final JTextField txtLastName;
	private final JTextField txtSecondLastName;
	private final JTextField txtEmail;
	private final JTextField txtUser;
	private final JComboBox<?> cmbUserType;

	public ClearUserFiltersAction(JTextField txtName, JTextField txtLastName, JTextField txtSecondLastName,
			JTextField txtEmail, JTextField txtUser, JComboBox<?> cmbUserType) {
		super("Limpiar Filtros");
		this.txtName = txtName;
		this.txtLastName = txtLastName;
		this.txtSecondLastName = txtSecondLastName;
		this.txtEmail = txtEmail;
		this.txtUser = txtUser;
		this.cmbUserType = cmbUserType;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			txtName.setText("");
			txtLastName.setText("");
			txtSecondLastName.setText("");
			txtEmail.setText("");
			txtUser.setText("");
			cmbUserType.setSelectedIndex(-1);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al limpiar filtros: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
