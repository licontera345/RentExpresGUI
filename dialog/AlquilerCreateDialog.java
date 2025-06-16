package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.pinguela.rentexpres.desktop.util.SessionManager;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.model.EstadoAlquilerDTO;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.EstadoAlquilerService;
import com.pinguela.rentexpres.service.impl.AlquilerServiceImpl;
import com.pinguela.rentexpres.service.impl.EstadoAlquilerServiceImpl;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

public class AlquilerCreateDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private final JSpinner spnIdReserva = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JDateChooser dcInicio = new JDateChooser();
	private final JDateChooser dcFin = new JDateChooser();
	private final JTextField txtKmInicio = new JTextField();
	private final JTextField txtKmFin = new JTextField();
	private final JTextField txtCosteTotal = new JTextField();
	private final JComboBox<EstadoAlquilerDTO> cmbEstado = new JComboBox<>();
	private final JButton btnCrear = new JButton("Crear");
	private final JButton btnCancelar = new JButton("Cancelar");
	private final JButton btnNuevaReserva = new JButton("Nueva Reserva");

	private final EstadoAlquilerService estadoService = new EstadoAlquilerServiceImpl();
	private boolean confirmed = false;

	public AlquilerCreateDialog(Frame owner) {
		super(owner, "Nuevo Alquiler", true);
		initComponents();
		loadEstados();
	}

	private void initComponents() {
		setLayout(new MigLayout("wrap 2", "[right]10[grow,fill]", "[][][][][][][][]"));
		dcInicio.setDateFormatString("yyyy-MM-dd");
		dcFin.setDateFormatString("yyyy-MM-dd");

		add(new JLabel("ID Reserva:"));
		add(spnIdReserva);
		add(btnNuevaReserva, "span, align right");
		add(new JLabel("Fecha Inicio:"));
		add(dcInicio);
		add(new JLabel("Fecha Fin:"));
		add(dcFin);
		add(new JLabel("KM Inicio:"));
		add(txtKmInicio);
		add(new JLabel("KM Fin:"));
		add(txtKmFin);
		add(new JLabel("Coste Total:"));
		add(txtCosteTotal);
		add(new JLabel("Estado:"));
		add(cmbEstado);
		add(btnCrear, "split 2");
		add(btnCancelar);

		// --- listeners ---
		btnCrear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onCrear();
			}
		});
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNuevaReserva.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirNuevaReserva();
			}
		});
		pack();
		setLocationRelativeTo(getOwner());
	}

	private void loadEstados() {
		try {
			List<EstadoAlquilerDTO> list = estadoService.findAll();
			DefaultComboBoxModel<EstadoAlquilerDTO> model = new DefaultComboBoxModel<>();
			for (EstadoAlquilerDTO e : list)
				model.addElement(e);
			cmbEstado.setModel(model);
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error cargando estados: " + ex.getMessage());
		}
	}

	private boolean validar() {
		if ((Integer) spnIdReserva.getValue() <= 0) {
			JOptionPane.showMessageDialog(this, "ID Reserva debe ser > 0", "Aviso", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (dcInicio.getDate() == null || dcFin.getDate() == null || !dcFin.getDate().after(dcInicio.getDate())) {
			JOptionPane.showMessageDialog(this, "Fechas inválidas", "Aviso", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		try {
			if (!txtKmInicio.getText().trim().isEmpty())
				Integer.parseInt(txtKmInicio.getText().trim());
			if (!txtKmFin.getText().isEmpty())
				Integer.parseInt(txtKmFin.getText().trim());
			if (!txtCosteTotal.getText().isEmpty())
				Integer.parseInt(txtCosteTotal.getText().trim());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "KM y Coste deben ser enteros.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (cmbEstado.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(this, "Selecciona un estado.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private void onCrear() {
		if (!validar())
			return;

		int idReserva = (Integer) spnIdReserva.getValue();
		try {
			boolean yaExiste = new AlquilerServiceImpl().existsByReserva(idReserva);
			if (yaExiste) {
				JOptionPane.showMessageDialog(this, "Esta reserva ya tiene un alquiler asignado.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (Exception e) {
			SwingUtils.showError(this, "Error verificando reserva: " + e.getMessage());
			return;
		}

		confirmed = true;
		dispose();
	}

	private void abrirNuevaReserva() {
		ReservaCreateDialog dlg = new ReservaCreateDialog((Frame) getOwner());
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			ReservaDTO nueva = dlg.getReserva();
			if (nueva != null && nueva.getId() != null) {
				spnIdReserva.setValue(nueva.getId());
				JOptionPane.showMessageDialog(this, "Reserva creada con ID: " + nueva.getId(), "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public AlquilerDTO getAlquiler() {
		AlquilerDTO dto = new AlquilerDTO();
		dto.setIdReserva((Integer) spnIdReserva.getValue());
		dto.setFechaInicioEfectivo(format(dcInicio.getDate()));
		dto.setFechaFinEfectivo(format(dcFin.getDate()));
		dto.setKmInicial(!txtKmInicio.getText().trim().isEmpty() ? Integer.parseInt(txtKmInicio.getText().trim()) : 0);
		dto.setKmFinal(!txtKmFin.getText().isEmpty() ? Integer.parseInt(txtKmFin.getText().trim()) : 0);
		dto.setCostetotal(!txtCosteTotal.getText().isEmpty() ? Integer.parseInt(txtCosteTotal.getText().trim()) : 0);
		dto.setIdEstadoAlquiler(((EstadoAlquilerDTO) cmbEstado.getSelectedItem()).getId());
		dto.setIdUsuario(SessionManager.getCurrentUserId());
		return dto;
	}

	public void setIdUsuario(Integer id) {
	}

	private String format(Date d) {
		return d == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(d);
	}
}