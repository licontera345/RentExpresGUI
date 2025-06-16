package com.pinguela.rentexpres.desktop.util;

import java.sql.Connection;

import com.pinguela.rentexpres.dao.UsuarioDAO;
import com.pinguela.rentexpres.dao.impl.UsuarioDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class AuthServiceImpl implements AuthService {
	private final UsuarioDAO usuarioDao = new UsuarioDAOImpl();

	@Override
	public UsuarioDTO authenticate(String username, String password) throws Exception {
		if (username == null || password == null) {
			return null;
		}
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
	
			return usuarioDao.autenticar(conn, username, password);
		} catch (DataException de) {
			throw de;
		} finally {
			JDBCUtils.close(conn);
		}
	}
}
