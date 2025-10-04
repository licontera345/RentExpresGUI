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
import com.pinguela.rentexpres.model.ReservationStatusDTO;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.service.ReservationStatusService;
import com.pinguela.rentexpres.service.impl.ReservationStatusServiceImpl;
import com.pinguela.rentexpres.service.CustomerService;
import com.pinguela.rentexpres.service.impl.CustomerServiceImpl;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.desktop.dialog.CustomerCreateDialog;
import com.toedter.calendar.JDateChooser;

/**
 * Diálogo de creación de reservations.
 */
public class ReservationCreateDialog extends JDialog implements ConfirmDialog<ReservationDTO> {

	private static final long serialVersionUID = 1L;

	protected final JTextField txtVeh = new JTextField(10);
	protected final JTextField txtCli = new JTextField(10);
	protected final JDateChooser dcInicio = new JDateChooser();
	protected final JDateChooser dcFin = new JDateChooser();
	protected final JComboBox<ReservationStatusDTO> cmbEst = new JComboBox<>();

        public final JButton btnCrear = new JButton("Crear");
        public final JButton btnCancelar = new JButton("Cancelar");
        private final JButton btnNuevoCustomer = new JButton("Nuevo Customer");

        private final CustomerService customerService = new CustomerServiceImpl();

        private final ReservationStatusService estadoService = new ReservationStatusServiceImpl();

	public boolean confirmed = false;
	private ReservationDTO createdReservation = null;

	public ReservationCreateDialog(Frame owner) {
		super(owner, "Crear Reservation", true);
		initComponents();
		loadEstados();
	}

	private void initComponents() {

                JPanel form = new JPanel(new MigLayout("wrap 4", "[right]10[grow,fill]20[right]10[grow,fill]", "[]8[]8[]8[]8[]"));

                // Fila 0
                form.add(new JLabel("Vehicle ID:"), "cell 0 0");
                form.add(txtVeh, "cell 1 0,growx");
                form.add(new JLabel("Customer ID:"), "cell 2 0");
                form.add(txtCli, "cell 3 0,growx,split 2");
                form.add(btnNuevoCustomer, "cell 3 0");

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
                btnNuevoCustomer.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                abrirNuevoCustomer();
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
			List<ReservationStatusDTO> estados = estadoService.findAll();
			DefaultComboBoxModel<ReservationStatusDTO> model = new DefaultComboBoxModel<>();
			for (ReservationStatusDTO e : estados) {
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
					if (value instanceof ReservationStatusDTO) {
						ReservationStatusDTO est = (ReservationStatusDTO) value;
						setText(est.getStatusName());
					}
					return this;
				}
			});
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error cargando estados: " + ex.getMessage());
		}
	}

        private ReservationDTO buildFromForm1() {
                ReservationDTO dto = new ReservationDTO();
                dto.setVehicleId(Integer.parseInt(txtVeh.getText().trim()));
                dto.setCustomerId(Integer.parseInt(txtCli.getText().trim()));
                dto.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(dcInicio.getDate()));
                dto.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(dcFin.getDate()));
                ReservationStatusDTO est = (ReservationStatusDTO) cmbEst.getSelectedItem();
                if (est != null) {
                        dto.setReservationIdStatus(est.getId());
                }
                if (AppContext.getCurrentUser() != null) {
                        dto.setUserId(AppContext.getCurrentUser().getId());
                }
                return dto;
        }

	protected void onCrear() {

		if (!validar()) {
			return;
		}

		ReservationDTO dto = buildFromForm1();

                createdReservation = dto;
                confirmed = true;
                JOptionPane.showMessageDialog(this, "Reservation creada exitosamente.", "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                dispose();
	}

	public boolean isConfirmed() {
		return confirmed;
	}

        public ReservationDTO getReservation() {
                return createdReservation;
        }

        @Override
        public ReservationDTO getValue() {
                return getReservation();
        }

        private void abrirNuevoCustomer() {
                CustomerCreateDialog dlg = new CustomerCreateDialog((Frame) getOwner());
                dlg.setVisible(true);
                if (dlg.isConfirmed()) {
                        try {
                                CustomerDTO nuevo = dlg.getCustomer();
                                if (customerService.create(nuevo)) {
                                        txtCli.setText(String.valueOf(nuevo.getId()));
                                        JOptionPane.showMessageDialog(this,
                                                        "Customer creado con ID: " + nuevo.getId(),
                                                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                }
                        } catch (RentexpresException ex) {
                                SwingUtils.showError(this, "Error creando customer: " + ex.getMessage());
                        }
                }
        }

	public boolean validar() {
		try {
			Integer.parseInt(txtVeh.getText().trim());
			Integer.parseInt(txtCli.getText().trim());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Vehicle y Customer deben ser números.", "Aviso",
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

	public void setUserId(Integer id) {
	}
}
