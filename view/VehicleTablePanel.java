package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.controller.SearchVehicleAction;
import com.pinguela.rentexpres.desktop.model.VehicleSearchTableModel;
import com.pinguela.rentexpres.desktop.renderer.VehicleActionsCellEditor;
import com.pinguela.rentexpres.desktop.renderer.VehicleActionsCellRenderer;
import com.pinguela.rentexpres.desktop.renderer.VehicleTableCellRenderer;
import com.pinguela.rentexpres.desktop.util.SelectionEditor;
import com.pinguela.rentexpres.desktop.util.SelectionRenderer;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleService;

public class VehicleTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

        private final JTable tableVehicle;
        private final VehicleService vehicleService;
        private boolean selectVisible = false;
	/** Mantiene la acción para recargar la tabla cuando se usan los editores */
        private SearchVehicleAction searchAction;

        public VehicleTablePanel(SearchVehicleAction searchAction, VehicleService vehicleService) {
                super(new BorderLayout());
                this.searchAction = searchAction;
                this.vehicleService = vehicleService;

                tableVehicle = new JTable();

                // Model inicial vacío
                tableVehicle.setModel(new VehicleSearchTableModel(Collections.emptyList()));

                // Ajustes básicos
                tableVehicle.setRowHeight(AppTheme.TABLE_ROW_HEIGHT);
                tableVehicle.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tableVehicle.getTableHeader().setReorderingAllowed(false);

                add(new JScrollPane(tableVehicle), BorderLayout.CENTER);

                configureColumns();

                // Cuando alguien cambie el model, volver a configurar columnas
                tableVehicle.addPropertyChangeListener("model", new java.beans.PropertyChangeListener() {
                        @Override
                        public void propertyChange(java.beans.PropertyChangeEvent evt) {
                                configureColumns();
                        }
                });
        }

        private void configureColumns() {
                if (tableVehicle.getColumnCount() == 0)
                        return;

                // Asegurarnos de que, al cambiar el model, forzamos tamaño de columnas
                ajustarColumnasAcciones();

                int lastCol = tableVehicle.getColumnModel().getColumnCount() - 1;
                tableVehicle.getColumnModel().getColumn(lastCol).setCellRenderer(new VehicleActionsCellRenderer());
                tableVehicle.getColumnModel().getColumn(lastCol)
                                .setCellEditor(new VehicleActionsCellEditor(tableVehicle, vehicleService, this.searchAction));

                if (hasSelectColumn()) {
                        tableVehicle.getColumn("Seleccionar").setCellRenderer(new SelectionRenderer());
                        tableVehicle.getColumn("Seleccionar").setCellEditor(new SelectionEditor());
                        setSelectColumnVisible(selectVisible);
                }

                for (int c = 0; c < tableVehicle.getColumnCount(); c++) {
                        String name = tableVehicle.getColumnName(c);
                        if (!"Acciones".equals(name) && !"Seleccionar".equals(name)) {
                                tableVehicle.getColumnModel().getColumn(c).setCellRenderer(new VehicleTableCellRenderer());
                        }
                }
        }

	/**
	 * Después de asignar un model nuevo (o en el constructor), llamamos a este
	 * método para forzar el ancho de la columna “Acciones”.
	 */
	private void ajustarColumnasAcciones() {
               com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new ActionCallback() {
                       @Override
                       public void execute() {
                               if (tableVehicle.getColumnCount() == 0)
                                       return;
                               int last = tableVehicle.getColumnCount() - 1;
                               tableVehicle.getColumnModel().getColumn(last).setPreferredWidth(100);
                               tableVehicle.getColumnModel().getColumn(last).setMinWidth(80);
                               tableVehicle.getColumnModel().getColumn(last).setMaxWidth(120);
                       }
               });
       }

	private boolean hasSelectColumn() {
		try {
			tableVehicle.getColumn("Seleccionar");
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
                javax.swing.table.TableColumn col = tableVehicle.getColumn("Seleccionar");
		int width = visible ? 80 : 0;
		col.setMinWidth(width);
		col.setMaxWidth(width);
		col.setPreferredWidth(width);
		tableVehicle.getTableHeader().resizeAndRepaint();
	}

	public void hideSelectColumn() {
		setSelectColumnVisible(false);
	}

	public VehicleDTO getSelected() {
		int r = tableVehicle.getSelectedRow();
		if (r < 0)
			return null;
		int modelRow = tableVehicle.convertRowIndexToModel(r);
		return ((VehicleSearchTableModel) tableVehicle.getModel()).getVehicleAt(modelRow);
	}

	public VehicleSearchTableModel getModel() {
		return (VehicleSearchTableModel) tableVehicle.getModel();
	}

	public JTable getTable() {
		return tableVehicle;
	}

	/**
	 * Permite reasignar la SearchVehicleAction, por ejemplo desde
	 * VehicleSearchView una vez que el controlador ya está construido.
	 */
        public void setSearchAction(SearchVehicleAction searchAction) {
                this.searchAction = searchAction;
                if (tableVehicle.getColumnCount() > 0) {
                        int last = tableVehicle.getColumnModel().getColumnCount() - 1;
                        javax.swing.table.TableCellEditor editor = tableVehicle.getColumnModel().getColumn(last).getCellEditor();
                        if (editor instanceof com.pinguela.rentexpres.desktop.renderer.VehicleActionsCellEditor) {
                                ((com.pinguela.rentexpres.desktop.renderer.VehicleActionsCellEditor) editor).setSearchAction(searchAction);
                        }
                }
        }
}
