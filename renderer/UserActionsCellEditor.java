package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;
import java.util.function.Supplier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.renderer.AbstractActionsCellEditor;

import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.controller.UserRowController;

import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserService;


public class UserActionsCellEditor extends AbstractActionsCellEditor {

        private static final long serialVersionUID = 1L;

        private final Frame owner;
        private final UserRowController controller;
        private final ActionCallback reload;
        private final Supplier<UserDTO> rowSupplier;
        private UserDTO userActual;

        public UserActionsCellEditor(Frame owner, UserService userService, ActionCallback reload,
                        Supplier<UserDTO> rowSupplier) {
                super();
                this.owner = owner;
                this.reload = reload;
                this.rowSupplier = rowSupplier;
                this.controller = new UserRowController(owner, userService, reload);

               btnView.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               controller.showDetail(userActual);
                               fireEditingStopped();
                       }
               });

               btnEdit.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               controller.edit(userActual);
                               fireEditingStopped();
                       }
               });

               btnDel.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                               controller.delete(userActual);
                               fireEditingStopped();
                       }
               });
	}

	@Override
       public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
               if (rowSupplier != null) {
                       userActual = rowSupplier.get();
               } else if (table.getModel() instanceof com.pinguela.rentexpres.desktop.model.UserSearchTableModel) {
                       com.pinguela.rentexpres.desktop.model.UserSearchTableModel m =
                               (com.pinguela.rentexpres.desktop.model.UserSearchTableModel) table.getModel();
                       userActual = m.getUserAt(table.convertRowIndexToModel(row));
               }
               return panel;
       }

	@Override
	public Object getCellEditorValue() {
		return null;
	}


	public Supplier<UserDTO> getRowSupplier() {
		return rowSupplier;
	}

        public ActionCallback getReload() {
                return reload;
        }

        public Frame getOwner() {
                return owner;
        }
}
