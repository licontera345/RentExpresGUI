package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UsuarioCriteria;
import com.pinguela.rentexpres.model.UsuarioDTO;

public interface UsuarioDAO {

	public UsuarioDTO findById(Connection connection, Integer id) throws DataException;

	public boolean create(Connection connection, UsuarioDTO usuario) throws DataException;

	public boolean update(Connection connection, UsuarioDTO usuario) throws DataException;

	public boolean delete(Connection connection, UsuarioDTO usuario, Integer id) throws DataException;

	public List<UsuarioDTO> findAll(Connection connection) throws DataException;

	public UsuarioDTO autenticar(Connection connection, String nombreUsuario, String contrasenaEnClaro)
			throws DataException;

	public Results<UsuarioDTO> findByCriteria(Connection connection, UsuarioCriteria criteria) throws DataException;

}
