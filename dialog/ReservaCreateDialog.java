package com.pinguela.rentexpres.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pinguela.rentexpres.desktop.util.SessionManager;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.EstadoReservaDTO;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.EstadoReservaService;
import com.pinguela.rentexpres.service.ReservaService;
import com.pinguela.rentexpres.service.impl.EstadoReservaServiceImpl;
import com.pinguela.rentexpres.service.impl.ReservaServiceImpl;
import com.toedter.calendar.JDateChooser;

/**
 * Diálogo de creación de reservas.
 */
public class ReservaCreateDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	protected final JTextField txtVeh = new JTextField(10);
	protected final JTextField txtCli = new JTextField(10);
	protected final JDateChooser dcInicio = new JDateChooser();
	protected final JDateChooser dcFin = new JDateChooser();
	protected final JComboBox<EstadoReservaDTO> cmbEst = new JComboBox<>();

	public final JButton btnCrear = new JButton("Crear");
	public final JButton btnCancelar = new JButton("Cancelar");

	private final EstadoReservaService estadoService = new EstadoReservaServiceImpl();
	private final ReservaService reservaService = new ReservaServiceImpl();

	public boolean confirmed = false;
	private ReservaDTO createdReserva = null;

	public ReservaCreateDialog(Frame owner) {
		super(owner, "Crear Reserva", true);
		initComponents();
		loadEstados();
	}

	private void initComponents() {

		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;

		int row = 0;

		gbc.gridx = 0;
		gbc.gridy = row;
		form.add(new JLabel("Vehículo ID:"), gbc);
		gbc.gridx = 1;
		form.add(txtVeh, gbc);
		row++;

		gbc.gridx = 0;
		gbc.gridy = row;
		form.add(new JLabel("Cliente ID:"), gbc);
		gbc.gridx = 1;
		form.add(txtCli, gbc);
		row++;

		gbc.gridx = 0;
		gbc.gridy = row;
		form.add(new JLabel("Fecha Inicio:"), gbc);
		gbc.gridx = 1;
		form.add(dcInicio, gbc);
		row++;

		gbc.gridx = 0;
		gbc.gridy = row;
		form.add(new JLabel("Fecha Fin:"), gbc);
		gbc.gridx = 1;
		form.add(dcFin, gbc);
		row++;

		gbc.gridx = 0;
		gbc.gridy = row;
		form.add(new JLabel("Estado:"), gbc);
		gbc.gridx = 1;
		form.add(cmbEst, gbc);
		row++;

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttons.add(btnCrear);
		buttons.add(btnCancelar);

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

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(8, 8));
		cp.add(form, BorderLayout.CENTER);
		cp.add(buttons, BorderLayout.SOUTH);

		pack();
		setResizable(false);
		setLocationRelativeTo(getOwner());
	}

	private void loadEstados() {
		try {
			List<EstadoReservaDTO> estados = estadoService.findAll();
			DefaultComboBoxModel<EstadoReservaDTO> model = new DefaultComboBoxModel<>();
			for (EstadoReservaDTO e : estados) {
				model.addElement(e);
			}
			cmbEst.setModel(model);

			cmbEst.setRenderer(new DefaultListCellRenderer() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value instanceof EstadoReservaDTO) {
						EstadoReservaDTO est = (EstadoReservaDTO) value;
						setText(est.getNombreEstado());
					}
					return this;
				}
			});
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error cargando estados: " + ex.getMessage());
		}
	}

	private ReservaDTO buildFromForm1() {
		ReservaDTO dto = new ReservaDTO();
		// … resto de campos …
		dto.setIdUsuario(SessionManager.getCurrentUserId());
		return dto;
	}

	protected void onCrear() {

		if (!validar()) {
			return;
		}

		ReservaDTO dto = buildFromForm1();

		try {
			boolean creado = reservaService.create(dto);
			if (creado) {
				createdReserva = dto;
				confirmed = true;
				JOptionPane.showMessageDialog(this, "Reserva creada exitosamente. ID " + dto.getId(), "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} else {
				SwingUtils.showError(this, "No se pudo crear la reserva.");
			}
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error creando reserva: " + ex.getMessage());
		}
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public ReservaDTO getReserva() {
		return createdReserva;
	}

	public boolean validar() {
		try {
			Integer.parseInt(txtVeh.getText().trim());
			Integer.parseInt(txtCli.getText().trim());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Vehículo y Cliente deben ser números.", "Aviso",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		if (dcInicio.getDate() == null || dcFin.getDate() == null) {
			JOptionPane.showMessageDialog(this, "Selecciona ambas fechas.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		if (!dcFin.getDate().after(dcInicio.getDate())) {
			JOptionPane.showMessageDialog(this, "La fecha fin debe ser posterior a la fecha inicio.", "Aviso",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	public void setIdUsuario(Integer id) {
	}
}
