package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import com.pinguela.rentexpres.desktop.dialog.UsuarioCreateDialog;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;

public class ShowUsuarioCreateAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final Frame parent;
    private final ActionCallback afterCreate;
    private final UsuarioService usuarioService;

    public ShowUsuarioCreateAction(Frame parent, UsuarioService usuarioService, ActionCallback afterCreate) {
        super("Nuevo");
        this.parent = parent;
        this.usuarioService = usuarioService;
        this.afterCreate = afterCreate;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UsuarioCreateDialog dlg = new UsuarioCreateDialog(parent);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            UsuarioDTO dto = dlg.getUsuario();
            usuarioService.create(dto);
        }
        afterCreate.execute();
    }
}
