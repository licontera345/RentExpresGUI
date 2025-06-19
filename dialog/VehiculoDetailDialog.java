package com.pinguela.rentexpres.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pinguela.rentexpres.model.VehiculoDTO;

import net.miginfocom.swing.MigLayout;

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
                JPanel panel = new JPanel(new MigLayout("wrap 2", "[right]10[grow]", "[]10[]10[]10[]10[]10[]10[]10[]20[]"));

                panel.add(new JLabel("Marca:"));
                panel.add(new JLabel(v.getMarca()));

                panel.add(new JLabel("Modelo:"));
                panel.add(new JLabel(v.getModelo()));

                panel.add(new JLabel("Año fabricación:"));
                panel.add(new JLabel(String.valueOf(v.getAnioFabricacion())));

                panel.add(new JLabel("Precio/Día:"));
                panel.add(new JLabel(String.valueOf(v.getPrecioDia())));

                panel.add(new JLabel("Placa:"));
                panel.add(new JLabel(v.getPlaca()));

                panel.add(new JLabel("Nº Bastidor:"));
                panel.add(new JLabel(v.getNumeroBastidor()));

                panel.add(new JLabel("Kilometraje Actual:"));
                panel.add(new JLabel(String.valueOf(v.getKilometrajeActual())));

                panel.add(new JLabel("Estado:"));
                panel.add(new JLabel(v.getNombreEstadoVehiculo()));

                panel.add(new JLabel("Categoría:"));
                panel.add(new JLabel(v.getNombreCategoria()));

                JButton btnCerrar = new JButton("Cerrar");
                btnCerrar.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                dispose();
                        }
                });
                panel.add(btnCerrar, "span, center, gaptop 10");

                getContentPane().add(panel, BorderLayout.CENTER);
        }
}
