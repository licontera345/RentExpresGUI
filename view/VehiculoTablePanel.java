package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.controller.SearchVehiculoAction;
import com.pinguela.rentexpres.desktop.model.VehiculoSearchTableModel;
import com.pinguela.rentexpres.desktop.renderer.VehiculoActionsCellEditor;
import com.pinguela.rentexpres.desktop.renderer.VehiculoActionsCellRenderer;
import com.pinguela.rentexpres.desktop.renderer.VehiculoTableCellRenderer;
import com.pinguela.rentexpres.desktop.util.SelectionEditor;
import com.pinguela.rentexpres.desktop.util.SelectionRenderer;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.VehiculoService;

public class VehiculoTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

        private final JTable tableVehiculo;
        private final VehiculoService vehiculoService;
        private boolean selectVisible = false;
	/** Mantiene la acción para recargar la tabla cuando se usan los editores */
        private SearchVehiculoAction searchAction;

        public VehiculoTablePanel(SearchVehiculoAction searchAction, VehiculoService vehiculoService) {
                super(new BorderLayout());
                this.searchAction = searchAction;
                this.vehiculoService = vehiculoService;

                tableVehiculo = new JTable();

                // Modelo inicial vacío
                tableVehiculo.setModel(new VehiculoSearchTableModel(Collections.emptyList()));

                // Ajustes básicos
                tableVehiculo.setRowHeight(AppTheme.TABLE_ROW_HEIGHT);
                tableVehiculo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tableVehiculo.getTableHeader().setReorderingAllowed(false);

                add(new JScrollPane(tableVehiculo), BorderLayout.CENTER);

                configureColumns();

                // Cuando alguien cambie el modelo, volver a configurar columnas
                tableVehiculo.addPropertyChangeListener("model", new java.beans.PropertyChangeListener() {
                        @Override
                        public void propertyChange(java.beans.PropertyChangeEvent evt) {
                                configureColumns();
                        }
                });
        }

        private void configureColumns() {
                if (tableVehiculo.getColumnCount() == 0)
                        return;

                // Asegurarnos de que, al cambiar el modelo, forzamos tamaño de columnas
                ajustarColumnasAcciones();

                int lastCol = tableVehiculo.getColumnModel().getColumnCount() - 1;
                tableVehiculo.getColumnModel().getColumn(lastCol).setCellRenderer(new VehiculoActionsCellRenderer());
                tableVehiculo.getColumnModel().getColumn(lastCol)
                                .setCellEditor(new VehiculoActionsCellEditor(tableVehiculo, vehiculoService, this.searchAction));

                if (hasSelectColumn()) {
                        tableVehiculo.getColumn("Seleccionar").setCellRenderer(new SelectionRenderer());
                        tableVehiculo.getColumn("Seleccionar").setCellEditor(new SelectionEditor());
                        setSelectColumnVisible(selectVisible);
                }

                for (int c = 0; c < tableVehiculo.getColumnCount(); c++) {
                        String name = tableVehiculo.getColumnName(c);
                        if (!"Acciones".equals(name) && !"Seleccionar".equals(name)) {
                                tableVehiculo.getColumnModel().getColumn(c).setCellRenderer(new VehiculoTableCellRenderer());
                        }
                }
        }

	/**
	 * Después de asignar un modelo nuevo (o en el constructor), llamamos a este
	 * método para forzar el ancho de la columna “Acciones”.
	 */
	private void ajustarColumnasAcciones() {
               com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                       @Override
                       public void execute() {
                               if (tableVehiculo.getColumnCount() == 0)
                                       return;
                               int last = tableVehiculo.getColumnCount() - 1;
                               tableVehiculo.getColumnModel().getColumn(last).setPreferredWidth(100);
                               tableVehiculo.getColumnModel().getColumn(last).setMinWidth(80);
                               tableVehiculo.getColumnModel().getColumn(last).setMaxWidth(120);
                       }
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
                javax.swing.table.TableColumn col = tableVehiculo.getColumn("Seleccionar");
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
                if (tableVehiculo.getColumnCount() > 0) {
                        int last = tableVehiculo.getColumnModel().getColumnCount() - 1;
                        javax.swing.table.TableCellEditor editor = tableVehiculo.getColumnModel().getColumn(last).getCellEditor();
                        if (editor instanceof com.pinguela.rentexpres.desktop.renderer.VehiculoActionsCellEditor) {
                                ((com.pinguela.rentexpres.desktop.renderer.VehiculoActionsCellEditor) editor).setSearchAction(searchAction);
                        }
                }
        }
}
