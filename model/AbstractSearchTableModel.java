package com.pinguela.rentexpres.desktop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

/**
 * Modelo base para todas las tablas de búsqueda. Añade: • Columna “Seleccionar”
 * (check-box) • Columna “Acciones” (renderizada con botones)
 */
public abstract class AbstractSearchTableModel<T> extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private final String[] columnNames;
	private final Class<?>[] columnClasses;

	protected final List<T> data = new ArrayList<>();
	private final Set<Integer> selectedIds = new HashSet<>();

	protected AbstractSearchTableModel(String[] columnNames, Class<?>[] columnClasses) {
		this.columnNames = columnNames;
		this.columnClasses = columnClasses;
	}

	/* ----------------------- API pública ----------------------- */

	public void setData(List<T> nuevos) {
		data.clear();
		if (nuevos != null)
			data.addAll(nuevos);
		clearSelection();
		fireTableDataChanged();
	}

	public T getItem(int row) {
		return data.get(row);
	}

	public List<Integer> getSelectedIds() {
		return new ArrayList<>(selectedIds);
	}

	public List<T> getSelectedItems() {
		List<T> list = new ArrayList<>();
		for (T el : data)
			if (selectedIds.contains(getIdOf(el)))
				list.add(el);
		return list;
	}

	public void clearSelection() {
		selectedIds.clear();
		if (getRowCount() > 0)
			fireTableRowsUpdated(0, getRowCount() - 1);
	}


	protected abstract Integer getIdOf(T element);

	protected abstract Object getFieldAt(T element, int dataCol);


	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return 1 + columnNames.length;
	}

	@Override
	public String getColumnName(int col) {
		return col == 0 ? "Seleccionar" : columnNames[col - 1];
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return col == 0 ? Boolean.class : columnClasses[col - 1];
	}

	@Override
	public Object getValueAt(int row, int col) {
		T element = data.get(row);
		if (col == 0) {
			return selectedIds.contains(getIdOf(element));
		}
		return getFieldAt(element, col - 1);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		String name = getColumnName(col);
		return "Seleccionar".equals(name) || "Acciones".equals(name);
	}

	@Override
	public void setValueAt(Object aValue, int row, int col) {
		if ("Seleccionar".equals(getColumnName(col))) {
			Integer id = getIdOf(data.get(row));
			if (Boolean.TRUE.equals(aValue))
				selectedIds.add(id);
			else
				selectedIds.remove(id);
			fireTableRowsUpdated(row, row);
		}
	}
}
