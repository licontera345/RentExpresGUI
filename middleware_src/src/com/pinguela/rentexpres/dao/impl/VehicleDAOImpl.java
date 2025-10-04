package com.pinguela.rentexpres.dao.impl;

import com.pinguela.rentexpres.dao.VehicleDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.util.JDBCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.pinguela.rentexpres.util.LogUtils;

public class VehicleDAOImpl implements VehicleDAO {

	private static final Logger logger = LogManager.getLogger(VehicleDAOImpl.class);

	private static final String VEHICULO_SELECT_BASE = "SELECT v.id_vehicle, v.make, v.model, v.anio_fabricacion, v.precio_dia, "
			+ "v.licensePlate, v.streetNumber_bastidor, v.kilometraje_actual, v.id_estado_vehicle, "
			+ "ev.name_estado AS vehicleStatusName, v.id_category, c.name_category AS categoryName "
			+ "FROM vehicle v " + "INNER JOIN estado_vehicle ev ON v.id_estado_vehicle = ev.id_estado_vehicle "
			+ "INNER JOIN category_vehicle c ON v.id_category = c.id_category";

	@Override
	public VehicleDTO findById(Connection connection, Integer id) throws DataException {
		VehicleDTO vehicle = null;
		String sql = VEHICULO_SELECT_BASE + " WHERE v.id_vehicle = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					vehicle = loadVehicleDTO(rs);
					logger.info(LogUtils.buildMessage(VehicleDAOImpl.class, "Vehicle encontrado con ID: {}"), id);
				}
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(VehicleDAOImpl.class, "Error al buscar Vehicle por ID: {}"), id, e);
			throw new DataException("Error al buscar Vehicle por ID: " + id, e);
		}
		return vehicle;
	}

	@Override
	public List<VehicleDTO> findAll(Connection connection) throws DataException {
		List<VehicleDTO> lista = new ArrayList<>();
		String sql = VEHICULO_SELECT_BASE;
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(loadVehicleDTO(rs));
			}
			logger.info(LogUtils.buildMessage(VehicleDAOImpl.class, "Total de Vehicles encontrados: {}"), lista.size());
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(VehicleDAOImpl.class, "Error en findAll de Vehicle"), e);
			throw new DataException("Error en findAll de Vehicle", e);
		}
		return lista;
	}

	@Override
	public boolean create(Connection connection, VehicleDTO vehicle) throws DataException {
		String sql = "INSERT INTO vehicle (make, model, anio_fabricacion, id_category, id_estado_vehicle, "
				+ "licensePlate, precio_dia, streetNumber_bastidor, kilometraje_actual) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setVehicleParameters(ps, vehicle);
			int rows = ps.executeUpdate();
			if (rows > 0) {
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						vehicle.setId(generatedKeys.getInt(1));
					}
				}
				logger.info(LogUtils.buildMessage(VehicleDAOImpl.class, "Vehicle creado exitosamente. ID: {}"), vehicle.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(VehicleDAOImpl.class, "Error al crear Vehicle"), e);
			throw new DataException("Error al crear Vehicle", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, VehicleDTO vehicle) throws DataException {
		String sql = "UPDATE vehicle SET make = ?, model = ?, anio_fabricacion = ?, id_category = ?, "
				+ "id_estado_vehicle = ?, licensePlate = ?, precio_dia = ?, streetNumber_bastidor = ?, "
				+ "kilometraje_actual = ?, WHERE id_vehicle = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setVehicleParameters(ps, vehicle);
			ps.setInt(11, vehicle.getId());
			int rows = ps.executeUpdate();
			if (rows > 0) {
				logger.info(LogUtils.buildMessage(VehicleDAOImpl.class, "Vehicle actualizado exitosamente. ID: {}"), vehicle.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(VehicleDAOImpl.class, "Error al actualizar Vehicle"), e);
			throw new DataException("Error al actualizar Vehicle", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, Integer id) throws DataException {
		String sql = "DELETE FROM vehicle WHERE id_vehicle = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(VehicleDAOImpl.class, "Error al delete Vehicle"), e);
			throw new DataException("Error al delete Vehicle", e);
		}
	}

	@Override
	public Results<VehicleDTO> findByCriteria(Connection connection, VehicleCriteria criteria) throws DataException {
		Results<VehicleDTO> results = new Results<>();
		List<VehicleDTO> lista = new ArrayList<>();
		int pageNumber = criteria.getPageNumber();
		int pageSize = criteria.getPageSize();
		int offset = (pageNumber - 1) * pageSize;

		StringBuilder sql = new StringBuilder(VEHICULO_SELECT_BASE);
		sql.append(" WHERE 1=1 ");

		if (criteria.getMake() != null && !criteria.getMake().isEmpty()) {
			sql.append(" AND v.make LIKE ? ");
		}
		if (criteria.getModel() != null && !criteria.getModel().isEmpty()) {
			sql.append(" AND v.model LIKE ? ");
		}
		if (criteria.getAnioDesde() != null) {
			sql.append(" AND v.anio_fabricacion >= ? ");
		}
		if (criteria.getAnioHasta() != null) {
			sql.append(" AND v.anio_fabricacion <= ? ");
		}
		if (criteria.getPrecioMax() != null) {
			sql.append(" AND v.precio_dia <= ? ");
		}
		if (criteria.getVehicleStatusId() != null) {
			sql.append(" AND v.id_estado_vehicle = ? ");
		}
		if (criteria.getCategoryId() != null) {
			sql.append(" AND v.id_category = ? ");
		}
		if (criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()) {
			sql.append(" AND v.licensePlate LIKE ? ");
		}
		if (criteria.getVin() != null && !criteria.getVin().isEmpty()) {
			sql.append(" AND v.streetNumber_bastidor LIKE ? ");
		}

		sql.append(" ORDER BY v.make, v.model ");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			int index = 1;

			if (criteria.getMake() != null && !criteria.getMake().isEmpty()) {
				ps.setString(index++, "%" + criteria.getMake() + "%");
			}
			if (criteria.getModel() != null && !criteria.getModel().isEmpty()) {
				ps.setString(index++, "%" + criteria.getModel() + "%");
			}
			if (criteria.getAnioDesde() != null) {
				ps.setInt(index++, criteria.getAnioDesde());
			}
			if (criteria.getAnioHasta() != null) {
				ps.setInt(index++, criteria.getAnioHasta());
			}
			if (criteria.getPrecioMax() != null) {
				ps.setDouble(index++, criteria.getPrecioMax());
			}
			if (criteria.getVehicleStatusId() != null) {
				ps.setInt(index++, criteria.getVehicleStatusId());
			}
			if (criteria.getCategoryId() != null) {
				ps.setInt(index++, criteria.getCategoryId());
			}
			if (criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()) {
				ps.setString(index++, "%" + criteria.getLicensePlate() + "%");
			}
			if (criteria.getVin() != null && !criteria.getVin().isEmpty()) {
				ps.setString(index++, "%" + criteria.getVin() + "%");
			}

			rs = ps.executeQuery();

			if (rs.absolute(offset + 1)) {
				int count = 0;
				do {
					lista.add(loadVehicleDTO(rs));
					count++;
				} while (count < pageSize && rs.next());
			}
			rs.last();
			int totalRecords = rs.getRow();

			results.setResults(lista);
			results.setPageNumber(pageNumber);
			results.setPageSize(pageSize);
			results.setTotalRecords(totalRecords);

			logger.info(LogUtils.buildMessage(VehicleDAOImpl.class, "findByCriteria de Vehicle completado: Página {} (Tamaño: {}), Total registros: {}"),
					pageNumber, pageSize, totalRecords);
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(VehicleDAOImpl.class, "Error en findByCriteria de Vehicle"), e);
			throw new DataException("Error en findByCriteria de Vehicle", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return results;
	}

	private void setVehicleParameters(PreparedStatement ps, VehicleDTO vehicle) throws SQLException {
		ps.setString(1, vehicle.getMake());
		ps.setString(2, vehicle.getModel());
		ps.setInt(3, vehicle.getManufactureYear());
		ps.setInt(4, vehicle.getCategoryId());
		ps.setInt(5, vehicle.getVehicleStatusId());
		ps.setString(6, vehicle.getLicensePlate());
		ps.setDouble(7, vehicle.getDailyPrice());
		ps.setString(8, vehicle.getVin());
		ps.setInt(9, vehicle.getCurrentMileage());
	}

	private VehicleDTO loadVehicleDTO(ResultSet rs) throws SQLException {
		VehicleDTO vehicle = new VehicleDTO();
		vehicle.setId(rs.getInt("id_vehicle"));
		vehicle.setMake(rs.getString("make"));
		vehicle.setModel(rs.getString("model"));
		vehicle.setManufactureYear(rs.getInt("anio_fabricacion"));
		vehicle.setDailyPrice(rs.getDouble("precio_dia"));
		vehicle.setLicensePlate(rs.getString("licensePlate"));
		vehicle.setVin(rs.getString("streetNumber_bastidor"));
		vehicle.setCurrentMileage(rs.getInt("kilometraje_actual"));
		vehicle.setVehicleStatusId(rs.getInt("id_estado_vehicle"));
		vehicle.setVehicleStatusName(rs.getString("vehicleStatusName"));
		vehicle.setCategoryId(rs.getInt("id_category"));
		vehicle.setCategoryName(rs.getString("categoryName"));
		return vehicle;
	}
}
