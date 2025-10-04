package com.pinguela.rentexpres.desktop.util;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserService;
import com.pinguela.rentexpres.service.impl.UserServiceImpl;

public class AuthServiceImpl implements AuthService {
        private final UserService userService = new UserServiceImpl();

	@Override
	public UserDTO authenticate(String username, String password) throws Exception {
                if (username == null || password == null) {
                        return null;
                }
                try {
                        return userService.autenticar(username, password);
                } catch (RentexpresException ex) {
                        throw ex;
                }
        }
}
