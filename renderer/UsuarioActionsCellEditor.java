package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;
import java.util.function.Supplier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.renderer.AbstractActionsCellEditor;

import com.pinguela.rentexpres.desktop.util.ActionCallback;

import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;


public class UsuarioActionsCellEditor extends AbstractActionsCellEditor {

        private static final long serialVersionUID = 1L;

	private final Frame owner;
	private final UsuarioService usuarioService;
        private final ActionCallback reload;
	private final Supplier<UsuarioDTO> rowSupplier;

        public UsuarioActionsCellEditor(Frame owner, UsuarioService usuarioService, ActionCallback reload,
                        Supplier<UsuarioDTO> rowSupplier) {
                super();
                this.owner = owner;
                this.usuarioService = usuarioService;
                this.reload = reload;
                this.rowSupplier = rowSupplier;

               btnView.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
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
                       }
               });

               btnEdit.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                       UsuarioDTO u = rowSupplier.get();
                       if (u != null) {

                               JOptionPane.showMessageDialog(owner, "Abre diálogo de edición para ID=" + u.getId(), "Editar Usuario",
                                               JOptionPane.INFORMATION_MESSAGE);
                       }
                       fireEditingStopped();
                       }
               });

               btnDel.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                       UsuarioDTO u = rowSupplier.get();
                       if (u != null) {
                               int resp = JOptionPane.showConfirmDialog(owner,
                                               "¿Seguro que deseas eliminar al usuario “" + u.getNombre() + "”?", "Eliminar Usuario",
                                               JOptionPane.YES_NO_OPTION);
                               if (resp == JOptionPane.YES_OPTION) {
                                       try {
                                               usuarioService.delete(u, u.getId());
                                               reload.execute();
                                       } catch (Exception ex) {
                                               JOptionPane.showMessageDialog(owner, "Error al eliminar usuario:\n" + ex.getMessage(), "Error",
                                                               JOptionPane.ERROR_MESSAGE);
                                       }
                               }
                       }
                       fireEditingStopped();
                       }
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


	public Supplier<UsuarioDTO> getRowSupplier() {
		return rowSupplier;
	}

        public ActionCallback getReload() {
                return reload;
        }

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public Frame getOwner() {
		return owner;
	}
}
