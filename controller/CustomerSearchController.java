package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.pinguela.rentexpres.desktop.dialog.CustomerCreateDialog;
import com.pinguela.rentexpres.desktop.dialog.CustomerDetailDialog;
import com.pinguela.rentexpres.desktop.model.CustomerSearchTableModel;
import com.pinguela.rentexpres.desktop.util.CatalogCache;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
import com.pinguela.rentexpres.desktop.view.CustomerFilterPanel;
import com.pinguela.rentexpres.desktop.view.CustomerSearchActionsView;
import com.pinguela.rentexpres.desktop.view.CustomerTablePanel;
import com.pinguela.rentexpres.desktop.view.CustomerSearchView;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CustomerCriteria;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.model.ProvinceDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.CustomerService;
import com.pinguela.rentexpres.service.CityService;
import com.pinguela.rentexpres.service.ProvinceService;

public class CustomerSearchController {
	private static final int PAGE_SIZE = 25;
	private static final String TODAS_PROVINCIAS = "Todas";
	private static final String TODAS_LOCALIDADES = "Todas";

	private final CustomerSearchView view;
	private final CustomerSearchTableModel model;
	private final CustomerService customerService;
	private final ProvinceService provinceService;
	private final CityService cityService;
	private final Frame frame;

	private int currentPage = 1;
	private int totalPages = 1;

	private boolean initializing = true;
	private boolean loading = false;

	public CustomerSearchController(CustomerSearchView view, CustomerService customerService,
			ProvinceService provinceService, CityService cityService, Frame owner)
			throws Exception {
		this.view = Objects.requireNonNull(view);
		this.customerService = Objects.requireNonNull(customerService);
		this.provinceService = Objects.requireNonNull(provinceService);
		this.cityService = Objects.requireNonNull(cityService);
		this.frame = Objects.requireNonNull(owner);

               java.util.Map<String, String> locMap = new java.util.HashMap<String, String>();
               for (CityDTO dto : CatalogCache.getCities(cityService)) {
                       String name = dto.getName();
                       if (!locMap.containsKey(name)) {
                               if (name == null)
                                       locMap.put(name, "");
                               else {
                                       String lower = name.toLowerCase();
                                       locMap.put(name, Character.toUpperCase(lower.charAt(0)) + lower.substring(1));
                               }
                       }
               }

               java.util.Map<String, String> provMap = new java.util.HashMap<String, String>();
               for (ProvinceDTO dto : CatalogCache.getProvinces(provinceService)) {
                       String name = dto.getName();
                       if (!provMap.containsKey(name)) {
                               if (name == null)
                                       provMap.put(name, "");
                               else {
                                       String lower = name.toLowerCase();
                                       provMap.put(name, Character.toUpperCase(lower.charAt(0)) + lower.substring(1));
                               }
                       }
               }

		// Ahora sí coinciden las firmas con tu model:
		this.model = new CustomerSearchTableModel(locMap, provMap, provinceService, cityService);

		view.getTable().setModel(model);
		wireListeners();
		cargarCombosIniciales();
		initializing = false;
	}

	public void init() throws Exception {
		initializing = true;
		try {
			cargarCombosIniciales();
		} catch (RentexpresException ex) {
			SwingUtils.showError(view, "Error cargando catálogos: " + ex.getMessage());
		}
		initializing = false;
		goFirstPage();
	}

	private void wireListeners() {
		CustomerFilterPanel filter = view.getFilter();
                CustomerSearchActionsView actions = view.getActions();
                CustomerTablePanel tablePanel = view.getTable();

               filter.setOnChange(new ActionCallback() {
                       @Override
                       public void execute() {
                               if (!initializing && !loading) {
                                       goFirstPage();
                               }
                       }
               });

               filter.setToggleListener(new ActionCallback() {
                       @Override
                       public void execute() {
                               tablePanel.toggleSelectColumn();
                       }
               });

               filter.getCmbProvince().addActionListener(new java.awt.event.ActionListener() {
                       @Override
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                               if (!initializing && !loading) {
                                       String provinceSeleccionada = (String) filter.getCmbProvince().getSelectedItem();
                                       try {
                                               cargarCitiesPorProvince(provinceSeleccionada);
                                       } catch (Exception e1) {
                                               e1.printStackTrace();
                                       }
                                       goFirstPage();
                               }
                       }
               });

               view.getPager().onPrev(new PaginationPanel.OnPagerListener() {
                       @Override
                       public void onAction() {
                               if (!loading && currentPage > 1) {
                                       currentPage--;
                                       buscar();
                               }
                       }
               });
               view.getPager().onNext(new PaginationPanel.OnPagerListener() {
                       @Override
                       public void onAction() {
                               if (!loading && currentPage < totalPages) {
                                       currentPage++;
                                       buscar();
                               }
                       }
               });
               view.getPager().onFirst(new PaginationPanel.OnPagerListener() {
                       @Override
                       public void onAction() {
                               if (!loading) {
                                       goFirstPage();
                               }
                       }
               });
               view.getPager().onLast(new PaginationPanel.OnPagerListener() {
                       @Override
                       public void onAction() {
                               if (!loading && currentPage < totalPages) {
                                       currentPage = totalPages;
                                       buscar();
                               }
                       }
               });

               actions.onNuevo(new ActionCallback() {
                       @Override
                       public void execute() {
                               CustomerCreateDialog dlg = new CustomerCreateDialog(frame);
                               dlg.setVisible(true);
                               if (dlg.isConfirmed()) {
                                       try {
                                               customerService.create(dlg.getCustomer());
                                               goFirstPage();
                                       } catch (RentexpresException ex) {
                                               SwingUtils.showError(view, "Error creando customer: " + ex.getMessage());
                                       }
                               }
                       }
               });
               actions.onBuscar(new ActionCallback() {
                       @Override
                       public void execute() {
                               goFirstPage();
                       }
               });

               actions.onLimpiar(new ActionCallback() {
                       @Override
                       public void execute() {
                               filter.clear();
                               view.getTable().hideSelectColumn();
                               goFirstPage();
                       }
               });

               actions.onBorrarSeleccionados(new ActionCallback() {
                       @Override
                       public void execute() {

                               CustomerSearchTableModel m = (CustomerSearchTableModel) view.getTable().getTable().getModel();
                               List<CustomerDTO> selected = m.getSelectedItems();
                               if (selected.isEmpty())
                                       return;

                               int resp = SwingUtils.showConfirm(frame, "Delete los customers selected?", "Confirm deletion");
                               if (resp == JOptionPane.YES_OPTION) {
                                       try {
                                               for (CustomerDTO c : selected) {
                                                       customerService.delete(c.getId());
                                               }
                                               goFirstPage();
                                       } catch (RentexpresException ex) {
                                               SwingUtils.showError(view, "Error al delete customers: " + ex.getMessage());
                                       }
                               }
                       }
               });

		view.getTable().getTable().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
					JTable tabla = view.getTable().getTable(); // << Ahora ´tabla´ es un JTable real
					int rowView = tabla.getSelectedRow();
					if (rowView < 0)
						return;
					int rowModel = tabla.convertRowIndexToModel(rowView);
					CustomerDTO sel = model.getCustomerAt(rowModel);
					if (sel != null) {
						CustomerDetailDialog dlg = new CustomerDetailDialog(frame, sel);
						dlg.setVisible(true);
					}
				}
			}
		});

	}

	private void cargarCombosIniciales() throws Exception {
		CustomerFilterPanel filter = view.getFilter();

		JComboBox<String> cmbProv = filter.getCmbProvince();
		cmbProv.removeAllItems();
		cmbProv.addItem(TODAS_PROVINCIAS);
               java.util.List<String> provs = new java.util.ArrayList<String>();
               for (ProvinceDTO p : CatalogCache.getProvinces(provinceService)) {
                       provs.add(p.getName());
               }
               java.util.Collections.sort(provs);
               for (String p : provs) {
                       cmbProv.addItem(p);
               }
		cmbProv.setSelectedIndex(0);
		cmbProv.setRenderer(new DefaultListCellRenderer());

		cargarCitiesPorProvince(null);
	}

	private void cargarCitiesPorProvince(String province) throws Exception {
		CustomerFilterPanel filter = view.getFilter();
		JComboBox<String> cmbLoc = filter.getCmbCity();
		cmbLoc.removeAllItems();
		cmbLoc.addItem(TODAS_LOCALIDADES);

               java.util.Set<String> locs = new java.util.HashSet<String>();
               if (province == null || TODAS_PROVINCIAS.equals(province)) {
                       for (CityDTO loc : CatalogCache.getCities(cityService)) {
                               locs.add(loc.getName());
                       }
               } else {
                       for (CityDTO loc : CatalogCache.getCities(cityService)) {
                               if (loc.getName().equals(province)) {
                                       locs.add(loc.getName());
                               }
                       }
               }
               java.util.List<String> locList = new java.util.ArrayList<String>(locs);
               java.util.Collections.sort(locList);
               for (String l : locList) {
                       cmbLoc.addItem(l);
               }
		cmbLoc.setSelectedIndex(0);
		cmbLoc.setRenderer(new DefaultListCellRenderer());
	}

	private CustomerCriteria buildCriteria() {
		CustomerFilterPanel f = view.getFilter();
		CustomerCriteria c = new CustomerCriteria();

		if (f.getId() != null && f.getId() > 0) {
			c.setId(f.getId());
		}
		if (f.getName() != null && !f.getName().trim().isEmpty()) {
			c.setName(f.getName());
		}
		if (f.getLastName() != null && !f.getLastName().trim().isEmpty()) {
			c.setLastName(f.getLastName());
		}
		if (f.getSecondLastName() != null && !f.getSecondLastName().trim().isEmpty()) {
			c.setSecondLastName(f.getSecondLastName());
		}
		if (f.getEmail() != null && !f.getEmail().trim().isEmpty()) {
			c.setEmail(f.getEmail());
		}
		if (f.getPhone() != null && !f.getPhone().trim().isEmpty()) {
			c.setPhone(f.getPhone());
		}
		if (f.getStreet() != null && !f.getStreet().trim().isEmpty()) {
			c.setStreet(f.getStreet());
		}
		if (f.getStreetNumber() != null && !f.getStreetNumber().trim().isEmpty()) {
			c.setStreetNumber(f.getStreetNumber());
		}
		String provSel = f.getProvince();
		if (provSel != null && !provSel.equals(TODAS_PROVINCIAS)) {
			c.setProvinceName(provSel);
		}
		String locSel = f.getCity();
		if (locSel != null && !locSel.equals(TODAS_LOCALIDADES)) {
			c.setCityName(locSel);
		}

		return c;
	}

	public void buscar() {
		if (loading)
			return;
		loading = true;
		try {
			CustomerCriteria crit = buildCriteria();
			crit.setPageNumber(currentPage);
			crit.setPageSize(PAGE_SIZE);

			Results<CustomerDTO> res = customerService.findByCriteria(crit);
			totalPages = Math.max(1, (int) Math.ceil(res.getTotalRecords() / (double) PAGE_SIZE));

			model.setCustomers(res.getResults());
			view.getPager().setInfo(currentPage, totalPages);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error al buscar customers: " + ex.getMessage());
		} finally {
			loading = false;
		}
	}

	public void goFirstPage() {
		currentPage = 1;
		buscar();
	}
}
