//package com.pinguela.rentexpres.desktop.view.usuarioview;
//
//import java.awt.BorderLayout;
//import java.awt.Frame;
//
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JToolBar;
//
//import com.pinguela.rentexpres.desktop.controller.usuariocontroller.UsuarioSearchController;
//import com.pinguela.rentexpres.exception.RentexpresException;
//import com.pinguela.rentexpres.service.UsuarioService;
//
//public class UsuarioSearchView extends JPanel {
//	private static final long serialVersionUID = 1L;
//
//	private final UsuarioFilterPanel filterPanel;
//	private final UsuarioSearchActionsView actionsView;
//	private final UsuarioTablePanel tablePanel;
//	private final Frame owner;
//	private UsuarioSearchController controller;
//	private boolean initialized = false;
//
//	public void initIfNeeded() {
//		if (!initialized) {
//			controller.search(); // o controller.init() si quieres otro flujo
//			initialized = true;
//		}
//	}
//
//	/**
//	 * @param service UsuarioService inyectado (por ejemplo, new
//	 *                UsuarioServiceImpl())
//	 * @param owner   Frame padre para diálogos
//	 * @throws RentexpresException
//	 */
//	public UsuarioSearchView(UsuarioService service, Frame owner) throws RentexpresException {
//		this.owner = owner;
//
//		setLayout(new BorderLayout(8, 8));
//
//		// 1) Panel de filtros
//		filterPanel = new UsuarioFilterPanel();
//
//		// 2) Barra de acciones
//		actionsView = new UsuarioSearchActionsView();
//
//		// 3) Panel de tabla
//		tablePanel = new UsuarioTablePanel(service, owner, () -> controller.search());
//
//		// 4) Construcción del JToolBar
//		JToolBar bar = new JToolBar();
//		bar.setFloatable(false);
//		bar.add(actionsView);
//
//		add(filterPanel, BorderLayout.NORTH);
//		add(bar, BorderLayout.CENTER);
//		add(new JScrollPane(tablePanel), BorderLayout.SOUTH);
//
//		// 5) Crear controller PASANDO owner, this y service
//		controller = new UsuarioSearchController(owner, this, service);
//
//		// 6) Registrar listener de cambio en filtros (después de instanciar el
//		// controller)
//		filterPanel.addPropertyChangeListener("filtrosCambio", e -> controller.search());
//		actionsView.setController(controller);
//	}
//
//	// ----- getters que usa el controlador -----
//
//	public UsuarioFilterPanel getFilterPanel() {
//		return filterPanel;
//	}
//
//	public UsuarioTablePanel getTablePanel() {
//		return tablePanel;
//	}
//
//	public UsuarioSearchActionsView getActionsView() {
//		return actionsView;
//	}
//
//	public Frame getOwnerFrame() {
//		return owner;
//	}
//}


