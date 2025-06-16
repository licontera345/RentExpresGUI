//package com.pinguela.rentexpres.desktop.controller.usuariocontroller;
//
//import java.awt.Frame;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.swing.JButton;
//import javax.swing.JOptionPane;
//import javax.swing.JTable;
//
//import com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel;
//import com.pinguela.rentexpres.desktop.view.usuarioview.UsuarioFilterPanel;
//import com.pinguela.rentexpres.desktop.view.usuarioview.UsuarioSearchActionsView;
//import com.pinguela.rentexpres.desktop.view.usuarioview.UsuarioSearchView;
//import com.pinguela.rentexpres.desktop.view.usuarioview.UsuarioTablePanel;
//import com.pinguela.rentexpres.model.TipoUsuarioDTO;
//import com.pinguela.rentexpres.model.UsuarioDTO;
//import com.pinguela.rentexpres.service.TipoUsuarioService;
//import com.pinguela.rentexpres.service.UsuarioService;
//import com.pinguela.rentexpres.service.impl.TipoUsuarioServiceImpl;
//
///**
// * Controller para la pantalla de búsqueda de Usuarios.
// */
//public class UsuarioSearchController {
//
//	private final UsuarioService usuarioService;
//	private final TipoUsuarioService tipoService;
//	private final UsuarioSearchView view;
//	private final Frame parent;
//
//	public UsuarioSearchController(Frame parent, UsuarioSearchView view, UsuarioService service) {
//		this.parent = parent; // Frame padre para diálogos
//		this.view = view; // La vista que controla
//		this.usuarioService = service; // Inyectado desde fuera
//		this.tipoService = new TipoUsuarioServiceImpl();
//		initController();
//	}
//
//	private void initController() {
//		UsuarioTablePanel tablePanel = view.getTablePanel();
//		JTable tbl = tablePanel.getTable();
//
//		UsuarioFilterPanel filter = view.getFilterPanel();
//
//		UsuarioSearchActionsView actions = view.getActionsView();
//		JButton btnLimpiarFiltros = actions.getBtnLimpiarFiltros();
//		JButton btnEliminar = actions.getBtnEliminar();
//		JButton btnEliminarSeleccionados = actions.getBtnEliminar();
//
//		btnLimpiarFiltros.setAction(new ClearUsuarioFiltersAction(filter.getTxtNombre(), filter.getTxtApellido1(),
//				filter.getTxtApellido2(), filter.getTxtEmail(), filter.getTxtUsuario(), filter.getCmbTipoUsuario()));
//		btnLimpiarFiltros.setText("Limpiar Filtros");
//
//		btnEliminar.setAction(new DeleteUsuarioAction(tbl));
//		btnEliminar.setText("Eliminar");
//
//		btnEliminarSeleccionados.setAction(new DeleteSelectedUsuarioAction(tbl));
//		btnEliminarSeleccionados.setText("Eliminar Seleccionados");
//
//		refreshTable();
//	}
//
//	public void search() {
//		try {
//
//			UsuarioFilterPanel f = view.getFilterPanel();
//			String nombre = f.getNombre();
//			String apellido1 = f.getApellido1();
//			String apellido2 = f.getApellido2();
//			String email = f.getEmail();
//			String usuario = f.getUsuarioLogin();
//			Integer idTipo = f.getIdTipoUsuario();
//
//			List<UsuarioDTO> lista = usuarioService.findAll();
//
//			List<TipoUsuarioDTO> listaTipos = tipoService.findAll();
//			Map<Integer, String> tipoMap = listaTipos.stream()
//					.collect(Collectors.toMap(TipoUsuarioDTO::getId, TipoUsuarioDTO::getNombreTipo));
//
//			// d) Crear y asignar el TableModel
//			UsuarioSearchTableModel model = new UsuarioSearchTableModel(tipoMap);
//			model.setUsuarios(lista);
//			view.getTablePanel().getTable().setModel(model);
//
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(parent, "Error al buscar usuarios: " + ex.getMessage(), "Error",
//					JOptionPane.ERROR_MESSAGE);
//		}
//	}
//
//	/**
//	 * Carga todos los usuarios sin aplicar filtro (para la vista inicial).
//	 */
//	private void refreshTable() {
//		try {
//			// 1) Obtener lista completa de Usuarios y lista de Tipos
//			List<UsuarioDTO> listaUsuarios = usuarioService.findAll();
//			List<TipoUsuarioDTO> listaTipos = tipoService.findAll();
//
//			// 2) Armar el mapa idTipoUsuario → nombreTipo
//			Map<Integer, String> tipoMap = listaTipos.stream()
//					.collect(Collectors.toMap(TipoUsuarioDTO::getId, TipoUsuarioDTO::getNombreTipo));
//
//			// 3) Crear el modelo y asignar datos
//			UsuarioSearchTableModel model = new UsuarioSearchTableModel(tipoMap);
//			model.setUsuarios(listaUsuarios);
//
//			// 4) Asignar el modelo a la tabla
//			view.getTablePanel().getTable().setModel(model);
//
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(parent, "Error al cargar usuarios: " + ex.getMessage(), "Error",
//					JOptionPane.ERROR_MESSAGE);
//		}
//	}
//}



