package com.pinguela.rentexpres.desktop.renderer;

import static com.pinguela.rentexpres.desktop.util.AppIcons.DELETE;
import static com.pinguela.rentexpres.desktop.util.AppIcons.EDIT;
import static com.pinguela.rentexpres.desktop.util.AppIcons.VIEW;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import com.pinguela.rentexpres.desktop.controller.SearchVehiculoAction;
import com.pinguela.rentexpres.desktop.dialog.VehiculoDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.VehiculoEditDialog;
import com.pinguela.rentexpres.desktop.model.VehiculoSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CategoriaVehiculoDTO;
import com.pinguela.rentexpres.model.EstadoVehiculoDTO;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.VehiculoService;

public class VehiculoActionsCellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;

	private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
	private final JButton btnVer = iconButton(VIEW, "Ver");
	private final JButton btnEdit = iconButton(EDIT, "Editar");
	private final JButton btnDel = iconButton(DELETE, "Borrar");

	private final Frame frame;
	private final VehiculoService service;
	private final SearchVehiculoAction searchAction;

	private VehiculoDTO vehiculoActual;

	public VehiculoActionsCellEditor(JTable table, VehiculoService vehiculoService, SearchVehiculoAction searchAction) {
		this.frame = (Frame) SwingUtilities.getWindowAncestor(table);
		this.service = vehiculoService;
		this.searchAction = searchAction;

		panel.add(btnVer);
		panel.add(btnEdit);
		panel.add(btnDel);

		// Acción Ver
		btnVer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (vehiculoActual != null) {
					new VehiculoDetailDialog(frame, vehiculoActual).setVisible(true);
				}
				fireEditingStopped();
			}
		});

		// Acción Editar
		btnEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (vehiculoActual != null) {
					try {
						List<EstadoVehiculoDTO> estados = searchAction.getEstadoService().findAll();
						List<CategoriaVehiculoDTO> categorias = searchAction.getCategoriaService().findAll();

						VehiculoEditDialog dlg = new VehiculoEditDialog(frame, vehiculoActual, categorias, estados);
						dlg.setVisible(true);

						if (dlg.isConfirmed()) {
							service.update(dlg.getVehiculo(), null);
							if (searchAction != null) {
								searchAction.load();
							}
						}
					} catch (RentexpresException ex) {
						SwingUtils.showError(frame, "Error al editar vehículo: " + ex.getMessage());
					}
				}
				fireEditingStopped();
			}
		});

		// Acción Eliminar
		btnDel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (vehiculoActual != null
						&& SwingUtils.showConfirm(frame, "¿Eliminar vehículo " + vehiculoActual.getId() + "?",
								"Confirmar borrado") == JOptionPane.YES_OPTION) {
					try {
						service.delete(vehiculoActual.getId());
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
		if (table.getModel() instanceof VehiculoSearchTableModel) {
			VehiculoSearchTableModel model = (VehiculoSearchTableModel) table.getModel();
			vehiculoActual = model.getVehiculoAt(table.convertRowIndexToModel(row));
		}
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject e) {
		return true;
	}

	private static JButton iconButton(ImageIcon ico, String tip) {
		JButton b = new JButton(ico);
		b.setToolTipText(tip);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		return b;
	}

	public SearchVehiculoAction getSearchAction() {
		return searchAction;
	}
}
