package com.pinguela.rentexpres.desktop.util;

import com.pinguela.rentexpres.model.UsuarioDTO;

/**
 * Maneja la sesión del usuario autenticado.
 * Implementación Singleton: un único usuario activo en toda la aplicación.
 */
public final class SessionManager {

    private static UsuarioDTO currentUser;

    private SessionManager() { /* no instancias */ }

    public static void setCurrentUser(UsuarioDTO user) {
        currentUser = user;
    }

    public static UsuarioDTO getCurrentUser() {
        return currentUser;
    }

    public static Integer getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        currentUser = null;
    }
}
