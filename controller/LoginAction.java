package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import com.pinguela.rentexpres.desktop.dialog.LoginDialog;
import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.model.UsuarioDTO;

public class LoginAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final Frame owner;

	public LoginAction(Frame owner) {
		super("Login");
		this.owner = owner;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LoginDialog dlg = new LoginDialog(owner);
		UsuarioDTO user = dlg.showDialog();
		if (user != null) {
			AppContext.setCurrentUser(user);
			
		}
	}
}
