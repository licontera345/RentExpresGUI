package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.dialog.LoginDialog;
import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.model.UserDTO;

public class LogoutAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final Frame owner;

	public LogoutAction(Frame owner) {
		super("Logout");
		this.owner = owner;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int resp = JOptionPane.showConfirmDialog(owner, "Do you want to log out?", "Confirm logout",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (resp == JOptionPane.YES_OPTION) {
			AppContext.clearCurrentUser();
			LoginDialog dlg = new LoginDialog(owner);
			UserDTO user = dlg.showDialog();
			if (user != null) {
				AppContext.setCurrentUser(user);
			} else {
				owner.dispose();
			}
		}
	}
}
