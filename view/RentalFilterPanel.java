package com.pinguela.rentexpres.desktop.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import java.text.NumberFormat;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.desktop.util.AppIcons;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.pinguela.rentexpres.model.RentalStatusDTO;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

import java.awt.Color;

public class RentalFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/* ────────── Controles ────────── */
	private final JSpinner spnIdRental = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spnReservationId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

	private final JDateChooser dcInicio = new JDateChooser();
	private final JDateChooser dcFin = new JDateChooser();

        private final JComboBox<RentalStatusDTO> cmbEstado = new JComboBox<RentalStatusDTO>();

        private final JFormattedTextField ftfStartKm;
        private final JFormattedTextField ftfEndKm;

        // extra filters
        private final JSpinner spnCustomerId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        private final JTextField txtName = new JTextField();
        private final JTextField txtApellido = new JTextField();
        private final JTextField txtPhone = new JTextField();

        private final JSpinner spnVehicleId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        private final JTextField txtLicensePlate = new JTextField();
        private final JTextField txtMake = new JTextField();
        private final JTextField txtModel = new JTextField();

        private final JFormattedTextField ftfCosteTotal;
        private final JFormattedTextField ftfDailyPrice;

	/* ────────── Callbacks ────────── */
	private OnChangeListener changeListener;
        private ToggleListener toggleListener;

        private JLabel lbl(String t) {
                JLabel l = new JLabel(t);
                l.setForeground(AppTheme.LABEL_FG);
                return l;
        }

        public RentalFilterPanel() {
                setBorder(new CompoundBorder(new TitledBorder("Filtros de Rental"), new EmptyBorder(10,10,10,10)));
                setLayout(new MigLayout("wrap 4,fillx", "[right]10[grow,fill]20[right]10[grow,fill]", "[]8[]8[]8[]8[]"));
                setBackground(AppTheme.FILTER_BG);

                NumberFormat intFormat = NumberFormat.getIntegerInstance();
                ftfStartKm = new JFormattedTextField(intFormat);
                ftfStartKm.putClientProperty("JTextField.placeholderText", "Inicial");
                ftfEndKm = new JFormattedTextField(intFormat);
                ftfEndKm.putClientProperty("JTextField.placeholderText", "Final");

                NumberFormat doubleFormat = NumberFormat.getNumberInstance();
                ftfCosteTotal = new JFormattedTextField(doubleFormat);
                ftfCosteTotal.putClientProperty("JTextField.placeholderText", "Total");
                ftfDailyPrice = new JFormattedTextField(doubleFormat);
                ftfDailyPrice.putClientProperty("JTextField.placeholderText", "€/día");

                txtName.putClientProperty("JTextField.placeholderText", "Name");
                txtApellido.putClientProperty("JTextField.placeholderText", "Apellido");
                txtPhone.putClientProperty("JTextField.placeholderText", "Teléfono");
                txtLicensePlate.putClientProperty("JTextField.placeholderText", "LicensePlate");
                txtMake.putClientProperty("JTextField.placeholderText", "Make");
                txtModel.putClientProperty("JTextField.placeholderText", "Model");

                dcInicio.setDateFormatString("yyyy-MM-dd");
                dcFin.setDateFormatString("yyyy-MM-dd");

                add(lbl("ID Rental:"), "cell 0 0");
                add(spnIdRental, "cell 1 0");
                add(lbl("ID Reservation:"), "cell 2 0");
                add(spnReservationId, "cell 3 0");

                add(lbl("Fecha Inicio:"), "cell 0 1");
                add(dcInicio, "cell 1 1");
                add(lbl("Fecha Fin:"), "cell 2 1");
                add(dcFin, "cell 3 1");

                add(lbl("KM Inicial:"), "cell 0 2");
                add(ftfStartKm, "cell 1 2");
                add(lbl("KM Final:"), "cell 2 2");
                add(ftfEndKm, "cell 3 2");

                add(lbl("Estado:"), "cell 0 3");
                add(cmbEstado, "cell 1 3, growx");
                add(lbl("Coste Total:"), "cell 2 3");
                add(ftfCosteTotal, "cell 3 3");

                add(lbl("ID Customer:"), "cell 0 4");
                add(spnCustomerId, "cell 1 4");
                add(lbl("Name:"), "cell 2 4");
                add(txtName, "cell 3 4");

                add(lbl("Apellido:"), "cell 0 5");
                add(txtApellido, "cell 1 5");
                add(lbl("Teléfono:"), "cell 2 5");
                add(txtPhone, "cell 3 5");

                add(lbl("ID Vehicle:"), "cell 0 6");
                add(spnVehicleId, "cell 1 6");
                add(lbl("LicensePlate:"), "cell 2 6");
                add(txtLicensePlate, "cell 3 6");

                add(lbl("Make:"), "cell 0 7");
                add(txtMake, "cell 1 7");
                add(lbl("Model:"), "cell 2 7");
                add(txtModel, "cell 3 7");

                add(lbl("Precio Día:"), "cell 0 8");
                add(ftfDailyPrice, "cell 1 8");

                javax.swing.JButton btnToggle = new javax.swing.JButton("Mostrar/Ocultar Selección");
                btnToggle.setBackground(AppTheme.PRIMARY);
                btnToggle.setForeground(Color.WHITE);
                btnToggle.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (toggleListener != null)
					toggleListener.onToggle();
			}
		});
                add(btnToggle, "cell 2 9, span 2");

		/* listeners que disparan cambios */
		ChangeListener spListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				fireChange();
			}
		};
                spnIdRental.addChangeListener(spListener);
                spnReservationId.addChangeListener(spListener);
                spnCustomerId.addChangeListener(spListener);
                spnVehicleId.addChangeListener(spListener);

		PropertyChangeListener dateListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				fireChange();
			}
		};
		dcInicio.getDateEditor().addPropertyChangeListener("date", dateListener);
		dcFin.getDateEditor().addPropertyChangeListener("date", dateListener);

		DocumentListener dListener = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				fireChange();
			}

			public void removeUpdate(DocumentEvent e) {
				fireChange();
			}

			public void changedUpdate(DocumentEvent e) {
				fireChange();
			}
		};
                ftfStartKm.getDocument().addDocumentListener(dListener);
                ftfEndKm.getDocument().addDocumentListener(dListener);
                ftfCosteTotal.getDocument().addDocumentListener(dListener);
                txtName.getDocument().addDocumentListener(dListener);
                txtApellido.getDocument().addDocumentListener(dListener);
                txtPhone.getDocument().addDocumentListener(dListener);
                txtLicensePlate.getDocument().addDocumentListener(dListener);
                txtMake.getDocument().addDocumentListener(dListener);
                txtModel.getDocument().addDocumentListener(dListener);
                ftfDailyPrice.getDocument().addDocumentListener(dListener);
	}

	/* ────────── Públicos ────────── */
        public void clear() {
                spnIdRental.setValue(0);
                spnReservationId.setValue(0);
                dcInicio.setDate(null);
                dcFin.setDate(null);
                ftfStartKm.setValue(null);
                ftfEndKm.setValue(null);
                cmbEstado.setSelectedIndex(-1);
                ftfCosteTotal.setValue(null);
                spnCustomerId.setValue(0);
                txtName.setText("");
                txtApellido.setText("");
                txtPhone.setText("");
                spnVehicleId.setValue(0);
                txtLicensePlate.setText("");
                txtMake.setText("");
                txtModel.setText("");
                ftfDailyPrice.setValue(null);
                fireChange();
        }

	public void setOnChange(OnChangeListener l) {
		changeListener = l;
	}

	public void setToggleListener(ToggleListener l) {
		toggleListener = l;
	}

	/* ────────── Getters usados por el controller ────────── */
	public Integer getIdRental() {
		return (Integer) spnIdRental.getValue();
	}

	public Integer getReservationId() {
		return (Integer) spnReservationId.getValue();
	}

	public String getStartDate() {
		return formatDate(dcInicio.getDate());
	}

	public String getEndDate() {
		return formatDate(dcFin.getDate());
	}

        public Integer getStartKm() {
                return parseIntValue(ftfStartKm.getValue());
        }

        public Integer getEndKm() {
                return parseIntValue(ftfEndKm.getValue());
        }

        public Integer getCosteTotal() {
                return parseIntValue(ftfCosteTotal.getValue());
        }

        public Integer getCustomerId() {
                return (Integer) spnCustomerId.getValue();
        }

        public String getName() {
                return txtName.getText().trim();
        }

        public String getApellido() {
                return txtApellido.getText().trim();
        }

        public String getPhone() {
                return txtPhone.getText().trim();
        }

        public Integer getVehicleId() {
                return (Integer) spnVehicleId.getValue();
        }

        public String getLicensePlate() {
                return txtLicensePlate.getText().trim();
        }

        public String getMake() {
                return txtMake.getText().trim();
        }

        public String getModel() {
                return txtModel.getText().trim();
        }

        public Double getDailyPrice() {
                try {
                        Object v = ftfDailyPrice.getValue();
                        return (v == null) ? null : Double.valueOf(v.toString());
                } catch (NumberFormatException ex) {
                        return null;
                }
        }

	public JComboBox<RentalStatusDTO> getCmbEstado() {
		return cmbEstado;
	}

	public RentalStatusDTO getEstadoSeleccionado() {
		return (RentalStatusDTO) cmbEstado.getSelectedItem();
	}

	/* ────────── Interno ────────── */
	private void fireChange() {
		if (changeListener != null)
			changeListener.onChange();
	}

	private static String formatDate(java.util.Date d) {
		return d == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(d);
	}

        private static Integer parseIntValue(Object v) {
                if (v == null)
                        return null;
                try {
                        return Integer.valueOf(v.toString());
                } catch (NumberFormatException ex) {
                        return null;
                }
        }

	/* ────────── Interfaces ────────── */
	public interface OnChangeListener {
		void onChange();
	}

	public interface ToggleListener {
		void onToggle();
	}
}
