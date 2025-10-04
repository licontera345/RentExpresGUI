package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.pinguela.rentexpres.desktop.renderer.AbstractActionsCellEditor;

import com.pinguela.rentexpres.desktop.controller.SearchVehicleAction;
import com.pinguela.rentexpres.desktop.dialog.VehicleDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.VehicleEditDialog;
import com.pinguela.rentexpres.desktop.model.VehicleSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleService;

public class VehicleActionsCellEditor extends AbstractActionsCellEditor {
        private static final long serialVersionUID = 1L;

        private final Frame frame;
        private final VehicleService service;
        private SearchVehicleAction searchAction;

	private VehicleDTO vehicleActual;

        public VehicleActionsCellEditor(JTable table, VehicleService vehicleService, SearchVehicleAction searchAction) {
                super();
                this.frame = (Frame) SwingUtilities.getWindowAncestor(table);
                this.service = vehicleService;
                this.searchAction = searchAction;

		// Acción Ver
		btnView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (vehicleActual != null) {
					new VehicleDetailDialog(frame, vehicleActual).setVisible(true);
				}
				fireEditingStopped();
			}
		});

		// Acción Editar
		btnEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
                                if (vehicleActual != null) {
                                        try {
                                                List<VehicleStatusDTO> estados = searchAction != null ? searchAction.getEstadoService().findAll() : java.util.Collections.emptyList();
                                                List<VehicleCategoryDTO> categorys = searchAction != null ? searchAction.getCategoryService().findAll() : java.util.Collections.emptyList();

                                                VehicleEditDialog dlg = new VehicleEditDialog(frame, vehicleActual, categorys, estados, service);
						dlg.setVisible(true);

						if (dlg.isConfirmed()) {
							service.update(dlg.getVehicle(), null);
							if (searchAction != null) {
								searchAction.load();
							}
						}
					} catch (RentexpresException ex) {
						SwingUtils.showError(frame, "Error al editar vehicle: " + ex.getMessage());
					}
				}
				fireEditingStopped();
			}
		});

		// Acción Delete
		btnDel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (vehicleActual != null
						&& SwingUtils.showConfirm(frame, "Delete vehicle " + vehicleActual.getId() + "?",
								"Confirm deletion") == JOptionPane.YES_OPTION) {
					try {
						service.delete(vehicleActual.getId());
						if (searchAction != null) {
							searchAction.load();
						}
					} catch (RentexpresException ex) {
						SwingUtils.showError(frame, "No se pudo borrar: " + ex.getMessage());
					}
				}
				fireEditingStopped();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (table.getModel() instanceof VehicleSearchTableModel) {
			VehicleSearchTableModel model = (VehicleSearchTableModel) table.getModel();
			vehicleActual = model.getVehicleAt(table.convertRowIndexToModel(row));
		}
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}



        public SearchVehicleAction getSearchAction() {
                return searchAction;
        }

        public void setSearchAction(SearchVehicleAction searchAction) {
                this.searchAction = searchAction;
        }
}
