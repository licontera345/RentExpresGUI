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

import com.pinguela.rentexpres.desktop.dialog.ClienteCreateDialog;
import com.pinguela.rentexpres.desktop.dialog.ClienteDetailDialog;
import com.pinguela.rentexpres.desktop.model.ClienteSearchTableModel;
import com.pinguela.rentexpres.desktop.util.CatalogCache;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.view.ClienteFilterPanel;
import com.pinguela.rentexpres.desktop.view.ClienteSearchActionsView;
import com.pinguela.rentexpres.desktop.view.ClienteTablePanel;
import com.pinguela.rentexpres.desktop.view.ClienteSearchView;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteCriteria;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.model.LocalidadDTO;
import com.pinguela.rentexpres.model.ProvinciaDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.ClienteService;
import com.pinguela.rentexpres.service.LocalidadService;
import com.pinguela.rentexpres.service.ProvinciaService;

public class ClienteSearchController {
	private static final int PAGE_SIZE = 25;
	private static final String TODAS_PROVINCIAS = "Todas";
	private static final String TODAS_LOCALIDADES = "Todas";

	private final ClienteSearchView view;
	private final ClienteSearchTableModel model;
	private final ClienteService clienteService;
	private final ProvinciaService provinciaService;
	private final LocalidadService localidadService;
	private final Frame frame;

	private int currentPage = 1;
	private int totalPages = 1;

	private boolean initializing = true;
	private boolean loading = false;

	public ClienteSearchController(ClienteSearchView view, ClienteService clienteService,
			ProvinciaService provinciaService, LocalidadService localidadService, Frame owner)
			throws Exception {
		this.view = Objects.requireNonNull(view);
		this.clienteService = Objects.requireNonNull(clienteService);
		this.provinciaService = Objects.requireNonNull(provinciaService);
		this.localidadService = Objects.requireNonNull(localidadService);
		this.frame = Objects.requireNonNull(owner);

               java.util.Map<String, String> locMap = new java.util.HashMap<String, String>();
               for (LocalidadDTO dto : CatalogCache.getLocalidades(localidadService)) {
                       String nombre = dto.getNombre();
                       if (!locMap.containsKey(nombre)) {
                               if (nombre == null)
                                       locMap.put(nombre, "");
                               else {
                                       String lower = nombre.toLowerCase();
                                       locMap.put(nombre, Character.toUpperCase(lower.charAt(0)) + lower.substring(1));
                               }
                       }
               }

               java.util.Map<String, String> provMap = new java.util.HashMap<String, String>();
               for (ProvinciaDTO dto : CatalogCache.getProvincias(provinciaService)) {
                       String nombre = dto.getNombre();
                       if (!provMap.containsKey(nombre)) {
                               if (nombre == null)
                                       provMap.put(nombre, "");
                               else {
                                       String lower = nombre.toLowerCase();
                                       provMap.put(nombre, Character.toUpperCase(lower.charAt(0)) + lower.substring(1));
                               }
                       }
               }

		// Ahora sí coinciden las firmas con tu modelo:
		this.model = new ClienteSearchTableModel(locMap, provMap, provinciaService, localidadService);

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
		ClienteFilterPanel filter = view.getFilter();
                ClienteSearchActionsView actions = view.getActions();
                ClienteTablePanel tablePanel = view.getTable();

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

               filter.getCmbProvincia().addActionListener(new java.awt.event.ActionListener() {
                       @Override
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                               if (!initializing && !loading) {
                                       String provinciaSeleccionada = (String) filter.getCmbProvincia().getSelectedItem();
                                       try {
                                               cargarLocalidadesPorProvincia(provinciaSeleccionada);
                                       } catch (Exception e1) {
                                               e1.printStackTrace();
                                       }
                                       goFirstPage();
                               }
                       }
               });

               view.getPager().onPrev(new ActionCallback() {
                       @Override
                       public void execute() {
                               if (!loading && currentPage > 1) {
                                       currentPage--;
                                       buscar();
                               }
                       }
               });
               view.getPager().onNext(new ActionCallback() {
                       @Override
                       public void execute() {
                               if (!loading && currentPage < totalPages) {
                                       currentPage++;
                                       buscar();
                               }
                       }
               });
               view.getPager().onFirst(new ActionCallback() {
                       @Override
                       public void execute() {
                               if (!loading) {
                                       goFirstPage();
                               }
                       }
               });
               view.getPager().onLast(new ActionCallback() {
                       @Override
                       public void execute() {
                               if (!loading && currentPage < totalPages) {
                                       currentPage = totalPages;
                                       buscar();
                               }
                       }
               });

               actions.onNuevo(new ActionCallback() {
                       @Override
                       public void execute() {
                               ClienteCreateDialog dlg = new ClienteCreateDialog(frame);
                               dlg.setVisible(true);
                               if (dlg.isConfirmed()) {
                                       try {
                                               clienteService.create(dlg.getCliente());
                                               goFirstPage();
                                       } catch (RentexpresException ex) {
                                               SwingUtils.showError(view, "Error creando cliente: " + ex.getMessage());
                                       }
                               }
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

                               ClienteSearchTableModel m = (ClienteSearchTableModel) view.getTable().getTable().getModel();
                               List<ClienteDTO> seleccionados = m.getSelectedItems();
                               if (seleccionados.isEmpty())
                                       return;

                               int resp = SwingUtils.showConfirm(frame, "¿Eliminar los clientes seleccionados?", "Confirmar borrado");
                               if (resp == JOptionPane.YES_OPTION) {
                                       try {
                                               for (ClienteDTO c : seleccionados) {
                                                       clienteService.delete(c.getId());
                                               }
                                               goFirstPage();
                                       } catch (RentexpresException ex) {
                                               SwingUtils.showError(view, "Error al eliminar clientes: " + ex.getMessage());
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
					ClienteDTO sel = model.getClienteAt(rowModel);
					if (sel != null) {
						ClienteDetailDialog dlg = new ClienteDetailDialog(frame, sel);
						dlg.setVisible(true);
					}
				}
			}
		});

	}

	private void cargarCombosIniciales() throws Exception {
		ClienteFilterPanel filter = view.getFilter();

		JComboBox<String> cmbProv = filter.getCmbProvincia();
		cmbProv.removeAllItems();
		cmbProv.addItem(TODAS_PROVINCIAS);
               java.util.List<String> provs = new java.util.ArrayList<String>();
               for (ProvinciaDTO p : CatalogCache.getProvincias(provinciaService)) {
                       provs.add(p.getNombre());
               }
               java.util.Collections.sort(provs);
               for (String p : provs) {
                       cmbProv.addItem(p);
               }
		cmbProv.setSelectedIndex(0);
		cmbProv.setRenderer(new DefaultListCellRenderer());

		cargarLocalidadesPorProvincia(null);
	}

	private void cargarLocalidadesPorProvincia(String provincia) throws Exception {
		ClienteFilterPanel filter = view.getFilter();
		JComboBox<String> cmbLoc = filter.getCmbLocalidad();
		cmbLoc.removeAllItems();
		cmbLoc.addItem(TODAS_LOCALIDADES);

               java.util.Set<String> locs = new java.util.HashSet<String>();
               if (provincia == null || TODAS_PROVINCIAS.equals(provincia)) {
                       for (LocalidadDTO loc : CatalogCache.getLocalidades(localidadService)) {
                               locs.add(loc.getNombre());
                       }
               } else {
                       for (LocalidadDTO loc : CatalogCache.getLocalidades(localidadService)) {
                               if (loc.getNombre().equals(provincia)) {
                                       locs.add(loc.getNombre());
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

	private ClienteCriteria buildCriteria() {
		ClienteFilterPanel f = view.getFilter();
		ClienteCriteria c = new ClienteCriteria();

		if (f.getId() != null && f.getId() > 0) {
			c.setId(f.getId());
		}
		if (f.getNombre() != null && !f.getNombre().trim().isEmpty()) {
			c.setNombre(f.getNombre());
		}
		if (f.getApellido1() != null && !f.getApellido1().trim().isEmpty()) {
			c.setApellido1(f.getApellido1());
		}
		if (f.getApellido2() != null && !f.getApellido2().trim().isEmpty()) {
			c.setApellido2(f.getApellido2());
		}
		if (f.getEmail() != null && !f.getEmail().trim().isEmpty()) {
			c.setEmail(f.getEmail());
		}
		if (f.getTelefono() != null && !f.getTelefono().trim().isEmpty()) {
			c.setTelefono(f.getTelefono());
		}
		if (f.getCalle() != null && !f.getCalle().trim().isEmpty()) {
			c.setCalle(f.getCalle());
		}
		if (f.getNumero() != null && !f.getNumero().trim().isEmpty()) {
			c.setNumero(f.getNumero());
		}
		String provSel = f.getProvincia();
		if (provSel != null && !provSel.equals(TODAS_PROVINCIAS)) {
			c.setNombreProvincia(provSel);
		}
		String locSel = f.getLocalidad();
		if (locSel != null && !locSel.equals(TODAS_LOCALIDADES)) {
			c.setNombreLocalidad(locSel);
		}

		return c;
	}

	public void buscar() {
		if (loading)
			return;
		loading = true;
		try {
			ClienteCriteria crit = buildCriteria();
			crit.setPageNumber(currentPage);
			crit.setPageSize(PAGE_SIZE);

			Results<ClienteDTO> res = clienteService.findByCriteria(crit);
			totalPages = Math.max(1, (int) Math.ceil(res.getTotalRecords() / (double) PAGE_SIZE));

			model.setClientes(res.getResults());
			view.getPager().setInfo(currentPage, totalPages);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error al buscar clientes: " + ex.getMessage());
		} finally {
			loading = false;
		}
	}

	public void goFirstPage() {
		currentPage = 1;
		buscar();
	}
}
