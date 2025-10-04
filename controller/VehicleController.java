package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.pinguela.rentexpres.desktop.model.VehicleSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.VehicleStatusService;
import com.pinguela.rentexpres.service.VehicleService;
import java.util.List;


public class VehicleController {

	private final Frame frame;
	private final VehicleService vehicleService;
	private final VehicleCategoryService categoryService;
	private final VehicleStatusService estadoService;

	private final JTable tableVehicle;
	private final JTextField txtMake;
	private final JTextField txtModel;
	private final JTextField txtAnioDesde;
	private final JTextField txtAnioHasta;
	private final JTextField txtPrecioMax;
	private final JComboBox<VehicleStatusDTO> cbEstado;
	private final JComboBox<VehicleCategoryDTO> cbCategory;

	private final JButton btnBuscar;
	private final JButton btnLimpiar;
	private final JButton btnCrear;
	private final JButton btnDetalle;
	private final JButton btnEditar;
	private final JButton btnDelete;
	private final JButton btnDeleteSeleccionados;

	private final SearchVehicleAction searchAction;

	public VehicleController(Frame frame, VehicleService vehicleService, VehicleCategoryService categoryService,
			VehicleStatusService estadoService, JTable tableVehicle, JTextField txtMake, JTextField txtModel,
			JTextField txtAnioDesde, JTextField txtAnioHasta, JTextField txtPrecioMax,
			JComboBox<VehicleStatusDTO> cbEstado, JComboBox<VehicleCategoryDTO> cbCategory, JButton btnBuscar,
			JButton btnLimpiar, JButton btnCrear, JButton btnDetalle, JButton btnEditar, JButton btnDelete,
			JButton btnDeleteSeleccionados) {
		this.frame = frame;
		this.vehicleService = vehicleService;
		this.categoryService = categoryService;
		this.estadoService = estadoService;

		this.tableVehicle = tableVehicle;
		this.txtMake = txtMake;
		this.txtModel = txtModel;
		this.txtAnioDesde = txtAnioDesde;
		this.txtAnioHasta = txtAnioHasta;
		this.txtPrecioMax = txtPrecioMax;
		this.cbEstado = cbEstado;
		this.cbCategory = cbCategory;

		this.btnBuscar = btnBuscar;
		this.btnLimpiar = btnLimpiar;
		this.btnCrear = btnCrear;
		this.btnDetalle = btnDetalle;
		this.btnEditar = btnEditar;
		this.btnDelete = btnDelete;
		this.btnDeleteSeleccionados = btnDeleteSeleccionados;

	
		this.searchAction = new SearchVehicleAction(frame, vehicleService, categoryService, estadoService,
				tableVehicle);

		initListeners();
		initialize();
	}

	private void initialize() {
		try {
			// Carga inicial sin filtros
			searchAction.load();
			cargarCombos();
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error cargando vehicles: " + ex.getMessage());
		}
	}

	private void cargarCombos() {
		try {
			cbEstado.removeAllItems();
			cbEstado.addItem(null); 
			for (VehicleStatusDTO ev : estadoService.findAll()) {
				cbEstado.addItem(ev);
			}

			cbCategory.removeAllItems();
			cbCategory.addItem(null);
			for (VehicleCategoryDTO cat : categoryService.findAll()) {
				cbCategory.addItem(cat);
			}

		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error cargando combos: " + ex.getMessage());
		}
	}

	private void initListeners() {
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buscarPorCriterios();
			}
		});

		btnLimpiar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				limpiarFiltros();
			}
		});

		btnCrear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarCrear();
			}
		});

		btnDetalle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarDetalle();
			}
		});

		btnEditar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarEditar();
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSeleccionado();
			}
		});

		btnDeleteSeleccionados.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteMultiples();
			}
		});
	}

	private void buscarPorCriterios() {
		try {
			VehicleCriteria criteria = new VehicleCriteria();

			if (!txtMake.getText().trim().isEmpty()) {
				criteria.setMake(txtMake.getText().trim());
			}
			if (!txtModel.getText().trim().isEmpty()) {
				criteria.setModel(txtModel.getText().trim());
			}
			if (!txtAnioDesde.getText().trim().isEmpty()) {
				criteria.setAnioDesde(Integer.parseInt(txtAnioDesde.getText().trim()));
			}
			if (!txtAnioHasta.getText().trim().isEmpty()) {
				criteria.setAnioHasta(Integer.parseInt(txtAnioHasta.getText().trim()));
			}
			if (!txtPrecioMax.getText().trim().isEmpty()) {
				criteria.setPrecioMax(Double.parseDouble(txtPrecioMax.getText().trim()));
			}
			VehicleStatusDTO ev = (VehicleStatusDTO) cbEstado.getSelectedItem();
			if (ev != null && ev.getId() != null) {
				criteria.setVehicleStatusId(ev.getId());
			}
			VehicleCategoryDTO cat = (VehicleCategoryDTO) cbCategory.getSelectedItem();
			if (cat != null && cat.getId() != null) {
				criteria.setCategoryId(cat.getId());
			}

			searchAction.loadByCriteria(criteria);
		} catch (NumberFormatException nfe) {
			SwingUtils.showError(frame, "Formato numérico inválido en filtros.");
		}
	}

	private void limpiarFiltros() {
		txtMake.setText("");
		txtModel.setText("");
		txtAnioDesde.setText("");
		txtAnioHasta.setText("");
		txtPrecioMax.setText("");
		if (cbEstado.getItemCount() > 0) {
			cbEstado.setSelectedIndex(0);
		}
		if (cbCategory.getItemCount() > 0) {
			cbCategory.setSelectedIndex(0);
		}
		try {
			searchAction.load();
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error recargando lista: " + ex.getMessage());
		}
	}

	private VehicleDTO getSelectedVehicle() {
		int viewRow = tableVehicle.getSelectedRow();
		if (viewRow < 0) {
			SwingUtils.showWarning(frame, "Seleccione un vehicle.");
			return null;
		}
		int modelRow = tableVehicle.convertRowIndexToModel(viewRow);
		return ((VehicleSearchTableModel) tableVehicle.getModel()).getVehicleAt(modelRow);
	}

	private void mostrarCrear() {
                try {
                        searchAction.showCreate(new ActionCallback() {
                                @Override
                                public void execute() {
                                        try {
                                                searchAction.load();
                                        } catch (Exception ex) {
                                                SwingUtils.showError(frame, "Error recargando lista: " + ex.getMessage());
                                        }
                                }
                        });
                } catch (Exception ex) {
                        SwingUtils.showError(frame, "Error al abrir formulario: " + ex.getMessage());
                }
        }

	private void mostrarDetalle() {
		VehicleDTO dto = getSelectedVehicle();
		if (dto != null) {
			searchAction.showDetail(dto);
		}
	}

	private void mostrarEditar() {
		VehicleDTO dto = getSelectedVehicle();
		if (dto != null) {
			try {
                                searchAction.showEdit(dto, new ActionCallback() {
                                        @Override
                                        public void execute() {
                                                try {
                                                        searchAction.load();
                                                } catch (Exception ex) {
                                                        SwingUtils.showError(frame, "Error recargando lista: " + ex.getMessage());
                                                }
                                        }
                                });
                        } catch (Exception ex) {
                                SwingUtils.showError(frame, "Error al abrir edición: " + ex.getMessage());
                        }
                }
	}

	private void deleteSeleccionado() {
		VehicleDTO dto = getSelectedVehicle();
		if (dto != null) {
			int confirm = JOptionPane.showConfirmDialog(frame, "Delete vehicle con ID " + dto.getId() + "?",
					"Confirm deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirm == JOptionPane.YES_OPTION) {
				try {
					vehicleService.delete(dto.getId());
					searchAction.load();
				} catch (Exception ex) {
					SwingUtils.showError(frame, "Error eliminando: " + ex.getMessage());
				}
			}
		}
	}

	private void deleteMultiples() {
                VehicleSearchTableModel model = (VehicleSearchTableModel) tableVehicle.getModel();
                List<VehicleDTO> selected = model.getSelectedItems();
		if (selected.isEmpty()) {
			SwingUtils.showWarning(frame, "No hay vehicles selected.");
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(frame,
				"Delete " + selected.size() + " vehicles selected?", "Confirm deletion",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}
		StringBuilder errores = new StringBuilder();
		for (VehicleDTO dto : selected) {
			try {
				vehicleService.delete(dto.getId());
			} catch (Exception ex) {
				errores.append("ID ").append(dto.getId()).append(": ").append(ex.getMessage()).append("\n");
			}
		}
		if (errores.length() > 0) {
			SwingUtils.showError(frame, "Errores al delete:\n" + errores);
		}
		try {
			searchAction.load();
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error recargando lista: " + ex.getMessage());
		}
	}
}
