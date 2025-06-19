package com.pinguela.rentexpres.desktop.controller;

import java.util.List;

import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;

/** Controller for user profile operations. */
public class ProfileController {
    private final UsuarioService usuarioService;

    public ProfileController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /** Returns the current user refreshed from persistence. */
    public UsuarioDTO getCurrentUser() throws Exception {
        UsuarioDTO current = AppContext.getCurrentUser();
        if (current == null) return null;
        UsuarioDTO refreshed = usuarioService.findById(current.getId());
        AppContext.setCurrentUser(refreshed);
        return refreshed;
    }

    public List<String> getUsuarioImages(int userId) throws Exception {
        return usuarioService.getUsuarioImages(userId);
    }

    /** Updates the user and refreshes the current user context. */
    public UsuarioDTO updateUsuario(UsuarioDTO updated) throws Exception {
        usuarioService.update(updated);
        UsuarioDTO refreshed = usuarioService.findById(updated.getId());
        AppContext.setCurrentUser(refreshed);
        return refreshed;
    }
}
