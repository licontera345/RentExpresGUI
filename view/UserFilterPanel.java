package com.pinguela.rentexpres.desktop.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.desktop.util.AppIcons;

import com.pinguela.rentexpres.model.UserTypeDTO;
import com.pinguela.rentexpres.service.UserTypeService;
import com.pinguela.rentexpres.service.impl.UserTypeServiceImpl;

import net.miginfocom.swing.MigLayout;

/**
 * Panel de filtros para buscar Users, adaptado al mismo estilo de
 * RentalFilterPanel: - Usa MigLayout - Tiene un borde con título - Se agrega
 * spinner para ID - Cada componente dispara el método fire() al modificarse
 */
public class UserFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JSpinner spnUserId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JTextField txtName = new JTextField(10);
	private final JTextField txtLastName = new JTextField(10);
	private final JTextField txtSecondLastName = new JTextField(10);
	private final JTextField txtEmail = new JTextField(12);
	private final JTextField txtUser = new JTextField(10);
	private final JComboBox<UserTypeDTO> cmbUserType = new JComboBox<>();

        private final UserTypeService motivoService = new UserTypeServiceImpl();

        private JLabel lbl(String t) {
                JLabel l = new JLabel(t);
                l.setForeground(AppTheme.LABEL_FG);
                return l;
        }

        public UserFilterPanel() {
                setBorder(new CompoundBorder(new TitledBorder("Filtros Users"), new EmptyBorder(12,12,12,12)));
                setLayout(new MigLayout("wrap 4,fillx", "[right]10[grow,fill]20[right]10[grow,fill]", "[]8[]8[]8[]8[]"));
                setBackground(AppTheme.FILTER_BG);

                txtName.putClientProperty("JTextField.placeholderText", "Name");
                txtLastName.putClientProperty("JTextField.placeholderText", "Apellido 1");
                txtSecondLastName.putClientProperty("JTextField.placeholderText", "Apellido 2");
                txtEmail.putClientProperty("JTextField.placeholderText", "Email");
                txtUser.putClientProperty("JTextField.placeholderText", "User");

                // Fila 0: ID | Name
                add(lbl("ID:"), "cell 0 0");
                add(spnUserId, "cell 1 0,growx");
                add(lbl("Name:"), "cell 2 0");
                add(txtName, "cell 3 0,growx");

                // Fila 1: LastName | SecondLastName
                add(lbl("LastName:"), "cell 0 1");
                add(txtLastName, "cell 1 1,growx");
                add(lbl("SecondLastName:"), "cell 2 1");
                add(txtSecondLastName, "cell 3 1,growx");

                // Fila 2: Email | User
                add(lbl("Email:"), "cell 0 2");
                add(txtEmail, "cell 1 2,growx");
                add(lbl("User (login):"), "cell 2 2");
                add(txtUser, "cell 3 2,growx");

                // Fila 3: Tipo user
                add(lbl("Tipo User:"), "cell 0 3");
                add(cmbUserType, "cell 1 3 3 1,growx");

                cargarTipos();
                cmbUserType.setRenderer(new com.pinguela.rentexpres.desktop.renderer.UserTypeRenderer());

		// Listeners para disparar evento de filtro en cada cambio
               spnUserId.addChangeListener(new ChangeListener() {
                       @Override
                       public void stateChanged(ChangeEvent e) {
                               fire();
                       }
               });
               txtName.getDocument().addDocumentListener(new SimpleDocumentListener() {
                       @Override
                       public void update(DocumentEvent e) {
                               fire();
                       }
               });
               txtLastName.getDocument().addDocumentListener(new SimpleDocumentListener() {
                       @Override
                       public void update(DocumentEvent e) {
                               fire();
                       }
               });
               txtSecondLastName.getDocument().addDocumentListener(new SimpleDocumentListener() {
                       @Override
                       public void update(DocumentEvent e) {
                               fire();
                       }
               });
               txtEmail.getDocument().addDocumentListener(new SimpleDocumentListener() {
                       @Override
                       public void update(DocumentEvent e) {
                               fire();
                       }
               });
               txtUser.getDocument().addDocumentListener(new SimpleDocumentListener() {
                       @Override
                       public void update(DocumentEvent e) {
                               fire();
                       }
               });
		cmbUserType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fire();
			}
		});
	}

	private void cargarTipos() {
		try {
			List<UserTypeDTO> lista = motivoService.findAll();
			cmbUserType.removeAllItems();
			for (UserTypeDTO t : lista) {
				cmbUserType.addItem(t);
			}
		} catch (Exception ex) {
			// si falla, dejamos combo vacío
		}
	}

	private void fire() {
		// Este método se encarga de notificar al controller que cambió algún filtro.
		// En la vista de búsqueda se registrará como listener para actualizar la tabla.
		this.firePropertyChange("filtrosCambio", null, null);
	}

	// Getters para recuperar criterios de búsqueda:
	public Integer getUserId() {
		return (Integer) spnUserId.getValue();
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

	public String getUserLogin() {
		return txtUser.getText().trim();
	}

	public Integer getUserIdType() {
		UserTypeDTO sel = (UserTypeDTO) cmbUserType.getSelectedItem();
		return (sel != null) ? sel.getId() : null;
	}

	// (Opcional) Exponer componentes si el controller/otra capa lo necesita:
	public JSpinner getSpnUserId() {
		return spnUserId;
	}

	public JTextField getTxtName() {
		return txtName;
	}

	public JTextField getTxtLastName() {
		return txtLastName;
	}

	public JTextField getTxtSecondLastName() {
		return txtSecondLastName;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	public JTextField getTxtUser() {
		return txtUser;
	}

        public JComboBox<UserTypeDTO> getCmbUserType() {
                return cmbUserType;
        }

        /**
         * Resetea todos los campos de filtro a su valor por defecto sin
         * disparar eventos de cambio.
         */
        public void clear() {
                spnUserId.setValue(0);
                txtName.setText("");
                txtLastName.setText("");
                txtSecondLastName.setText("");
                txtEmail.setText("");
                txtUser.setText("");
                cmbUserType.setSelectedIndex(-1);
        }
}

/**
 * Listener simplificado para DocumentListener que solo necesita un método:
 */
@FunctionalInterface
interface SimpleDocumentListener extends javax.swing.event.DocumentListener {
	void update(javax.swing.event.DocumentEvent e);

	@Override
	default void insertUpdate(javax.swing.event.DocumentEvent e) {
		update(e);
	}

	@Override
	default void removeUpdate(javax.swing.event.DocumentEvent e) {
		update(e);
	}

	@Override
	default void changedUpdate(javax.swing.event.DocumentEvent e) {
		update(e);
	}
}
