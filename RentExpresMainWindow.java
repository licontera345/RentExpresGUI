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
import com.pinguela.rentexpres.desktop.view.RentalSearchView;
import com.pinguela.rentexpres.desktop.view.CustomerSearchView;
import com.pinguela.rentexpres.desktop.view.ProfileView;
import com.pinguela.rentexpres.desktop.view.ReservationSearchView;
import com.pinguela.rentexpres.desktop.view.VehicleSearchView;
import com.pinguela.rentexpres.desktop.view.CalendarView;
import com.pinguela.rentexpres.desktop.view.UserSearchView;
import com.pinguela.rentexpres.desktop.view.StatisticsView;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.RentalService;
import com.pinguela.rentexpres.service.StatisticsService;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.CustomerService;
import com.pinguela.rentexpres.service.RentalStatusService;
import com.pinguela.rentexpres.service.VehicleStatusService;
import com.pinguela.rentexpres.service.CityService;
import com.pinguela.rentexpres.service.ProvinceService;
import com.pinguela.rentexpres.service.UserService;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.service.impl.RentalServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleCategoryServiceImpl;
import com.pinguela.rentexpres.service.impl.CustomerServiceImpl;
import com.pinguela.rentexpres.service.impl.RentalStatusServiceImpl;
import com.pinguela.rentexpres.service.impl.ReservationStatusServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleStatusServiceImpl;
import com.pinguela.rentexpres.service.impl.CityServiceImpl;
import com.pinguela.rentexpres.service.impl.ProvinceServiceImpl;
import com.pinguela.rentexpres.service.impl.ReservationServiceImpl;
import com.pinguela.rentexpres.service.impl.UserServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleServiceImpl;
import com.pinguela.rentexpres.service.impl.StatisticsServiceImpl;

public class RentExpresMainWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private final RentalService rentalService = new RentalServiceImpl();
        private final VehicleService vehicleService = new VehicleServiceImpl();
        private final CustomerService customerService = new CustomerServiceImpl();
        private final CityService cityService = new CityServiceImpl();
        private final ProvinceService provinceService = new ProvinceServiceImpl();
        private final UserService userService = new UserServiceImpl();
        private final StatisticsService statisticsService = new StatisticsServiceImpl();

	private final JPanel navPanel = new JPanel();
	private final JPanel contentPanel = new JPanel(new CardLayout());
	private final JToolBar topBar = new JToolBar();

	public RentExpresMainWindow() throws Exception {
		super("RentExpres");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		if (AppIcons.USER != null) {
			setIconImage(AppIcons.USER.getImage());
		}

               AppTheme.setup();

		UserDTO user = showLoginDialog();
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

	private UserDTO showLoginDialog() {
		LoginDialog dlg = new LoginDialog(this);
		return dlg.showDialog();
	}

	private void initTopBar() {
        topBar.setFloatable(false);
        topBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        topBar.setBackground(AppTheme.TOPBAR_BG);

		JLabel lblUser = new JLabel("User: " + AppContext.getCurrentUser().getUsername(), AppIcons.USER,
				JLabel.LEFT);
		lblUser.setFont(lblUser.getFont().deriveFont(Font.PLAIN, 14f));

                JButton btnProfile = new JButton("Profile", AppIcons.VIEW);
                JButton btnLogout = new JButton("Logout", AppIcons.CLEAR);
                JButton btnTheme = new JButton(AppTheme.isDark() ? "Modo claro" : "Modo oscuro");

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
                               UserDTO current = AppContext.getCurrentUser();
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

                btnTheme.setFocusPainted(false);
                btnTheme.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnTheme.setToolTipText("Cambiar tema");
               btnTheme.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               AppTheme.toggleDarkMode();
                               topBar.removeAll();
                               initTopBar();
                               navPanel.removeAll();
                               initNavigation();
                               SwingUtilities.updateComponentTreeUI(RentExpresMainWindow.this);
                       }
               });

                topBar.add(Box.createHorizontalGlue());
                topBar.add(lblUser);
                topBar.add(btnProfile);
                topBar.add(btnTheme);
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
                navPanel.add(createNavButton("Home", AppIcons.HOME, btnBg, btnHoverBg, btnFg));
                navPanel.add(Box.createVerticalStrut(10));
                navPanel.add(createNavButton("Reservations", AppIcons.RESERVATION, btnBg, btnHoverBg, btnFg));
                navPanel.add(Box.createVerticalStrut(10));
                navPanel.add(createNavButton("Rentals", AppIcons.RENTAL, btnBg, btnHoverBg, btnFg));
                navPanel.add(Box.createVerticalStrut(10));
                navPanel.add(createNavButton("Calendar", AppIcons.RESERVATION, btnBg, btnHoverBg, btnFg));
                navPanel.add(Box.createVerticalStrut(10));
                navPanel.add(createNavButton("Statistics", AppIcons.SEARCH, btnBg, btnHoverBg, btnFg));
                navPanel.add(Box.createVerticalStrut(10));
                navPanel.add(createNavButton("Customers", AppIcons.CUSTOMER, btnBg, btnHoverBg, btnFg));
                navPanel.add(Box.createVerticalStrut(10));

                if (RoleController.isAdmin()) {
                        navPanel.add(createNavButton("Users", AppIcons.USER, btnBg, btnHoverBg, btnFg));
                        navPanel.add(Box.createVerticalStrut(10));
                        navPanel.add(createNavButton("Vehicles", AppIcons.VEHICLE, btnBg, btnHoverBg, btnFg));
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

		if (AppIcons.HOME != null) {
			ImageIcon ic = AppIcons.HOME;
			Image scaled = ic.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
			JLabel lblIcon = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
			inicioPanel.add(lblIcon, BorderLayout.CENTER);
		}

		contentPanel.add(inicioPanel, "Home");

		ReservationSearchView rsv = new ReservationSearchView(new ReservationServiceImpl(), new ReservationStatusServiceImpl(),
				vehicleService, this);
		rsv.initIfNeeded();
		contentPanel.add(rsv, "Reservations");

                RentalSearchView asv = new RentalSearchView(rentalService, estadoAlqService(), vehicleService, this);
                asv.initIfNeeded();
                contentPanel.add(asv, "Rentals");

                CalendarView calView = new CalendarView();
                contentPanel.add(calView, "Calendar");

                StatisticsView estView = new StatisticsView(statisticsService);
                contentPanel.add(estView, "Statistics");

                CustomerSearchView csv = new CustomerSearchView(customerService, provinceService, cityService, this);
                csv.initIfNeeded();
                contentPanel.add(csv, "Customers");

                // Solo administradores
                if (RoleController.isAdmin()) {
                        UserSearchView usv = new UserSearchView(userService, this);
                        usv.initIfNeeded();
                        contentPanel.add(usv, "Users");

                        VehicleSearchView vsv = new VehicleSearchView(vehicleService, catVehService(), estadoVehService(), this);
                        vsv.initIfNeeded();
                        contentPanel.add(vsv, "Vehicles");
                }
        }

	private RentalStatusService estadoAlqService() {
		return new RentalStatusServiceImpl();
	}

	private VehicleCategoryService catVehService() {
		return new VehicleCategoryServiceImpl();
	}

	private VehicleStatusService estadoVehService() {
		return new VehicleStatusServiceImpl();
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
