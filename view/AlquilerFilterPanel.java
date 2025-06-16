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

		javax.swing.JButton btnToggle = new javax.swing.JButton("Mostrar/Ocultar Selección");
		btnToggle.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (toggleListener != null)
					toggleListener.onToggle();
			}
		});
		add(btnToggle, "cell 2 3, span 2");

		/* listeners que disparan cambios */
		ChangeListener spListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				fireChange();
			}
		};
		spnIdAlquiler.addChangeListener(spListener);
		spnIdReserva.addChangeListener(spListener);

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
