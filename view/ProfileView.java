// ProfileView.java
package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.desktop.util.AppIcons;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.dialog.UsuarioEditDialog;
import com.pinguela.rentexpres.service.UsuarioService;
import com.pinguela.rentexpres.service.impl.UsuarioServiceImpl;
import com.pinguela.rentexpres.model.UsuarioDTO;

import net.miginfocom.swing.MigLayout;

/**
 * Diálogo de Perfil de Usuario, con diseño estilizado. El botón "Cerrar Sesión"
 * aquí solo cierra el diálogo; el logout real se hace desde la barra principal.
 */
public class ProfileView extends JDialog {
	private static final long serialVersionUID = 1L;

	private final JLabel lblAvatar = new JLabel();
	private final JLabel lblNombre = new JLabel();
        private final JLabel lblEmail = new JLabel();
        private final JLabel lblUsuario = new JLabel();
        private final JLabel lblTelefono = new JLabel();
        private final JLabel lblTipo = new JLabel();
        private final JButton btnEdit = new JButton("Editar", AppIcons.EDIT);
        private final JButton btnClose = new JButton("Cerrar", AppIcons.CLEAR);

        private final Frame parent;
        private final UsuarioService usuarioService = new UsuarioServiceImpl();

        public ProfileView(Frame parent) {
                super(parent, "Perfil de Usuario", true);
                this.parent = parent;
                initComponents();
		loadUserData();
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	private void initComponents() {
		// Contenedor principal con fondo blanco y padding
		JPanel container = new JPanel(new BorderLayout());
		container.setBackground(Color.WHITE);
		container.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(container);

		// Panel superior: avatar y título
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		topPanel.setBackground(Color.WHITE);
		if (AppIcons.USUARIO != null) {
			ImageIcon avatarIcon = AppIcons.USUARIO;
			lblAvatar.setIcon(new ImageIcon(avatarIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
		}
		topPanel.add(lblAvatar);
		JLabel lblTitle = new JLabel("Perfil de Usuario");
		lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20f));
		lblTitle.setForeground(new Color(45, 45, 45));
		topPanel.add(lblTitle);
		container.add(topPanel, BorderLayout.NORTH);

		// Panel central: información de usuario en líneas
                JPanel infoPanel = new JPanel();
                infoPanel.setBackground(Color.WHITE);
                infoPanel.setLayout(new MigLayout("wrap 2", "[right]10[grow,fill]", "[]10[]10[]10[]10[]10[]"));
                infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                lblNombre.setFont(lblNombre.getFont().deriveFont(Font.PLAIN, 16f));
                lblNombre.setForeground(new Color(30, 30, 30));
                lblNombre.setBorder(new EmptyBorder(0, 0, 0, 0));
                infoPanel.add(new JLabel("Nombre:"));
                infoPanel.add(lblNombre);

                lblEmail.setFont(lblEmail.getFont().deriveFont(Font.PLAIN, 16f));
                lblEmail.setForeground(new Color(30, 30, 30));
                lblEmail.setBorder(new EmptyBorder(0, 0, 0, 0));
                infoPanel.add(new JLabel("Email:"));
                infoPanel.add(lblEmail);

                lblUsuario.setFont(lblUsuario.getFont().deriveFont(Font.PLAIN, 16f));
                lblUsuario.setForeground(new Color(30, 30, 30));
                lblUsuario.setBorder(new EmptyBorder(0, 0, 0, 0));
                infoPanel.add(new JLabel("Usuario:"));
                infoPanel.add(lblUsuario);

                lblTelefono.setFont(lblTelefono.getFont().deriveFont(Font.PLAIN, 16f));
                lblTelefono.setForeground(new Color(30, 30, 30));
                lblTelefono.setBorder(new EmptyBorder(0, 0, 0, 0));
                infoPanel.add(new JLabel("Teléfono:"));
                infoPanel.add(lblTelefono);

                lblTipo.setFont(lblTipo.getFont().deriveFont(Font.PLAIN, 16f));
                lblTipo.setForeground(new Color(30, 30, 30));
                lblTipo.setBorder(new EmptyBorder(0, 0, 10, 0));
                infoPanel.add(new JLabel("Tipo:"));
                infoPanel.add(lblTipo);

		// Separator para estética
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(200, 200, 200));
		infoPanel.add(separator);

		container.add(infoPanel, BorderLayout.CENTER);

		// Panel inferior: botón de cerrar diálogo
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		btnPanel.setBackground(Color.WHITE);

		btnClose.setPreferredSize(new Dimension(120, 40));
		btnClose.setFont(btnClose.getFont().deriveFont(Font.PLAIN, 14f));
		btnClose.setFocusPainted(false);
		btnClose.setBackground(new Color(200, 200, 200));
		btnClose.setForeground(Color.DARK_GRAY);
                btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnClose.setToolTipText("Cerrar este diálogo");
                btnClose.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                onClose(e);
                        }
                });

                btnEdit.setPreferredSize(new Dimension(120, 40));
                btnEdit.setFont(btnEdit.getFont().deriveFont(Font.PLAIN, 14f));
                btnEdit.setFocusPainted(false);
                btnEdit.setBackground(new Color(180, 180, 180));
                btnEdit.setForeground(Color.DARK_GRAY);
                btnEdit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnEdit.setToolTipText("Modificar datos");
                btnEdit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                onEdit();
                        }
                });

                btnPanel.add(btnEdit);
                btnPanel.add(btnClose);
                container.add(btnPanel, BorderLayout.SOUTH);

		// Cerrar con la X sin logout
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void loadUserData() {
                UsuarioDTO u = AppContext.getCurrentUser();
                if (u != null) {
                        lblNombre.setText(u.getNombre() + " " + u.getApellido1() + " " + u.getApellido2());
                        lblEmail.setText(u.getEmail());
                        lblUsuario.setText(u.getNombreUsuario());
                        lblTelefono.setText(u.getTelefono());
                        lblTipo.setText(String.valueOf(u.getIdTipoUsuario()));
                } else {
                        lblNombre.setText("-");
                        lblEmail.setText("-");
                        lblUsuario.setText("-");
                        lblTelefono.setText("-");
                        lblTipo.setText("-");
                }
	}

        private void onClose(ActionEvent e) {
                dispose();
        }

        private void onEdit() {
                UsuarioDTO current = AppContext.getCurrentUser();
                if (current == null) {
                        return;
                }
                UsuarioEditDialog dlg = new UsuarioEditDialog(parent, current.getId());
                dlg.setVisible(true);
                if (dlg.isConfirmed()) {
                        try {
                                UsuarioDTO updated = dlg.getUsuario();
                                usuarioService.update(updated);
                                UsuarioDTO refreshed = usuarioService.findById(updated.getId());
                                AppContext.setCurrentUser(refreshed);
                                loadUserData();
                        } catch (Exception ex) {
                                SwingUtils.showError(this, "No se pudo actualizar el usuario.");
                        }
                }
        }
}
