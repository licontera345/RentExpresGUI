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
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pinguela.rentexpres.desktop.controller.LogoutAction;
import com.pinguela.rentexpres.desktop.controller.RoleController;
import com.pinguela.rentexpres.desktop.dialog.LoginDialog;
import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.desktop.util.AppIcons;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.view.AlquilerSearchView;
import com.pinguela.rentexpres.desktop.view.ClienteSearchView;
import com.pinguela.rentexpres.desktop.view.ProfileView;
import com.pinguela.rentexpres.desktop.view.ReservaSearchView;
import com.pinguela.rentexpres.desktop.view.VehiculoSearchView;
import com.pinguela.rentexpres.desktop.view.CalendarView;
import com.pinguela.rentexpres.desktop.view.UsuarioSearchView;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.AlquilerService;
import com.pinguela.rentexpres.service.CategoriaVehiculoService;
import com.pinguela.rentexpres.service.ClienteService;
import com.pinguela.rentexpres.service.EstadoAlquilerService;
import com.pinguela.rentexpres.service.EstadoVehiculoService;
import com.pinguela.rentexpres.service.LocalidadService;
import com.pinguela.rentexpres.service.ProvinciaService;
import com.pinguela.rentexpres.service.UsuarioService;
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
import com.pinguela.rentexpres.service.impl.UsuarioServiceImpl;
import com.pinguela.rentexpres.service.impl.VehiculoServiceImpl;

public class RentExpresMainWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private final AlquilerService alquilerService = new AlquilerServiceImpl();
        private final VehiculoService vehiculoService = new VehiculoServiceImpl();
        private final ClienteService clienteService = new ClienteServiceImpl();
        private final LocalidadService localidadService = new LocalidadServiceImpl();
        private final ProvinciaService provinciaService = new ProvinciaServiceImpl();
        private final UsuarioService usuarioService = new UsuarioServiceImpl();

	private final JPanel navPanel = new JPanel();
	private final JPanel contentPanel = new JPanel(new CardLayout());
	private final JToolBar topBar = new JToolBar();

	public RentExpresMainWindow() throws Exception {
		super("RentExpres");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		if (AppIcons.USUARIO != null) {
			setIconImage(AppIcons.USUARIO.getImage());
		}

               AppTheme.setup();

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
        topBar.setBackground(AppTheme.TOPBAR_BG);

		JLabel lblUser = new JLabel("Usuario: " + AppContext.getCurrentUser().getNombreUsuario(), AppIcons.USUARIO,
				JLabel.LEFT);
		lblUser.setFont(lblUser.getFont().deriveFont(Font.PLAIN, 14f));

		JButton btnProfile = new JButton("Perfil", AppIcons.VIEW);
		JButton btnLogout = new JButton("Logout", AppIcons.CLEAR);

		btnProfile.setFocusPainted(false);
		btnProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnProfile.setToolTipText("Ver perfil");
               btnProfile.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               new ProfileView(RentExpresMainWindow.this).setVisible(true);
                       }
               });

		btnLogout.setFocusPainted(false);
		btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogout.setToolTipText("Cerrar sesión");
               btnLogout.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               new LogoutAction(RentExpresMainWindow.this).actionPerformed(null);
                               UsuarioDTO current = AppContext.getCurrentUser();
                               if (current != null) {
                                       topBar.removeAll();
                                       initTopBar();
                                       revalidate();
                                       repaint();
                               } else {
                                       dispose();
                               }
                       }
               });

		topBar.add(Box.createHorizontalGlue());
		topBar.add(lblUser);
		topBar.add(btnProfile);
		topBar.add(btnLogout);
	}

	private void initNavigation() {
        // Colores para la navegación
        Color navBg = AppTheme.NAV_BG;
        Color btnBg = AppTheme.NAV_BTN_BG;
        Color btnHoverBg = AppTheme.NAV_BTN_HOVER_BG;
        Color btnFg = AppTheme.NAV_BTN_FG;

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
                navPanel.add(createNavButton("Calendario", AppIcons.RESERVA, btnBg, btnHoverBg, btnFg));
                navPanel.add(Box.createVerticalStrut(10));
                navPanel.add(createNavButton("Clientes", AppIcons.CLIENTE, btnBg, btnHoverBg, btnFg));
                navPanel.add(Box.createVerticalStrut(10));

                if (RoleController.isAdmin()) {
                        navPanel.add(createNavButton("Usuarios", AppIcons.USUARIO, btnBg, btnHoverBg, btnFg));
                        navPanel.add(Box.createVerticalStrut(10));
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
               btn.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               cl.show(contentPanel, text);
                       }
               });
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
                asv.initIfNeeded();
                contentPanel.add(asv, "Alquileres");

                CalendarView calView = new CalendarView();
                contentPanel.add(calView, "Calendario");

                ClienteSearchView csv = new ClienteSearchView(clienteService, provinciaService, localidadService, this);
                csv.initIfNeeded();
                contentPanel.add(csv, "Clientes");

                // Solo administradores
                if (RoleController.isAdmin()) {
                        UsuarioSearchView usv = new UsuarioSearchView(usuarioService, this);
                        usv.initIfNeeded();
                        contentPanel.add(usv, "Usuarios");

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
               System.setProperty("log4j.configurationFile", "config/log4j2.properties");
               com.pinguela.rentexpres.desktop.util.SwingUtils.invokeLater(new ActionCallback() {
                       @Override
                       public void execute() {
                               try {
                                       new RentExpresMainWindow();
                               } catch (Exception e) {
                                       e.printStackTrace();
                               }
                       }
               });
       }
}
