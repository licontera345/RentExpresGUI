
package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.view.ReservationSearchView;

public class ClearReservationFiltersAction implements ActionListener {

	private final ReservationSearchView view;

	public ClearReservationFiltersAction(ReservationSearchView view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.getFilter().clear();
	}

}
