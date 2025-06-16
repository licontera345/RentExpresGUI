package com.pinguela.rentexpres.desktop;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.pinguela.rentexpres.desktop.controller.LogoutAction;
import com.pinguela.rentexpres.desktop.dialog.LoginDialog;
import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.desktop.util.AppIcons;
import com.pinguela.rentexpres.desktop.view.AlquilerSearchView;
import com.pinguela.rentexpres.desktop.view.ClienteSearchView;
import com.pinguela.rentexpres.desktop.view.ProfileView;
import com.pinguela.rentexpres.desktop.view.ReservaSearchView;
import com.pinguela.rentexpres.desktop.view.VehiculoSearchView;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.AlquilerService;
import com.pinguela.rentexpres.service.CategoriaVehiculoService;
import com.pinguela.rentexpres.service.ClienteService;
import com.pinguela.rentexpres.service.EstadoAlquilerService;
import com.pinguela.rentexpres.service.EstadoVehiculoService;
import com.pinguela.rentexpres.service.LocalidadService;
import com.pinguela.rentexpres.service.ProvinciaService;
import com.pinguela.rentexpres.service.VehiculoService;
import com.pinguela.rentexpres.service.impl.AlquilerServiceImpl;
import com.pinguela.rentexpres.service.impl.CategoriaVehiculoServiceImpl;
import com.pinguela.rentexpres.service.impl.ClienteServiceImpl;
import com.pinguela.rentexpres.service.impl.EstadoAlquilerServiceImpl;
import com.pinguela.rentexpres.service.impl.EstadoReservaServiceImpl;
import com.pinguela.rentexpres.service.impl.EstadoVehiculoServiceImpl;
import com.pinguela.rentexpres.service.impl.LocalidadServiceImpl;
import com.pinguela.rentexpres.service.impl.ProvinciaServiceImpl;
import com.pinguela.rentexpres.service.impl.ReservaServiceImpl;
import com.pinguela.rentexpres.service.impl.VehiculoServiceImpl;

public class RentExpresMainWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private final AlquilerService alquilerService = new AlquilerServiceImpl();
	private final VehiculoService vehiculoService = new VehiculoServiceImpl();
	private final ClienteService clienteService = new ClienteServiceImpl();
	private final LocalidadService localidadService = new LocalidadServiceImpl();
	private final ProvinciaService provinciaService = new ProvinciaServiceImpl();

	private final JPanel navPanel = new JPanel();
	private final JPanel contentPanel = new JPanel(new CardLayout());
	private final JToolBar topBar = new JToolBar();

	public RentExpresMainWindow() throws Exception {
		super("RentExpres");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		if (AppIcons.USUARIO != null) {
			setIconImage(AppIcons.USUARIO.getImage());
		}

		FlatLightLaf.setup();
		UIManager.put("Component.arc", 8);
		UIManager.put("Button.arc", 12);
		UIManager.put("Table.alternateRowColor", new Color(245, 249, 254));
		UIManager.put("Table.selectionBackground", new Color(0xC7DEF8));
		UIManager.put("Table.selectionForeground", Color.BLACK);

		UsuarioDTO user = showLoginDialog();
		if (user == null) {
			dispose();
			return;
		}
		AppContext.setCurrentUser(user);

		initTopBar();
		initNavigation();
		initContent();

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topBar, BorderLayout.NORTH);
		getContentPane().add(navPanel, BorderLayout.WEST);
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		setMinimumSize(new Dimension(1200, 700));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private UsuarioDTO showLoginDialog() {
		LoginDialog dlg = new LoginDialog(this);
		return dlg.showDialog();
	}

	private void initTopBar() {
		topBar.setFloatable(false);
		topBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 5));
		topBar.setBackground(new Color(245, 245, 245));

		JLabel lblUser = new JLabel("Usuario: " + AppContext.getCurrentUser().getNombreUsuario(), AppIcons.USUARIO,
				JLabel.LEFT);
		lblUser.setFont(lblUser.getFont().deriveFont(Font.PLAIN, 14f));

		JButton btnProfile = new JButton("Perfil", AppIcons.VIEW);
		JButton btnLogout = new JButton("Logout", AppIcons.CLEAR);

		btnProfile.setFocusPainted(false);
		btnProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnProfile.setToolTipText("Ver perfil");
		btnProfile.addActionListener(e -> new ProfileView(this).setVisible(true));

		btnLogout.setFocusPainted(false);
		btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogout.setToolTipText("Cerrar sesión");
		btnLogout.addActionListener(e -> {
			new LogoutAction(this).actionPerformed(null);
			UsuarioDTO newUser = showLoginDialog();
			if (newUser != null) {
				AppContext.setCurrentUser(newUser);
				topBar.removeAll();
				initTopBar();
				revalidate();
				repaint();
			} else {
				dispose();
			}
		});

		topBar.add(Box.createHorizontalGlue());
		topBar.add(lblUser);
		topBar.add(btnProfile);
		topBar.add(btnLogout);
	}

	private void initNavigation() {
		// Colores para la navegación
		Color navBg = new Color(240, 242, 245);
		Color btnBg = new Color(255, 255, 255);
		Color btnHoverBg = new Color(220, 224, 230);
		Color btnFg = new Color(45, 52, 70);

		navPanel.setBackground(navBg);
		navPanel.setPreferredSize(new Dimension(200, getHeight()));
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));

		navPanel.add(Box.createVerticalStrut(20));
		navPanel.add(createNavButton("Inicio", AppIcons.INICIO, btnBg, btnHoverBg, btnFg));
		navPanel.add(Box.createVerticalStrut(10));
		navPanel.add(createNavButton("Reservas", AppIcons.RESERVA, btnBg, btnHoverBg, btnFg));
		navPanel.add(Box.createVerticalStrut(10));
		navPanel.add(createNavButton("Alquileres", AppIcons.ALQUILER, btnBg, btnHoverBg, btnFg));
		navPanel.add(Box.createVerticalStrut(10));
		navPanel.add(createNavButton("Clientes", AppIcons.CLIENTE, btnBg, btnHoverBg, btnFg));
		navPanel.add(Box.createVerticalStrut(10));

		if (AppContext.getCurrentUser().getIdTipoUsuario() == 1) {
			navPanel.add(createNavButton("Vehículos", AppIcons.VEHICULO, btnBg, btnHoverBg, btnFg));
		}
		navPanel.add(Box.createVerticalGlue());
	}

	private JButton createNavButton(String text, ImageIcon icon, Color bg, Color hoverBg, Color fg) {
		JButton btn = new JButton(text, icon);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setMaximumSize(new Dimension(180, 50));
		btn.setPreferredSize(new Dimension(180, 50));
		btn.setFocusPainted(false);
		btn.setBackground(bg);
		btn.setForeground(fg);
		btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
		btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setToolTipText(text);

		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btn.setBackground(hoverBg);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btn.setBackground(bg);
			}
		});

		CardLayout cl = (CardLayout) contentPanel.getLayout();
		btn.addActionListener(e -> cl.show(contentPanel, text));
		return btn;
	}

	private void initContent() throws Exception {
		JPanel inicioPanel = new JPanel(new BorderLayout());
		inicioPanel.setBackground(Color.WHITE);
		JLabel lblWelcome = new JLabel("Bienvenido a RentExpres", SwingConstants.CENTER);
		lblWelcome.setFont(lblWelcome.getFont().deriveFont(Font.BOLD, 24f));
		lblWelcome.setForeground(new Color(33, 150, 243));
		lblWelcome.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		inicioPanel.add(lblWelcome, BorderLayout.NORTH);

		if (AppIcons.INICIO != null) {
			ImageIcon ic = AppIcons.INICIO;
			Image scaled = ic.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
			JLabel lblIcon = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
			inicioPanel.add(lblIcon, BorderLayout.CENTER);
		}

		contentPanel.add(inicioPanel, "Inicio");

		ReservaSearchView rsv = new ReservaSearchView(new ReservaServiceImpl(), new EstadoReservaServiceImpl(),
				vehiculoService, this);
		rsv.initIfNeeded();
		contentPanel.add(rsv, "Reservas");

		AlquilerSearchView asv = new AlquilerSearchView(alquilerService, estadoAlqService(), vehiculoService, this);
		contentPanel.add(asv, "Alquileres");

		ClienteSearchView csv = new ClienteSearchView(clienteService, provinciaService, localidadService, this);
		csv.initIfNeeded();
		contentPanel.add(csv, "Clientes");

		// Solo administradores
		if (AppContext.getCurrentUser().getIdTipoUsuario() == 1) {
			VehiculoSearchView vsv = new VehiculoSearchView(vehiculoService, catVehService(), estadoVehService(), this);
			vsv.initIfNeeded();
			contentPanel.add(vsv, "Vehículos");
		}
	}

	private EstadoAlquilerService estadoAlqService() {
		return new EstadoAlquilerServiceImpl();
	}

	private CategoriaVehiculoService catVehService() {
		return new CategoriaVehiculoServiceImpl();
	}

	private EstadoVehiculoService estadoVehService() {
		return new EstadoVehiculoServiceImpl();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				new RentExpresMainWindow();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
