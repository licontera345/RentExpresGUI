package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.CustomerDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.CustomerCriteria;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class CustomerDAOImpl implements CustomerDAO {

	private static final Logger logger = LogManager.getLogger(CustomerDAOImpl.class);
	private static final String CLIENTE_SELECT_BASE = "SELECT c.id_customer, c.name, c.lastName, c.secondLastName, "
			+ "c.fecha_nacimiento, c.phone, c.email, c.id_address, "
			+ "d.street, d.streetNumber, l.name_city, p.name_province " + "FROM customer c "
			+ "LEFT JOIN address d ON c.id_address = d.id_address "
			+ "LEFT JOIN city l ON d.id_city = l.id_city "
			+ "LEFT JOIN province p ON l.id_province = p.id_province";

	@Override
	public CustomerDTO findById(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn(LogUtils.buildMessage(CustomerDAOImpl.class, "findById null id."));
			return null;
		}
		String sql = CLIENTE_SELECT_BASE + " WHERE c.id_customer = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				logger.info(LogUtils.buildMessage(CustomerDAOImpl.class, "Customer found with id: {}"), id);
				return loadCustomer(rs);
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(CustomerDAOImpl.class, "Error en findById for id: {}"), id, e);
			throw new DataException("Error en findById Customer", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return null;
	}

	@Override
	public List<CustomerDTO> findAll(Connection connection) throws DataException {
		List<CustomerDTO> lista = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(CLIENTE_SELECT_BASE);
			rs = ps.executeQuery();
			while (rs.next()) {
				lista.add(loadCustomer(rs));
			}
			logger.info(LogUtils.buildMessage(CustomerDAOImpl.class, "Total Customers BD: {}"), lista.size());
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(CustomerDAOImpl.class, "Error en findAll Customer"), e);
			throw new DataException("Error en findAll Customer", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return lista;
	}

	@Override
	public boolean create(Connection connection, CustomerDTO customer) throws DataException {
		if (customer == null) {
			logger.warn(LogUtils.buildMessage(CustomerDAOImpl.class, "create llamada con Customer nulo."));
			return false;
		}
		String sql = "INSERT INTO customer (name, lastName, secondLastName, fecha_nacimiento, email, phone, id_address) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		try {
			ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			setCustomerParameters(ps, customer, false);
			if (ps.executeUpdate() > 0) {
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					customer.setId(generatedKeys.getInt(1));
				}
				logger.info(LogUtils.buildMessage(CustomerDAOImpl.class, "Customer creado con éxito, id: {}"), customer.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(CustomerDAOImpl.class, "Error en create Customer"), e);
			throw new DataException("Error en create Customer", e);
		} finally {
			JDBCUtils.close(ps, generatedKeys);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, CustomerDTO customer) throws DataException {
		if (customer == null || customer.getId() == null) {
			logger.warn(LogUtils.buildMessage(CustomerDAOImpl.class, "update llamado con Customer o id nulo."));
			return false;
		}
		String sql = "UPDATE customer SET name = ?, lastName = ?, secondLastName = ?, fecha_nacimiento = ?, "
				+ "email = ?, phone = ?, id_address = ? WHERE id_customer = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			setCustomerParameters(ps, customer, true);
			if (ps.executeUpdate() > 0) {
				logger.info(LogUtils.buildMessage(CustomerDAOImpl.class, "Customer actualizado correctamente, id: {}"), customer.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(CustomerDAOImpl.class, "Error en update Customer"), e);
			throw new DataException("Error en update Customer", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn(LogUtils.buildMessage(CustomerDAOImpl.class, "delete llamado con null id."));
			return false;
		}
		String sql = "DELETE FROM customer WHERE id_customer = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			if (ps.executeUpdate() > 0) {
				logger.info(LogUtils.buildMessage(CustomerDAOImpl.class, "Customer eliminado, id: {}"), id);
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(CustomerDAOImpl.class, "Error en delete Customer"), e);
			throw new DataException("Error en delete Customer", e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public Results<CustomerDTO> findByCriteria(Connection connection, CustomerCriteria criteria) throws DataException {
		Results<CustomerDTO> results = new Results<>();
		List<CustomerDTO> lista = new ArrayList<>();
		int pageNumber = criteria.getPageNumber();
		int pageSize = criteria.getPageSize();
		int offset = (pageNumber - 1) * pageSize;

		StringBuilder sql = new StringBuilder();
		sql.append(CLIENTE_SELECT_BASE);
		sql.append(" WHERE 1=1 ");

		// FILTRO POR ID
		if (criteria.getId() != null && criteria.getId() > 0) {
			sql.append(" AND c.id_customer = ? ");
		}
		// Resto de filtros (name, apellidos, email, tel, street, streetNumber, city,
		// province)
		if (criteria.getName() != null && !criteria.getName().isEmpty()) {
			sql.append(" AND c.name LIKE ? ");
		}
		if (criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
			sql.append(" AND c.lastName LIKE ? ");
		}
		if (criteria.getSecondLastName() != null && !criteria.getSecondLastName().isEmpty()) {
			sql.append(" AND c.secondLastName LIKE ? ");
		}
		if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
			sql.append(" AND c.email LIKE ? ");
		}
		if (criteria.getPhone() != null && !criteria.getPhone().isEmpty()) {
			sql.append(" AND c.phone LIKE ? ");
		}
		if (criteria.getStreet() != null && !criteria.getStreet().isEmpty()) {
			sql.append(" AND d.street LIKE ? ");
		}
		if (criteria.getStreetNumber() != null && !criteria.getStreetNumber().isEmpty()) {
			sql.append(" AND d.streetNumber LIKE ? ");
		}
		if (criteria.getCityName() != null && !criteria.getCityName().isEmpty()) {
			sql.append(" AND l.name_city LIKE ? ");
		}
		if (criteria.getProvinceName() != null && !criteria.getProvinceName().isEmpty()) {
			sql.append(" AND p.name_province LIKE ? ");
		}
		sql.append(" ORDER BY c.name ");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			int idx = 1;
			if (criteria.getId() != null && criteria.getId() > 0) {
				ps.setInt(idx++, criteria.getId());
			}
			if (criteria.getName() != null && !criteria.getName().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getName() + "%");
			}
			if (criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getLastName() + "%");
			}
			if (criteria.getSecondLastName() != null && !criteria.getSecondLastName().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getSecondLastName() + "%");
			}
			if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getEmail() + "%");
			}
			if (criteria.getPhone() != null && !criteria.getPhone().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getPhone() + "%");
			}
			if (criteria.getStreet() != null && !criteria.getStreet().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getStreet() + "%");
			}
			if (criteria.getStreetNumber() != null && !criteria.getStreetNumber().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getStreetNumber() + "%");
			}
			if (criteria.getCityName() != null && !criteria.getCityName().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getCityName() + "%");
			}
			if (criteria.getProvinceName() != null && !criteria.getProvinceName().isEmpty()) {
				ps.setString(idx++, "%" + criteria.getProvinceName() + "%");
			}

			rs = ps.executeQuery();
			if (rs.absolute(offset + 1)) {
				int count = 0;
				do {
					CustomerDTO dto = new CustomerDTO();
					dto.setId(rs.getInt("id_customer"));
					dto.setName(rs.getString("name"));
					dto.setLastName(rs.getString("lastName"));
					dto.setSecondLastName(rs.getString("secondLastName"));
					dto.setPhone(rs.getString("phone"));
					dto.setEmail(rs.getString("email"));
					dto.setStreet(rs.getString("street"));
					dto.setStreetNumber(rs.getString("streetNumber"));
					dto.setCityName(rs.getString("name_city"));
					dto.setProvinceName(rs.getString("name_province"));
					lista.add(dto);
					count++;
				} while (count < pageSize && rs.next());
			}
			rs.last();
			int totalRecords = rs.getRow();

			results.setResults(lista);
			results.setPageNumber(pageNumber);
			results.setPageSize(pageSize);
			results.setTotalRecords(totalRecords);

			logger.info(LogUtils.buildMessage(CustomerDAOImpl.class, "findByCriteria de Customer completado: Página {} (Tamaño: {}), Total registros: {}"), pageNumber,
					pageSize, totalRecords);
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(CustomerDAOImpl.class, "Error en findByCriteria de Customer"), e);
			throw new DataException("Error en findByCriteria de Customer", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return results;
	}

	private CustomerDTO loadCustomer(ResultSet rs) throws SQLException {
		CustomerDTO c = new CustomerDTO();
		c.setId(rs.getInt("id_customer"));
		c.setName(rs.getString("name"));
		c.setLastName(rs.getString("lastName"));
		c.setSecondLastName(rs.getString("secondLastName"));
		c.setBirthDate(rs.getString("fecha_nacimiento"));
		c.setEmail(rs.getString("email"));
		c.setPhone(rs.getString("phone"));
		c.setAddressId(rs.getInt("id_address"));
		c.setStreet(rs.getString("street"));
		c.setStreetNumber(rs.getString("streetNumber"));
		c.setCityName(rs.getString("name_city"));
		c.setProvinceName(rs.getString("name_province"));
		return c;
	}

	private void setCustomerParameters(PreparedStatement ps, CustomerDTO customer, boolean isUpdate) throws SQLException {
		ps.setString(1, customer.getName());
		ps.setString(2, customer.getLastName());
		ps.setString(3, customer.getSecondLastName());
		ps.setString(4, customer.getBirthDate());
		ps.setString(5, customer.getEmail());
		ps.setString(6, customer.getPhone());
		ps.setInt(7, customer.getAddressId());
		if (isUpdate) {
			ps.setInt(8, customer.getId());
		}
	}
}
