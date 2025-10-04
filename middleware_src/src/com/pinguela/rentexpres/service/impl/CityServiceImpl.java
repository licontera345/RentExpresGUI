package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.CityDAO;
import com.pinguela.rentexpres.dao.impl.CityDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.service.CityService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

/**
 * Implementación de CityService.  
 * Gestiona la transacción y delega la lógica de persistencia al DAO.
 */
public class CityServiceImpl implements CityService {

	/* --------------------------------------------------------- */
	private static final Logger logger = LogManager.getLogger(CityServiceImpl.class);

	private final CityDAO cityDAO = new CityDAOImpl();

	/* --------------------------------------------------------- */
	@Override
	public CityDTO findById(Integer id) throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			CityDTO dto = cityDAO.findById(c, id);
			JDBCUtils.commitTransaction(c);
			logger.info(LogUtils.buildMessage(CityServiceImpl.class, "findById City OK (id={})"), id);
			return dto;
		} catch (SQLException | DataException ex) {
			logger.error(LogUtils.buildMessage(CityServiceImpl.class, "findById City ERROR"), ex);
			throw new RentexpresException("Error buscando city", ex);
		}
	}

	@Override
	public List<CityDTO> findAll() throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			List<CityDTO> list = cityDAO.findAll(c);
			JDBCUtils.commitTransaction(c);
			logger.info(LogUtils.buildMessage(CityServiceImpl.class, "findAll City OK ({} filas)"), list.size());
			return list;
		} catch (SQLException | DataException ex) {
			logger.error(LogUtils.buildMessage(CityServiceImpl.class, "findAll City ERROR"), ex);
			throw new RentexpresException("Error listando cities", ex);
		}
	}

	/* -------- NUEVO -------- */
	@Override
	public List<CityDTO> findByProvinceId(Integer idProvince) throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			List<CityDTO> list = cityDAO.findByProvinceId(c, idProvince);
			JDBCUtils.commitTransaction(c);
			logger.info(LogUtils.buildMessage(CityServiceImpl.class, "findByProvinceId City OK (prov={}, {} filas)"), idProvince, list.size());
			return list;
		} catch (SQLException | DataException ex) {
			logger.error(LogUtils.buildMessage(CityServiceImpl.class, "findByProvinceId City ERROR"), ex);
			throw new RentexpresException("Error buscando cities por province", ex);
		}
	}

	@Override
	public boolean create(CityDTO l) throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			boolean ok = cityDAO.create(c, l);
			if (ok) JDBCUtils.commitTransaction(c); else JDBCUtils.rollbackTransaction(c);
			return ok;
		} catch (SQLException | DataException ex) {
			logger.error(LogUtils.buildMessage(CityServiceImpl.class, "create City ERROR"), ex);
			throw new RentexpresException("Error creando city", ex);
		}
	}

	@Override
	public boolean update(CityDTO l) throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			boolean ok = cityDAO.update(c, l);
			if (ok) JDBCUtils.commitTransaction(c); else JDBCUtils.rollbackTransaction(c);
			return ok;
		} catch (SQLException | DataException ex) {
			logger.error(LogUtils.buildMessage(CityServiceImpl.class, "update City ERROR"), ex);
			throw new RentexpresException("Error actualizando city", ex);
		}
	}

	@Override
	public boolean delete(CityDTO l) throws RentexpresException {
		try (Connection c = JDBCUtils.getConnection()) {
			JDBCUtils.beginTransaction(c);
			boolean ok = cityDAO.delete(c, l);
			if (ok) JDBCUtils.commitTransaction(c); else JDBCUtils.rollbackTransaction(c);
			return ok;
		} catch (SQLException | DataException ex) {
			logger.error(LogUtils.buildMessage(CityServiceImpl.class, "delete City ERROR"), ex);
			throw new RentexpresException("Error eliminando city", ex);
		}
	}
}
