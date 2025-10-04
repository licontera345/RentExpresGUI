package com.pinguela.rentexpres.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pinguela.rentexpres.model.VehicleDTO;

import net.miginfocom.swing.MigLayout;

/**
 * Diálogo de sólo lectura para mostrar los datos de un Vehicle existente.
 * Sigue el mismo patrón que ReservationDetailDialog y RentalDetailDialog.
 */
public class VehicleDetailDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public VehicleDetailDialog(Frame owner, VehicleDTO dto) {
		super(owner, "Detalle Vehicle", true);
		initComponents(dto);
		pack();
		setLocationRelativeTo(owner);
	}

        private void initComponents(VehicleDTO v) {
                JPanel panel = new JPanel(new MigLayout("wrap 2", "[right]10[grow]", "[]10[]10[]10[]10[]10[]10[]10[]20[]"));

                panel.add(new JLabel("Make:"));
                panel.add(new JLabel(v.getMake()));

                panel.add(new JLabel("Model:"));
                panel.add(new JLabel(v.getModel()));

                panel.add(new JLabel("Año fabricación:"));
                panel.add(new JLabel(String.valueOf(v.getManufactureYear())));

                panel.add(new JLabel("Precio/Día:"));
                panel.add(new JLabel(String.valueOf(v.getDailyPrice())));

                panel.add(new JLabel("LicensePlate:"));
                panel.add(new JLabel(v.getLicensePlate()));

                panel.add(new JLabel("Nº Bastidor:"));
                panel.add(new JLabel(v.getVin()));

                panel.add(new JLabel("Kilometraje Actual:"));
                panel.add(new JLabel(String.valueOf(v.getCurrentMileage())));

                panel.add(new JLabel("Estado:"));
                panel.add(new JLabel(v.getVehicleStatusName()));

                panel.add(new JLabel("Categoría:"));
                panel.add(new JLabel(v.getCategoryName()));

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
