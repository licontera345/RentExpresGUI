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
import com.pinguela.rentexpres.desktop.util.AppConfig;
import com.pinguela.rentexpres.desktop.dialog.UserEditDialog;
import com.pinguela.rentexpres.desktop.controller.ProfileController;
import com.pinguela.rentexpres.service.impl.UserServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import com.pinguela.rentexpres.model.UserDTO;

import net.miginfocom.swing.MigLayout;

/**
 * Diálogo de Perfil de User, con diseño estilizado. El botón "Cerrar Sesión"
 * aquí solo cierra el diálogo; el logout real se hace desde la barra principal.
 */
public class ProfileView extends JDialog {
	private static final long serialVersionUID = 1L;

	private final JLabel lblAvatar = new JLabel();
	private final JLabel lblName = new JLabel();
        private final JLabel lblEmail = new JLabel();
        private final JLabel lblUser = new JLabel();
        private final JLabel lblPhone = new JLabel();
        private final JLabel lblTipo = new JLabel();
        private final JButton btnEdit = new JButton("Editar", AppIcons.EDIT);
        private final JButton btnClose = new JButton("Cerrar", AppIcons.CLEAR);

        private final Frame parent;
        private final ProfileController controller = new ProfileController(new UserServiceImpl());

        public ProfileView(Frame parent) {
                super(parent, "Perfil de User", true);
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
		if (AppIcons.USER != null) {
			ImageIcon avatarIcon = AppIcons.USER;
			lblAvatar.setIcon(new ImageIcon(avatarIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
		}
		topPanel.add(lblAvatar);
		JLabel lblTitle = new JLabel("Perfil de User");
		lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20f));
		lblTitle.setForeground(new Color(45, 45, 45));
		topPanel.add(lblTitle);
		container.add(topPanel, BorderLayout.NORTH);

		// Panel central: información de user en líneas
                JPanel infoPanel = new JPanel();
                infoPanel.setBackground(Color.WHITE);
                infoPanel.setLayout(new MigLayout("wrap 2", "[right]10[grow,fill]", "[]10[]10[]10[]10[]10[]"));
                infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                lblName.setFont(lblName.getFont().deriveFont(Font.PLAIN, 16f));
                lblName.setForeground(new Color(30, 30, 30));
                lblName.setBorder(new EmptyBorder(0, 0, 0, 0));
                infoPanel.add(new JLabel("Name:"));
                infoPanel.add(lblName);

                lblEmail.setFont(lblEmail.getFont().deriveFont(Font.PLAIN, 16f));
                lblEmail.setForeground(new Color(30, 30, 30));
                lblEmail.setBorder(new EmptyBorder(0, 0, 0, 0));
                infoPanel.add(new JLabel("Email:"));
                infoPanel.add(lblEmail);

                lblUser.setFont(lblUser.getFont().deriveFont(Font.PLAIN, 16f));
                lblUser.setForeground(new Color(30, 30, 30));
                lblUser.setBorder(new EmptyBorder(0, 0, 0, 0));
                infoPanel.add(new JLabel("User:"));
                infoPanel.add(lblUser);

                lblPhone.setFont(lblPhone.getFont().deriveFont(Font.PLAIN, 16f));
                lblPhone.setForeground(new Color(30, 30, 30));
                lblPhone.setBorder(new EmptyBorder(0, 0, 0, 0));
                infoPanel.add(new JLabel("Teléfono:"));
                infoPanel.add(lblPhone);

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
                try {
                        UserDTO u = controller.getCurrentUser();
                        if (u != null) {
                                lblName.setText(u.getName() + " " + u.getLastName() + " " + u.getSecondLastName());
                                lblEmail.setText(u.getEmail());
                                lblUser.setText(u.getUsername());
                                lblPhone.setText(u.getPhone());
                                lblTipo.setText(String.valueOf(u.getUserIdType()));

                                List<String> imgs = controller.getUserImages(u.getId());
                                if (imgs != null && !imgs.isEmpty()) {
                                        Path imgFile = AppConfig.getImageDir().resolve(imgs.get(0));
                                        if (Files.exists(imgFile)) {
                                                ImageIcon avatarIcon = new ImageIcon(new ImageIcon(imgFile.toString()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
                                                lblAvatar.setIcon(avatarIcon);
                                        }
                                }
                        } else {
                                lblName.setText("-");
                                lblEmail.setText("-");
                                lblUser.setText("-");
                                lblPhone.setText("-");
                                lblTipo.setText("-");
                        }
                } catch (Exception ex) {
                        SwingUtils.showError(this, "Error cargando user: " + ex.getMessage());
                }
        }

        private void onClose(ActionEvent e) {
                dispose();
        }

        private void onEdit() {
                UserDTO current = AppContext.getCurrentUser();
                if (current == null) {
                        return;
                }
                UserEditDialog dlg = new UserEditDialog(parent, current.getId());
                dlg.setVisible(true);
                if (dlg.isConfirmed()) {
                        try {
                                UserDTO updated = dlg.getUser();
                                controller.updateUser(updated);
                                loadUserData();
                        } catch (Exception ex) {
                                SwingUtils.showError(this, "No se pudo actualizar el user.");
                        }
                }
        }
}
