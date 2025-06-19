package com.pinguela.rentexpres.desktop.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.pinguela.rentexpres.desktop.util.AppIcons;


public class LoginFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;

        private final JTextField txtUsername = new JTextField(20);
        private final JPasswordField txtPassword = new JPasswordField(20);
        private final JCheckBox chkRemember = new JCheckBox("Recordarme");
        private final JCheckBox chkShowPass = new JCheckBox("Mostrar contraseña");
        private final char defaultEcho = txtPassword.getEchoChar();

	public LoginFormPanel() {
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(20, 20, 20, 20));
		setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

                JLabel lblUser = new JLabel("Usuario");
                lblUser.setFont(lblUser.getFont().deriveFont(Font.PLAIN, 14f));
                gbc.gridx = 0;
                gbc.gridy = 0;
                add(lblUser, gbc);

                txtUsername.setFont(txtUsername.getFont().deriveFont(14f));
                txtUsername.putClientProperty("JTextField.placeholderText", "Nombre de usuario");
                txtUsername.putClientProperty("JTextField.leadingIcon", AppIcons.USUARIO);
                gbc.gridx = 0;
                gbc.gridy = 1;
                add(txtUsername, gbc);

                JLabel lblPass = new JLabel("Contraseña");
                lblPass.setFont(lblPass.getFont().deriveFont(Font.PLAIN, 14f));
                gbc.gridx = 0;
                gbc.gridy = 2;
                add(lblPass, gbc);

                txtPassword.setFont(txtPassword.getFont().deriveFont(14f));
                txtPassword.putClientProperty("JTextField.placeholderText", "Contraseña");
                txtPassword.putClientProperty("JTextField.leadingIcon", AppIcons.VIEW);
                gbc.gridx = 0;
                gbc.gridy = 3;
                add(txtPassword, gbc);

                chkRemember.setOpaque(false);
                gbc.gridx = 0;
                gbc.gridy = 4;
                add(chkRemember, gbc);

                chkShowPass.setOpaque(false);
                chkShowPass.addActionListener(e -> {
                        txtPassword.setEchoChar(chkShowPass.isSelected() ? (char)0 : defaultEcho);
                });
                gbc.gridx = 0;
                gbc.gridy = 5;
                add(chkShowPass, gbc);
        }

	public String getUsername() {
		return txtUsername.getText().trim();
	}

        public String getPassword() {
                return new String(txtPassword.getPassword());
        }

        public boolean isRememberSelected() {
                return chkRemember.isSelected();
        }

        public void clear() {
                txtUsername.setText("");
                txtPassword.setText("");
                chkRemember.setSelected(false);
                chkShowPass.setSelected(false);
                txtPassword.setEchoChar(defaultEcho);
        }

        public void clearPassword() {
                txtPassword.setText("");
        }

        public void setUsername(String username) {
                txtUsername.setText(username == null ? "" : username);
        }
}
