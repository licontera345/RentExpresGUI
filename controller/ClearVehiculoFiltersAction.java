package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.VehiculoCriteria;

public class ClearVehiculoFiltersAction implements ActionListener {

	private final JTextField txtMarca;
	private final JTextField txtModelo;
	private final JTextField txtAnioDesde;
	private final JTextField txtAnioHasta;
	private final JTextField txtPrecioMax;
	private final JComboBox<?> cbEstado;
	private final JComboBox<?> cbCategoria;
	private final SearchVehiculoAction searchAction;

	public ClearVehiculoFiltersAction(JTextField txtMarca, JTextField txtModelo, JTextField txtAnioDesde,
			JTextField txtAnioHasta, JTextField txtPrecioMax, JComboBox<?> cbEstado, JComboBox<?> cbCategoria,
			SearchVehiculoAction searchAction) {
		this.txtMarca = txtMarca;
		this.txtModelo = txtModelo;
		this.txtAnioDesde = txtAnioDesde;
		this.txtAnioHasta = txtAnioHasta;
		this.txtPrecioMax = txtPrecioMax;
		this.cbEstado = cbEstado;
		this.cbCategoria = cbCategoria;
		this.searchAction = searchAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		txtMarca.setText("");
		txtModelo.setText("");
		txtAnioDesde.setText("");
		txtAnioHasta.setText("");
		txtPrecioMax.setText("");

		if (cbEstado.getItemCount() > 0) {
			cbEstado.setSelectedIndex(0);
		}
		if (cbCategoria.getItemCount() > 0) {
			cbCategoria.setSelectedIndex(0);
		}

		try {
			VehiculoCriteria criteria = new VehiculoCriteria();
			searchAction.loadByCriteria(criteria);
		} catch (Exception ex) {
			SwingUtils.showError(null, "Error al limpiar filtros: " + ex.getMessage());
		}
	}
}
