package com.pinguela.rentexpres.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
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

import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.EstadoReservaDTO;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.EstadoReservaService;
import com.pinguela.rentexpres.service.impl.EstadoReservaServiceImpl;
import com.pinguela.rentexpres.service.ClienteService;
import com.pinguela.rentexpres.service.impl.ClienteServiceImpl;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.desktop.dialog.ClienteCreateDialog;
import com.toedter.calendar.JDateChooser;

/**
 * Diálogo de creación de reservas.
 */
public class ReservaCreateDialog extends JDialog implements ConfirmDialog<ReservaDTO> {

	private static final long serialVersionUID = 1L;

	protected final JTextField txtVeh = new JTextField(10);
	protected final JTextField txtCli = new JTextField(10);
	protected final JDateChooser dcInicio = new JDateChooser();
	protected final JDateChooser dcFin = new JDateChooser();
	protected final JComboBox<EstadoReservaDTO> cmbEst = new JComboBox<>();

        public final JButton btnCrear = new JButton("Crear");
        public final JButton btnCancelar = new JButton("Cancelar");
        private final JButton btnNuevoCliente = new JButton("Nuevo Cliente");

        private final ClienteService clienteService = new ClienteServiceImpl();

        private final EstadoReservaService estadoService = new EstadoReservaServiceImpl();

	public boolean confirmed = false;
	private ReservaDTO createdReserva = null;

	public ReservaCreateDialog(Frame owner) {
		super(owner, "Crear Reserva", true);
		initComponents();
		loadEstados();
	}

	private void initComponents() {

                JPanel form = new JPanel(new MigLayout("wrap 4", "[right]10[grow,fill]20[right]10[grow,fill]", "[]8[]8[]8[]8[]"));

                // Fila 0
                form.add(new JLabel("Vehículo ID:"), "cell 0 0");
                form.add(txtVeh, "cell 1 0,growx");
                form.add(new JLabel("Cliente ID:"), "cell 2 0");
                form.add(txtCli, "cell 3 0,growx,split 2");
                form.add(btnNuevoCliente, "cell 3 0");

                // Fila 1
                form.add(new JLabel("Fecha Inicio:"), "cell 0 1");
                form.add(dcInicio, "cell 1 1,growx");
                form.add(new JLabel("Fecha Fin:"), "cell 2 1");
                form.add(dcFin, "cell 3 1,growx");

                // Fila 2
                form.add(new JLabel("Estado:"), "cell 0 2");
                form.add(cmbEst, "cell 1 2 3 1,growx");

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
                btnNuevoCliente.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                abrirNuevoCliente();
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
                dto.setIdVehiculo(Integer.parseInt(txtVeh.getText().trim()));
                dto.setIdCliente(Integer.parseInt(txtCli.getText().trim()));
                dto.setFechaInicio(new SimpleDateFormat("yyyy-MM-dd").format(dcInicio.getDate()));
                dto.setFechaFin(new SimpleDateFormat("yyyy-MM-dd").format(dcFin.getDate()));
                EstadoReservaDTO est = (EstadoReservaDTO) cmbEst.getSelectedItem();
                if (est != null) {
                        dto.setIdEstadoReserva(est.getId());
                }
                if (AppContext.getCurrentUser() != null) {
                        dto.setIdUsuario(AppContext.getCurrentUser().getId());
                }
                return dto;
        }

	protected void onCrear() {

		if (!validar()) {
			return;
		}

		ReservaDTO dto = buildFromForm1();

                createdReserva = dto;
                confirmed = true;
                JOptionPane.showMessageDialog(this, "Reserva creada exitosamente.", "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                dispose();
	}

	public boolean isConfirmed() {
		return confirmed;
	}

        public ReservaDTO getReserva() {
                return createdReserva;
        }

        @Override
        public ReservaDTO getValue() {
                return getReserva();
        }

        private void abrirNuevoCliente() {
                ClienteCreateDialog dlg = new ClienteCreateDialog((Frame) getOwner());
                dlg.setVisible(true);
                if (dlg.isConfirmed()) {
                        try {
                                ClienteDTO nuevo = dlg.getCliente();
                                if (clienteService.create(nuevo)) {
                                        txtCli.setText(String.valueOf(nuevo.getId()));
                                        JOptionPane.showMessageDialog(this,
                                                        "Cliente creado con ID: " + nuevo.getId(),
                                                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                }
                        } catch (RentexpresException ex) {
                                SwingUtils.showError(this, "Error creando cliente: " + ex.getMessage());
                        }
                }
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
