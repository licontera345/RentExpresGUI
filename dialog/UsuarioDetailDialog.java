package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.TipoUsuarioDTO;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.TipoUsuarioService;
import com.pinguela.rentexpres.service.UsuarioService;
import com.pinguela.rentexpres.service.impl.TipoUsuarioServiceImpl;
import com.pinguela.rentexpres.service.impl.UsuarioServiceImpl;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

public class UsuarioDetailDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtEmail;
	private JTextField txtUsuario;
	private JTextField txtTipoUsuario;
	private JButton btnCerrar;

	private UsuarioService usuarioService = new UsuarioServiceImpl();
	private TipoUsuarioService tipoUsuarioService = new TipoUsuarioServiceImpl();

        public UsuarioDetailDialog(Frame parent, Integer idUsuario) {
                super(parent, "Detalle Usuario", true);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                initComponents();
		cargarUsuario(idUsuario);
		pack();
		setLocationRelativeTo(parent);
	}

	private void initComponents() {
		getContentPane().setLayout(new MigLayout("wrap 2", "[grow][grow]", "[][grow][]"));

		JLabel lblTitulo = new JLabel("Detalle Usuario");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitulo.setForeground(new Color(45, 45, 45));
		getContentPane().add(lblTitulo, "span, align center, gapbottom 15");

		// Nombre
		getContentPane().add(new JLabel("Nombre:"), "align label");
		txtNombre = new JTextField(25);
		txtNombre.setEditable(false);
		getContentPane().add(txtNombre, "growx");

		// Apellidos
		getContentPane().add(new JLabel("Apellidos:"), "align label");
		txtApellidos = new JTextField(25);
		txtApellidos.setEditable(false);
		getContentPane().add(txtApellidos, "growx");

		// Email
		getContentPane().add(new JLabel("Email:"), "align label");
		txtEmail = new JTextField(25);
		txtEmail.setEditable(false);
		getContentPane().add(txtEmail, "growx");

		// Usuario (login)
		getContentPane().add(new JLabel("Usuario:"), "align label");
		txtUsuario = new JTextField(25);
		txtUsuario.setEditable(false);
		getContentPane().add(txtUsuario, "growx");

		// Tipo Usuario
		getContentPane().add(new JLabel("Tipo Usuario:"), "align label");
		txtTipoUsuario = new JTextField(25);
		txtTipoUsuario.setEditable(false);
		getContentPane().add(txtTipoUsuario, "growx");

		// Botón cerrar
		btnCerrar = new JButton("Cerrar");
		getContentPane().add(btnCerrar, "span, align center, gapy 15");
               btnCerrar.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               dispose();
                       }
               });
	}

	private void cargarUsuario(Integer id) {
		try {
			UsuarioDTO dto = usuarioService.findById(id);
			if (dto == null) {
				SwingUtils.showWarning(this, "Usuario no encontrado.");
				dispose();
				return;
			}
                        txtNombre.setText(dto.getNombre());
                        txtApellidos.setText(dto.getApellido1() + " " + dto.getApellido2());
                        txtEmail.setText(dto.getEmail());
			txtUsuario.setText(dto.getNombreUsuario());

			TipoUsuarioDTO tipo = tipoUsuarioService.findById(dto.getIdTipoUsuario());
			txtTipoUsuario.setText(tipo != null ? tipo.getNombreTipo() : "–");
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error al cargar datos de Usuario: " + ex.getMessage());
			dispose();
		}
	}
}
