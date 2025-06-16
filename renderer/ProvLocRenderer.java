package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.pinguela.rentexpres.model.LocalidadDTO;
import com.pinguela.rentexpres.model.ProvinciaDTO;

/** Muestra sólo el nombre (ProvinciaDTO / LocalidadDTO) en un JComboBox */
public class ProvLocRenderer<E> extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		if (value instanceof ProvinciaDTO p)
			setText(p.getNombre());
		if (value instanceof LocalidadDTO l)
			setText(l.getNombre());

		return this;
	}
}
