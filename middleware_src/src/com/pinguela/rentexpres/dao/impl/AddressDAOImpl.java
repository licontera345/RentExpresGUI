package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.AddressDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.AddressDTO;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class AddressDAOImpl implements AddressDAO {

	private static final Logger logger = LogManager.getLogger(AddressDAOImpl.class);

	@Override
	public AddressDTO findById(Connection connection, Integer id) throws DataException {
		AddressDTO d = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT d.id_address, d.id_city, d.street, d.streetNumber, "
					+ "l.name_city, p.name_province " + "FROM address d "
					+ "JOIN city l ON d.id_city = l.id_city "
					+ "JOIN province p ON l.id_province = p.id_province " + "WHERE d.id_address = ?";
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				d = loadAddress(rs);
				logger.info(LogUtils.buildMessage(AddressDAOImpl.class, "Dirección encontrada, id: {}"), id);
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(AddressDAOImpl.class, "Error al buscar la dirección por id: {}"), id, e);
			throw new DataException("Error al buscar la dirección por id: " + id, e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return d;
	}

	@Override
	public boolean create(Connection connection, AddressDTO address) throws DataException {
		if (address == null) {
			logger.warn(LogUtils.buildMessage(AddressDAOImpl.class, "create llamado con Address nula."));
			return false;
		}
		String sql = "INSERT INTO address (id_city, street, streetNumber) VALUES (?, ?, ?)";
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setAddressParameters(ps, address, false);
			if (ps.executeUpdate() > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					address.setId(generatedKeys.getInt(1));
				}
				logger.info(LogUtils.buildMessage(AddressDAOImpl.class, "Dirección creada con éxito, id: {}"), address.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(AddressDAOImpl.class, "Error al crear la dirección: {}"), e.getMessage(), e);
			throw new DataException("Error al crear la dirección", e);
		} finally {
			JDBCUtils.close(ps, generatedKeys);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, AddressDTO address) throws DataException {
		if (address == null || address.getId() == null) {
			logger.warn(LogUtils.buildMessage(AddressDAOImpl.class, "update llamado con dirección nula o sin id."));
			return false;
		}
		String sql = "UPDATE address SET id_city = ?, street = ?, streetNumber = ? WHERE id_address = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setAddressParameters(ps, address, true);
			int rows = ps.executeUpdate();
			if (rows > 0) {
				logger.info(LogUtils.buildMessage(AddressDAOImpl.class, "Dirección actualizada con éxito, id: {}"), address.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(AddressDAOImpl.class, "Error al actualizar la dirección: {}"), e.getMessage(), e);
			throw new DataException("Error al actualizar la dirección", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, AddressDTO address, Integer id) throws DataException {
		if (id == null) {
			logger.warn(LogUtils.buildMessage(AddressDAOImpl.class, "delete llamado con id nulo."));
			return false;
		}
		String sql = "DELETE FROM address WHERE id_address = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			if (ps.executeUpdate() > 0) {
				logger.info(LogUtils.buildMessage(AddressDAOImpl.class, "Dirección eliminada, id: {}"), id);
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(AddressDAOImpl.class, "Error al delete la dirección: {}"), e.getMessage(), e);
			throw new DataException("Error al delete la dirección", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	private AddressDTO loadAddress(ResultSet rs) throws SQLException {
		AddressDTO d = new AddressDTO();
		d.setId(rs.getInt("id_address"));
		d.setIdCity(rs.getInt("id_city"));
		d.setStreet(rs.getString("street"));
		d.setStreetNumber(rs.getString("streetNumber"));
		d.setCityName(rs.getString("name_city"));
		d.setProvinceName(rs.getString("name_province"));
		return d;
	}

	private void setAddressParameters(PreparedStatement ps, AddressDTO address, boolean isUpdate)
			throws SQLException {
		ps.setInt(1, address.getIdCity());
		ps.setString(2, address.getStreet());
		ps.setString(3, address.getStreetNumber());
		if (isUpdate) {
			ps.setInt(4, address.getId());
		}
	}
}
