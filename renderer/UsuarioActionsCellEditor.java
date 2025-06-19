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
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.dialog.UsuarioDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.UsuarioEditDialog;

import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;


public class UsuarioActionsCellEditor extends AbstractActionsCellEditor {

        private static final long serialVersionUID = 1L;

        private final Frame owner;
        private final UsuarioService usuarioService;
        private final ActionCallback reload;
        private final Supplier<UsuarioDTO> rowSupplier;
        private UsuarioDTO usuarioActual;

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
                               if (usuarioActual != null) {
                                       new UsuarioDetailDialog(owner, usuarioActual.getId()).setVisible(true);
                               }
                               fireEditingStopped();
                       }
               });

               btnEdit.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               if (usuarioActual != null) {
                                       UsuarioEditDialog dlg = new UsuarioEditDialog(owner, usuarioActual.getId());
                                       dlg.setVisible(true);
                                       if (dlg.isConfirmed()) {
                                               try {
                                                       usuarioService.update(dlg.getUsuario());
                                                       if (reload != null) {
                                                               reload.execute();
                                                       }
                                               } catch (Exception ex) {
                                                       SwingUtils.showError(owner, ex.getMessage());
                                               }
                                       }
                               }
                               fireEditingStopped();
                       }
               });

               btnDel.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               if (usuarioActual != null) {
                                       int resp = JOptionPane.showConfirmDialog(owner,
                                                       "¿Seguro que deseas eliminar al usuario " + usuarioActual.getNombre() + "?", "Eliminar Usuario",
                                                       JOptionPane.YES_NO_OPTION);
                                       if (resp == JOptionPane.YES_OPTION) {
                                               try {
                                                       usuarioService.delete(usuarioActual, usuarioActual.getId());
                                                       if (reload != null) {
                                                               reload.execute();
                                                       }
                                               } catch (Exception ex) {
                                                       SwingUtils.showError(owner, ex.getMessage());
                                               }
                                       }
                               }
                               fireEditingStopped();
                       }
               });
	}

	@Override
       public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
               if (rowSupplier != null) {
                       usuarioActual = rowSupplier.get();
               } else if (table.getModel() instanceof com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel) {
                       com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel m =
                               (com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel) table.getModel();
                       usuarioActual = m.getUsuarioAt(table.convertRowIndexToModel(row));
               }
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
