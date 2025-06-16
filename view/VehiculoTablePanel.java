package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import com.pinguela.rentexpres.desktop.controller.SearchVehiculoAction;
import com.pinguela.rentexpres.desktop.model.VehiculoSearchTableModel;
import com.pinguela.rentexpres.desktop.renderer.VehiculoActionsCellEditor;
import com.pinguela.rentexpres.desktop.renderer.VehiculoActionsCellRenderer;
import com.pinguela.rentexpres.desktop.renderer.VehiculoTableCellRenderer;
import com.pinguela.rentexpres.desktop.util.SelectionEditor;
import com.pinguela.rentexpres.desktop.util.SelectionRenderer;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.VehiculoService;

public class VehiculoTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTable tableVehiculo;
	private boolean selectVisible = false;
	/** Mantiene la acción para recargar la tabla cuando se usan los editores */
	private SearchVehiculoAction searchAction;

	public VehiculoTablePanel(SearchVehiculoAction searchAction, VehiculoService vehiculoService) {
		super(new BorderLayout());
		this.searchAction = searchAction;

		tableVehiculo = new JTable();

		// Modelo inicial vacío
		tableVehiculo.setModel(new VehiculoSearchTableModel(Collections.emptyList()));

		// Ajustes básicos
		tableVehiculo.setRowHeight(28);
		tableVehiculo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableVehiculo.getTableHeader().setReorderingAllowed(false);

		// Asegurarnos de que, al cambiar el modelo, forzamos tamaño de columnas:
		ajustarColumnasAcciones();

		// Asignar render/editor para la columna "Acciones"
		int lastCol = tableVehiculo.getColumnModel().getColumnCount() - 1;
		tableVehiculo.getColumnModel().getColumn(lastCol).setCellRenderer(new VehiculoActionsCellRenderer());
		tableVehiculo.getColumnModel().getColumn(lastCol)
				.setCellEditor(new VehiculoActionsCellEditor(tableVehiculo, vehiculoService, this.searchAction));

		// Columna "Seleccionar" (si existe)
		if (hasSelectColumn()) {
			tableVehiculo.getColumn("Seleccionar").setCellRenderer(new SelectionRenderer());
			tableVehiculo.getColumn("Seleccionar").setCellEditor(new SelectionEditor());
			setSelectColumnVisible(false);
		}

		// Renderizador genérico para las demás columnas
		DefaultTableCellRenderer gen = new DefaultTableCellRenderer();
		gen.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		for (int c = 0; c < tableVehiculo.getColumnCount(); c++) {
			String name = tableVehiculo.getColumnName(c);
			if (!"Acciones".equals(name) && !"Seleccionar".equals(name)) {
				tableVehiculo.getColumnModel().getColumn(c).setCellRenderer(new VehiculoTableCellRenderer());
			}
		}

		add(new JScrollPane(tableVehiculo), BorderLayout.CENTER);
	}

	/**
	 * Después de asignar un modelo nuevo (o en el constructor), llamamos a este
	 * método para forzar el ancho de la columna “Acciones”.
	 */
	private void ajustarColumnasAcciones() {
		SwingUtilities.invokeLater(() -> {
			if (tableVehiculo.getColumnCount() == 0)
				return;
			int last = tableVehiculo.getColumnCount() - 1;
			tableVehiculo.getColumnModel().getColumn(last).setPreferredWidth(100);
			tableVehiculo.getColumnModel().getColumn(last).setMinWidth(80);
			tableVehiculo.getColumnModel().getColumn(last).setMaxWidth(120);
		});
	}

	private boolean hasSelectColumn() {
		try {
			tableVehiculo.getColumn("Seleccionar");
			return true;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}

	public void toggleSelectColumn() {
		if (!hasSelectColumn())
			return;
		setSelectColumnVisible(!selectVisible);
	}

	private void setSelectColumnVisible(boolean visible) {
		selectVisible = visible;
		var col = tableVehiculo.getColumn("Seleccionar");
		int width = visible ? 80 : 0;
		col.setMinWidth(width);
		col.setMaxWidth(width);
		col.setPreferredWidth(width);
		tableVehiculo.getTableHeader().resizeAndRepaint();
	}

	public void hideSelectColumn() {
		setSelectColumnVisible(false);
	}

	public VehiculoDTO getSelected() {
		int r = tableVehiculo.getSelectedRow();
		if (r < 0)
			return null;
		int modelRow = tableVehiculo.convertRowIndexToModel(r);
		return ((VehiculoSearchTableModel) tableVehiculo.getModel()).getVehiculoAt(modelRow);
	}

	public VehiculoSearchTableModel getModel() {
		return (VehiculoSearchTableModel) tableVehiculo.getModel();
	}

	public JTable getTable() {
		return tableVehiculo;
	}

	/**
	 * Permite reasignar la SearchVehiculoAction, por ejemplo desde
	 * VehiculoSearchView una vez que el controlador ya está construido.
	 */
	public void setSearchAction(SearchVehiculoAction searchAction) {
		this.searchAction = searchAction;
	}
}
