package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.UsuarioEditDialog;
import com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;

public class ShowUsuarioEditAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final Frame parent;
    private final JTable table;
    private final ActionCallback afterEdit;
    private final UsuarioService usuarioService;

    public ShowUsuarioEditAction(Frame parent, JTable table, UsuarioService usuarioService, ActionCallback afterEdit) {
        super("Editar");
        this.parent = parent;
        this.table = table;
        this.usuarioService = usuarioService;
        this.afterEdit = afterEdit;
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
            UsuarioEditDialog dlg = new UsuarioEditDialog(parent, dto.getId());
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                UsuarioDTO updated = dlg.getUsuario();
                usuarioService.update(updated);
            }
            afterEdit.execute();
        }
    }
}
