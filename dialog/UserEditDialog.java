package com.pinguela.rentexpres.desktop.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collections;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.AppConfig;
import com.pinguela.rentexpres.model.UserTypeDTO;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserTypeService;
import com.pinguela.rentexpres.service.UserService;
import com.pinguela.rentexpres.service.impl.UserTypeServiceImpl;
import com.pinguela.rentexpres.service.impl.UserServiceImpl;

import java.awt.Image;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.ImageIcon;
import java.util.List;

import net.miginfocom.swing.MigLayout;

/**
 * Diálogo para EDITAR un User existente.
 */
public class UserEditDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField txtName;
        private JTextField txtLastName;
        private JTextField txtSecondLastName;
        private JTextField txtPhone;
	private JTextField txtEmail;
	private JTextField txtUser; // username (no editable)
	private JPasswordField txtContrasena; // si el user quiere cambiarla
	private JComboBox<UserTypeDTO> cmbUserType;
        private JButton btnGuardar;
        private JButton btnCancelar;

        private JButton btnSeleccionarImagen;
        private JLabel lblImagenPreview;
        private File imagenSeleccionada;

        private UserService userService = new UserServiceImpl();
        private UserTypeService userTypeService = new UserTypeServiceImpl();
        private boolean confirmed = false;
        private UserDTO user;

	private Integer userId;

        public UserEditDialog(Frame parent, Integer userId) {
                super(parent, "Editar User", true);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                this.userId = userId;
                initComponents();
		cargarTiposUser();
		cargarDatosUser(userId);
		pack();
		setLocationRelativeTo(parent);
	}

	private void initComponents() {
		getContentPane().setLayout(new MigLayout("wrap 2", "[grow][grow]", "[][grow][]"));

		JLabel lblTitulo = new JLabel("Editar User");
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

		// User (login) — no editable
		getContentPane().add(new JLabel("User:"), "align label");
		txtUser = new JTextField(25);
		txtUser.setEditable(false);
		getContentPane().add(txtUser, "growx");

		// Contraseña — opcional; solo si el user la cambia
		getContentPane().add(new JLabel("Contraseña nueva:"), "align label");
		txtContrasena = new JPasswordField(25);
		getContentPane().add(txtContrasena, "growx");

                // Tipo User
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
                                int resp = chooser.showOpenDialog(UserEditDialog.this);
                                if (resp == JFileChooser.APPROVE_OPTION) {
                                        imagenSeleccionada = chooser.getSelectedFile();
                                        javax.swing.ImageIcon ico = new javax.swing.ImageIcon(new javax.swing.ImageIcon(imagenSeleccionada.getAbsolutePath()).getImage().getScaledInstance(120, 90, java.awt.Image.SCALE_SMOOTH));
                                        lblImagenPreview.setIcon(ico);
                                }
                        }
                });

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

	private void cargarTiposUser() {
		try {
			cmbUserType.removeAllItems();
			for (UserTypeDTO t : userTypeService.findAll()) {
				cmbUserType.addItem(t);
			}
		} catch (Exception ex) {
			SwingUtils.showError(this, "No se pudieron cargar los tipos de user.");
		}
	}

	private void cargarDatosUser(Integer id) {
		try {
			UserDTO dto = userService.findById(id);
			if (dto == null) {
				SwingUtils.showWarning(this, "User no encontrado.");
				dispose();
				return;
			}
                        txtName.setText(dto.getName());
                        txtLastName.setText(dto.getLastName());
                        txtSecondLastName.setText(dto.getSecondLastName());
                        txtPhone.setText(dto.getPhone());
                        txtEmail.setText(dto.getEmail());
                        txtUser.setText(dto.getUsername());

                        Integer idTipo = dto.getUserIdType();
                        for (int i = 0; i < cmbUserType.getItemCount(); i++) {
                                if (cmbUserType.getItemAt(i).getId().equals(idTipo)) {
                                        cmbUserType.setSelectedIndex(i);
                                        break;
                                }
                        }

                        List<String> imgs = userService.getUserImages(id);
                        if (imgs != null && !imgs.isEmpty()) {
                                Path imgFile = AppConfig.getImageDir().resolve(imgs.get(0));
                                if (Files.exists(imgFile)) {
                                        ImageIcon ico = new ImageIcon(new ImageIcon(imgFile.toString()).getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH));
                                        lblImagenPreview.setIcon(ico);
                                }
                        }
                } catch (Exception ex) {
			SwingUtils.showError(this, "Error al cargar datos de User: " + ex.getMessage());
			dispose();
		}
	}

	private void onGuardar() {
		// Validaciones mínimas (except user, que no cambia)
                if (txtName.getText().trim().isEmpty() || txtLastName.getText().trim().isEmpty()
                                || txtEmail.getText().trim().isEmpty() || cmbUserType.getSelectedItem() == null) {
                        SwingUtils.showWarning(this, "Name, Apellidos, Email y Tipo User son obligatorios.");
                        return;
                }

                UserDTO dto = new UserDTO();
                dto.setId(userId);
                dto.setName(txtName.getText().trim());
                dto.setLastName(txtLastName.getText().trim());
                dto.setSecondLastName(txtSecondLastName.getText().trim());
                dto.setPhone(txtPhone.getText().trim());
                dto.setEmail(txtEmail.getText().trim());
                if (txtContrasena.getPassword().length > 0) {
                        dto.setContrasena(new String(txtContrasena.getPassword()));
                }

                dto.setUserIdType(((UserTypeDTO) cmbUserType.getSelectedItem()).getId());

                if (imagenSeleccionada != null) {
                        dto.setImagenes(Collections.singletonList(imagenSeleccionada));
                }

                user = dto;
                confirmed = true;
                dispose();
        }

        public UserDTO getUser() {
                return user;
        }

        public boolean isConfirmed() {
                return confirmed;
        }
}
