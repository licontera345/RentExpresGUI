package com.pinguela.rentexpres.desktop.util;

import com.pinguela.rentexpres.model.UsuarioDTO;


public interface AuthService {
 
    UsuarioDTO authenticate(String username, String password) throws Exception;
}
