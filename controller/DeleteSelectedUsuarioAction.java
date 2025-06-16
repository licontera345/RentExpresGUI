//package com.pinguela.rentexpres.desktop.controller;
//
//import java.awt.event.ActionEvent;
//import java.text.MessageFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.AbstractAction;
//import javax.swing.JOptionPane;
//import javax.swing.JTable;
//
//import com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel;
//import com.pinguela.rentexpres.model.UsuarioDTO;
//import com.pinguela.rentexpres.service.UsuarioService;
//import com.pinguela.rentexpres.service.impl.UsuarioServiceImpl;
//
//public class DeleteSelectedUsuarioAction extends AbstractAction {
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		// TODO Auto-generated method stub
//
//	}
//	private static final long serialVersionUID = 1L;
//	private final JTable table;
//	private final UsuarioService usuarioService;
//
//	public DeleteSelectedUsuarioAction(JTable table) {
//		super("Eliminar Seleccionados");
//		this.table = table;
//		this.usuarioService = new UsuarioServiceImpl();
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		UsuarioSearchTableModel model = (UsuarioSearchTableModel) table.getModel();
//		List<UsuarioDTO> seleccionados = new ArrayList<>();
//		for (int i = 0; i < model.getRowCount(); i++) {
//			try {
//				boolean sel = (boolean) model.getValueAt(i, model.getColumnIndex("Seleccionar"));
//				if (sel) {
//					seleccionados.add(model.getUsuarioAt(i));
//				}
//			} catch (IllegalArgumentException ex) {
//				JOptionPane.showMessageDialog(table, "La columna \"Seleccionar\" no está disponible.", "Error",
//						JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//		}
//
//		if (seleccionados.isEmpty()) {
//			JOptionPane.showMessageDialog(table, "No hay usuarios seleccionados.", "Advertencia",
//					JOptionPane.WARNING_MESSAGE);
//			return;
//		}
//
//		int resp = JOptionPane.showConfirmDialog(table,
//				MessageFormat.format("¿Está seguro de eliminar {0} usuario(s)?", seleccionados.size()),
//				"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
//		if (resp != JOptionPane.YES_OPTION) {
//			return;
//		}
//
//		boolean todosOk = true;
//		StringBuilder errores = new StringBuilder();
//		for (UsuarioDTO u : seleccionados) {
//			try {
//				boolean ok = usuarioService.delete(u, u.getId());
//				if (!ok) {
//					todosOk = false;
//					errores.append(u.getNombreUsuario()).append("; ");
//				}
//			} catch (Exception ex) {
//				todosOk = false;
//				errores.append(u.getNombreUsuario()).append(": ").append(ex.getMessage()).append("; ");
//			}
//		}
//
//		if (todosOk) {
//			JOptionPane.showMessageDialog(table, "Usuarios seleccionados eliminados correctamente.", "Éxito",
//					JOptionPane.INFORMATION_MESSAGE);
//		} else {
//			JOptionPane.showMessageDialog(table,
//					MessageFormat.format("No se pudieron eliminar algunos usuarios: {0}", errores.toString()), "Error",
//					JOptionPane.ERROR_MESSAGE);
//		}
//		// Refrescar tabla con todos los usuarios
//		try {
//			model.setUsuarios(usuarioService.findAll());
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(table, MessageFormat.format("Error al recargar tabla: {0}", ex.getMessage()),
//					"Error", JOptionPane.ERROR_MESSAGE);
//		}
//	}
