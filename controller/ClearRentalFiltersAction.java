
package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.view.RentalSearchView;

public class ClearRentalFiltersAction implements ActionListener {

	private final RentalSearchView view;

	public ClearRentalFiltersAction(RentalSearchView view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.getFilter().clear();
	}

}
