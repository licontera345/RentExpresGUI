package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;
import java.util.EventObject;
import java.util.function.Supplier;

import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.renderer.AbstractActionsCellEditor;

import com.pinguela.rentexpres.desktop.model.CustomerSearchTableModel;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.service.CustomerService;
import com.pinguela.rentexpres.desktop.controller.CustomerRowController;

public class CustomerActionsCellEditor extends AbstractActionsCellEditor {
	private static final long serialVersionUID = 1L;

        private final Frame frame;
        private final CustomerRowController controller;
        private final ActionCallback reload;
        private final Supplier<CustomerDTO> rowSupplier;
        private CustomerDTO customerActual;

        public CustomerActionsCellEditor(Frame owner, CustomerService service, ActionCallback reload,
                        Supplier<CustomerDTO> rowSupplier) {
                super();
                this.frame = owner;
                this.reload = reload;
                this.rowSupplier = rowSupplier;
                this.controller = new CustomerRowController(owner, service, reload);

                btnView.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                controller.showDetail(customerActual);
                                fireEditingStopped();
                        }
                });

                btnEdit.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                controller.edit(customerActual);
                                fireEditingStopped();
                        }
                });

                btnDel.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                controller.delete(customerActual);
                                fireEditingStopped();
                        }
                });
        }

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (rowSupplier != null) {
                        customerActual = rowSupplier.get();
                } else if (table.getModel() instanceof CustomerSearchTableModel) {
                        CustomerSearchTableModel model = (CustomerSearchTableModel) table.getModel();
                        customerActual = model.getCustomerAt(row);
                }
                return panel;
        }

	@Override
	public Object getCellEditorValue() {
		return null;
	}

}
