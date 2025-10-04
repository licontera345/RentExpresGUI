package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.VehicleCategoryDAO;
import com.pinguela.rentexpres.dao.impl.VehicleCategoryDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class VehicleCategoryServiceImpl implements VehicleCategoryService {

	private static final Logger logger = LogManager.getLogger(VehicleCategoryServiceImpl.class);
	private VehicleCategoryDAO categoryDAO;

	public VehicleCategoryServiceImpl() {
		this.categoryDAO = new VehicleCategoryDAOImpl();
	}

	@Override
	public VehicleCategoryDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		VehicleCategoryDTO category = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				category = categoryDAO.findById(connection, id);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(VehicleCategoryServiceImpl.class, "findById de VehicleCategory completado. ID: " + id));
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(VehicleCategoryServiceImpl.class, "Error en findById de VehicleCategory: "), e);
			throw new RentexpresException("Error en findById de VehicleCategory", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return category;
	}

	@Override
	public List<VehicleCategoryDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<VehicleCategoryDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			try {
				lista = categoryDAO.findAll(connection);
			} catch (DataException e) {
				e.printStackTrace();
			}
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(VehicleCategoryServiceImpl.class, "findAll de VehicleCategory completado. Cantidad: " + (lista != null ? lista.size() : 0)));
		} catch (SQLException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(VehicleCategoryServiceImpl.class, "Error en findAll de VehicleCategory: "), e);
			throw new RentexpresException("Error en findAll de VehicleCategory", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}
}
