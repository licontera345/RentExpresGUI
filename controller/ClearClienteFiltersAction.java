package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.view.ClienteSearchView;

public class ClearClienteFiltersAction implements ActionListener {

	private final ClienteSearchView view;

	public ClearClienteFiltersAction(ClienteSearchView view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.getFilter().clear();
	}
}
