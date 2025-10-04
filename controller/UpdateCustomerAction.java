package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.dialog.CustomerEditDialog;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.service.CustomerService;

public class UpdateCustomerAction implements ActionListener {

	private final Frame frame;
	private final Supplier<CustomerDTO> currentSupplier;
        private final CustomerService service;
        private final ActionCallback onRefresh;

        public UpdateCustomerAction(Frame frame, Supplier<CustomerDTO> currentSupplier, CustomerService service,
                        ActionCallback onRefresh) {
		this.frame = frame;
		this.currentSupplier = currentSupplier;
		this.service = service;
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		CustomerDTO dto = currentSupplier.get();
		if (dto == null) {
			SwingUtils.showWarning(frame, "Selecciona un customer para editar.");
			return;
		}
		CustomerEditDialog dlg = new CustomerEditDialog(frame, dto);
		dlg.setVisible(true);
		if (!dlg.isConfirmed())
			return;

		try {
			service.update(dlg.getCustomer());
                        if (onRefresh != null)
                                onRefresh.execute();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(frame, "Error actualizando el customer: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
