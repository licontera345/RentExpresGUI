package com.pinguela.rentexpres.desktop.util;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;
import com.pinguela.rentexpres.service.impl.UsuarioServiceImpl;

public class AuthServiceImpl implements AuthService {
        private final UsuarioService usuarioService = new UsuarioServiceImpl();

	@Override
	public UsuarioDTO authenticate(String username, String password) throws Exception {
                if (username == null || password == null) {
                        return null;
                }
                try {
                        return usuarioService.autenticar(username, password);
                } catch (RentexpresException ex) {
                        throw ex;
                }
        }
}
