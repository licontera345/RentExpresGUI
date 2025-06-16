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
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.model.LocalidadDTO;
import com.pinguela.rentexpres.model.ProvinciaDTO;

public class ClienteEditDialog extends ClienteCreateDialog {

	private static final long serialVersionUID = 1L;

	private final ClienteDTO original;
	public ClienteEditDialog(Frame owner, ClienteDTO dto) {
		super(owner);
		this.original = Objects.requireNonNull(dto);
		setTitle("Editar Cliente");
		btnGuardar.setText("Guardar");
		precargarDatos();
	}

	private void precargarDatos() {

		txtNombre.setText(original.getNombre());
		txtAp1.setText(original.getApellido1());
		txtAp2.setText(original.getApellido2());

		if (original.getFechaNacimiento() != null) {
			try {
				dcNac.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(original.getFechaNacimiento()));
			} catch (ParseException ignored) {
				 }
		}

		txtEmail.setText(original.getEmail());
		txtTelefono.setText(original.getTelefono());
		txtCalle.setText(original.getCalle());
		txtNumero.setText(original.getNumero());


		String nombreProv = original.getNombreProvincia();
		String nombreLoc = original.getNombreLocalidad();

	
		if (nombreProv != null && !nombreProv.trim().isEmpty()) {
			for (int i = 0; i < cmbProvincia.getItemCount(); i++) {
				ProvinciaDTO p = cmbProvincia.getItemAt(i);
				if (nombreProv.equalsIgnoreCase(p.getNombre())) {
					cmbProvincia.setSelectedIndex(i);
					break;
				}
			}
		}

		cargarLocalidades();

		if (nombreLoc != null && !nombreLoc.trim().isEmpty()) {
			for (int i = 0; i < cmbLocalidad.getItemCount(); i++) {
				LocalidadDTO l = cmbLocalidad.getItemAt(i);
				if (nombreLoc.equalsIgnoreCase(l.getNombre())) {
					cmbLocalidad.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	@Override
	protected ClienteDTO buildFromForm() {
		ClienteDTO dto = super.buildFromForm();
		dto.setId(original.getId());
		return dto;
	}


	protected void cargarLocalidades() {
		cmbLocalidad.removeAllItems();
		ProvinciaDTO provincia = (ProvinciaDTO) cmbProvincia.getSelectedItem();
		if (provincia == null)
			return;

		try {
			List<LocalidadDTO> locs = localidadSvc.findByProvinciaId(provincia.getId());
			for (LocalidadDTO l : locs)
				cmbLocalidad.addItem(l);
			cmbLocalidad.setRenderer(new ProvLocRenderer<>());
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error cargando localidades: " + ex.getMessage());
		}
	}

	private static class ProvLocRenderer<T> extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof ProvinciaDTO p) {
				setText(p.getNombre());
			} else if (value instanceof LocalidadDTO l) {
				setText(l.getNombre());
			}
			return this;
		}
	}
}
