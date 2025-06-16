package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.pinguela.rentexpres.desktop.dialog.AlquilerDetailDialog;
import com.pinguela.rentexpres.model.AlquilerDTO;

public class ShowAlquilerDetailAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final Frame parent;
	private final AlquilerDTO alquiler;

	public ShowAlquilerDetailAction(Frame parent, AlquilerDTO alquiler) {
		this.parent = parent;
		this.alquiler = alquiler;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		var dlg = new AlquilerDetailDialog(parent, alquiler);
		dlg.setVisible(true);
	}
}
