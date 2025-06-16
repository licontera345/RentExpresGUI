package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;
import com.pinguela.rentexpres.service.impl.UsuarioServiceImpl;

public class DeleteUsuarioAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private final JTable table;
	private final UsuarioService usuarioService;

	public DeleteUsuarioAction(JTable table) {
		super("Eliminar");
		this.table = table;
		this.usuarioService = new UsuarioServiceImpl();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(table, "Seleccione un usuario.", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int modelRow = table.convertRowIndexToModel(row);
		UsuarioSearchTableModel model = (UsuarioSearchTableModel) table.getModel();
		UsuarioDTO dto = model.getUsuarioAt(modelRow);

		int resp = JOptionPane.showConfirmDialog(table,
				MessageFormat.format("¿Está seguro de eliminar el usuario \"{0}\"?", dto.getNombreUsuario()),
				"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (resp != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			boolean ok = usuarioService.delete(dto, dto.getId());
			if (ok) {
				JOptionPane.showMessageDialog(table, "Usuario eliminado correctamente.", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				
				model.setUsuarios(usuarioService.findAll());
			} else {
				JOptionPane.showMessageDialog(table, "No se pudo eliminar el usuario.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(table,
					MessageFormat.format("Error al eliminar usuario: {0}", ex.getMessage()), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
