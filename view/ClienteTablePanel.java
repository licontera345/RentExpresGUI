package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.pinguela.rentexpres.desktop.model.ClienteSearchTableModel;
import com.pinguela.rentexpres.desktop.renderer.ClienteActionsCellEditor;
import com.pinguela.rentexpres.desktop.renderer.ClienteActionsCellRenderer;
import com.pinguela.rentexpres.desktop.renderer.ClienteTableCellRenderer;
import com.pinguela.rentexpres.desktop.util.SelectionRenderer;
import com.pinguela.rentexpres.desktop.util.SelectionEditor;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;

public class ClienteTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTable table = new JTable();
	private final ClienteService service;
	private final Frame owner;
	private final Runnable reload;

	private boolean selectVisible = false;

	public ClienteTablePanel(ClienteService svc, Frame owner, Runnable reload) {
		this.service = svc;
		this.owner = owner;
		this.reload = reload;
		setLayout(new BorderLayout());
		table.setRowHeight(24);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	public void setModel(ClienteSearchTableModel m) {
		table.setModel(m);

		// Ajustar anchos de columna (ID, Nombre, etc.)
		int[] widths = { 40, 100, 100, 100, 100, 120, 120, 60, 100, 100, 150, 150 };
		for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
		}

		// Columna “Acciones”
		table.getColumn("Acciones").setCellRenderer(new ClienteActionsCellRenderer());
		table.getColumn("Acciones")
				.setCellEditor(new ClienteActionsCellEditor(owner, service, reload, this::getSelected));

		// Columna “Seleccionar”
		if (hasSelectColumn()) {
			table.getColumn("Seleccionar").setCellRenderer(new SelectionRenderer());
			table.getColumn("Seleccionar").setCellEditor(new SelectionEditor());
			table.getColumn("Seleccionar").setMinWidth(selectVisible ? 30 : 0);
			table.getColumn("Seleccionar").setMaxWidth(selectVisible ? 30 : 0);
		}

		// Renderer genérico para celdas (izquierda/centro)
		DefaultTableCellRenderer gen = new DefaultTableCellRenderer();
		gen.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

		for (int c = 0; c < table.getColumnCount(); c++) {
			String name = table.getColumnName(c);
			if (!"Acciones".equals(name) && !"Seleccionar".equals(name)) {
				table.getColumnModel().getColumn(c).setCellRenderer(new ClienteTableCellRenderer());
			}
		}
	}

	public boolean hasSelectColumn() {
		try {
			table.getColumn("Seleccionar");
			return true;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}

	public void toggleSelectColumn() {
		if (!hasSelectColumn())
			return;
		selectVisible = !selectVisible;
		var col = table.getColumn("Seleccionar");
		col.setMinWidth(selectVisible ? 80 : 0);
		col.setMaxWidth(selectVisible ? 80 : 0);
		col.setPreferredWidth(selectVisible ? 80 : 0);
		table.getTableHeader().resizeAndRepaint();
	}

	public void hideSelectColumn() {
		if (!hasSelectColumn())
			return;
		selectVisible = false;
		var col = table.getColumn("Seleccionar");
		col.setMinWidth(0);
		col.setMaxWidth(0);
		col.setPreferredWidth(0);
		table.getTableHeader().resizeAndRepaint();
	}

	public ClienteDTO getSelected() {
		int r = table.getSelectedRow();
		if (r < 0)
			return null;
		return ((ClienteSearchTableModel) table.getModel()).getClienteAt(table.convertRowIndexToModel(r));
	}

	public ClienteSearchTableModel getModel() {
		return (ClienteSearchTableModel) table.getModel();
	}
	public JTable getTable() {
	    return table;
	}

	
}
