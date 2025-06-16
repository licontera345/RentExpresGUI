package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.pinguela.rentexpres.desktop.model.VehiculoSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.CategoriaVehiculoDTO;
import com.pinguela.rentexpres.model.EstadoVehiculoDTO;
import com.pinguela.rentexpres.model.VehiculoCriteria;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.CategoriaVehiculoService;
import com.pinguela.rentexpres.service.EstadoVehiculoService;
import com.pinguela.rentexpres.service.VehiculoService;
import java.util.List;


public class VehiculoController {

	private final Frame frame;
	private final VehiculoService vehiculoService;
	private final CategoriaVehiculoService categoriaService;
	private final EstadoVehiculoService estadoService;

	private final JTable tableVehiculo;
	private final JTextField txtMarca;
	private final JTextField txtModelo;
	private final JTextField txtAnioDesde;
	private final JTextField txtAnioHasta;
	private final JTextField txtPrecioMax;
	private final JComboBox<EstadoVehiculoDTO> cbEstado;
	private final JComboBox<CategoriaVehiculoDTO> cbCategoria;

	private final JButton btnBuscar;
	private final JButton btnLimpiar;
	private final JButton btnCrear;
	private final JButton btnDetalle;
	private final JButton btnEditar;
	private final JButton btnEliminar;
	private final JButton btnEliminarSeleccionados;

	private final SearchVehiculoAction searchAction;

	public VehiculoController(Frame frame, VehiculoService vehiculoService, CategoriaVehiculoService categoriaService,
			EstadoVehiculoService estadoService, JTable tableVehiculo, JTextField txtMarca, JTextField txtModelo,
			JTextField txtAnioDesde, JTextField txtAnioHasta, JTextField txtPrecioMax,
			JComboBox<EstadoVehiculoDTO> cbEstado, JComboBox<CategoriaVehiculoDTO> cbCategoria, JButton btnBuscar,
			JButton btnLimpiar, JButton btnCrear, JButton btnDetalle, JButton btnEditar, JButton btnEliminar,
			JButton btnEliminarSeleccionados) {
		this.frame = frame;
		this.vehiculoService = vehiculoService;
		this.categoriaService = categoriaService;
		this.estadoService = estadoService;

		this.tableVehiculo = tableVehiculo;
		this.txtMarca = txtMarca;
		this.txtModelo = txtModelo;
		this.txtAnioDesde = txtAnioDesde;
		this.txtAnioHasta = txtAnioHasta;
		this.txtPrecioMax = txtPrecioMax;
		this.cbEstado = cbEstado;
		this.cbCategoria = cbCategoria;

		this.btnBuscar = btnBuscar;
		this.btnLimpiar = btnLimpiar;
		this.btnCrear = btnCrear;
		this.btnDetalle = btnDetalle;
		this.btnEditar = btnEditar;
		this.btnEliminar = btnEliminar;
		this.btnEliminarSeleccionados = btnEliminarSeleccionados;

	
		this.searchAction = new SearchVehiculoAction(frame, vehiculoService, categoriaService, estadoService,
				tableVehiculo);

		initListeners();
		initialize();
	}

	private void initialize() {
		try {
			// Carga inicial sin filtros
			searchAction.load();
			cargarCombos();
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error cargando vehículos: " + ex.getMessage());
		}
	}

	private void cargarCombos() {
		try {
			cbEstado.removeAllItems();
			cbEstado.addItem(null); 
			for (EstadoVehiculoDTO ev : estadoService.findAll()) {
				cbEstado.addItem(ev);
			}

			cbCategoria.removeAllItems();
			cbCategoria.addItem(null);
			for (CategoriaVehiculoDTO cat : categoriaService.findAll()) {
				cbCategoria.addItem(cat);
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

		btnEliminar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eliminarSeleccionado();
			}
		});

		btnEliminarSeleccionados.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eliminarMultiples();
			}
		});
	}

	private void buscarPorCriterios() {
		try {
			VehiculoCriteria criteria = new VehiculoCriteria();

			if (!txtMarca.getText().trim().isEmpty()) {
				criteria.setMarca(txtMarca.getText().trim());
			}
			if (!txtModelo.getText().trim().isEmpty()) {
				criteria.setModelo(txtModelo.getText().trim());
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
			EstadoVehiculoDTO ev = (EstadoVehiculoDTO) cbEstado.getSelectedItem();
			if (ev != null && ev.getId() != null) {
				criteria.setIdEstadoVehiculo(ev.getId());
			}
			CategoriaVehiculoDTO cat = (CategoriaVehiculoDTO) cbCategoria.getSelectedItem();
			if (cat != null && cat.getId() != null) {
				criteria.setIdCategoria(cat.getId());
			}

			searchAction.loadByCriteria(criteria);
		} catch (NumberFormatException nfe) {
			SwingUtils.showError(frame, "Formato numérico inválido en filtros.");
		}
	}

	private void limpiarFiltros() {
		txtMarca.setText("");
		txtModelo.setText("");
		txtAnioDesde.setText("");
		txtAnioHasta.setText("");
		txtPrecioMax.setText("");
		if (cbEstado.getItemCount() > 0) {
			cbEstado.setSelectedIndex(0);
		}
		if (cbCategoria.getItemCount() > 0) {
			cbCategoria.setSelectedIndex(0);
		}
		try {
			searchAction.load();
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error recargando lista: " + ex.getMessage());
		}
	}

	private VehiculoDTO getSelectedVehiculo() {
		int viewRow = tableVehiculo.getSelectedRow();
		if (viewRow < 0) {
			SwingUtils.showWarning(frame, "Seleccione un vehículo.");
			return null;
		}
		int modelRow = tableVehiculo.convertRowIndexToModel(viewRow);
		return ((VehiculoSearchTableModel) tableVehiculo.getModel()).getVehiculoAt(modelRow);
	}

	private void mostrarCrear() {
		try {
			searchAction.showCreate(() -> {
				try {
					searchAction.load();
				} catch (Exception ex) {
					SwingUtils.showError(frame, "Error recargando lista: " + ex.getMessage());
				}
			});
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error al abrir formulario: " + ex.getMessage());
		}
	}

	private void mostrarDetalle() {
		VehiculoDTO dto = getSelectedVehiculo();
		if (dto != null) {
			searchAction.showDetail(dto);
		}
	}

	private void mostrarEditar() {
		VehiculoDTO dto = getSelectedVehiculo();
		if (dto != null) {
			try {
				searchAction.showEdit(dto, () -> {
					try {
						searchAction.load();
					} catch (Exception ex) {
						SwingUtils.showError(frame, "Error recargando lista: " + ex.getMessage());
					}
				});
			} catch (Exception ex) {
				SwingUtils.showError(frame, "Error al abrir edición: " + ex.getMessage());
			}
		}
	}

	private void eliminarSeleccionado() {
		VehiculoDTO dto = getSelectedVehiculo();
		if (dto != null) {
			int confirm = JOptionPane.showConfirmDialog(frame, "¿Eliminar vehículo con ID " + dto.getId() + "?",
					"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirm == JOptionPane.YES_OPTION) {
				try {
					vehiculoService.delete(dto.getId());
					searchAction.load();
				} catch (Exception ex) {
					SwingUtils.showError(frame, "Error eliminando: " + ex.getMessage());
				}
			}
		}
	}

	private void eliminarMultiples() {
                VehiculoSearchTableModel model = (VehiculoSearchTableModel) tableVehiculo.getModel();
                List<VehiculoDTO> seleccionados = model.getSelectedItems();
		if (seleccionados.isEmpty()) {
			SwingUtils.showWarning(frame, "No hay vehículos seleccionados.");
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(frame,
				"¿Eliminar " + seleccionados.size() + " vehículos seleccionados?", "Confirmar eliminación",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}
		StringBuilder errores = new StringBuilder();
		for (VehiculoDTO dto : seleccionados) {
			try {
				vehiculoService.delete(dto.getId());
			} catch (Exception ex) {
				errores.append("ID ").append(dto.getId()).append(": ").append(ex.getMessage()).append("\n");
			}
		}
		if (errores.length() > 0) {
			SwingUtils.showError(frame, "Errores al eliminar:\n" + errores);
		}
		try {
			searchAction.load();
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error recargando lista: " + ex.getMessage());
		}
	}
}
