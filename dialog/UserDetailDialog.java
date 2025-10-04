package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.UserTypeDTO;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserTypeService;
import com.pinguela.rentexpres.service.UserService;
import com.pinguela.rentexpres.service.impl.UserTypeServiceImpl;
import com.pinguela.rentexpres.service.impl.UserServiceImpl;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

public class UserDetailDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField txtName;
	private JTextField txtApellidos;
	private JTextField txtEmail;
	private JTextField txtUser;
	private JTextField txtUserType;
	private JButton btnCerrar;

	private UserService userService = new UserServiceImpl();
	private UserTypeService userTypeService = new UserTypeServiceImpl();

        public UserDetailDialog(Frame parent, Integer userId) {
                super(parent, "Detalle User", true);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                initComponents();
		cargarUser(userId);
		pack();
		setLocationRelativeTo(parent);
	}

	private void initComponents() {
		getContentPane().setLayout(new MigLayout("wrap 2", "[grow][grow]", "[][grow][]"));

		JLabel lblTitulo = new JLabel("Detalle User");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitulo.setForeground(new Color(45, 45, 45));
		getContentPane().add(lblTitulo, "span, align center, gapbottom 15");

		// Name
		getContentPane().add(new JLabel("Name:"), "align label");
		txtName = new JTextField(25);
		txtName.setEditable(false);
		getContentPane().add(txtName, "growx");

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

		// User (login)
		getContentPane().add(new JLabel("User:"), "align label");
		txtUser = new JTextField(25);
		txtUser.setEditable(false);
		getContentPane().add(txtUser, "growx");

		// Tipo User
		getContentPane().add(new JLabel("Tipo User:"), "align label");
		txtUserType = new JTextField(25);
		txtUserType.setEditable(false);
		getContentPane().add(txtUserType, "growx");

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

	private void cargarUser(Integer id) {
		try {
			UserDTO dto = userService.findById(id);
			if (dto == null) {
				SwingUtils.showWarning(this, "User no encontrado.");
				dispose();
				return;
			}
                        txtName.setText(dto.getName());
                        txtApellidos.setText(dto.getLastName() + " " + dto.getSecondLastName());
                        txtEmail.setText(dto.getEmail());
			txtUser.setText(dto.getUsername());

			UserTypeDTO tipo = userTypeService.findById(dto.getUserIdType());
			txtUserType.setText(tipo != null ? tipo.getNameTipo() : "–");
		} catch (Exception ex) {
			SwingUtils.showError(this, "Error al cargar datos de User: " + ex.getMessage());
			dispose();
		}
	}
}
