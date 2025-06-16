package com.pinguela.rentexpres.desktop.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Panel que permite filtrar Clientes por: - ID - Nombre / Apellido1 / Apellido2
 * / Email / Teléfono - Calle / Número - Provincia (de un JComboBox) - Localidad
 * (de un JComboBox en cascada)
 */
public class ClienteFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// Filtros de “cliente”
	private final JSpinner spnId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JTextField txtNombre = new JTextField(12);
	private final JTextField txtApellido1 = new JTextField(12);
	private final JTextField txtApellido2 = new JTextField(12);
	private final JTextField txtEmail = new JTextField(12);
	private final JTextField txtTelefono = new JTextField(12);

	// Filtros de “dirección”
	private final JTextField txtCalle = new JTextField(12);
	private final JTextField txtNumero = new JTextField(6);

	// Filtros de “provincia” y “localidad”
	private final JComboBox<String> cmbProvincia = new JComboBox<>();
	private final JComboBox<String> cmbLocalidad = new JComboBox<>();

	// Botón para ocultar / mostrar la columna “Seleccionar”
	private final JButton btnToggle = new JButton("Seleccionar");

	// Callbacks
	private Runnable onChange = null;
	private Runnable toggleListener = null;
	private java.util.function.Consumer<String> onProvinciaChange = null;

	public ClienteFilterPanel() {
		super(new GridBagLayout());
		setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros de Cliente"));
		initLayout();
		initListeners();
	}

	private void initLayout() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;

		int row = 0;

		gbc.gridx = 0;
		gbc.gridy = row;
		add(new JLabel("ID:"), gbc);
		gbc.gridx = 1;
		add(spnId, gbc);

		gbc.gridx = 2;
		add(new JLabel("Nombre:"), gbc);
		gbc.gridx = 3;
		add(txtNombre, gbc);

		row++;
		gbc.gridx = 0;
		gbc.gridy = row;
		add(new JLabel("Apellido 1:"), gbc);
		gbc.gridx = 1;
		add(txtApellido1, gbc);

		gbc.gridx = 2;
		add(new JLabel("Apellido 2:"), gbc);
		gbc.gridx = 3;
		add(txtApellido2, gbc);

		row++;
		gbc.gridx = 0;
		gbc.gridy = row;
		add(new JLabel("Email:"), gbc);
		gbc.gridx = 1;
		add(txtEmail, gbc);

		gbc.gridx = 2;
		add(new JLabel("Teléfono:"), gbc);
		gbc.gridx = 3;
		add(txtTelefono, gbc);

		row++;
		gbc.gridx = 0;
		gbc.gridy = row;
		add(new JLabel("Calle:"), gbc);
		gbc.gridx = 1;
		add(txtCalle, gbc);

		gbc.gridx = 2;
		add(new JLabel("Nº:"), gbc);
		gbc.gridx = 3;
		add(txtNumero, gbc);

		row++;
		gbc.gridx = 0;
		gbc.gridy = row;
		add(new JLabel("Provincia:"), gbc);
		gbc.gridx = 1;
		add(cmbProvincia, gbc);

		gbc.gridx = 2;
		add(new JLabel("Localidad:"), gbc);
		gbc.gridx = 3;
		add(cmbLocalidad, gbc);

		row++;
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 4;
		gbc.anchor = GridBagConstraints.EAST;
		add(btnToggle, gbc);
	}

	private void initListeners() {
		// ID spinner
		spnId.addChangeListener(e -> fireChange());

		// TextFields
		DocumentListener docListener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				fireChange();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				fireChange();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				fireChange();
			}
		};
		txtNombre.getDocument().addDocumentListener(docListener);
		txtApellido1.getDocument().addDocumentListener(docListener);
		txtApellido2.getDocument().addDocumentListener(docListener);
		txtEmail.getDocument().addDocumentListener(docListener);
		txtTelefono.getDocument().addDocumentListener(docListener);
		txtCalle.getDocument().addDocumentListener(docListener);
		txtNumero.getDocument().addDocumentListener(docListener);

		cmbProvincia.addActionListener(e -> {
			fireChange();
			if (onProvinciaChange != null) {
				String prov = (String) cmbProvincia.getSelectedItem();
				onProvinciaChange.accept(prov);
			}
		});

		cmbLocalidad.addActionListener(e -> fireChange());

		btnToggle.addActionListener(e -> {
			if (toggleListener != null)
				toggleListener.run();
		});
	}

	private void fireChange() {
		if (onChange != null)
			onChange.run();
	}

	public Integer getId() {
		int val = (Integer) spnId.getValue();
		return val <= 0 ? null : val;
	}

	public String getNombre() {
		return txtNombre.getText().trim();
	}

	public String getApellido1() {
		return txtApellido1.getText().trim();
	}

	public String getApellido2() {
		return txtApellido2.getText().trim();
	}

	public String getEmail() {
		return txtEmail.getText().trim();
	}

	public String getTelefono() {
		return txtTelefono.getText().trim();
	}

	public String getCalle() {
		return txtCalle.getText().trim();
	}

	public String getNumero() {
		return txtNumero.getText().trim();
	}

	public String getProvincia() {
		Object sel = cmbProvincia.getSelectedItem();
		return sel == null ? null : sel.toString();
	}

	public String getLocalidad() {
		Object sel = cmbLocalidad.getSelectedItem();
		return sel == null ? null : sel.toString();
	}

	// Expose combo controls so controller can pre‐fill them
	public JComboBox<String> getCmbProvincia() {
		return cmbProvincia;
	}

	public JComboBox<String> getCmbLocalidad() {
		return cmbLocalidad;
	}

	// Clear all filters
	public void clear() {
		spnId.setValue(0);
		txtNombre.setText("");
		txtApellido1.setText("");
		txtApellido2.setText("");
		txtEmail.setText("");
		txtTelefono.setText("");
		txtCalle.setText("");
		txtNumero.setText("");
		cmbProvincia.setSelectedIndex(0);
		cmbLocalidad.setSelectedIndex(0);
	}

	// Set callbacks
	public void setOnChange(Runnable r) {
		onChange = r;
	}

	public void setToggleListener(Runnable r) {
		toggleListener = r;
	}

	public void setOnProvinciaChange(java.util.function.Consumer<String> c) {
		onProvinciaChange = c;
	}
}
