package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.CityDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.util.LogUtils;

public class CityDAOImpl implements CityDAO {

	private static final Logger logger = LogManager.getLogger(CityDAOImpl.class);

	private static final String BASE_SELECT = "SELECT id_city, name_city, id_province FROM city";

	@Override
	public CityDTO findById(Connection c, Integer id) throws DataException {
		String sql = BASE_SELECT + " WHERE id_city = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? load(rs) : null;
			}
		} catch (SQLException ex) {
			logger.error(LogUtils.buildMessage(CityDAOImpl.class, "findById {}"), id, ex);
			throw new DataException("Error buscando city", ex);
		}
	}

	@Override
	public List<CityDTO> findAll(Connection c) throws DataException {
		try (PreparedStatement ps = c.prepareStatement(BASE_SELECT); ResultSet rs = ps.executeQuery()) {

			List<CityDTO> out = new ArrayList<>();
			while (rs.next())
				out.add(load(rs));
			return out;

		} catch (SQLException ex) {
			logger.error(LogUtils.buildMessage(CityDAOImpl.class, "findAll"), ex);
			throw new DataException("Error listando cities", ex);
		}
	}

	@Override
	public List<CityDTO> findByProvinceId(Connection c, Integer idProvince) throws DataException {
		String sql = BASE_SELECT + " WHERE id_province = ? ORDER BY name_city";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, idProvince);
			try (ResultSet rs = ps.executeQuery()) {
				List<CityDTO> out = new ArrayList<>();
				while (rs.next())
					out.add(load(rs));
				return out;
			}
		} catch (SQLException ex) {
			logger.error(LogUtils.buildMessage(CityDAOImpl.class, "findByProvinceId {}"), idProvince, ex);
			throw new DataException("Error buscando cities por province", ex);
		}
	}

	
	@Override
	public boolean create(Connection c, CityDTO l) throws DataException {
		String sql = "INSERT INTO city (name_city, id_province) VALUES (?, ?)";
		try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			set(ps, l, false);
			if (ps.executeUpdate() > 0) {
				try (ResultSet keys = ps.getGeneratedKeys()) {
					if (keys.next())
						l.setId(keys.getInt(1));
				}
				return true;
			}
			return false;
		} catch (SQLException ex) {
			throw new DataException("Error insertando city", ex);
		}
	}

	@Override
	public boolean update(Connection c, CityDTO l) throws DataException {
		String sql = "UPDATE city SET name_city = ?, id_province = ? WHERE id_city = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			set(ps, l, true);
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			throw new DataException("Error actualizando city", ex);
		}
	}

	@Override
	public boolean delete(Connection c, CityDTO l) throws DataException {
		String sql = "DELETE FROM city WHERE id_city = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, l.getId());
			return ps.executeUpdate() > 0;
		} catch (SQLException ex) {
			throw new DataException("Error eliminando city", ex);
		}
	}

	
	private static void set(PreparedStatement ps, CityDTO l, boolean update) throws SQLException {
		ps.setString(1, l.getName());
		ps.setInt(2, l.getIdProvince());
		if (update)
			ps.setInt(3, l.getId());
	}

	private static CityDTO load(ResultSet rs) throws SQLException {
		CityDTO l = new CityDTO();
		l.setId(rs.getInt("id_city"));
		l.setName(rs.getString("name_city"));
		l.setIdProvince(rs.getInt("id_province"));
		return l;
	}
}
