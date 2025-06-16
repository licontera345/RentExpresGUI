package com.pinguela.rentexpres.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pinguela.rentexpres.model.VehiculoDTO;

/**
 * Diálogo de sólo lectura para mostrar los datos de un Vehículo existente.
 * Sigue el mismo patrón que ReservaDetailDialog y AlquilerDetailDialog.
 */
public class VehiculoDetailDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public VehiculoDetailDialog(Frame owner, VehiculoDTO dto) {
		super(owner, "Detalle Vehículo", true);
		initComponents(dto);
		pack();
		setLocationRelativeTo(owner);
	}

	private void initComponents(VehiculoDTO v) {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JLabel("Marca:"), gbc);
		gbc.gridx = 1;
		panel.add(new JLabel(v.getMarca()), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Modelo:"), gbc);
		gbc.gridx = 1;
		panel.add(new JLabel(v.getModelo()), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Año fabricación:"), gbc);
		gbc.gridx = 1;
		panel.add(new JLabel(String.valueOf(v.getAnioFabricacion())), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Precio/Día:"), gbc);
		gbc.gridx = 1;
		panel.add(new JLabel(String.valueOf(v.getPrecioDia())), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Placa:"), gbc);
		gbc.gridx = 1;
		panel.add(new JLabel(v.getPlaca()), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Nº Bastidor:"), gbc);
		gbc.gridx = 1;
		panel.add(new JLabel(v.getNumeroBastidor()), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Kilometraje Actual:"), gbc);
		gbc.gridx = 1;
		panel.add(new JLabel(String.valueOf(v.getKilometrajeActual())), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Estado:"), gbc);
		gbc.gridx = 1;
		panel.add(new JLabel(v.getNombreEstadoVehiculo()), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Categoría:"), gbc);
		gbc.gridx = 1;
		panel.add(new JLabel(v.getNombreCategoria()), gbc);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(btnCerrar, gbc);

		getContentPane().add(panel, BorderLayout.CENTER);
	}
}
