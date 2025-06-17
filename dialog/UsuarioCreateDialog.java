package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.TipoUsuarioDTO;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.TipoUsuarioService;
import com.pinguela.rentexpres.service.impl.TipoUsuarioServiceImpl;

import net.miginfocom.swing.MigLayout;

/**
 * Diálogo para CREAR un nuevo Usuario.
 */
public class UsuarioCreateDialog extends JDialog implements ConfirmDialog<UsuarioDTO> {

	private static final long serialVersionUID = 1L;

	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtEmail;
	private JTextField txtUsuario; // nombreUsuario
	private JPasswordField txtContrasena; // contraseña en claro
	private JComboBox<TipoUsuarioDTO> cmbTipoUsuario;
	private JButton btnGuardar;
	private JButton btnCancelar;

        private TipoUsuarioService tipoUsuarioService = new TipoUsuarioServiceImpl();
        private boolean confirmed = false;
        private UsuarioDTO usuario;

	public UsuarioCreateDialog(Frame parent) {
		super(parent, "Crear Usuario", true);
		initComponents();
		cargarTiposUsuario();
		pack();
		setLocationRelativeTo(parent);
	}

	private void initComponents() {
		getContentPane().setLayout(new MigLayout("wrap 2", "[grow][grow]", "[][grow][]"));

		JLabel lblTitulo = new JLabel("Nuevo Usuario");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitulo.setForeground(new Color(45, 45, 45));
		getContentPane().add(lblTitulo, "span, align center, gapbottom 15");

		// Nombre
		getContentPane().add(new JLabel("Nombre:"), "align label");
		txtNombre = new JTextField(25);
		getContentPane().add(txtNombre, "growx");

		// Apellidos
		getContentPane().add(new JLabel("Apellidos:"), "align label");
		txtApellidos = new JTextField(25);
		getContentPane().add(txtApellidos, "growx");

		// Email
		getContentPane().add(new JLabel("Email:"), "align label");
		txtEmail = new JTextField(25);
		getContentPane().add(txtEmail, "growx");

		// Usuario (nombreUsuario)
		getContentPane().add(new JLabel("Usuario (login):"), "align label");
		txtUsuario = new JTextField(25);
		getContentPane().add(txtUsuario, "growx");

		// Contraseña
		getContentPane().add(new JLabel("Contraseña:"), "align label");
		txtContrasena = new JPasswordField(25);
		getContentPane().add(txtContrasena, "growx");

		// Tipo de Usuario
		getContentPane().add(new JLabel("Tipo Usuario:"), "align label");
		cmbTipoUsuario = new JComboBox<>();
		getContentPane().add(cmbTipoUsuario, "growx");

		// Botones en panel aparte
		JPanel pnlBotones = new JPanel();
		btnGuardar = new JButton("Guardar");
		btnCancelar = new JButton("Cancelar");
		pnlBotones.add(btnGuardar);
		pnlBotones.add(btnCancelar);
		getContentPane().add(pnlBotones, "span, align center, gapy 15");

		// Acciones
		btnGuardar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onGuardar();
			}
		});
               btnCancelar.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               dispose();
                       }
               });
	}

	private void cargarTiposUsuario() {
		try {
			List<TipoUsuarioDTO> lista = tipoUsuarioService.findAll();
			cmbTipoUsuario.removeAllItems();
			for (TipoUsuarioDTO t : lista) {
				cmbTipoUsuario.addItem(t);
			}
		} catch (Exception ex) {
			SwingUtils.showError(this, "No se pudieron cargar los tipos de usuario.");
		}
	}

	private void onGuardar() {
		// Validaciones básicas
		if (txtNombre.getText().trim().isEmpty() || txtApellidos.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()
				|| txtUsuario.getText().trim().isEmpty() || txtContrasena.getPassword().length == 0
				|| cmbTipoUsuario.getSelectedItem() == null) {
			SwingUtils.showWarning(this, "Todos los campos obligatorios deben estar completos.");
			return;
		}

                UsuarioDTO dto = new UsuarioDTO();
                dto.setNombre(txtNombre.getText().trim());
                dto.setApellido1(txtApellidos.getText().trim());
                dto.setApellido2(txtApellidos.getText().trim());
                dto.setEmail(txtEmail.getText().trim());
                dto.setNombreUsuario(txtUsuario.getText().trim());
                dto.setContrasena(new String(txtContrasena.getPassword()));
                dto.setIdTipoUsuario(((TipoUsuarioDTO) cmbTipoUsuario.getSelectedItem()).getId());

                usuario = dto;
                confirmed = true;
                dispose();
        }

        public boolean isConfirmed() {
                return confirmed;
        }

        public UsuarioDTO getUsuario() {
                return usuario;
        }

        @Override
        public UsuarioDTO getValue() {
                return getUsuario();
        }
}
