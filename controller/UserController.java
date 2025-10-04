//// UserController.java
//package com.pinguela.rentexpres.desktop.controller.usercontroller;
//
//import java.awt.Frame;
//
//import javax.swing.JButton;
//import javax.swing.JOptionPane;
//import javax.swing.JTable;
//
//import com.pinguela.rentexpres.desktop.model.UserSearchTableModel;
//import com.pinguela.rentexpres.desktop.view.userview.UserFilterPanel;
//import com.pinguela.rentexpres.desktop.view.userview.UserSearchView;
//import com.pinguela.rentexpres.model.UserDTO;
//import com.pinguela.rentexpres.service.UserService;
//import com.pinguela.rentexpres.service.impl.UserServiceImpl;
//
//
//public class UserController {
//
//	private final UserService userService = new UserServiceImpl();
//	private final UserSearchView view;
//	private final Frame parent;
//
//	public UserController(Frame parent, UserSearchView view) {
//		this.parent = parent;
//		this.view = view;
//		initController();
//	}
//
//	private void initController() {
//		JTable tbl = view.getTblUsers();
//		UserFilterPanel filter = view.getFilterPanel();
//
////		JButton btnBuscar = view.getBtnBuscar();
//		JButton btnLimpiar = view.getBtnLimpiar();
//		JButton btnDelete = view.getBtnDelete();
//		JButton btnDeleteSeleccionados = view.getBtnDeleteSeleccionados();
//
//		// 2) Asignar ClearUserFiltersAction al botón "Limpiar Filtros"
//		btnLimpiar.setAction(new ClearUserFiltersAction(filter.getTxtName(), filter.getTxtLastName(),
//				filter.getTxtSecondLastName(), filter.getTxtEmail(), filter.getTxtUser(), filter.getCmbUserType()));
//		btnLimpiar.setText("Limpiar Filtros");
//
//		// 3) Asignar DeleteUserAction al botón "Delete"
//		btnDelete.setAction(new DeleteUserAction(tbl));
//		btnDelete.setText("Delete");
//
//		// 4) Asignar DeleteSelectedUserAction al botón "Delete Seleccionados"
//		btnDeleteSeleccionados.setAction(new DeleteSelectedUserAction(tbl));
//		btnDeleteSeleccionados.setText("Delete Seleccionados");
//
//		// 5) Carga inicial de todos los users en la tabla
//		refreshTable();
//	}
//
//	/**
//	 * Recupera todos los users y actualiza el model de la tabla.
//	 */
//	private void refreshTable() {
//		try {
//			java.util.List<UserDTO> lista = userService.findAll();
//			UserSearchTableModel model = new UserSearchTableModel(lista);
//			view.getTblUsers().setModel(model);
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(view, "Error al cargar users: " + ex.getMessage(), "Error",
//					JOptionPane.ERROR_MESSAGE);
//		}
//	}
//}


