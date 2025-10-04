package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.RentalDAO;
import com.pinguela.rentexpres.dao.impl.RentalDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalCriteria;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.RentalService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class RentalServiceImpl implements RentalService {

	private static final Logger logger = LogManager.getLogger(RentalServiceImpl.class);
	private RentalDAO rentalDAO;
	Connection connection = null;

	public RentalServiceImpl() {
		this.rentalDAO = new RentalDAOImpl();
	}

	@Override
	public RentalDTO findById(Integer id) throws RentexpresException {
		RentalDTO rental = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			rental = rentalDAO.findById(connection, id);
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(RentalServiceImpl.class, "Transacción findById de Rental completada. ID: {}"), id);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(RentalServiceImpl.class, "Error en findById de Rental: "), e);
			throw new RentexpresException("Error en findById de Rental", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return rental;
	}

	@Override
	public List<RentalDTO> findAll() throws RentexpresException {
		List<RentalDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			lista = rentalDAO.findAll(connection);
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(RentalServiceImpl.class, "Transacción findAll de Rental completada. Cantidad: {}"), (lista != null ? lista.size() : 0));
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(RentalServiceImpl.class, "Error en findAll de Rental: "), e);
			throw new RentexpresException("Error en findAll de Rental", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}

	@Override
	public boolean create(RentalDTO rental) throws RentexpresException {
		boolean creado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			creado = rentalDAO.create(connection, rental);
			if (creado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(RentalServiceImpl.class, "Rental creado exitosamente. ID: {}"), rental.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(RentalServiceImpl.class, "No se pudo crear el Rental."));
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(RentalServiceImpl.class, "Error en create Rental: "), e);
			throw new RentexpresException("Error en create Rental", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return creado;
	}

	@Override
	public boolean update(RentalDTO rental) throws RentexpresException {
		boolean actualizado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			actualizado = rentalDAO.update(connection, rental);
			if (actualizado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(RentalServiceImpl.class, "Rental actualizado exitosamente. ID: {}"), rental.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(RentalServiceImpl.class, "No se pudo actualizar el Rental. ID: {}"), rental.getId());
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(RentalServiceImpl.class, "Error en update Rental: "), e);
			throw new RentexpresException("Error en update Rental", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return actualizado;
	}

	@Override
	public boolean delete(Integer id) throws RentexpresException {
		boolean eliminado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			eliminado = rentalDAO.delete(connection, id);
			if (eliminado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(RentalServiceImpl.class, "Rental eliminado exitosamente. ID: {}"), id);
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(RentalServiceImpl.class, "No se pudo delete el Rental. ID: {}"), id);
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(RentalServiceImpl.class, "Error en delete Rental: "), e);
			throw new RentexpresException("Error en delete Rental", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return eliminado;
	}

	@Override
	public Results<RentalDTO> findByCriteria(RentalCriteria criteria) throws RentexpresException {
		Results<RentalDTO> results = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			results = rentalDAO.findByCriteria(connection, criteria);
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(RentalServiceImpl.class, "findByCriteria de Rental completado. Cantidad: {}"),
					(results != null && results.getResults() != null ? results.getResults().size() : 0));
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(RentalServiceImpl.class, "Error en findByCriteria de Rental"), e);
			throw new RentexpresException("Error en findByCriteria de Rental", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return results;
	}
	@Override
	public boolean existsByReservation(Integer reservationId) throws RentexpresException {
	    try {
	        return rentalDAO.existsByReservation(reservationId);
	    } catch (DataException e) {
	        throw new RentexpresException("Error comprobando existencia de rental para reservation ID " + reservationId, e);
	    }
	}

}
