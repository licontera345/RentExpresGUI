package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.pinguela.rentexpres.desktop.model.AlquilerSearchTableModel;
import com.pinguela.rentexpres.desktop.renderer.AlquilerActionsCellEditor;
import com.pinguela.rentexpres.desktop.renderer.AlquilerActionsCellRenderer;
import com.pinguela.rentexpres.desktop.util.SelectionEditor;
import com.pinguela.rentexpres.desktop.util.SelectionRenderer;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.service.AlquilerService;

public class AlquilerTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTable table = new JTable();
	private final Frame owner;
	private final AlquilerService service;
	private ReloadCallback reloadCallback;
	private boolean selectVisible = false;

	public AlquilerTablePanel(Frame owner, AlquilerService service) {
		this.owner = owner;
		this.service = service;

		setLayout(new BorderLayout());
		table.setRowHeight(24);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	public void setModel(AlquilerSearchTableModel model) {
		table.setModel(model);

                int[] widths = { 40, 40, 60, 100, 100, 100, 100, 100, 100, 80, 80, 80, 90, 150 };
		for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
		}

		table.getColumn("Acciones").setCellRenderer(new AlquilerActionsCellRenderer());
		table.getColumn("Acciones").setCellEditor(new AlquilerActionsCellEditor(owner, service, this));

		if (hasSelectColumn()) {
			table.getColumn("Seleccionar").setCellRenderer(new SelectionRenderer());
			table.getColumn("Seleccionar").setCellEditor(new SelectionEditor());
			updateSelectColumnWidth();
		}

		DefaultTableCellRenderer gen = new DefaultTableCellRenderer();
		gen.setHorizontalAlignment(SwingConstants.LEFT);
		for (int c = 0; c < table.getColumnCount(); c++) {
			String name = table.getColumnName(c);
			if (!name.equals("Acciones") && !name.equals("Seleccionar")) {
				table.getColumnModel().getColumn(c).setCellRenderer(gen);
			}
		}
	}

	private boolean hasSelectColumn() {
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
		updateSelectColumnWidth();
	}

	public void hideSelectColumn() {
		if (!hasSelectColumn())
			return;
		selectVisible = false;
		updateSelectColumnWidth();
	}

	private void updateSelectColumnWidth() {
		TableColumn col = table.getColumn("Seleccionar");
		int w = selectVisible ? 80 : 0;
		col.setMinWidth(w);
		col.setMaxWidth(w);
		col.setPreferredWidth(w);
		table.getTableHeader().resizeAndRepaint();
	}

	public AlquilerDTO getSelected() {
		int r = table.getSelectedRow();
		return r < 0 ? null : ((AlquilerSearchTableModel) table.getModel()).getAlquilerAt(r);
	}

	/* ══════════ Callback para recargar ══════════ */
	public void setReloadCallback(ReloadCallback cb) {
		this.reloadCallback = cb;
	}

	public void reloadIfNeeded() {
		if (reloadCallback != null)
			reloadCallback.reload();
	}

	/* ══════════ Interface clásica ══════════ */
	public interface ReloadCallback {
		void reload();
	}
}
