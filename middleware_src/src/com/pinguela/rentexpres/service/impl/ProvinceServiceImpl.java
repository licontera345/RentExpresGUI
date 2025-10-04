package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.ProvinceDAO;
import com.pinguela.rentexpres.dao.impl.ProvinceDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ProvinceDTO;
import com.pinguela.rentexpres.service.ProvinceService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class ProvinceServiceImpl implements ProvinceService {

	private static final Logger logger = LogManager.getLogger(ProvinceServiceImpl.class);
	private ProvinceDAO provinceDAO;

	public ProvinceServiceImpl() {
		this.provinceDAO = new ProvinceDAOImpl();
	}

	@Override
	public ProvinceDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		ProvinceDTO province = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			province = provinceDAO.findById(connection, id);
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(ProvinceServiceImpl.class, "findById de Province completado. ID: {}"), id);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ProvinceServiceImpl.class, "Error en findById de Province: "), e);
			throw new RentexpresException("Error en findById de Province", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return province;
	}

	@Override
	public List<ProvinceDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<ProvinceDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			lista = provinceDAO.findAll(connection);
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(ProvinceServiceImpl.class, "findAll de Province completado. Cantidad: {}"), (lista != null ? lista.size() : 0));
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ProvinceServiceImpl.class, "Error en findAll de Province: "), e);
			throw new RentexpresException("Error en findAll de Province", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}

	@Override
	public boolean create(ProvinceDTO province) throws RentexpresException {
		Connection connection = null;
		boolean creado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			creado = provinceDAO.create(connection, province);
			if (creado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(ProvinceServiceImpl.class, "Province creada exitosamente. ID: {}"), province.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(ProvinceServiceImpl.class, "No se pudo crear la Province."));
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ProvinceServiceImpl.class, "Error en create de Province: "), e);
			throw new RentexpresException("Error en create de Province", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return creado;
	}

	@Override
	public boolean update(ProvinceDTO province) throws RentexpresException {
		Connection connection = null;
		boolean actualizado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			actualizado = provinceDAO.update(connection, province);
			if (actualizado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(ProvinceServiceImpl.class, "Province actualizada exitosamente. ID: {}"), province.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(ProvinceServiceImpl.class, "No se pudo actualizar la Province. ID: {}"), province.getId());
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ProvinceServiceImpl.class, "Error en update de Province: "), e);
			throw new RentexpresException("Error en update de Province", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return actualizado;
	}

	@Override
	public boolean delete(ProvinceDTO province) throws RentexpresException {
		Connection connection = null;
		boolean eliminado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			eliminado = provinceDAO.delete(connection, province);
			if (eliminado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(ProvinceServiceImpl.class, "Province eliminada exitosamente. ID: {}"), province.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(ProvinceServiceImpl.class, "No se pudo delete la Province. ID: {}"), province.getId());
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ProvinceServiceImpl.class, "Error en delete de Province: "), e);
			throw new RentexpresException("Error en delete de Province", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return eliminado;
	}
}
