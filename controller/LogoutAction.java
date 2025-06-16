package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.dialog.LoginDialog;
import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.model.UsuarioDTO;

public class LogoutAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final Frame owner;

	public LogoutAction(Frame owner) {
		super("Logout");
		this.owner = owner;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int resp = JOptionPane.showConfirmDialog(owner, "¿Desea cerrar sesión?", "Confirmar Logout",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (resp == JOptionPane.YES_OPTION) {
			AppContext.clearCurrentUser();
			LoginDialog dlg = new LoginDialog(owner);
			UsuarioDTO user = dlg.showDialog();
			if (user != null) {
				AppContext.setCurrentUser(user);
			} else {
				owner.dispose();
			}
		}
	}
}
