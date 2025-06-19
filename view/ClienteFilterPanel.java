package com.pinguela.rentexpres.desktop.view;

import net.miginfocom.swing.MigLayout;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.desktop.util.AppIcons;

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
        private ActionCallback onChange = null;
        private ActionCallback toggleListener = null;
       private java.util.function.Consumer<String> onProvinciaChange = null;

       private JLabel lbl(String t) {
               JLabel l = new JLabel(t);
               l.setForeground(AppTheme.LABEL_FG);
               return l;
       }

       public ClienteFilterPanel() {
               super(new MigLayout("wrap 4", "[right]10[150!]20[right]10[150!]", "[]8[]8[]8[]8[]"));
               setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros de Cliente"));
               setBackground(AppTheme.FILTER_BG);

               txtNombre.putClientProperty("JTextField.placeholderText", "Nombre");
               txtNombre.putClientProperty("JTextField.leadingIcon", AppIcons.CLIENTE);
               txtApellido1.putClientProperty("JTextField.placeholderText", "Apellido 1");
               txtApellido1.putClientProperty("JTextField.leadingIcon", AppIcons.CLIENTE);
               txtApellido2.putClientProperty("JTextField.placeholderText", "Apellido 2");
               txtApellido2.putClientProperty("JTextField.leadingIcon", AppIcons.CLIENTE);
               txtEmail.putClientProperty("JTextField.placeholderText", "Email");
               txtEmail.putClientProperty("JTextField.leadingIcon", AppIcons.VER);
               txtTelefono.putClientProperty("JTextField.placeholderText", "Teléfono");
               txtTelefono.putClientProperty("JTextField.leadingIcon", AppIcons.VER);
               txtCalle.putClientProperty("JTextField.placeholderText", "Calle");
               txtCalle.putClientProperty("JTextField.leadingIcon", AppIcons.CLIENTE);
               txtNumero.putClientProperty("JTextField.placeholderText", "Nº");

               initLayout();
               initListeners();
       }

       private void initLayout() {
               add(lbl("ID:"), "cell 0 0");
               add(spnId, "cell 1 0");
               add(lbl("Nombre:"), "cell 2 0");
               add(txtNombre, "cell 3 0, growx");

               add(lbl("Apellido 1:"), "cell 0 1");
               add(txtApellido1, "cell 1 1, growx");
               add(lbl("Apellido 2:"), "cell 2 1");
               add(txtApellido2, "cell 3 1, growx");

               add(lbl("Email:"), "cell 0 2");
               add(txtEmail, "cell 1 2, growx");
               add(lbl("Teléfono:"), "cell 2 2");
               add(txtTelefono, "cell 3 2, growx");

               add(lbl("Calle:"), "cell 0 3");
               add(txtCalle, "cell 1 3, growx");
               add(lbl("Nº:"), "cell 2 3");
               add(txtNumero, "cell 3 3, growx");

               add(lbl("Provincia:"), "cell 0 4");
               add(cmbProvincia, "cell 1 4, growx");
               add(lbl("Localidad:"), "cell 2 4");
               add(cmbLocalidad, "cell 3 4, growx");

               btnToggle.setBackground(AppTheme.PRIMARY);
               btnToggle.setForeground(Color.WHITE);
               add(btnToggle, "cell 0 5 4 1, alignx right");
       }

	private void initListeners() {
		// ID spinner
               spnId.addChangeListener(new javax.swing.event.ChangeListener() {
                       @Override
                       public void stateChanged(javax.swing.event.ChangeEvent e) {
                               fireChange();
                       }
               });

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

               cmbProvincia.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               fireChange();
                               if (onProvinciaChange != null) {
                                       String prov = (String) cmbProvincia.getSelectedItem();
                                       onProvinciaChange.accept(prov);
                               }
                       }
               });

               cmbLocalidad.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               fireChange();
                       }
               });

               btnToggle.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               if (toggleListener != null)
                                       toggleListener.execute();
                       }
               });
	}

        private void fireChange() {
                if (onChange != null)
                        onChange.execute();
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
        public void setOnChange(ActionCallback r) {
                onChange = r;
       }

        public void setToggleListener(ActionCallback r) {
                toggleListener = r;
       }

	public void setOnProvinciaChange(java.util.function.Consumer<String> c) {
		onProvinciaChange = c;
	}
}
