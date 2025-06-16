package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.TipoUsuarioDTO;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.TipoUsuarioService;
import com.pinguela.rentexpres.service.UsuarioService;
import com.pinguela.rentexpres.service.impl.TipoUsuarioServiceImpl;
import com.pinguela.rentexpres.service.impl.UsuarioServiceImpl;

import net.miginfocom.swing.MigLayout;

/**
 * Diálogo para EDITAR un Usuario existente.
 */
public class UsuarioEditDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtEmail;
	private JTextField txtUsuario; // nombreUsuario (no editable)
	private JPasswordField txtContrasena; // si el usuario quiere cambiarla
	private JComboBox<TipoUsuarioDTO> cmbTipoUsuario;
	private JButton btnGuardar;
	private JButton btnCancelar;

	private UsuarioService usuarioService = new UsuarioServiceImpl();
	private TipoUsuarioService tipoUsuarioService = new TipoUsuarioServiceImpl();

	private Integer idUsuario;

	public UsuarioEditDialog(Frame parent, Integer idUsuario) {
		super(parent, "Editar Usuario", true);
		this.idUsuario = idUsuario;
		initComponents();
		cargarTiposUsuario();
		cargarDatosUsuario(idUsuario);
		pack();
		setLocationRelativeTo(parent);
	}

	private void initComponents() {
		getContentPane().setLayout(new MigLayout("wrap 2", "[grow][grow]", "[][grow][]"));

		JLabel lblTitulo = new JLabel("Editar Usuario");
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

		// Usuario (login) — no editable
		getContentPane().add(new JLabel("Usuario:"), "align label");
		txtUsuario = new JTextField(25);
		txtUsuario.setEditable(false);
		getContentPane().add(txtUsuario, "growx");

		// Contraseña — opcional; solo si el usuario la cambia
		getContentPane().add(new JLabel("Contraseña nueva:"), "align label");
		txtContrasena = new JPasswordField(25);
		getContentPane().add(txtContrasena, "growx");

		// Tipo Usuario
		getContentPane().add(new JLabel("Tipo Usuario:"), "align label");
		cmbTipoUsuario = new JComboBox<>();
		getContentPane().add(cmbTipoUsuario, "growx");

		// Botones
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
			cmbTipoUsuario.removeAllItems();
			for (TipoUsuarioDTO t : tipoUsuarioService.findAll()) {
				cmbTipoUsuario.addItem(t);
			}
		} catch (Exception ex) {
			SwingUtils.showError(this, "No se pudieron cargar los tipos de usuario.");
		}
	}

	private void cargarDatosUsuario(Integer id) {
		try {
			UsuarioDTO dto = usuarioService.findById(id);
			if (dto == null) {
				SwingUtils.showWarning(this, "Usuario no encontrado.");
				dispose();
				return;
			}
			txtNombre.setText(dto.getNombre());
			txtApellidos.setText(dto.getApellido1());
			txtApellidos.setText(dto.getApellido2());
			txtEmail.setText(dto.getEmail());
			txtUsuario.setText(dto.getNombreUsuario());

			Integer idTipo = dto.getIdTipoUsuario();
			for (int i = 0; i < cmbTipoUsuario.getItemCount(); i++) {
				if (cmbTipoUsuario.getItemAt(i).getId().equals(idTipo)) {
					cmbTipoUsuario.setSelectedIndex(i);
					break;
				}
			}
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error al cargar datos de Usuario: " + ex.getMessage());
			dispose();
		}
	}

	private void onGuardar() {
		// Validaciones mínimas (except usuario, que no cambia)
		if (txtNombre.getText().trim().isEmpty() || txtApellidos.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()
				|| cmbTipoUsuario.getSelectedItem() == null) {
			SwingUtils.showWarning(this, "Nombre, Apellidos, Email y Tipo Usuario son obligatorios.");
			return;
		}

		try {
			UsuarioDTO dto = new UsuarioDTO();
			dto.setId(idUsuario);
			dto.setNombre(txtNombre.getText().trim());
			dto.setApellido1(txtApellidos.getText().trim());
			dto.setApellido2(txtApellidos.getText().trim());
			dto.setEmail(txtEmail.getText().trim());
			if (txtContrasena.getPassword().length > 0) {
				dto.setContrasena(new String(txtContrasena.getPassword()));
			}

			dto.setIdTipoUsuario(((TipoUsuarioDTO) cmbTipoUsuario.getSelectedItem()).getId());

			boolean modificado = usuarioService.update(dto);
			if (modificado) {
				SwingUtils.showInfo(this, "Usuario actualizado correctamente.");
				dispose();
			} else {
				SwingUtils.showError(this, "No se pudo actualizar el Usuario.");
			}
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error al actualizar Usuario: " + ex.getMessage());
		}
	}

	public UsuarioDTO getUsuario() {
		// TODO Auto-generated method stub
		return null;
	}
}
