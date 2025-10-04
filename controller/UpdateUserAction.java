package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.util.List;
import java.text.MessageFormat;

import com.pinguela.rentexpres.desktop.model.UserSearchTableModel;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserService;
import com.pinguela.rentexpres.service.impl.UserServiceImpl;

public class UpdateUserAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final JTable table;
    private final UserService userService = new UserServiceImpl();

    public UpdateUserAction(JTable table) {
        super("Actualizar");
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            List<UserDTO> lista = userService.findAll();
            UserSearchTableModel model = (UserSearchTableModel) table.getModel();
            model.setUsers(lista);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(table,
                MessageFormat.format("Error al cargar users: {0}", ex.getMessage()),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
