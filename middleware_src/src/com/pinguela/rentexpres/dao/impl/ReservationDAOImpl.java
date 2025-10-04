package com.pinguela.rentexpres.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.ReservationDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.ReservationCriteria;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class ReservationDAOImpl implements ReservationDAO {

	private static final Logger logger = LogManager.getLogger(ReservationDAOImpl.class);

	private static final String RESERVA_SELECT_BASE = "SELECT r.id_reservation, r.id_vehicle, r.id_customer, r.fecha_inicio, r.fecha_fin, r.id_user, "
			+ "r.id_estado_reservation, e.name_estado, v.make, v.licensePlate, v.model, v.precio_dia, "
			+ "c.name, c.phone, c.lastName " + "FROM reservation r "
			+ "INNER JOIN vehicle v ON r.id_vehicle = v.id_vehicle "
			+ "INNER JOIN customer c ON r.id_customer = c.id_customer "
			+ "INNER JOIN estado_reservation e ON r.id_estado_reservation = e.id_estado_reservation";

	@Override
	public ReservationDTO findById(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn(LogUtils.buildMessage(ReservationDAOImpl.class, "findById null id."));
			return null;
		}
		String sql = RESERVA_SELECT_BASE + " WHERE r.id_reservation = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				logger.info(LogUtils.buildMessage(ReservationDAOImpl.class, "Reservation id: {}"), id);
				return loadReservationDTO(rs);
			} else {
				logger.warn(LogUtils.buildMessage(ReservationDAOImpl.class, "No se encontró Reservation con id: {}"), id);
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ReservationDAOImpl.class, "Error en findById for id: {}"), id, e);
			throw new DataException("Error en findById Reservation", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return null;
	}

	@Override
	public List<ReservationDTO> findAll(Connection connection) throws DataException {
		List<ReservationDTO> lista = new ArrayList<>();
		String sql = RESERVA_SELECT_BASE + " ORDER BY r.fecha_inicio DESC";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				lista.add(loadReservationDTO(rs));
			}
			logger.info(LogUtils.buildMessage(ReservationDAOImpl.class, "Total Reservations found: {}"), lista.size());
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ReservationDAOImpl.class, "Error en findAll Reservation"), e);
			throw new DataException("Error en findAll Reservation", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return lista;
	}

	@Override
	public boolean create(Connection connection, ReservationDTO reservation) throws DataException {
		if (reservation == null) {
			logger.warn(LogUtils.buildMessage(ReservationDAOImpl.class, "create null Reservation."));
			return false;
		}

		String sql = "INSERT INTO reservation (id_vehicle, id_customer, fecha_inicio, fecha_fin, id_user, id_estado_reservation) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setReservationParameters(ps, reservation, false);
			if (ps.executeUpdate() > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					reservation.setId(generatedKeys.getInt(1));
				}
				logger.info(LogUtils.buildMessage(ReservationDAOImpl.class, "Reservation creada exitosamente, id: {}"), reservation.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ReservationDAOImpl.class, "Error en create Reservation"), e);
			throw new DataException("Error en create Reservation", e);
		} finally {
			JDBCUtils.close(ps, generatedKeys);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, ReservationDTO reservation) throws DataException {
		if (reservation == null || reservation.getId() == null) {
			logger.warn(LogUtils.buildMessage(ReservationDAOImpl.class, "update null Reservation or id."));
			return false;
		}
		String sql = "UPDATE reservation SET id_vehicle = ?, id_customer = ?, fecha_inicio = ?, fecha_fin = ?, id_user = ?, id_estado_reservation = ? WHERE id_reservation = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setReservationParameters(ps, reservation, true);
			if (ps.executeUpdate() > 0) {
				logger.info(LogUtils.buildMessage(ReservationDAOImpl.class, "Reservation updated, id: {}"), reservation.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ReservationDAOImpl.class, "Error en update Reservation"), e);
			throw new DataException("Error en update Reservation", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn(LogUtils.buildMessage(ReservationDAOImpl.class, "delete null id."));
			return false;
		}
		String sql = "DELETE FROM reservation WHERE id_reservation = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			if (ps.executeUpdate() > 0) {
				logger.info(LogUtils.buildMessage(ReservationDAOImpl.class, "Reservation deleted, id: {}"), id);
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ReservationDAOImpl.class, "Error en delete Reservation"), e);
			throw new DataException("Error en delete Reservation", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public Results<ReservationDTO> findByCriteria(Connection connection, ReservationCriteria criteria) throws DataException {
		Results<ReservationDTO> results = new Results<>();
		List<ReservationDTO> lista = new ArrayList<>();
		int pageNumber = criteria.getPageNumber();
		int pageSize = criteria.getPageSize();
		int offset = (pageNumber - 1) * pageSize;

		StringBuilder sql = new StringBuilder(RESERVA_SELECT_BASE);
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM reservation r ");
		sql.append(" WHERE 1=1 ");
		sqlCount.append(" WHERE 1=1 ");

		List<Object> params = new ArrayList<>();

		if (criteria.getId() != null) {
			sql.append(" AND r.id_reservation = ? ");
			sqlCount.append(" AND r.id_reservation = ? ");
			params.add(criteria.getId());
		}
		if (criteria.getVehicleId() != null) {
			sql.append(" AND r.id_vehicle = ? ");
			sqlCount.append(" AND r.id_vehicle = ? ");
			params.add(criteria.getVehicleId());
		}
		if (criteria.getCustomerId() != null) {
			sql.append(" AND r.id_customer = ? ");
			sqlCount.append(" AND r.id_customer = ? ");
			params.add(criteria.getCustomerId());
		}
		if (criteria.getStartDate() != null && !criteria.getStartDate().isEmpty()) {
			sql.append(" AND r.fecha_inicio LIKE ? ");
			sqlCount.append(" AND r.fecha_inicio LIKE ? ");
			params.add("%" + criteria.getStartDate() + "%");
		}
		if (criteria.getEndDate() != null && !criteria.getEndDate().isEmpty()) {
			sql.append(" AND r.fecha_fin LIKE ? ");
			sqlCount.append(" AND r.fecha_fin LIKE ? ");
			params.add("%" + criteria.getEndDate() + "%");
		}
		if (criteria.getMake() != null && !criteria.getMake().isEmpty()) {
			sql.append(" AND v.make LIKE ? ");
			sqlCount.append(" AND r.id_vehicle IN (SELECT id_vehicle FROM vehicle v WHERE make LIKE ?) ");
			params.add("%" + criteria.getMake() + "%");
		}
		if (criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()) {
			sql.append(" AND v.licensePlate LIKE ? ");
			sqlCount.append(" AND r.id_vehicle IN (SELECT id_vehicle FROM vehicle v WHERE licensePlate LIKE ?) ");
			params.add("%" + criteria.getLicensePlate() + "%");
		}
		if (criteria.getModel() != null && !criteria.getModel().isEmpty()) {
			sql.append(" AND v.model LIKE ? ");
			sqlCount.append(" AND r.id_vehicle IN (SELECT id_vehicle FROM vehicle v WHERE model LIKE ?) ");
			params.add("%" + criteria.getModel() + "%");
		}
		if (criteria.getDailyPrice() != null) {
			sql.append(" AND v.precio_dia <= ? ");
			sqlCount.append(" AND r.id_vehicle IN (SELECT id_vehicle FROM vehicle v WHERE precio_dia <= ?) ");
			params.add(criteria.getDailyPrice());
		}
		if (criteria.getName() != null && !criteria.getName().isEmpty()) {
			sql.append(" AND c.name LIKE ? ");
			sqlCount.append(" AND r.id_customer IN (SELECT id_customer FROM customer c WHERE name LIKE ?) ");
			params.add("%" + criteria.getName() + "%");
		}
		if (criteria.getPhone() != null && !criteria.getPhone().isEmpty()) {
			sql.append(" AND c.phone LIKE ? ");
			sqlCount.append(" AND r.id_customer IN (SELECT id_customer FROM customer c WHERE phone LIKE ?) ");
			params.add("%" + criteria.getPhone() + "%");
		}
		if (criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
			sql.append(" AND c.lastName LIKE ? ");
			sqlCount.append(" AND r.id_customer IN (SELECT id_customer FROM customer c WHERE lastName LIKE ?) ");
			params.add("%" + criteria.getLastName() + "%");
		}

		sql.append(" ORDER BY r.fecha_inicio DESC LIMIT ? OFFSET ?");
		params.add(pageSize);
		params.add(offset);

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				lista.add(loadReservationDTO(rs));
			}
			JDBCUtils.close(ps, rs);

			// COUNT
			ps = connection.prepareStatement(sqlCount.toString());
			for (int i = 0; i < params.size() - 2; i++) { // -2 porque LIMIT y OFFSET no van en el COUNT
				ps.setObject(i + 1, params.get(i));
			}
			rs = ps.executeQuery();
			int totalRecords = 0;
			if (rs.next()) {
				totalRecords = rs.getInt(1);
			}

			results.setResults(lista);
			results.setPageNumber(pageNumber);
			results.setPageSize(pageSize);
			results.setTotalRecords(totalRecords);

			logger.info(LogUtils.buildMessage(ReservationDAOImpl.class, "findByCriteria de Reservation completado: Página {} (Tamaño: {}), Total registros: {}"),
					pageNumber, pageSize, totalRecords);
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ReservationDAOImpl.class, "Error en findByCriteria de Reservation"), e);
			throw new DataException("Error en findByCriteria de Reservation", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return results;
	}

	private void setReservationParameters(PreparedStatement ps, ReservationDTO reservation, boolean isUpdate) throws SQLException {
		ps.setInt(1, reservation.getVehicleId());
		ps.setInt(2, reservation.getCustomerId());
		ps.setString(3, reservation.getStartDate());
		ps.setString(4, reservation.getEndDate());
		ps.setInt(5, reservation.getUserId());
		if (reservation.getReservationIdStatus() != null) {
			ps.setInt(6, reservation.getReservationIdStatus());
		} else {
			ps.setNull(6, java.sql.Types.INTEGER);
		}
		if (isUpdate) {
			ps.setInt(7, reservation.getId());
		}
	}

	private ReservationDTO loadReservationDTO(ResultSet rs) throws SQLException {
		ReservationDTO dto = new ReservationDTO();
		dto.setId(rs.getInt("id_reservation"));
		dto.setVehicleId(rs.getInt("id_vehicle"));
		dto.setCustomerId(rs.getInt("id_customer"));
		dto.setStartDate(rs.getString("fecha_inicio"));
		dto.setEndDate(rs.getString("fecha_fin"));
		dto.setUserId(rs.getInt("id_user"));
		dto.setReservationIdStatus(rs.getInt("id_estado_reservation"));
		dto.setNameReservationStatus(rs.getString("name_estado"));
		dto.setMake(rs.getString("make"));
		dto.setLicensePlate(rs.getString("licensePlate"));
		dto.setModel(rs.getString("model"));
		dto.setDailyPrice(rs.getDouble("precio_dia"));
		dto.setName(rs.getString("name"));
		dto.setPhone(rs.getString("phone"));
		dto.setLastName(rs.getString("lastName"));
		return dto;
	}
}
