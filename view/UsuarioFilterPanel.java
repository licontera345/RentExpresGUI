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

import com.pinguela.rentexpres.model.TipoUsuarioDTO;
import com.pinguela.rentexpres.service.TipoUsuarioService;
import com.pinguela.rentexpres.service.impl.TipoUsuarioServiceImpl;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Panel de filtros para buscar Usuarios, adaptado al mismo estilo de
 * AlquilerFilterPanel: - Usa MigLayout - Tiene un borde con título - Se agrega
 * spinner para ID - Cada componente dispara el método fire() al modificarse
 */
public class UsuarioFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JSpinner spnIdUsuario = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JTextField txtNombre = new JTextField(10);
	private final JTextField txtApellido1 = new JTextField(10);
	private final JTextField txtApellido2 = new JTextField(10);
	private final JTextField txtEmail = new JTextField(12);
	private final JTextField txtUsuario = new JTextField(10);
	private final JComboBox<TipoUsuarioDTO> cmbTipoUsuario = new JComboBox<>();

        private final TipoUsuarioService motivoService = new TipoUsuarioServiceImpl();

        private JLabel lbl(String t) {
                JLabel l = new JLabel(t);
                l.setForeground(AppTheme.LABEL_FG);
                return l;
        }

        public UsuarioFilterPanel() {
                setBorder(new CompoundBorder(new TitledBorder("Filtros Usuarios"), new EmptyBorder(12,12,12,12)));
                setLayout(new GridBagLayout());
                setBackground(AppTheme.FILTER_BG);

                txtNombre.putClientProperty("JTextField.placeholderText", "Nombre");
                txtApellido1.putClientProperty("JTextField.placeholderText", "Apellido 1");
                txtApellido2.putClientProperty("JTextField.placeholderText", "Apellido 2");
                txtEmail.putClientProperty("JTextField.placeholderText", "Email");
                txtUsuario.putClientProperty("JTextField.placeholderText", "Usuario");

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(4, 4, 4, 4);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // fila 0
                gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0;
                add(lbl("ID:"), gbc);
                gbc.gridx = 1; gbc.weightx = 1; add(spnIdUsuario, gbc);
                gbc.gridx = 2; gbc.weightx = 0; add(lbl("Nombre:"), gbc);
                gbc.gridx = 3; gbc.weightx = 1; add(txtNombre, gbc);

                // fila 1
                gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0;
                add(lbl("Apellido1:"), gbc);
                gbc.gridx = 1; gbc.weightx = 1; add(txtApellido1, gbc);
                gbc.gridx = 2; gbc.weightx = 0; add(lbl("Apellido2:"), gbc);
                gbc.gridx = 3; gbc.weightx = 1; add(txtApellido2, gbc);

                // fila 2
                gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0;
                add(lbl("Email:"), gbc);
                gbc.gridx = 1; gbc.weightx = 1; add(txtEmail, gbc);
                gbc.gridx = 2; gbc.weightx = 0; add(lbl("Usuario (login):"), gbc);
                gbc.gridx = 3; gbc.weightx = 1; add(txtUsuario, gbc);

                // fila 3
                gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0;
                add(lbl("Tipo Usuario:"), gbc);
                gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1; add(cmbTipoUsuario, gbc);

		cargarTipos();

		// Listeners para disparar evento de filtro en cada cambio
               spnIdUsuario.addChangeListener(new ChangeListener() {
                       @Override
                       public void stateChanged(ChangeEvent e) {
                               fire();
                       }
               });
               txtNombre.getDocument().addDocumentListener(new SimpleDocumentListener() {
                       @Override
                       public void update(DocumentEvent e) {
                               fire();
                       }
               });
               txtApellido1.getDocument().addDocumentListener(new SimpleDocumentListener() {
                       @Override
                       public void update(DocumentEvent e) {
                               fire();
                       }
               });
               txtApellido2.getDocument().addDocumentListener(new SimpleDocumentListener() {
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
               txtUsuario.getDocument().addDocumentListener(new SimpleDocumentListener() {
                       @Override
                       public void update(DocumentEvent e) {
                               fire();
                       }
               });
		cmbTipoUsuario.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fire();
			}
		});
	}

	private void cargarTipos() {
		try {
			List<TipoUsuarioDTO> lista = motivoService.findAll();
			cmbTipoUsuario.removeAllItems();
			for (TipoUsuarioDTO t : lista) {
				cmbTipoUsuario.addItem(t);
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
	public Integer getIdUsuario() {
		return (Integer) spnIdUsuario.getValue();
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

	public String getUsuarioLogin() {
		return txtUsuario.getText().trim();
	}

	public Integer getIdTipoUsuario() {
		TipoUsuarioDTO sel = (TipoUsuarioDTO) cmbTipoUsuario.getSelectedItem();
		return (sel != null) ? sel.getId() : null;
	}

	// (Opcional) Exponer componentes si el controller/otra capa lo necesita:
	public JSpinner getSpnIdUsuario() {
		return spnIdUsuario;
	}

	public JTextField getTxtNombre() {
		return txtNombre;
	}

	public JTextField getTxtApellido1() {
		return txtApellido1;
	}

	public JTextField getTxtApellido2() {
		return txtApellido2;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	public JTextField getTxtUsuario() {
		return txtUsuario;
	}

	public JComboBox<TipoUsuarioDTO> getCmbTipoUsuario() {
		return cmbTipoUsuario;
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
