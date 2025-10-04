package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JTable;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.ActionCallbackThread;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.dialog.CustomerDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.CustomerEditDialog;
import com.pinguela.rentexpres.desktop.model.CustomerSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.service.CustomerService;
import java.util.List;

public class CustomerController {

	private static final String TXT_CARGA_ERR = "Error cargando customers:\n";
	private static final String TXT_UPD_ERR = "Error actualizando customer:\n";
	private static final String TXT_SELECCIONA = "Selecciona un customer para continuar";

	private final Frame frame;
	private final CustomerService service;
	private final JTable table;
	private final JButton btnVer;
	private final JButton btnEditar;

	public CustomerController(Frame frame, CustomerService service, JTable table, JButton btnVer, JButton btnEditar)
			throws RentexpresException {

		if (frame == null || service == null || table == null || btnVer == null || btnEditar == null)
			throw new IllegalArgumentException("Los argumentos del controlador no pueden ser nulos");

		this.frame = frame;
		this.service = service;
		this.table = table;
		this.btnVer = btnVer;
		this.btnEditar = btnEditar;

		bindActions();
		loadDataAsync();
	}

    private void bindActions() {
        btnVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDetail();
            }
        });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEdit();
            }
        });
    }

	private CustomerDTO getSelectedCustomer() {
		int row = table.getSelectedRow();
		return row < 0 ? null : ((CustomerSearchTableModel) table.getModel()).getCustomerAt(row);
	}

	private void showDetail() {
		CustomerDTO dto = getSelectedCustomer();
		if (dto == null) {
			SwingUtils.showWarning(frame, TXT_SELECCIONA);
			return;
		}
		new CustomerDetailDialog(frame, dto).setVisible(true);
	}

	private void showEdit() {
		CustomerDTO dto = getSelectedCustomer();
		if (dto == null) {
			SwingUtils.showWarning(frame, TXT_SELECCIONA);
			return;
		}

                CustomerEditDialog dlg = new CustomerEditDialog(frame, dto);
		dlg.setVisible(true);

		if (dlg.isConfirmed())
			updateCustomerAsync(dlg.getCustomer());
	}

        private void loadDataAsync() {
                new ActionCallbackThread(new ActionCallback() {
                        @Override
                        public void execute() {
                                try {
                                        List<CustomerDTO> customers = service.findAll();
                                        SwingUtils.invokeLater(new ActionCallback() {
                                                @Override
                                                public void execute() {
                                                        table.setModel(new CustomerSearchTableModel(customers,
                                                                        CustomerSearchTableModel.buildCityMap(customers),
                                                                        CustomerSearchTableModel.buildProvinceMap(customers)));
                                                }
                                        });
                                } catch (RentexpresException ex) {
                                        SwingUtils.invokeLater(new ActionCallback() {
                                                @Override
                                                public void execute() {
                                                        SwingUtils.showError(frame, TXT_CARGA_ERR + ex.getMessage());
                                                }
                                        });
                                }
                        }
                }).start();
        }

        private void updateCustomerAsync(CustomerDTO cli) {
                new ActionCallbackThread(new ActionCallback() {
                        @Override
                        public void execute() {
                                try {
                                        service.update(cli);
                                        SwingUtils.invokeLater(new ActionCallback() {
                                                @Override
                                                public void execute() {
                                                        loadDataAsync();
                                                }
                                        });
                                } catch (RentexpresException ex) {
                                        SwingUtils.invokeLater(new ActionCallback() {
                                                @Override
                                                public void execute() {
                                                        SwingUtils.showError(frame, TXT_UPD_ERR + ex.getMessage());
                                                }
                                        });
                                }
                        }
                }).start();
        }

	public void refreshData() {
		loadDataAsync();
	}
}
