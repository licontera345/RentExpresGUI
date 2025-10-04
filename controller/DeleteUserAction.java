package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.model.UserSearchTableModel;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserService;
import com.pinguela.rentexpres.service.impl.UserServiceImpl;

public class DeleteUserAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private final JTable table;
	private final UserService userService;

	public DeleteUserAction(JTable table) {
		super("Delete");
		this.table = table;
		this.userService = new UserServiceImpl();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(table, "Seleccione un user.", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int modelRow = table.convertRowIndexToModel(row);
		UserSearchTableModel model = (UserSearchTableModel) table.getModel();
		UserDTO dto = model.getUserAt(modelRow);

		int resp = JOptionPane.showConfirmDialog(table,
				MessageFormat.format("Está seguro de delete el user \"{0}\"?", dto.getUsername()),
				"Confirm deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (resp != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			boolean ok = userService.delete(dto, dto.getId());
			if (ok) {
				JOptionPane.showMessageDialog(table, "User eliminado correctamente.", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				
				model.setUsers(userService.findAll());
			} else {
				JOptionPane.showMessageDialog(table, "No se pudo delete el user.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(table,
					MessageFormat.format("Error al delete user: {0}", ex.getMessage()), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
