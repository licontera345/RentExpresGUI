package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.pinguela.rentexpres.desktop.model.ReservationSearchTableModel;
import com.pinguela.rentexpres.desktop.renderer.ReservationActionsCellEditor;
import com.pinguela.rentexpres.desktop.renderer.ReservationActionsCellRenderer;
import com.pinguela.rentexpres.desktop.util.SelectionEditor;
import com.pinguela.rentexpres.desktop.util.SelectionRenderer;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.ReservationService;

public class ReservationTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTable table = new JTable();
	private final ReservationService service;
	private final Frame owner;
	private boolean selectVisible = false;

	public ReservationTablePanel(ReservationService svc, Frame owner, ReloadCallback reload) {
		this.service = svc;
		this.owner = owner;
                setLayout(new BorderLayout());
                table.setRowHeight(AppTheme.TABLE_ROW_HEIGHT);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	public void setModel(ReservationSearchTableModel m) {
		table.setModel(m);

		int[] widths = { 40, 40, 60, 100, 100, 80, 90, 100, 100, 90, 90, 90, 90, 150 };
		for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
		}

		table.getColumn("Acciones").setCellRenderer(new ReservationActionsCellRenderer());
		table.getColumn("Acciones").setCellEditor(new ReservationActionsCellEditor(owner, service));

		if (hasSelectColumn()) {
			table.getColumn("Seleccionar").setCellRenderer(new SelectionRenderer());
			table.getColumn("Seleccionar").setCellEditor(new SelectionEditor());
			TableColumn col = table.getColumn("Seleccionar");
			col.setMinWidth(selectVisible ? 30 : 0);
			col.setMaxWidth(selectVisible ? 30 : 0);
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
		TableColumn col = table.getColumn("Seleccionar");
		int width = selectVisible ? 80 : 0;
		col.setMinWidth(width);
		col.setMaxWidth(width);
		col.setPreferredWidth(width);
		table.getTableHeader().resizeAndRepaint();
	}

	public void hideSelectColumn() {
		if (!hasSelectColumn())
			return;
		selectVisible = false;
		TableColumn col = table.getColumn("Seleccionar");
		col.setMinWidth(0);
		col.setMaxWidth(0);
		col.setPreferredWidth(0);
		table.getTableHeader().resizeAndRepaint();
	}

	public ReservationDTO getSelected() {
		int r = table.getSelectedRow();
		return r < 0 ? null : ((ReservationSearchTableModel) table.getModel()).getReservationAt(r);
	}

	public ReservationSearchTableModel getModel() {
		return (ReservationSearchTableModel) table.getModel();
	}

	public JTable getTable() {
		return table;
	}

        // Interfaz funcional para recargar la tabla
        public interface ReloadCallback {
                void reload();
        }
}
