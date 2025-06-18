package com.pinguela.rentexpres.desktop.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.pinguela.rentexpres.model.EstadoAlquilerDTO;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

public class AlquilerFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/* ────────── Controles ────────── */
	private final JSpinner spnIdAlquiler = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spnIdReserva = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

	private final JDateChooser dcInicio = new JDateChooser();
	private final JDateChooser dcFin = new JDateChooser();

        private final JComboBox<EstadoAlquilerDTO> cmbEstado = new JComboBox<EstadoAlquilerDTO>();

        private final JTextField txtKmInicial = new JTextField();
        private final JTextField txtKmFinal = new JTextField();

        // extra filters
        private final JSpinner spnIdCliente = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        private final JTextField txtNombre = new JTextField();
        private final JTextField txtApellido = new JTextField();
        private final JTextField txtTelefono = new JTextField();

        private final JSpinner spnIdVehiculo = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        private final JTextField txtPlaca = new JTextField();
        private final JTextField txtMarca = new JTextField();
        private final JTextField txtModelo = new JTextField();

        private final JTextField txtCosteTotal = new JTextField();
        private final JTextField txtPrecioDia = new JTextField();

	/* ────────── Callbacks ────────── */
	private OnChangeListener changeListener;
	private ToggleListener toggleListener;

	public AlquilerFilterPanel() {
		setBorder(new TitledBorder("Filtros de Alquiler"));
		setLayout(new MigLayout("wrap 4", "[right]10[150!]20[right]10[150!]", "[]8[]8[]8[]8[]"));

		dcInicio.setDateFormatString("yyyy-MM-dd");
		dcFin.setDateFormatString("yyyy-MM-dd");

		add(new JLabel("ID Alquiler:"), "cell 0 0");
		add(spnIdAlquiler, "cell 1 0");
		add(new JLabel("ID Reserva:"), "cell 2 0");
		add(spnIdReserva, "cell 3 0");

		add(new JLabel("Fecha Inicio:"), "cell 0 1");
		add(dcInicio, "cell 1 1");
		add(new JLabel("Fecha Fin:"), "cell 2 1");
		add(dcFin, "cell 3 1");

		add(new JLabel("KM Inicial:"), "cell 0 2");
		add(txtKmInicial, "cell 1 2");
		add(new JLabel("KM Final:"), "cell 2 2");
		add(txtKmFinal, "cell 3 2");

                add(new JLabel("Estado:"), "cell 0 3");
                add(cmbEstado, "cell 1 3, growx");
                add(new JLabel("Coste Total:"), "cell 2 3");
                add(txtCosteTotal, "cell 3 3");

                add(new JLabel("ID Cliente:"), "cell 0 4");
                add(spnIdCliente, "cell 1 4");
                add(new JLabel("Nombre:"), "cell 2 4");
                add(txtNombre, "cell 3 4");

                add(new JLabel("Apellido:"), "cell 0 5");
                add(txtApellido, "cell 1 5");
                add(new JLabel("Teléfono:"), "cell 2 5");
                add(txtTelefono, "cell 3 5");

                add(new JLabel("ID Vehículo:"), "cell 0 6");
                add(spnIdVehiculo, "cell 1 6");
                add(new JLabel("Placa:"), "cell 2 6");
                add(txtPlaca, "cell 3 6");

                add(new JLabel("Marca:"), "cell 0 7");
                add(txtMarca, "cell 1 7");
                add(new JLabel("Modelo:"), "cell 2 7");
                add(txtModelo, "cell 3 7");

                add(new JLabel("Precio Día:"), "cell 0 8");
                add(txtPrecioDia, "cell 1 8");

                javax.swing.JButton btnToggle = new javax.swing.JButton("Mostrar/Ocultar Selección");
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
                spnIdAlquiler.addChangeListener(spListener);
                spnIdReserva.addChangeListener(spListener);
                spnIdCliente.addChangeListener(spListener);
                spnIdVehiculo.addChangeListener(spListener);

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
                txtKmInicial.getDocument().addDocumentListener(dListener);
                txtKmFinal.getDocument().addDocumentListener(dListener);
                txtCosteTotal.getDocument().addDocumentListener(dListener);
                txtNombre.getDocument().addDocumentListener(dListener);
                txtApellido.getDocument().addDocumentListener(dListener);
                txtTelefono.getDocument().addDocumentListener(dListener);
                txtPlaca.getDocument().addDocumentListener(dListener);
                txtMarca.getDocument().addDocumentListener(dListener);
                txtModelo.getDocument().addDocumentListener(dListener);
                txtPrecioDia.getDocument().addDocumentListener(dListener);
	}

	/* ────────── Públicos ────────── */
        public void clear() {
                spnIdAlquiler.setValue(0);
                spnIdReserva.setValue(0);
                dcInicio.setDate(null);
                dcFin.setDate(null);
                txtKmInicial.setText("");
                txtKmFinal.setText("");
                cmbEstado.setSelectedIndex(-1);
                txtCosteTotal.setText("");
                spnIdCliente.setValue(0);
                txtNombre.setText("");
                txtApellido.setText("");
                txtTelefono.setText("");
                spnIdVehiculo.setValue(0);
                txtPlaca.setText("");
                txtMarca.setText("");
                txtModelo.setText("");
                txtPrecioDia.setText("");
                fireChange();
        }

	public void setOnChange(OnChangeListener l) {
		changeListener = l;
	}

	public void setToggleListener(ToggleListener l) {
		toggleListener = l;
	}

	/* ────────── Getters usados por el controller ────────── */
	public Integer getIdAlquiler() {
		return (Integer) spnIdAlquiler.getValue();
	}

	public Integer getIdReserva() {
		return (Integer) spnIdReserva.getValue();
	}

	public String getFechaInicio() {
		return formatDate(dcInicio.getDate());
	}

	public String getFechaFin() {
		return formatDate(dcFin.getDate());
	}

	public Integer getKmInicial() {
		return parseInt(txtKmInicial.getText());
	}

        public Integer getKmFinal() {
                return parseInt(txtKmFinal.getText());
        }

        public Integer getCosteTotal() {
                return parseInt(txtCosteTotal.getText());
        }

        public Integer getIdCliente() {
                return (Integer) spnIdCliente.getValue();
        }

        public String getNombre() {
                return txtNombre.getText().trim();
        }

        public String getApellido() {
                return txtApellido.getText().trim();
        }

        public String getTelefono() {
                return txtTelefono.getText().trim();
        }

        public Integer getIdVehiculo() {
                return (Integer) spnIdVehiculo.getValue();
        }

        public String getPlaca() {
                return txtPlaca.getText().trim();
        }

        public String getMarca() {
                return txtMarca.getText().trim();
        }

        public String getModelo() {
                return txtModelo.getText().trim();
        }

        public Double getPrecioDia() {
                try {
                        return Double.valueOf(txtPrecioDia.getText().trim());
                } catch (NumberFormatException ex) {
                        return null;
                }
        }

	public JComboBox<EstadoAlquilerDTO> getCmbEstado() {
		return cmbEstado;
	}

	public EstadoAlquilerDTO getEstadoSeleccionado() {
		return (EstadoAlquilerDTO) cmbEstado.getSelectedItem();
	}

	/* ────────── Interno ────────── */
	private void fireChange() {
		if (changeListener != null)
			changeListener.onChange();
	}

	private static String formatDate(java.util.Date d) {
		return d == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(d);
	}

	private static Integer parseInt(String t) {
		try {
			return Integer.valueOf(t.trim());
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
