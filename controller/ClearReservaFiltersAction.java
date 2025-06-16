
package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.view.ReservaSearchView;

public class ClearReservaFiltersAction implements ActionListener {

	private final ReservaSearchView view;

	public ClearReservaFiltersAction(ReservaSearchView view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.getFilter().clear();
	}

}
