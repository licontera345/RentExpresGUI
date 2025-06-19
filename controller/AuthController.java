package com.pinguela.rentexpres.desktop.controller;

import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.desktop.util.AuthService;
import com.pinguela.rentexpres.desktop.util.AuthServiceImpl;
import com.pinguela.rentexpres.model.UsuarioDTO;

/** Simple controller for login operations. */
public class AuthController {
    private final AuthService authService = new AuthServiceImpl();

    /**
     * Attempt to authenticate the user with the given credentials.
     * If successful, updates the {@link AppContext} accordingly.
     */
    public UsuarioDTO login(String username, String password, boolean remember) throws Exception {
        UsuarioDTO user = authService.authenticate(username, password);
        if (user != null) {
            AppContext.setCurrentUser(user);
            if (remember) {
                AppContext.setRememberedUser(username);
            } else {
                AppContext.setRememberedUser(null);
            }
        }
        return user;
    }
}
