package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.CustomerEditDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.service.CustomerService;

public class ShowCustomerEditAction {

	private final Frame frame;
	private final CustomerService customerService;

	public ShowCustomerEditAction(Frame frame, CustomerService customerService) {
		this.frame = frame;
		this.customerService = customerService;
	}

	public void execute(CustomerDTO dto) throws RentexpresException {
		CustomerEditDialog dlg = new CustomerEditDialog(frame, dto);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			customerService.update(dlg.getCustomer());
		}
	}
}
