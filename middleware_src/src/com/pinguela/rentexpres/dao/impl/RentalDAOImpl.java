package com.pinguela.rentexpres.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.RentalDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.model.RentalCriteria;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class RentalDAOImpl implements RentalDAO {

	private static final Logger logger = LogManager.getLogger(RentalDAOImpl.class);

	private static final String SELECT_BASE = "SELECT a.id_rental, a.id_reservation, a.fecha_inicio_efectivo, a.fecha_fin_efectivo, "
			+ "a.km_inicial, a.km_final, a.id_estado_rental, a.coste_total, e.name_estado AS statusName, "
			+ "r.id_vehicle, v.licensePlate, v.make, v.model, "
			+ "r.id_customer, c.name AS nameCustomer, c.lastName, c.phone " + "FROM rental a "
			+ "INNER JOIN estado_rental e ON a.id_estado_rental = e.id_estado_rental "
			+ "INNER JOIN reservation r ON a.id_reservation = r.id_reservation "
			+ "INNER JOIN vehicle v ON r.id_vehicle = v.id_vehicle "
			+ "INNER JOIN customer c ON r.id_customer = c.id_customer";

	@Override
	public RentalDTO findById(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn(LogUtils.buildMessage(RentalDAOImpl.class, "findById de Rental llamado con id nulo."));
			return null;
		}
		String sql = SELECT_BASE + " WHERE a.id_rental = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					logger.info(LogUtils.buildMessage(RentalDAOImpl.class, "Rental encontrado, id: {}"), id);
					return loadRental(rs);
				} else {
					logger.warn(LogUtils.buildMessage(RentalDAOImpl.class, "No se encontró Rental con id: {}"), id);
				}
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(RentalDAOImpl.class, "Error en findById de Rental para id: {}"), id, e);
			throw new DataException("Error en findById de Rental", e);
		}
		return null;
	}

	@Override
	public List<RentalDTO> findAll(Connection connection) throws DataException {
		List<RentalDTO> lista = new ArrayList<>();
		String sql = SELECT_BASE + " ORDER BY a.fecha_inicio_efectivo DESC";
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(loadRental(rs));
			}
			logger.info(LogUtils.buildMessage(RentalDAOImpl.class, "Total de Rentals encontrados: {}"), lista.size());
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(RentalDAOImpl.class, "Error en findAll de Rental"), e);
			throw new DataException("Error en findAll de Rental", e);
		}
		return lista;
	}

	@Override
	public boolean create(Connection connection, RentalDTO rental) throws DataException {
		if (rental == null) {
			logger.warn(LogUtils.buildMessage(RentalDAOImpl.class, "create null Rental."));
			return false;
		}
		String sql = "INSERT INTO rental (id_reservation, fecha_inicio_efectivo, fecha_fin_efectivo, km_inicial, km_final, id_estado_rental, coste_total) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			setRentalParameters(ps, rental, false);
			if (ps.executeUpdate() > 0) {
				try (ResultSet gen = ps.getGeneratedKeys()) {
					if (gen.next()) {
						rental.setId(gen.getInt(1));
					}
				}
				logger.info(LogUtils.buildMessage(RentalDAOImpl.class, "Rental creado exitosamente. Nuevo ID: {}"), rental.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(RentalDAOImpl.class, "Error al crear Rental"), e);
			throw new DataException("Error al crear Rental", e);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, RentalDTO rental) throws DataException {
		if (rental == null || rental.getId() == null) {
			logger.warn(LogUtils.buildMessage(RentalDAOImpl.class, "update null Rental o id nulo."));
			return false;
		}
		String sql = "UPDATE rental SET fecha_inicio_efectivo = ?, fecha_fin_efectivo = ?, km_inicial = ?, km_final = ?, id_estado_rental = ?, coste_total = ? "
				+ "WHERE id_rental = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			setRentalParameters(ps, rental, true);
			if (ps.executeUpdate() > 0) {
				logger.info(LogUtils.buildMessage(RentalDAOImpl.class, "Rental actualizado exitosamente. ID: {}"), rental.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(RentalDAOImpl.class, "Error al actualizar Rental"), e);
			throw new DataException("Error al actualizar Rental", e);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn(LogUtils.buildMessage(RentalDAOImpl.class, "delete null id de Rental."));
			return false;
		}
		String sql = "DELETE FROM rental WHERE id_rental = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			if (ps.executeUpdate() > 0) {
				logger.info(LogUtils.buildMessage(RentalDAOImpl.class, "Rental eliminado. ID: {}"), id);
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(RentalDAOImpl.class, "Error al delete Rental con ID: {}"), id, e);
			throw new DataException("Error al delete Rental con ID: " + id, e);
		}
		return false;
	}

	@Override
	public Results<RentalDTO> findByCriteria(Connection connection, RentalCriteria criteria) throws DataException {
		Results<RentalDTO> results = new Results<>();
		List<RentalDTO> lista = new ArrayList<>();
		int pageNumber = criteria.getPageNumber();
		int pageSize = criteria.getPageSize();
		int offset = (pageNumber - 1) * pageSize;

		StringBuilder sql = new StringBuilder(SELECT_BASE);
		sql.append(" WHERE 1=1 ");
		if (criteria.getIdRental() != null)
			sql.append(" AND a.id_rental = ? ");
		if (criteria.getReservationId() != null)
			sql.append(" AND a.id_reservation = ? ");
		if (criteria.getActualStartDate() != null && !criteria.getActualStartDate().isEmpty())
			sql.append(" AND a.fecha_inicio_efectivo LIKE ? ");
                if (criteria.getActualEndDate() != null && !criteria.getActualEndDate().isEmpty())
                        sql.append(" AND a.fecha_fin_efectivo LIKE ? ");
                if (criteria.getStartKm() != null)
                        sql.append(" AND a.km_inicial >= ? ");
                if (criteria.getEndKm() != null)
                        sql.append(" AND a.km_final <= ? ");
                if (criteria.getRentalStatusId() != null)
                        sql.append(" AND a.id_estado_rental = ? ");
                if (criteria.getTotalCost() != null)
                        sql.append(" AND a.coste_total = ? ");

                if (criteria.getCustomerId() != null)
                        sql.append(" AND r.id_customer = ? ");
                if (criteria.getName() != null && !criteria.getName().isEmpty())
                        sql.append(" AND c.name LIKE ? ");
                if (criteria.getLastName() != null && !criteria.getLastName().isEmpty())
                        sql.append(" AND c.lastName LIKE ? ");
                if (criteria.getPhone() != null && !criteria.getPhone().isEmpty())
                        sql.append(" AND c.phone LIKE ? ");

                if (criteria.getVehicleId() != null)
                        sql.append(" AND r.id_vehicle = ? ");
                if (criteria.getMake() != null && !criteria.getMake().isEmpty())
                        sql.append(" AND v.make LIKE ? ");
                if (criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty())
                        sql.append(" AND v.licensePlate LIKE ? ");
                if (criteria.getModel() != null && !criteria.getModel().isEmpty())
                        sql.append(" AND v.model LIKE ? ");
                if (criteria.getDailyPrice() != null)
                        sql.append(" AND v.precio_dia <= ? ");
		sql.append(" ORDER BY a.fecha_inicio_efectivo DESC LIMIT ? OFFSET ?");

		try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
			int idx = 1;
			if (criteria.getIdRental() != null)
				ps.setInt(idx++, criteria.getIdRental());
			if (criteria.getReservationId() != null)
				ps.setInt(idx++, criteria.getReservationId());
			if (criteria.getActualStartDate() != null && !criteria.getActualStartDate().isEmpty())
				ps.setString(idx++, "%" + criteria.getActualStartDate() + "%");
			if (criteria.getActualEndDate() != null && !criteria.getActualEndDate().isEmpty())
				ps.setString(idx++, "%" + criteria.getActualEndDate() + "%");
			if (criteria.getStartKm() != null)
				ps.setInt(idx++, criteria.getStartKm());
                        if (criteria.getEndKm() != null)
                                ps.setInt(idx++, criteria.getEndKm());
                        if (criteria.getRentalStatusId() != null)
                                ps.setInt(idx++, criteria.getRentalStatusId());
                        if (criteria.getTotalCost() != null)
                                ps.setInt(idx++, criteria.getTotalCost());

                        if (criteria.getCustomerId() != null)
                                ps.setInt(idx++, criteria.getCustomerId());
                        if (criteria.getName() != null && !criteria.getName().isEmpty())
                                ps.setString(idx++, "%" + criteria.getName() + "%");
                        if (criteria.getLastName() != null && !criteria.getLastName().isEmpty())
                                ps.setString(idx++, "%" + criteria.getLastName() + "%");
                        if (criteria.getPhone() != null && !criteria.getPhone().isEmpty())
                                ps.setString(idx++, "%" + criteria.getPhone() + "%");

                        if (criteria.getVehicleId() != null)
                                ps.setInt(idx++, criteria.getVehicleId());
                        if (criteria.getMake() != null && !criteria.getMake().isEmpty())
                                ps.setString(idx++, "%" + criteria.getMake() + "%");
                        if (criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty())
                                ps.setString(idx++, "%" + criteria.getLicensePlate() + "%");
                        if (criteria.getModel() != null && !criteria.getModel().isEmpty())
                                ps.setString(idx++, "%" + criteria.getModel() + "%");
                        if (criteria.getDailyPrice() != null)
                                ps.setDouble(idx++, criteria.getDailyPrice());

                        ps.setInt(idx++, pageSize);
                        ps.setInt(idx++, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(loadRental(rs));
				}
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(RentalDAOImpl.class, "Error ejecutando búsqueda paginada de rentals"), e);
			throw new DataException("Error ejecutando búsqueda", e);
		}

		int totalRecords = 0;
		StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM rental a ");
		countSql.append("INNER JOIN estado_rental e ON a.id_estado_rental = e.id_estado_rental ");
		countSql.append("INNER JOIN reservation r ON a.id_reservation = r.id_reservation ");
		countSql.append("INNER JOIN vehicle v ON r.id_vehicle = v.id_vehicle ");
		countSql.append("INNER JOIN customer c ON r.id_customer = c.id_customer ");
		countSql.append(" WHERE 1=1 ");
		if (criteria.getIdRental() != null)
			countSql.append(" AND a.id_rental = ? ");
		if (criteria.getReservationId() != null)
			countSql.append(" AND a.id_reservation = ? ");
		if (criteria.getActualStartDate() != null && !criteria.getActualStartDate().isEmpty())
			countSql.append(" AND a.fecha_inicio_efectivo LIKE ? ");
		if (criteria.getActualEndDate() != null && !criteria.getActualEndDate().isEmpty())
			countSql.append(" AND a.fecha_fin_efectivo LIKE ? ");
		if (criteria.getStartKm() != null)
			countSql.append(" AND a.km_inicial >= ? ");
                if (criteria.getEndKm() != null)
                        countSql.append(" AND a.km_final <= ? ");
                if (criteria.getRentalStatusId() != null)
                        countSql.append(" AND a.id_estado_rental = ? ");
                if (criteria.getTotalCost() != null)
                        countSql.append(" AND a.coste_total = ? ");

                if (criteria.getCustomerId() != null)
                        countSql.append(" AND r.id_customer = ? ");
                if (criteria.getName() != null && !criteria.getName().isEmpty())
                        countSql.append(" AND c.name LIKE ? ");
                if (criteria.getLastName() != null && !criteria.getLastName().isEmpty())
                        countSql.append(" AND c.lastName LIKE ? ");
                if (criteria.getPhone() != null && !criteria.getPhone().isEmpty())
                        countSql.append(" AND c.phone LIKE ? ");

                if (criteria.getVehicleId() != null)
                        countSql.append(" AND r.id_vehicle = ? ");
                if (criteria.getMake() != null && !criteria.getMake().isEmpty())
                        countSql.append(" AND v.make LIKE ? ");
                if (criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty())
                        countSql.append(" AND v.licensePlate LIKE ? ");
                if (criteria.getModel() != null && !criteria.getModel().isEmpty())
                        countSql.append(" AND v.model LIKE ? ");
                if (criteria.getDailyPrice() != null)
                        countSql.append(" AND v.precio_dia <= ? ");

		try (PreparedStatement countPs = connection.prepareStatement(countSql.toString())) {
			int idx = 1;
			if (criteria.getIdRental() != null)
				countPs.setInt(idx++, criteria.getIdRental());
			if (criteria.getReservationId() != null)
				countPs.setInt(idx++, criteria.getReservationId());
			if (criteria.getActualStartDate() != null && !criteria.getActualStartDate().isEmpty())
				countPs.setString(idx++, "%" + criteria.getActualStartDate() + "%");
			if (criteria.getActualEndDate() != null && !criteria.getActualEndDate().isEmpty())
				countPs.setString(idx++, "%" + criteria.getActualEndDate() + "%");
			if (criteria.getStartKm() != null)
				countPs.setInt(idx++, criteria.getStartKm());
                        if (criteria.getEndKm() != null)
                                countPs.setInt(idx++, criteria.getEndKm());
                        if (criteria.getRentalStatusId() != null)
                                countPs.setInt(idx++, criteria.getRentalStatusId());
                        if (criteria.getTotalCost() != null)
                                countPs.setInt(idx++, criteria.getTotalCost());

                        if (criteria.getCustomerId() != null)
                                countPs.setInt(idx++, criteria.getCustomerId());
                        if (criteria.getName() != null && !criteria.getName().isEmpty())
                                countPs.setString(idx++, "%" + criteria.getName() + "%");
                        if (criteria.getLastName() != null && !criteria.getLastName().isEmpty())
                                countPs.setString(idx++, "%" + criteria.getLastName() + "%");
                        if (criteria.getPhone() != null && !criteria.getPhone().isEmpty())
                                countPs.setString(idx++, "%" + criteria.getPhone() + "%");

                        if (criteria.getVehicleId() != null)
                                countPs.setInt(idx++, criteria.getVehicleId());
                        if (criteria.getMake() != null && !criteria.getMake().isEmpty())
                                countPs.setString(idx++, "%" + criteria.getMake() + "%");
                        if (criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty())
                                countPs.setString(idx++, "%" + criteria.getLicensePlate() + "%");
                        if (criteria.getModel() != null && !criteria.getModel().isEmpty())
                                countPs.setString(idx++, "%" + criteria.getModel() + "%");
                        if (criteria.getDailyPrice() != null)
                                countPs.setDouble(idx++, criteria.getDailyPrice());

			try (ResultSet rs = countPs.executeQuery()) {
				if (rs.next()) {
					totalRecords = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(RentalDAOImpl.class, "Error ejecutando conteo total de rentals"), e);
			throw new DataException("Error ejecutando conteo total", e);
		}

		results.setResults(lista);
		results.setPageNumber(pageNumber);
		results.setPageSize(pageSize);
		results.setTotalRecords(totalRecords);
		return results;
	}

	private void setRentalParameters(PreparedStatement ps, RentalDTO rental, boolean isUpdate)
			throws SQLException {
		if (!isUpdate) {
			ps.setInt(1, rental.getReservationId());
			ps.setString(2, rental.getActualStartDate());
			ps.setString(3, rental.getActualEndDate());
			ps.setInt(4, rental.getStartKm());
			ps.setInt(5, rental.getEndKm());
			ps.setInt(6, rental.getRentalStatusId());
			ps.setInt(7, rental.getTotalCost());
		} else {
			ps.setString(1, rental.getActualStartDate());
			ps.setString(2, rental.getActualEndDate());
			ps.setInt(3, rental.getStartKm());
			ps.setInt(4, rental.getEndKm());
			ps.setInt(5, rental.getRentalStatusId());
			ps.setInt(6, rental.getTotalCost());
			ps.setInt(7, rental.getId());
		}
	}

	private RentalDTO loadRental(ResultSet rs) throws SQLException {
		RentalDTO dto = new RentalDTO();
		dto.setId(rs.getInt("id_rental"));
		dto.setReservationId(rs.getInt("id_reservation"));
		dto.setActualStartDate(rs.getString("fecha_inicio_efectivo"));
		dto.setActualEndDate(rs.getString("fecha_fin_efectivo"));
		dto.setStartKm(rs.getInt("km_inicial"));
		dto.setEndKm(rs.getInt("km_final"));
		dto.setRentalStatusId(rs.getInt("id_estado_rental"));
		dto.setTotalCost(rs.getInt("coste_total"));
		dto.setStatusName(rs.getString("statusName"));
		dto.setVehicleId(rs.getInt("id_vehicle"));
		dto.setLicensePlate(rs.getString("licensePlate"));
		dto.setMake(rs.getString("make"));
		dto.setModel(rs.getString("model"));
		dto.setCustomerId(rs.getInt("id_customer"));
		dto.setName(rs.getString("nameCustomer"));
		dto.setLastName(rs.getString("lastName"));
		dto.setPhone(rs.getString("phone"));
		return dto;
	}

	@Override
	public boolean existsByReservation(Integer reservationId) throws DataException {
		final String sql = "SELECT COUNT(*) FROM rental WHERE id_reservation = ?";

		try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, reservationId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
				return false;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(RentalDAOImpl.class, "Error comprobando existencia de rental por reservation"), e);
			throw new DataException(e);
		}
	}

}
