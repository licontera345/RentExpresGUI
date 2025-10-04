package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.UserTypeDAO;
import com.pinguela.rentexpres.dao.impl.UserTypeDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.UserTypeDTO;
import com.pinguela.rentexpres.service.UserTypeService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class UserTypeServiceImpl implements UserTypeService {

	private static final Logger logger = LogManager.getLogger(UserTypeServiceImpl.class);
	private UserTypeDAO userTypeDAO;

	public UserTypeServiceImpl() {
		this.userTypeDAO = new UserTypeDAOImpl();
	}

	@Override
	public UserTypeDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		UserTypeDTO tipo = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				tipo = userTypeDAO.findById(connection, id);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(UserTypeServiceImpl.class, "findById de UserType completado. ID: " + id));
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UserTypeServiceImpl.class, "Error en findById de UserType: "), e);
			throw new RentexpresException("Error en findById de UserType", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return tipo;
	}

	@Override
	public List<UserTypeDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<UserTypeDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				lista = userTypeDAO.findAll(connection);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(UserTypeServiceImpl.class, "findAll de UserType completado. Cantidad: " + (lista != null ? lista.size() : 0)));
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UserTypeServiceImpl.class, "Error en findAll de UserType: "), e);
			throw new RentexpresException("Error en findAll de UserType", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}
}
