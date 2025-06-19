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
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.desktop.util.AppIcons;

import com.pinguela.rentexpres.model.TipoUsuarioDTO;
import com.pinguela.rentexpres.service.TipoUsuarioService;
import com.pinguela.rentexpres.service.impl.TipoUsuarioServiceImpl;

import net.miginfocom.swing.MigLayout;

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
                setBorder(new TitledBorder("Filtros Usuarios"));
                setLayout(new MigLayout("wrap 4", "[right][grow,fill][right][grow,fill]", "[]10[]10[]"));
                setBackground(AppTheme.FILTER_BG);

                txtNombre.putClientProperty("JTextField.placeholderText", "Nombre");
                txtNombre.putClientProperty("JTextField.leadingIcon", AppIcons.CLIENTE);
                txtApellido1.putClientProperty("JTextField.placeholderText", "Apellido 1");
                txtApellido1.putClientProperty("JTextField.leadingIcon", AppIcons.CLIENTE);
                txtApellido2.putClientProperty("JTextField.placeholderText", "Apellido 2");
                txtApellido2.putClientProperty("JTextField.leadingIcon", AppIcons.CLIENTE);
                txtEmail.putClientProperty("JTextField.placeholderText", "Email");
                txtEmail.putClientProperty("JTextField.leadingIcon", AppIcons.VER);
                txtUsuario.putClientProperty("JTextField.placeholderText", "Usuario");
                txtUsuario.putClientProperty("JTextField.leadingIcon", AppIcons.USUARIO);

		// Fila 1: ID y Nombre
                add(lbl("ID:"), "right");
                add(spnIdUsuario, "growx");
                add(lbl("Nombre:"), "right");
                add(txtNombre, "growx, wrap");

		// Fila 2: Apellido1 y Apellido2
                add(lbl("Apellido1:"), "right");
                add(txtApellido1, "growx");
                add(lbl("Apellido2:"), "right");
                add(txtApellido2, "growx, wrap");

		// Fila 3: Email y Usuario (login)
                add(lbl("Email:"), "right");
                add(txtEmail, "growx");
                add(lbl("Usuario (login):"), "right");
                add(txtUsuario, "growx, wrap");

		// Fila 4: TipoUsuario
                add(lbl("Tipo Usuario:"), "right");
                add(cmbTipoUsuario, "span 3, growx");

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
