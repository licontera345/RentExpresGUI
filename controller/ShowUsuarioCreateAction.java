package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;


import com.pinguela.rentexpres.desktop.dialog.UsuarioCreateDialog;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;

public class ShowUsuarioCreateAction extends AbstractCreateAction<UsuarioDTO, UsuarioCreateDialog> {
    private static final long serialVersionUID = 1L;
    private final UsuarioService usuarioService;

    public ShowUsuarioCreateAction(Frame parent, UsuarioService usuarioService, ActionCallback afterCreate) {
        super("Nuevo", parent, afterCreate);
        this.usuarioService = usuarioService;
    }

    @Override
    protected UsuarioCreateDialog createDialog() {
        return new UsuarioCreateDialog(frame);
    }

    @Override
    protected void save(UsuarioDTO dto) throws RentexpresException {
        usuarioService.create(dto);
    }
}
