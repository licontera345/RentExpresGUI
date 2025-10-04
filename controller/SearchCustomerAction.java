package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.ActionCallbackThread;

import com.pinguela.rentexpres.desktop.dialog.CustomerDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.CustomerEditDialog;
import com.pinguela.rentexpres.desktop.model.CustomerSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.model.ProvinceDTO;
import com.pinguela.rentexpres.service.CustomerService;
import com.pinguela.rentexpres.service.CityService;
import com.pinguela.rentexpres.service.ProvinceService;


public class SearchCustomerAction {

	private static final String ERROR_CARGANDO = "Error cargando customers:\n";
	private static final String ERROR_ACTUALIZANDO = "Error actualizando customer:\n";

	private final Frame frame;
	private final CustomerService customerService;
	private final CityService cityService;
	private final ProvinceService provinceService;
	private final JTable table;

	private CustomerSearchTableModel model;

	public SearchCustomerAction(Frame frame, CustomerService customerService, CityService cityService,
			ProvinceService provinceService, JTable table) {
		this.frame = frame;
		this.customerService = customerService;
		this.cityService = cityService;
		this.provinceService = provinceService;
		this.table = table;
	}


	public void loadAsync() {
       new ActionCallbackThread(new ActionCallback() {
                       @Override
                       public void execute() {
                               try {
                                       List<CustomerDTO> customers = customerService.findAll();
                                       Map<String, String> locMap = buildLocMap();
                                       Map<String, String> provMap = buildProvMap();

                                       model = new CustomerSearchTableModel(customers, locMap, provMap);

                                       SwingUtils.invokeLater(new ActionCallback() {
                                               @Override
                                               public void execute() {
                                                       table.setModel(model);
                                               }
                                       });

                               } catch (RentexpresException ex) {
                                       SwingUtils.invokeLater(new ActionCallback() {
                                               @Override
                                               public void execute() {
                                                       SwingUtils.showError(frame, ERROR_CARGANDO + ex.getMessage());
                                               }
                                       });
                               }
                       }
               }).start();
	}


	public void showDetail() {
		CustomerDTO sel = getSelected();
		if (sel != null)
			new CustomerDetailDialog(frame, sel).setVisible(true);
		else
			SwingUtils.showWarning(frame, "Selecciona un customer para ver el detalle.");
	}

	public void showEdit() {
		CustomerDTO sel = getSelected();
		if (sel == null) {
			SwingUtils.showWarning(frame, "Selecciona un customer para editar.");
			return;
		}

                CustomerEditDialog dlg = new CustomerEditDialog(frame, sel);
		dlg.setVisible(true);

		if (dlg.isConfirmed()) {
			updateAsync(dlg.getCustomer());
		}
	}


	private CustomerDTO getSelected() {
		int row = table.getSelectedRow();
		return row < 0 ? null : model.getCustomerAt(row);
	}

	private void updateAsync(CustomerDTO customer) {
       new ActionCallbackThread(new ActionCallback() {
                       @Override
                       public void execute() {
                               try {
                                       customerService.update(customer);
                                       loadAsync();
                               } catch (RentexpresException ex) {
                                       SwingUtils.invokeLater(new ActionCallback() {
                                               @Override
                                               public void execute() {
                                                       SwingUtils.showError(frame, ERROR_ACTUALIZANDO + ex.getMessage());
                                               }
                                       });
                               }
                       }
               }).start();
	}

        private Map<String, String> buildLocMap() throws RentexpresException {
                java.util.Map<String, String> map = new java.util.LinkedHashMap<String, String>();
                java.util.List<CityDTO> list = cityService.findAll();
                for (CityDTO l : list) {
                        map.put(l.getName(), l.getName());
                }
                return map;
        }

        private Map<String, String> buildProvMap() throws RentexpresException {
                java.util.Map<String, String> map = new java.util.LinkedHashMap<String, String>();
                java.util.List<ProvinceDTO> list = provinceService.findAll();
                for (ProvinceDTO p : list) {
                        map.put(p.getName(), p.getName());
                }
                return map;
        }
}
