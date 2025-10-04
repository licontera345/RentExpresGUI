package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.view.CustomerSearchView;

public class ClearCustomerFiltersAction implements ActionListener {

	private final CustomerSearchView view;

	public ClearCustomerFiltersAction(CustomerSearchView view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.getFilter().clear();
	}
}
