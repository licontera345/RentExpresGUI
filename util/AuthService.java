package com.pinguela.rentexpres.desktop.util;

import com.pinguela.rentexpres.model.UserDTO;


public interface AuthService {
 
    UserDTO authenticate(String username, String password) throws Exception;
}
