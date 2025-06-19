package com.pinguela.rentexpres.desktop.controller;

import com.pinguela.rentexpres.desktop.util.AppContext;

public class RoleController {
    private RoleController() {}

    public static boolean isAdmin() {
        return AppContext.getCurrentUser() != null &&
               AppContext.getCurrentUser().getIdTipoUsuario() == 1;
    }
}
