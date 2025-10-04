package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.UserTypeDTO;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserTypeService;
import com.pinguela.rentexpres.service.impl.UserTypeServiceImpl;

import net.miginfocom.swing.MigLayout;

/**
 * Diálogo para CREAR un nuevo User.
 */
public class UserCreateDialog extends JDialog implements ConfirmDialog<UserDTO> {

	private static final long serialVersionUID = 1L;

	private JTextField txtName;
        private JTextField txtLastName;
        private JTextField txtSecondLastName;
        private JTextField txtPhone;
	private JTextField txtEmail;
	private JTextField txtUser; // username
	private JPasswordField txtContrasena; // contraseña en claro
	private JComboBox<UserTypeDTO> cmbUserType;
        private JButton btnGuardar;
        private JButton btnCancelar;

        private JButton btnSeleccionarImagen;
        private JLabel lblImagenPreview;
        private File imagenSeleccionada;

        private UserTypeService userTypeService = new UserTypeServiceImpl();
        private boolean confirmed = false;
        private UserDTO user;

        public UserCreateDialog(Frame parent) {
                super(parent, "Crear User", true);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                initComponents();
		cargarTiposUser();
		pack();
		setLocationRelativeTo(parent);
	}

	private void initComponents() {
		getContentPane().setLayout(new MigLayout("wrap 2", "[grow][grow]", "[][grow][]"));

		JLabel lblTitulo = new JLabel("Nuevo User");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitulo.setForeground(new Color(45, 45, 45));
		getContentPane().add(lblTitulo, "span, align center, gapbottom 15");

		// Name
		getContentPane().add(new JLabel("Name:"), "align label");
		txtName = new JTextField(25);
		getContentPane().add(txtName, "growx");

                // LastName
                getContentPane().add(new JLabel("1.º Apellido:"), "align label");
                txtLastName = new JTextField(25);
                getContentPane().add(txtLastName, "growx, wrap");

                // SecondLastName
                getContentPane().add(new JLabel("2.º Apellido:"), "align label");
                txtSecondLastName = new JTextField(25);
                getContentPane().add(txtSecondLastName, "growx");

                // Teléfono
                getContentPane().add(new JLabel("Teléfono:"), "align label");
                txtPhone = new JTextField(15);
                getContentPane().add(txtPhone, "growx");

		// Email
		getContentPane().add(new JLabel("Email:"), "align label");
		txtEmail = new JTextField(25);
		getContentPane().add(txtEmail, "growx");

		// User (username)
		getContentPane().add(new JLabel("User (login):"), "align label");
		txtUser = new JTextField(25);
		getContentPane().add(txtUser, "growx");

		// Contraseña
		getContentPane().add(new JLabel("Contraseña:"), "align label");
		txtContrasena = new JPasswordField(25);
		getContentPane().add(txtContrasena, "growx");

                // Tipo de User
                getContentPane().add(new JLabel("Tipo User:"), "align label");
                cmbUserType = new JComboBox<>();
                cmbUserType.setRenderer(new com.pinguela.rentexpres.desktop.renderer.UserTypeRenderer());
                getContentPane().add(cmbUserType, "growx");

                // Imagen de perfil
                getContentPane().add(new JLabel("Imagen:"), "align label");
                btnSeleccionarImagen = new JButton("Seleccionar Imagen");
                lblImagenPreview = new JLabel();
                lblImagenPreview.setPreferredSize(new java.awt.Dimension(120, 90));
                JPanel imgPanel = new JPanel(new java.awt.BorderLayout());
                imgPanel.add(btnSeleccionarImagen, java.awt.BorderLayout.WEST);
                imgPanel.add(lblImagenPreview, java.awt.BorderLayout.CENTER);
                getContentPane().add(imgPanel, "growx, wrap");

                btnSeleccionarImagen.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                JFileChooser chooser = new JFileChooser();
                                chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));
                                int resp = chooser.showOpenDialog(UserCreateDialog.this);
                                if (resp == JFileChooser.APPROVE_OPTION) {
                                        imagenSeleccionada = chooser.getSelectedFile();
                                        javax.swing.ImageIcon ico = new javax.swing.ImageIcon(new javax.swing.ImageIcon(imagenSeleccionada.getAbsolutePath()).getImage().getScaledInstance(120, 90, java.awt.Image.SCALE_SMOOTH));
                                        lblImagenPreview.setIcon(ico);
                                }
                        }
                });

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

	private void cargarTiposUser() {
		try {
			List<UserTypeDTO> lista = userTypeService.findAll();
			cmbUserType.removeAllItems();
			for (UserTypeDTO t : lista) {
				cmbUserType.addItem(t);
			}
		} catch (Exception ex) {
			SwingUtils.showError(this, "No se pudieron cargar los tipos de user.");
		}
	}

	private void onGuardar() {
		// Validaciones básicas
                if (txtName.getText().trim().isEmpty() || txtLastName.getText().trim().isEmpty()
                                || txtEmail.getText().trim().isEmpty() || txtUser.getText().trim().isEmpty()
                                || txtContrasena.getPassword().length == 0 || cmbUserType.getSelectedItem() == null) {
                        SwingUtils.showWarning(this, "Todos los campos obligatorios deben estar completos.");
                        return;
                }

                UserDTO dto = new UserDTO();
                dto.setName(txtName.getText().trim());
                dto.setLastName(txtLastName.getText().trim());
                dto.setSecondLastName(txtSecondLastName.getText().trim());
                dto.setEmail(txtEmail.getText().trim());
                dto.setPhone(txtPhone.getText().trim());
                dto.setUsername(txtUser.getText().trim());
                dto.setContrasena(new String(txtContrasena.getPassword()));
                dto.setUserIdType(((UserTypeDTO) cmbUserType.getSelectedItem()).getId());

                if (imagenSeleccionada != null) {
                        dto.setImagenes(Collections.singletonList(imagenSeleccionada));
                }

                user = dto;
                confirmed = true;
                dispose();
        }

        public boolean isConfirmed() {
                return confirmed;
        }

        public UserDTO getUser() {
                return user;
        }

        @Override
        public UserDTO getValue() {
                return getUser();
        }
}
