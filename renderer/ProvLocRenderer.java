package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.model.ProvinceDTO;

/** Muestra sólo el name (ProvinceDTO / CityDTO) en un JComboBox */
public class ProvLocRenderer<E> extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		if (value instanceof ProvinceDTO p)
			setText(p.getName());
		if (value instanceof CityDTO l)
			setText(l.getName());

		return this;
	}
}
