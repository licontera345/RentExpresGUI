package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.ReservationDAO;
import com.pinguela.rentexpres.dao.impl.ReservationDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationCriteria;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.ReservationService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class ReservationServiceImpl implements ReservationService {

	private static final Logger logger = LogManager.getLogger(ReservationServiceImpl.class);
	private ReservationDAO reservationDAO;

	public ReservationServiceImpl() {
		this.reservationDAO = new ReservationDAOImpl();
	}

	@Override
	public ReservationDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		ReservationDTO reservation = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			reservation = reservationDAO.findById(connection, id);
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(ReservationServiceImpl.class, "Transacción findById de Reservation completada. ID: {}"), id);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ReservationServiceImpl.class, "Error en findById de Reservation: "), e);
			throw new RentexpresException("Error en findById de Reservation", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return reservation;
	}

	@Override
	public List<ReservationDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<ReservationDTO> reservations = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			reservations = reservationDAO.findAll(connection);
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(ReservationServiceImpl.class, "Transacción findAll de Reservation completada. Cantidad: {}"),
					reservations != null ? reservations.size() : 0);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ReservationServiceImpl.class, "Error en findAll de Reservation: "), e);
			throw new RentexpresException("Error en findAll de Reservation", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return reservations;
	}

	@Override
	public boolean create(ReservationDTO reservation) throws RentexpresException {
		Connection connection = null;
		boolean creado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			creado = reservationDAO.create(connection, reservation);
			if (creado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(ReservationServiceImpl.class, "Reservation creada exitosamente. ID: {}"), reservation.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(ReservationServiceImpl.class, "No se pudo crear la Reservation."));
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ReservationServiceImpl.class, "Error en create Reservation: "), e);
			throw new RentexpresException("Error en create Reservation", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return creado;
	}

	@Override
	public boolean update(ReservationDTO reservation) throws RentexpresException {
		Connection connection = null;
		boolean actualizado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			actualizado = reservationDAO.update(connection, reservation);
			if (actualizado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(ReservationServiceImpl.class, "Reservation actualizada exitosamente. ID: {}"), reservation.getId());
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(ReservationServiceImpl.class, "No se pudo actualizar la Reservation. ID: {}"), reservation.getId());
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ReservationServiceImpl.class, "Error en update Reservation: "), e);
			throw new RentexpresException("Error en update Reservation", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return actualizado;
	}

	@Override
	public boolean delete(Integer id) throws RentexpresException {
		Connection connection = null;
		boolean eliminado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			eliminado = reservationDAO.delete(connection, id);
			if (eliminado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(ReservationServiceImpl.class, "Reservation eliminada exitosamente. ID: {}"), id);
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(ReservationServiceImpl.class, "No se pudo delete la Reservation. ID: {}"), id);
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ReservationServiceImpl.class, "Error en delete Reservation: "), e);
			throw new RentexpresException("Error en delete Reservation", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return eliminado;
	}

	@Override
	public Results<ReservationDTO> findByCriteria(ReservationCriteria criteria) throws RentexpresException {
		Connection connection = null;
		Results<ReservationDTO> results = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);
			results = reservationDAO.findByCriteria(connection, criteria);
			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(ReservationServiceImpl.class, "findByCriteria de Reservation completado: Página {} (Tamaño: {}), Total registros: {}"),
					criteria.getPageNumber(), criteria.getPageSize(), results.getTotalRecords());
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(ReservationServiceImpl.class, "Error en findByCriteria de Reservation: "), e);
			throw new RentexpresException("Error en findByCriteria de Reservation", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return results;
	}

}
