package com.pinguela.rentexpres.desktop.view;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

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
 * Panel que permite filtrar Customers por: - ID - Name / LastName / SecondLastName
 * / Email / Teléfono - Street / Número - Province (de un JComboBox) - City
 * (de un JComboBox en cascada)
 */
public class CustomerFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// Filtros de “customer”
	private final JSpinner spnId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JTextField txtName = new JTextField(12);
	private final JTextField txtLastName = new JTextField(12);
	private final JTextField txtSecondLastName = new JTextField(12);
	private final JTextField txtEmail = new JTextField(12);
	private final JTextField txtPhone = new JTextField(12);

	// Filtros de “dirección”
	private final JTextField txtStreet = new JTextField(12);
	private final JTextField txtStreetNumber = new JTextField(6);

	// Filtros de “province” y “city”
	private final JComboBox<String> cmbProvince = new JComboBox<>();
	private final JComboBox<String> cmbCity = new JComboBox<>();

	// Botón para ocultar / mostrar la columna “Seleccionar”
	private final JButton btnToggle = new JButton("Seleccionar");

	// Callbacks
        private ActionCallback onChange = null;
        private ActionCallback toggleListener = null;
       private java.util.function.Consumer<String> onProvinceChange = null;

       private JLabel lbl(String t) {
               JLabel l = new JLabel(t);
               l.setForeground(AppTheme.LABEL_FG);
               return l;
       }

       public CustomerFilterPanel() {
               super(new GridBagLayout());
               setBorder(new CompoundBorder(new TitledBorder("Filtros de Customer"), new EmptyBorder(12,12,12,12)));
               setBackground(AppTheme.FILTER_BG);

               txtName.putClientProperty("JTextField.placeholderText", "Name");
               txtLastName.putClientProperty("JTextField.placeholderText", "Apellido 1");
               txtSecondLastName.putClientProperty("JTextField.placeholderText", "Apellido 2");
               txtEmail.putClientProperty("JTextField.placeholderText", "Email");
               txtPhone.putClientProperty("JTextField.placeholderText", "Teléfono");
               txtStreet.putClientProperty("JTextField.placeholderText", "Street");
               txtStreetNumber.putClientProperty("JTextField.placeholderText", "Nº");

               initLayout();
               initListeners();
       }

       private void initLayout() {
               GridBagConstraints gbc = new GridBagConstraints();
               gbc.insets = new Insets(4, 4, 4, 4);
               gbc.fill = GridBagConstraints.HORIZONTAL;

               // fila 0
               gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0;
               add(lbl("ID:"), gbc);
               gbc.gridx = 1; add(spnId, gbc);
               gbc.gridx = 2; add(lbl("Name:"), gbc);
               gbc.gridx = 3; gbc.weightx = 1; add(txtName, gbc);

               // fila 1
               gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0;
               add(lbl("Apellido 1:"), gbc);
               gbc.gridx = 1; gbc.weightx = 1; add(txtLastName, gbc);
               gbc.gridx = 2; gbc.weightx = 0; add(lbl("Apellido 2:"), gbc);
               gbc.gridx = 3; gbc.weightx = 1; add(txtSecondLastName, gbc);

               // fila 2
               gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0;
               add(lbl("Email:"), gbc);
               gbc.gridx = 1; gbc.weightx = 1; add(txtEmail, gbc);
               gbc.gridx = 2; gbc.weightx = 0; add(lbl("Teléfono:"), gbc);
               gbc.gridx = 3; gbc.weightx = 1; add(txtPhone, gbc);

               // fila 3
               gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0;
               add(lbl("Street:"), gbc);
               gbc.gridx = 1; gbc.weightx = 1; add(txtStreet, gbc);
               gbc.gridx = 2; gbc.weightx = 0; add(lbl("Nº:"), gbc);
               gbc.gridx = 3; gbc.weightx = 1; add(txtStreetNumber, gbc);

               // fila 4
               gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0;
               add(lbl("Province:"), gbc);
               gbc.gridx = 1; gbc.weightx = 1; add(cmbProvince, gbc);
               gbc.gridx = 2; gbc.weightx = 0; add(lbl("City:"), gbc);
               gbc.gridx = 3; gbc.weightx = 1; add(cmbCity, gbc);

               // botón toggle
               btnToggle.setBackground(AppTheme.PRIMARY);
               btnToggle.setForeground(Color.WHITE);
               gbc.gridy = 5; gbc.gridx = 3; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
               add(btnToggle, gbc);
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
		txtName.getDocument().addDocumentListener(docListener);
		txtLastName.getDocument().addDocumentListener(docListener);
		txtSecondLastName.getDocument().addDocumentListener(docListener);
		txtEmail.getDocument().addDocumentListener(docListener);
		txtPhone.getDocument().addDocumentListener(docListener);
		txtStreet.getDocument().addDocumentListener(docListener);
		txtStreetNumber.getDocument().addDocumentListener(docListener);

               cmbProvince.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               fireChange();
                               if (onProvinceChange != null) {
                                       String prov = (String) cmbProvince.getSelectedItem();
                                       onProvinceChange.accept(prov);
                               }
                       }
               });

               cmbCity.addActionListener(new ActionListener() {
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

	public String getName() {
		return txtName.getText().trim();
	}

	public String getLastName() {
		return txtLastName.getText().trim();
	}

	public String getSecondLastName() {
		return txtSecondLastName.getText().trim();
	}

	public String getEmail() {
		return txtEmail.getText().trim();
	}

	public String getPhone() {
		return txtPhone.getText().trim();
	}

	public String getStreet() {
		return txtStreet.getText().trim();
	}

	public String getStreetNumber() {
		return txtStreetNumber.getText().trim();
	}

	public String getProvince() {
		Object sel = cmbProvince.getSelectedItem();
		return sel == null ? null : sel.toString();
	}

	public String getCity() {
		Object sel = cmbCity.getSelectedItem();
		return sel == null ? null : sel.toString();
	}

	// Expose combo controls so controller can pre‐fill them
	public JComboBox<String> getCmbProvince() {
		return cmbProvince;
	}

	public JComboBox<String> getCmbCity() {
		return cmbCity;
	}

	// Clear all filters
	public void clear() {
		spnId.setValue(0);
		txtName.setText("");
		txtLastName.setText("");
		txtSecondLastName.setText("");
		txtEmail.setText("");
		txtPhone.setText("");
		txtStreet.setText("");
		txtStreetNumber.setText("");
		cmbProvince.setSelectedIndex(0);
		cmbCity.setSelectedIndex(0);
	}

	// Set callbacks
        public void setOnChange(ActionCallback r) {
                onChange = r;
       }

        public void setToggleListener(ActionCallback r) {
                toggleListener = r;
       }

	public void setOnProvinceChange(java.util.function.Consumer<String> c) {
		onProvinceChange = c;
	}
}
