package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;
import java.util.function.Supplier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.renderer.AbstractActionsCellEditor;

import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.controller.UsuarioRowController;

import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;


public class UsuarioActionsCellEditor extends AbstractActionsCellEditor {

        private static final long serialVersionUID = 1L;

        private final Frame owner;
        private final UsuarioRowController controller;
        private final ActionCallback reload;
        private final Supplier<UsuarioDTO> rowSupplier;
        private UsuarioDTO usuarioActual;

        public UsuarioActionsCellEditor(Frame owner, UsuarioService usuarioService, ActionCallback reload,
                        Supplier<UsuarioDTO> rowSupplier) {
                super();
                this.owner = owner;
                this.reload = reload;
                this.rowSupplier = rowSupplier;
                this.controller = new UsuarioRowController(owner, usuarioService, reload);

               btnView.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               controller.showDetail(usuarioActual);
                               fireEditingStopped();
                       }
               });

               btnEdit.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               controller.edit(usuarioActual);
                               fireEditingStopped();
                       }
               });

               btnDel.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               controller.delete(usuarioActual);
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

        public Frame getOwner() {
                return owner;
        }
}
