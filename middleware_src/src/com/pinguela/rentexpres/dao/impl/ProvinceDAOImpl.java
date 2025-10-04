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
import com.pinguela.rentexpres.dao.ProvinceDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ProvinceDTO;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class ProvinceDAOImpl implements ProvinceDAO {

	private static final Logger logger = LogManager.getLogger(ProvinceDAOImpl.class);

	@Override
	public ProvinceDTO findById(Connection connection, Integer id) throws DataException {
		ProvinceDTO prov = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT id_province, name_province FROM province WHERE id_province = ?";
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				prov = loadProvince(rs);
				logger.info(LogUtils.buildMessage(ProvinceDAOImpl.class, "Province encontrada, id: {}"), id);
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ProvinceDAOImpl.class, "Error al buscar Province por ID: {}"), id, e);
			throw new DataException("Error al buscar Province por ID: " + id, e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return prov;
	}

	@Override
	public List<ProvinceDTO> findAll(Connection connection) throws DataException {
		List<ProvinceDTO> lista = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT id_province, name_province FROM province";
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				lista.add(loadProvince(rs));
			}
			logger.info(LogUtils.buildMessage(ProvinceDAOImpl.class, "Total de Provinces encontradas: {}"), lista.size());
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ProvinceDAOImpl.class, "Error al obtener todas las Provinces"), e);
			throw new DataException("Error al obtener todas las Provinces", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return lista;
	}

	@Override
	public boolean create(Connection connection, ProvinceDTO province) throws DataException {
		if (province == null) {
			logger.warn(LogUtils.buildMessage(ProvinceDAOImpl.class, "create llamado con Province nula."));
			return false;
		}
		String sql = "INSERT INTO province (name_province) VALUES (?)";
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setProvinceParameters(ps, province, false);
			if (ps.executeUpdate() > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					province.setId(generatedKeys.getInt(1));
				}
				logger.info(LogUtils.buildMessage(ProvinceDAOImpl.class, "Province creada con éxito, id: {}"), province.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ProvinceDAOImpl.class, "Error al crear Province"), e);
			throw new DataException("Error al crear Province", e);
		} finally {
			JDBCUtils.close(ps, generatedKeys);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, ProvinceDTO province) throws DataException {
		if (province == null || province.getId() == null) {
			logger.warn(LogUtils.buildMessage(ProvinceDAOImpl.class, "update llamado con Province nula o sin id."));
			return false;
		}
		String sql = "UPDATE province SET name_province = ? WHERE id_province = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setProvinceParameters(ps, province, true);
			int rows = ps.executeUpdate();
			if (rows > 0) {
				logger.info(LogUtils.buildMessage(ProvinceDAOImpl.class, "Province actualizada con éxito, id: {}"), province.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ProvinceDAOImpl.class, "Error al actualizar Province: {}"), e.getMessage(), e);
			throw new DataException("Error al actualizar Province", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, ProvinceDTO province) throws DataException {
		if (province == null || province.getId() == null) {
			logger.warn(LogUtils.buildMessage(ProvinceDAOImpl.class, "delete llamado con Province nula o sin id."));
			return false;
		}
		String sql = "DELETE FROM province WHERE id_province = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, province.getId());
			if (ps.executeUpdate() > 0) {
				logger.info(LogUtils.buildMessage(ProvinceDAOImpl.class, "Province eliminada, id: {}"), province.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(ProvinceDAOImpl.class, "Error al delete Province: {}"), e.getMessage(), e);
			throw new DataException("Error al delete Province", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	private void setProvinceParameters(PreparedStatement ps, ProvinceDTO province, boolean isUpdate)
			throws SQLException {
		ps.setString(1, province.getName());
		if (isUpdate) {
			ps.setInt(2, province.getId());
		}
	}

	private ProvinceDTO loadProvince(ResultSet rs) throws SQLException {
		ProvinceDTO p = new ProvinceDTO();
		p.setId(rs.getInt("id_province"));
		p.setName(rs.getString("name_province"));
		return p;
	}
}
