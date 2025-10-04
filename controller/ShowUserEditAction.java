package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.UserEditDialog;
import com.pinguela.rentexpres.desktop.model.UserSearchTableModel;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserService;

public class ShowUserEditAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final Frame parent;
    private final JTable table;
    private final ActionCallback afterEdit;
    private final UserService userService;

    public ShowUserEditAction(Frame parent, JTable table, UserService userService, ActionCallback afterEdit) {
        super("Editar");
        this.parent = parent;
        this.table = table;
        this.userService = userService;
        this.afterEdit = afterEdit;
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
            UserEditDialog dlg = new UserEditDialog(parent, dto.getId());
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                UserDTO updated = dlg.getUser();
                userService.update(updated);
            }
            afterEdit.execute();
        }
    }
}
