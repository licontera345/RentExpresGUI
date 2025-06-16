//// UsuarioController.java
//package com.pinguela.rentexpres.desktop.controller.usuariocontroller;
//
//import java.awt.Frame;
//
//import javax.swing.JButton;
//import javax.swing.JOptionPane;
//import javax.swing.JTable;
//
//import com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel;
//import com.pinguela.rentexpres.desktop.view.usuarioview.UsuarioFilterPanel;
//import com.pinguela.rentexpres.desktop.view.usuarioview.UsuarioSearchView;
//import com.pinguela.rentexpres.model.UsuarioDTO;
//import com.pinguela.rentexpres.service.UsuarioService;
//import com.pinguela.rentexpres.service.impl.UsuarioServiceImpl;
//
//
//public class UsuarioController {
//
//	private final UsuarioService usuarioService = new UsuarioServiceImpl();
//	private final UsuarioSearchView view;
//	private final Frame parent;
//
//	public UsuarioController(Frame parent, UsuarioSearchView view) {
//		this.parent = parent;
//		this.view = view;
//		initController();
//	}
//
//	private void initController() {
//		JTable tbl = view.getTblUsuarios();
//		UsuarioFilterPanel filter = view.getFilterPanel();
//
////		JButton btnBuscar = view.getBtnBuscar();
//		JButton btnLimpiar = view.getBtnLimpiar();
//		JButton btnEliminar = view.getBtnEliminar();
//		JButton btnEliminarSeleccionados = view.getBtnEliminarSeleccionados();
//
//		// 2) Asignar ClearUsuarioFiltersAction al botón "Limpiar Filtros"
//		btnLimpiar.setAction(new ClearUsuarioFiltersAction(filter.getTxtNombre(), filter.getTxtApellido1(),
//				filter.getTxtApellido2(), filter.getTxtEmail(), filter.getTxtUsuario(), filter.getCmbTipoUsuario()));
//		btnLimpiar.setText("Limpiar Filtros");
//
//		// 3) Asignar DeleteUsuarioAction al botón "Eliminar"
//		btnEliminar.setAction(new DeleteUsuarioAction(tbl));
//		btnEliminar.setText("Eliminar");
//
//		// 4) Asignar DeleteSelectedUsuarioAction al botón "Eliminar Seleccionados"
//		btnEliminarSeleccionados.setAction(new DeleteSelectedUsuarioAction(tbl));
//		btnEliminarSeleccionados.setText("Eliminar Seleccionados");
//
//		// 5) Carga inicial de todos los usuarios en la tabla
//		refreshTable();
//	}
//
//	/**
//	 * Recupera todos los usuarios y actualiza el modelo de la tabla.
//	 */
//	private void refreshTable() {
//		try {
//			java.util.List<UsuarioDTO> lista = usuarioService.findAll();
//			UsuarioSearchTableModel model = new UsuarioSearchTableModel(lista);
//			view.getTblUsuarios().setModel(model);
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(view, "Error al cargar usuarios: " + ex.getMessage(), "Error",
//					JOptionPane.ERROR_MESSAGE);
//		}
//	}
//}


