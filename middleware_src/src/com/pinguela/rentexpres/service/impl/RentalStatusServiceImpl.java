package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.RentalStatusDAO;
import com.pinguela.rentexpres.dao.impl.RentalStatusDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalStatusDTO;
import com.pinguela.rentexpres.service.RentalStatusService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class RentalStatusServiceImpl implements RentalStatusService {

	private static final Logger logger = LogManager.getLogger(RentalStatusServiceImpl.class);
	private RentalStatusDAO rentalStatusDAO;

	public RentalStatusServiceImpl() {
		this.rentalStatusDAO = new RentalStatusDAOImpl();
	}

	@Override
	public RentalStatusDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		RentalStatusDTO estado = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				estado = rentalStatusDAO.findById(connection, id);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(RentalStatusServiceImpl.class, "findById de RentalStatus completado. ID: " + id));
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(RentalStatusServiceImpl.class, "Error en findById de RentalStatus: "), e);
			throw new RentexpresException("Error en findById de RentalStatus", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return estado;
	}

	@Override
	public List<RentalStatusDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<RentalStatusDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				lista = rentalStatusDAO.findAll(connection);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(RentalStatusServiceImpl.class, "findAll de RentalStatus completado. Cantidad: " + (lista != null ? lista.size() : 0)));
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(RentalStatusServiceImpl.class, "Error en findAll de RentalStatus: "), e);
			throw new RentexpresException("Error en findAll de RentalStatus", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}
}
