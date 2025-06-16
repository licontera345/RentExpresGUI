package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.UsuarioDetailDialog;
import com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel;
import com.pinguela.rentexpres.model.UsuarioDTO;

public class ShowUsuarioDetailAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final Frame parent;
    private final JTable table;

    public ShowUsuarioDetailAction(Frame parent, JTable table) {
        super("Ver");
        this.parent = parent;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(parent,
                "Seleccione un usuario.", "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        UsuarioSearchTableModel model = (UsuarioSearchTableModel) table.getModel();
        UsuarioDTO dto = model.getUsuarioAt(modelRow);
        if (dto != null) {
            new UsuarioDetailDialog(parent, dto.getId()).setVisible(true);
        }
    }
}
