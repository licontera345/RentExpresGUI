package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Component;
import java.awt.Frame;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.model.ProvinceDTO;

public class CustomerEditDialog extends CustomerCreateDialog {

	private static final long serialVersionUID = 1L;

	private final CustomerDTO original;
	public CustomerEditDialog(Frame owner, CustomerDTO dto) {
		super(owner);
		this.original = Objects.requireNonNull(dto);
		setTitle("Editar Customer");
		btnGuardar.setText("Guardar");
		precargarDatos();
	}

	private void precargarDatos() {

		txtName.setText(original.getName());
		txtAp1.setText(original.getLastName());
		txtAp2.setText(original.getSecondLastName());

		if (original.getBirthDate() != null) {
			try {
				dcNac.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(original.getBirthDate()));
			} catch (ParseException ignored) {
				 }
		}

		txtEmail.setText(original.getEmail());
		txtPhone.setText(original.getPhone());
		txtStreet.setText(original.getStreet());
		txtStreetNumber.setText(original.getStreetNumber());


		String nameProv = original.getProvinceName();
		String nameLoc = original.getCityName();

	
		if (nameProv != null && !nameProv.trim().isEmpty()) {
			for (int i = 0; i < cmbProvince.getItemCount(); i++) {
				ProvinceDTO p = cmbProvince.getItemAt(i);
				if (nameProv.equalsIgnoreCase(p.getName())) {
					cmbProvince.setSelectedIndex(i);
					break;
				}
			}
		}

		cargarCities();

		if (nameLoc != null && !nameLoc.trim().isEmpty()) {
			for (int i = 0; i < cmbCity.getItemCount(); i++) {
				CityDTO l = cmbCity.getItemAt(i);
				if (nameLoc.equalsIgnoreCase(l.getName())) {
					cmbCity.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	@Override
	protected CustomerDTO buildFromForm() {
		CustomerDTO dto = super.buildFromForm();
		dto.setId(original.getId());
		return dto;
	}


	protected void cargarCities() {
		cmbCity.removeAllItems();
		ProvinceDTO province = (ProvinceDTO) cmbProvince.getSelectedItem();
		if (province == null)
			return;

		try {
			List<CityDTO> locs = citySvc.findByProvinceId(province.getId());
			for (CityDTO l : locs)
				cmbCity.addItem(l);
			cmbCity.setRenderer(new ProvLocRenderer<>());
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error cargando cities: " + ex.getMessage());
		}
	}

	private static class ProvLocRenderer<T> extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof ProvinceDTO p) {
				setText(p.getName());
			} else if (value instanceof CityDTO l) {
				setText(l.getName());
			}
			return this;
		}
	}
}
