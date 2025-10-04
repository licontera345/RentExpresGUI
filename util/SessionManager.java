package com.pinguela.rentexpres.desktop.util;

import com.pinguela.rentexpres.model.UserDTO;

/**
 * Maneja la sesión del user autenticado.
 * Implementación Singleton: un único user activo en toda la aplicación.
 */
public final class SessionManager {

    private static UserDTO currentUser;

    private SessionManager() { /* no instancias */ }

    public static void setCurrentUser(UserDTO user) {
        currentUser = user;
    }

    public static UserDTO getCurrentUser() {
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
