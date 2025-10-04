package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.pinguela.rentexpres.desktop.dialog.RentalDetailDialog;
import com.pinguela.rentexpres.model.RentalDTO;

public class ShowRentalDetailAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final Frame parent;
	private final RentalDTO rental;

	public ShowRentalDetailAction(Frame parent, RentalDTO rental) {
		this.parent = parent;
		this.rental = rental;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
                RentalDetailDialog dlg = new RentalDetailDialog(parent, rental);
		dlg.setVisible(true);
	}
}
