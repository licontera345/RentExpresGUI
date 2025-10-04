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

import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.model.RentalStatusDTO;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.service.RentalStatusService;
import com.pinguela.rentexpres.service.CustomerService;
import com.pinguela.rentexpres.service.ReservationService;
import com.pinguela.rentexpres.service.impl.RentalStatusServiceImpl;
import com.pinguela.rentexpres.service.impl.CustomerServiceImpl;
import com.pinguela.rentexpres.service.impl.ReservationServiceImpl;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.desktop.dialog.CustomerCreateDialog;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

public class RentalCreateDialog extends JDialog implements ConfirmDialog<RentalDTO> {
	private static final long serialVersionUID = 1L;

	private final JSpinner spnReservationId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JDateChooser dcInicio = new JDateChooser();
	private final JDateChooser dcFin = new JDateChooser();
	private final JTextField txtKmInicio = new JTextField();
	private final JTextField txtKmFin = new JTextField();
	private final JTextField txtCosteTotal = new JTextField();
	private final JComboBox<RentalStatusDTO> cmbEstado = new JComboBox<>();
	private final JButton btnCrear = new JButton("Crear");
       private final JButton btnCancelar = new JButton("Cancelar");
       private final JButton btnNuevaReservation = new JButton("Nueva Reservation");
       private final JButton btnNuevoCustomer = new JButton("Nuevo Customer");

       private final CustomerService customerService = new CustomerServiceImpl();
       private final ReservationService reservationService = new ReservationServiceImpl();
       private Integer ultimoCustomerId = null;

	private final RentalStatusService estadoService = new RentalStatusServiceImpl();
	private boolean confirmed = false;

	public RentalCreateDialog(Frame owner) {
		super(owner, "Nuevo Rental", true);
		initComponents();
		loadEstados();
	}

	private void initComponents() {
               // Simplified column constraints to avoid parsing issues in some environments
               setLayout(new MigLayout(
                               "wrap 4,insets 15",
                               "[right]10[grow,fill]20[right]10[grow,fill]",
                               "[]10[]10[]10[]10[]"));
                dcInicio.setDateFormatString("yyyy-MM-dd");
                dcFin.setDateFormatString("yyyy-MM-dd");

               add(new JLabel("ID Reservation:"), "cell 0 0");
               add(spnReservationId, "cell 1 0");
               add(btnNuevaReservation, "cell 2 0");
               add(btnNuevoCustomer, "cell 3 0, right, wrap");

                add(new JLabel("Fecha Inicio:"), "cell 0 1");
                add(dcInicio, "cell 1 1");
                add(new JLabel("Fecha Fin:"), "cell 2 1");
                add(dcFin, "cell 3 1, wrap");

                add(new JLabel("KM Inicio:"), "cell 0 2");
                add(txtKmInicio, "cell 1 2");
                add(new JLabel("KM Fin:"), "cell 2 2");
                add(txtKmFin, "cell 3 2, wrap");

                add(new JLabel("Coste Total:"), "cell 0 3");
                add(txtCosteTotal, "cell 1 3");
                add(new JLabel("Estado:"), "cell 2 3");
                add(cmbEstado, "cell 3 3, wrap");

                add(btnCrear, "cell 2 4");
                add(btnCancelar, "cell 3 4");

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
                btnNuevaReservation.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                abrirNuevaReservation();
                        }
                });
               btnNuevoCustomer.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               abrirNuevoCustomer();
                       }
               });
                pack();
                setLocationRelativeTo(getOwner());
        }

	private void loadEstados() {
		try {
			List<RentalStatusDTO> list = estadoService.findAll();
			DefaultComboBoxModel<RentalStatusDTO> model = new DefaultComboBoxModel<>();
			for (RentalStatusDTO e : list)
				model.addElement(e);
			cmbEstado.setModel(model);
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error cargando estados: " + ex.getMessage());
		}
	}

	private boolean validar() {
		if ((Integer) spnReservationId.getValue() <= 0) {
			JOptionPane.showMessageDialog(this, "ID Reservation debe ser > 0", "Aviso", JOptionPane.WARNING_MESSAGE);
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

                confirmed = true;
                dispose();
        }

        private void abrirNuevaReservation() {
                ReservationCreateDialog dlg = new ReservationCreateDialog((Frame) getOwner());
               if (ultimoCustomerId != null) {
                       dlg.txtCli.setText(String.valueOf(ultimoCustomerId));
               }
                dlg.setVisible(true);
                if (dlg.isConfirmed()) {
                        try {
                                ReservationDTO nueva = dlg.getReservation();
                                if (reservationService.create(nueva)) {
                                        spnReservationId.setValue(nueva.getId());
                                        JOptionPane.showMessageDialog(this,
                                                        "Reservation creada con ID: " + nueva.getId(),
                                                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                }
                        } catch (RentexpresException ex) {
                                SwingUtils.showError(this, "Error creando reservation: " + ex.getMessage());
                        }
                }
        }

       private void abrirNuevoCustomer() {
               CustomerCreateDialog dlg = new CustomerCreateDialog((Frame) getOwner());
               dlg.setVisible(true);
               if (dlg.isConfirmed()) {
                       try {
                               CustomerDTO nuevo = dlg.getCustomer();
                               if (customerService.create(nuevo)) {
                                       ultimoCustomerId = nuevo.getId();
                                       JOptionPane.showMessageDialog(this, "Customer creado con ID: " + nuevo.getId(), "Éxito",
                                                       JOptionPane.INFORMATION_MESSAGE);
                               }
                       } catch (RentexpresException ex) {
                               SwingUtils.showError(this, "Error creando customer: " + ex.getMessage());
                       }
               }
       }

	public boolean isConfirmed() {
		return confirmed;
	}

        public RentalDTO getRental() {
                RentalDTO dto = new RentalDTO();
		dto.setReservationId((Integer) spnReservationId.getValue());
		dto.setActualStartDate(format(dcInicio.getDate()));
		dto.setActualEndDate(format(dcFin.getDate()));
		dto.setStartKm(!txtKmInicio.getText().trim().isEmpty() ? Integer.parseInt(txtKmInicio.getText().trim()) : 0);
		dto.setEndKm(!txtKmFin.getText().isEmpty() ? Integer.parseInt(txtKmFin.getText().trim()) : 0);
		dto.setTotalCost(!txtCosteTotal.getText().isEmpty() ? Integer.parseInt(txtCosteTotal.getText().trim()) : 0);
		dto.setRentalStatusId(((RentalStatusDTO) cmbEstado.getSelectedItem()).getId());
                if (AppContext.getCurrentUser() != null) {
                        dto.setUserId(AppContext.getCurrentUser().getId());
                }
                return dto;
        }

        @Override
        public RentalDTO getValue() {
                return getRental();
        }

	public void setUserId(Integer id) {
	}

	private String format(Date d) {
		return d == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(d);
	}
}