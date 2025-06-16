
package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.view.AlquilerSearchView;

public class ClearAlquilerFiltersAction implements ActionListener {

	private final AlquilerSearchView view;

	public ClearAlquilerFiltersAction(AlquilerSearchView view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.getFilter().clear();
	}

}
