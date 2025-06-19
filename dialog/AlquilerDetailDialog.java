package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.model.EstadoAlquilerDTO;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.impl.ClienteServiceImpl;
import com.pinguela.rentexpres.service.impl.EstadoAlquilerServiceImpl;
import com.pinguela.rentexpres.service.impl.VehiculoServiceImpl;

import net.miginfocom.swing.MigLayout;

public class AlquilerDetailDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd");

	public AlquilerDetailDialog(Frame owner, AlquilerDTO a) {
		super(owner, "Detalle del Alquiler", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                JPanel content = new JPanel(
                                new MigLayout("wrap 4",
                                                "[right]10[grow,fill]20[right]10[grow,fill]",
                                                "[]10[]10[]10[]10[]10[]10[]10[]10[]10[]10[]20[]"));
		content.setBorder(new EmptyBorder(15, 20, 15, 20));
		ClienteDTO cliente;
		EstadoAlquilerDTO estado;
		VehiculoDTO vehiculo;

		try {
			cliente = new ClienteServiceImpl().findById(a.getIdCliente());
		} catch (Exception e) {
			cliente = new ClienteDTO();
			cliente.setNombre("Desconocido");
			cliente.setApellido1("");
		}
		try {
			estado = new EstadoAlquilerServiceImpl().findById(a.getIdEstadoAlquiler());
		} catch (Exception e) {
			estado = new EstadoAlquilerDTO();
			estado.setNombreEstado("Desconocido");
		}
		try {
			vehiculo = new VehiculoServiceImpl().findById(a.getIdVehiculo());
		} catch (Exception e) {
			vehiculo = new VehiculoDTO();
			vehiculo.setPlaca("Desconocida");
			vehiculo.setMarca("-");
			vehiculo.setModelo("-");
		}

		content.add(boldLabel("ID Alquiler:"));
		content.add(new JLabel(String.valueOf(a.getId())));

		content.add(boldLabel("ID Reserva:"));
		content.add(new JLabel(String.valueOf(a.getIdReserva())));

		content.add(boldLabel("Vehículo:"));
		content.add(new JLabel(vehiculo.getPlaca()));

		content.add(boldLabel("Marca:"));
		content.add(new JLabel(vehiculo.getMarca()));

		content.add(boldLabel("Modelo:"));
		content.add(new JLabel(vehiculo.getModelo()));

		content.add(boldLabel("Cliente:"));
		content.add(new JLabel(cliente.getNombre() + " " + cliente.getApellido1()));

		content.add(boldLabel("Fecha Inicio:"));
		content.add(new JLabel(FMT.format(java.sql.Date.valueOf(a.getFechaInicioEfectivo()))));

		content.add(boldLabel("Fecha Fin:"));
		content.add(new JLabel(FMT.format(java.sql.Date.valueOf(a.getFechaFinEfectivo()))));

		content.add(boldLabel("KM Inicio:"));
		content.add(new JLabel(String.valueOf(a.getKmInicial())));

		content.add(boldLabel("KM Fin:"));
		content.add(new JLabel(String.valueOf(a.getKmFinal())));

		content.add(boldLabel("Estado:"));
		content.add(new JLabel(estado.getNombreEstado()));

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		content.add(btnCerrar, "span, center");

		add(content);
		pack();
		setResizable(false);
		setLocationRelativeTo(owner);
	}

	private JLabel boldLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		return label;
	}
}
