package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.UserDetailDialog;
import com.pinguela.rentexpres.desktop.model.UserSearchTableModel;
import com.pinguela.rentexpres.model.UserDTO;

public class ShowUserDetailAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final Frame parent;
    private final JTable table;

    public ShowUserDetailAction(Frame parent, JTable table) {
        super("Ver");
        this.parent = parent;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(parent,
                "Seleccione un user.", "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        UserSearchTableModel model = (UserSearchTableModel) table.getModel();
        UserDTO dto = model.getUserAt(modelRow);
        if (dto != null) {
            new UserDetailDialog(parent, dto.getId()).setVisible(true);
        }
    }
}
