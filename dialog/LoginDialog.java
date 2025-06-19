package com.pinguela.rentexpres.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.pinguela.rentexpres.desktop.util.AppIcons;
import com.pinguela.rentexpres.desktop.util.AuthService;
import com.pinguela.rentexpres.desktop.util.AuthServiceImpl;
import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.GradientPanel;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.desktop.view.LoginFormPanel;
import com.pinguela.rentexpres.model.UsuarioDTO;

public class LoginDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private final LoginFormPanel formPanel = new LoginFormPanel();
	private final JButton btnIngresar = new JButton("Ingresar");
	private final JButton btnCancelar = new JButton("Cancelar");

        private UsuarioDTO authenticatedUser = null;
        private boolean rememberUser = false;
	private final AuthService authService = new AuthServiceImpl();

	public LoginDialog(Frame parent) {
		super(parent, "Bienvenido a RentExpres", true);
		initComponents();
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	private void initComponents() {
                GradientPanel container = new GradientPanel(AppTheme.LOGIN_GRADIENT_START,
                                AppTheme.LOGIN_GRADIENT_END);
                container.setLayout(new BorderLayout());
                container.setBorder(new EmptyBorder(20, 20, 20, 20));
                getContentPane().add(container);

                JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                topPanel.setOpaque(false);

                JLabel lblTitle = new JLabel("¡Bienvenido a RentExpres!");
                lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 22f));
                lblTitle.setForeground(new Color(45, 45, 45));

                if (AppIcons.ALQUILER != null) {
                        ImageIcon vehicleIcon = AppIcons.ALQUILER;
                        Image scaled = vehicleIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        JLabel lblIcon = new JLabel(new ImageIcon(scaled));
                        topPanel.add(lblIcon);
                }

                topPanel.add(lblTitle);
                container.add(topPanel, BorderLayout.NORTH);

                JPanel centerPanel = new JPanel(new BorderLayout());
                centerPanel.setOpaque(false);
                centerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
                centerPanel.add(formPanel, BorderLayout.CENTER);
                container.add(centerPanel, BorderLayout.CENTER);

                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
                btnPanel.setOpaque(false);

		btnIngresar.setPreferredSize(new Dimension(120, 40));
		btnIngresar.setFont(btnIngresar.getFont().deriveFont(Font.PLAIN, 14f));
		btnIngresar.setFocusPainted(false);
                btnIngresar.setBackground(AppTheme.PRIMARY);
                btnIngresar.setForeground(Color.WHITE);
                btnIngresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnCancelar.setPreferredSize(new Dimension(120, 40));
		btnCancelar.setFont(btnCancelar.getFont().deriveFont(Font.PLAIN, 14f));
		btnCancelar.setFocusPainted(false);
                btnCancelar.setBackground(AppTheme.CANCEL);
                btnCancelar.setForeground(AppTheme.CANCEL_FG);
		btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
               btnCancelar.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               authenticatedUser = null;
                               dispose();
                       }
               });


               // botón Ingresar
               btnIngresar.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               onIngresar(e);
                       }
               });

               btnPanel.add(btnIngresar);
               btnPanel.add(btnCancelar);

		container.add(btnPanel, BorderLayout.SOUTH);

		for (Component comp : formPanel.getComponents()) {
			comp.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						onIngresar(null);
					}
				}
			});
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

       private void onIngresar(ActionEvent e) {
               String username = formPanel.getUsername();
               String password = formPanel.getPassword();

               if (username.trim().isEmpty() || password.trim().isEmpty()) {
                       SwingUtils.showWarning(this, "Debe ingresar usuario y contraseña.");
                       return;
               }

               try {
                       UsuarioDTO user = authService.authenticate(username, password);
                       if (user == null) {
                               SwingUtils.showError(this, "Credenciales incorrectas.");
                               formPanel.clearPassword();
                               return;
                       }
                       authenticatedUser = user;
                       AppContext.setCurrentUser(user);
                       rememberUser = formPanel.isRememberSelected();
                       if (rememberUser) {
                               AppContext.setRememberedUser(username);
                       } else {
                               AppContext.setRememberedUser(null);
                       }
                       dispose();
               } catch (Exception ex) {
                       SwingUtils.showError(this, "Error al autenticar: " + ex.getMessage());
               }
       }

       public UsuarioDTO showDialog() {
               formPanel.clear();
               formPanel.setUsername(AppContext.getRememberedUser());
               setVisible(true);
               return authenticatedUser;
       }
}
