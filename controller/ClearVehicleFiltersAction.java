package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.VehicleCriteria;

public class ClearVehicleFiltersAction implements ActionListener {

	private final JTextField txtMake;
	private final JTextField txtModel;
	private final JTextField txtAnioDesde;
	private final JTextField txtAnioHasta;
	private final JTextField txtPrecioMax;
	private final JComboBox<?> cbEstado;
	private final JComboBox<?> cbCategory;
	private final SearchVehicleAction searchAction;

	public ClearVehicleFiltersAction(JTextField txtMake, JTextField txtModel, JTextField txtAnioDesde,
			JTextField txtAnioHasta, JTextField txtPrecioMax, JComboBox<?> cbEstado, JComboBox<?> cbCategory,
			SearchVehicleAction searchAction) {
		this.txtMake = txtMake;
		this.txtModel = txtModel;
		this.txtAnioDesde = txtAnioDesde;
		this.txtAnioHasta = txtAnioHasta;
		this.txtPrecioMax = txtPrecioMax;
		this.cbEstado = cbEstado;
		this.cbCategory = cbCategory;
		this.searchAction = searchAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		txtMake.setText("");
		txtModel.setText("");
		txtAnioDesde.setText("");
		txtAnioHasta.setText("");
		txtPrecioMax.setText("");

		if (cbEstado.getItemCount() > 0) {
			cbEstado.setSelectedIndex(0);
		}
		if (cbCategory.getItemCount() > 0) {
			cbCategory.setSelectedIndex(0);
		}

		try {
			VehicleCriteria criteria = new VehicleCriteria();
			searchAction.loadByCriteria(criteria);
		} catch (Exception ex) {
			SwingUtils.showError(null, "Error al limpiar filtros: " + ex.getMessage());
		}
	}
}
