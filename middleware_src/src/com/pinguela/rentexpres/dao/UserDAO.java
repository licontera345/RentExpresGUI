package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UserCriteria;
import com.pinguela.rentexpres.model.UserDTO;

public interface UserDAO {

	public UserDTO findById(Connection connection, Integer id) throws DataException;

	public boolean create(Connection connection, UserDTO user) throws DataException;

	public boolean update(Connection connection, UserDTO user) throws DataException;

	public boolean delete(Connection connection, UserDTO user, Integer id) throws DataException;

	public List<UserDTO> findAll(Connection connection) throws DataException;

	public UserDTO autenticar(Connection connection, String username, String contrasenaEnClaro)
			throws DataException;

	public Results<UserDTO> findByCriteria(Connection connection, UserCriteria criteria) throws DataException;

}
