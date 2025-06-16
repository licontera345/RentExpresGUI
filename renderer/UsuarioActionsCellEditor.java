package com.pinguela.rentexpres.desktop.renderer;

import static com.pinguela.rentexpres.desktop.util.AppIcons.DELETE;
import static com.pinguela.rentexpres.desktop.util.AppIcons.EDIT;
import static com.pinguela.rentexpres.desktop.util.AppIcons.VIEW;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.EventObject;
import java.util.function.Supplier;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;


public class UsuarioActionsCellEditor extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 1L;

	private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
	private final JButton btnView = new JButton(VIEW);
	private final JButton btnEdit = new JButton(EDIT);
	private final JButton btnDelete = new JButton(DELETE);

	private final Frame owner;
	private final UsuarioService usuarioService;
	private final Runnable reload;
	private final Supplier<UsuarioDTO> rowSupplier;

	public UsuarioActionsCellEditor(Frame owner, UsuarioService usuarioService, Runnable reload,
			Supplier<UsuarioDTO> rowSupplier) {
		this.owner = owner;
		this.usuarioService = usuarioService;
		this.reload = reload;
		this.rowSupplier = rowSupplier;

		for (JButton b : new JButton[] { btnView, btnEdit, btnDelete }) {
			b.setBorderPainted(false);
			b.setFocusPainted(false);
			b.setContentAreaFilled(false);
			panel.add(b);
		}

		btnView.addActionListener(e -> {
			UsuarioDTO u = rowSupplier.get();
			if (u != null) {
				StringBuilder info = new StringBuilder();
				info.append("ID: ").append(u.getId()).append("\n");
				info.append("Nombre: ").append(u.getNombre()).append(" ").append(u.getApellido1()).append(" ")
						.append(u.getApellido2()).append("\n");
				info.append("Email: ").append(u.getEmail()).append("\n");
				info.append("Usuario: ").append(u.getNombreUsuario()).append("\n");
				info.append("Tipo Usuario (ID): ").append(u.getIdTipoUsuario()).append("\n");
				JOptionPane.showMessageDialog(owner, info.toString(), "Ver Usuario", JOptionPane.INFORMATION_MESSAGE);
			}
			fireEditingStopped();
		});

		btnEdit.addActionListener(e -> {
			UsuarioDTO u = rowSupplier.get();
			if (u != null) {

				JOptionPane.showMessageDialog(owner, "Abre diálogo de edición para ID=" + u.getId(), "Editar Usuario",
						JOptionPane.INFORMATION_MESSAGE);
			}
			fireEditingStopped();
		});

		btnDelete.addActionListener(e -> {
			UsuarioDTO u = rowSupplier.get();
			if (u != null) {
				int resp = JOptionPane.showConfirmDialog(owner,
						"¿Seguro que deseas eliminar al usuario “" + u.getNombre() + "”?", "Eliminar Usuario",
						JOptionPane.YES_NO_OPTION);
				if (resp == JOptionPane.YES_OPTION) {
					try {
						usuarioService.delete(u, u.getId());
						reload.run();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(owner, "Error al eliminar usuario:\n" + ex.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			fireEditingStopped();
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject e) {
		return true;
	}

	public Supplier<UsuarioDTO> getRowSupplier() {
		return rowSupplier;
	}

	public Runnable getReload() {
		return reload;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public Frame getOwner() {
		return owner;
	}
}
