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
import org.jasypt.util.password.StrongPasswordEncryptor;
import com.pinguela.rentexpres.dao.UserDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.model.UserCriteria;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class UserDAOImpl implements UserDAO {

	private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
	private static final StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

	private static final String USUARIO_SELECT_BASE = "SELECT id_user, name_user, contrasena, id_tipo_user, name, lastName, secondLastName, phone, email FROM user";

	@Override
	public UserDTO findById(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn(LogUtils.buildMessage(UserDAOImpl.class, "findById streetd with null id."));
			return null;
		}
		String sql = USUARIO_SELECT_BASE + " WHERE id_user = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				logger.info(LogUtils.buildMessage(UserDAOImpl.class, "User encontrado con id: {}"), id);
				return loadUser(rs, false);
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(UserDAOImpl.class, "Error al buscar User por ID: {}"), id, e);
			throw new DataException("Error al buscar User por ID: " + id, e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return null;
	}

	@Override
	public boolean create(Connection connection, UserDTO user) throws DataException {
		if (user == null) {
			logger.warn(LogUtils.buildMessage(UserDAOImpl.class, "create streetd with null User."));
			return false;
		}
		String sql = "INSERT INTO user (name_user, contrasena, id_tipo_user, name, lastName, secondLastName, phone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setUserParameters(ps, user, false);
			if (ps.executeUpdate() > 0) {

				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						user.setId(generatedKeys.getInt(1));
					}
				}
				logger.info(LogUtils.buildMessage(UserDAOImpl.class, "User creado exitosamente, name_user: {}"), user.getUsername());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(UserDAOImpl.class, "Error al crear User: {}"), user.getUsername(), e);
			throw new DataException("Error al crear User: " + user.getUsername(), e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, UserDTO user) throws DataException {
		if (user == null || user.getId() == null) {
			logger.warn(LogUtils.buildMessage(UserDAOImpl.class, "update streetd with null User or id."));
			return false;
		}
                StringBuilder sql = new StringBuilder("UPDATE user SET name_user = ?,");
                boolean updatePassword = user.getContrasena() != null && !user.getContrasena().isEmpty();
                if (updatePassword) {
                        sql.append(" contrasena = ?,");
                }
                sql.append(" id_tipo_user = ?, name = ?, lastName = ?, secondLastName = ?, phone = ?, email = ? WHERE id_user = ?");
                PreparedStatement ps = null;
                try {
                        ps = connection.prepareStatement(sql.toString());
                        int idx = 1;
                        ps.setString(idx++, user.getUsername());
                        if (updatePassword) {
                                ps.setString(idx++, passwordEncryptor.encryptPassword(user.getContrasena()));
                        }
                        ps.setInt(idx++, user.getUserIdType());
                        ps.setString(idx++, user.getName());
                        ps.setString(idx++, user.getLastName());
                        ps.setString(idx++, user.getSecondLastName());
                        ps.setString(idx++, user.getPhone());
                        ps.setString(idx++, user.getEmail());
                        ps.setInt(idx++, user.getId());
			if (ps.executeUpdate() > 0) {
				logger.info(LogUtils.buildMessage(UserDAOImpl.class, "User actualizado exitosamente, id: {}"), user.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(UserDAOImpl.class, "Error al actualizar User: {}"), user.getId(), e);
			throw new DataException("Error al actualizar User: " + user.getId(), e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, UserDTO user, Integer id) throws DataException {
		if (user == null || user.getId() == null) {
			logger.warn(LogUtils.buildMessage(UserDAOImpl.class, "delete streetd with null User or id."));
			return false;
		}
		String sql = "DELETE FROM user WHERE id_user = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, user.getId());
			if (ps.executeUpdate() > 0) {
				logger.info(LogUtils.buildMessage(UserDAOImpl.class, "User eliminado, id: {}"), user.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(UserDAOImpl.class, "Error al delete User: {}"), user.getId(), e);
			throw new DataException("Error al delete User: " + user.getId(), e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public UserDTO autenticar(Connection connection, String username, String contrasenaEnClaro)
			throws DataException {
		if (username == null || contrasenaEnClaro == null) {
			logger.warn(LogUtils.buildMessage(UserDAOImpl.class, "autenticar streetd with null parameters."));
			return null;
		}
		String sql = USUARIO_SELECT_BASE + " WHERE name_user = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if (rs.next() && passwordEncryptor.checkPassword(contrasenaEnClaro, rs.getString("contrasena"))) {
				logger.info(LogUtils.buildMessage(UserDAOImpl.class, "User autenticado: {}"), username);
				return loadUser(rs, true);
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(UserDAOImpl.class, "Error al autenticar User: {}"), username, e);
			throw new DataException("Error al autenticar User: " + username, e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return null;
	}

	@Override
	public List<UserDTO> findAll(Connection connection) throws DataException {
		List<UserDTO> users = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(USUARIO_SELECT_BASE);
			rs = ps.executeQuery();
			while (rs.next()) {
				users.add(loadUser(rs, false));
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(UserDAOImpl.class, "Error al obtener todos los Users"), e);
			throw new DataException("Error al obtener todos los Users", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return users;
	}

	@Override
	public Results<UserDTO> findByCriteria(Connection connection, UserCriteria criteria) throws DataException {
		Results<UserDTO> results = new Results<>();
		List<UserDTO> listaCompleta = new ArrayList<>();

		StringBuilder sql = new StringBuilder(USUARIO_SELECT_BASE);
		sql.append(" WHERE 1=1 ");
		if (criteria.getUsername() != null && !criteria.getUsername().isEmpty()) {
			sql.append(" AND name_user LIKE ? ");
		}
		if (criteria.getName() != null && !criteria.getName().isEmpty()) {
			sql.append(" AND name LIKE ? ");
		}
		if (criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
			sql.append(" AND lastName LIKE ? ");
		}
		if (criteria.getSecondLastName() != null && !criteria.getSecondLastName().isEmpty()) {
			sql.append(" AND secondLastName LIKE ? ");
		}
		if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
			sql.append(" AND email LIKE ? ");
		}
		if (criteria.getPhone() != null && !criteria.getPhone().isEmpty()) {
			sql.append(" AND phone LIKE ? ");
		}
		sql.append(" ORDER BY id_user ");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql.toString());
			int index = 1;
			if (criteria.getUsername() != null && !criteria.getUsername().isEmpty()) {
				ps.setString(index++, "%" + criteria.getUsername() + "%");
			}
			if (criteria.getName() != null && !criteria.getName().isEmpty()) {
				ps.setString(index++, "%" + criteria.getName() + "%");
			}
			if (criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
				ps.setString(index++, "%" + criteria.getLastName() + "%");
			}
			if (criteria.getSecondLastName() != null && !criteria.getSecondLastName().isEmpty()) {
				ps.setString(index++, "%" + criteria.getSecondLastName() + "%");
			}
			if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
				ps.setString(index++, "%" + criteria.getEmail() + "%");
			}
			if (criteria.getPhone() != null && !criteria.getPhone().isEmpty()) {
				ps.setString(index++, "%" + criteria.getPhone() + "%");
			}

			rs = ps.executeQuery();
			while (rs.next()) {
				listaCompleta.add(loadUser(rs, false));
			}

			int page = criteria.getPageNumber() <= 0 ? 1 : criteria.getPageNumber();
			int size = criteria.getPageSize() <= 0 ? 25 : criteria.getPageSize();

			int totalRecords = listaCompleta.size();
			int offset = (page - 1) * size;
			int toIndex = Math.min(offset + size, totalRecords);
			List<UserDTO> paginatedList = new ArrayList<>();
			if (offset < totalRecords && offset >= 0) {
				paginatedList = listaCompleta.subList(offset, toIndex);
			}

			results.setResults(paginatedList);
			results.setPageNumber(page);
			results.setPageSize(size);
			results.setTotalRecords(totalRecords);

			logger.info(LogUtils.buildMessage(UserDAOImpl.class, "findByCriteria de User completado: Página {} (Tamaño: {}), Total registros: {}"), page, size,
					totalRecords);

		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(UserDAOImpl.class, "Error en findByCriteria de User"), e);
			throw new DataException("Error en findByCriteria de User", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return results;
	}

	private UserDTO loadUser(ResultSet rs, boolean authenticated) throws SQLException {
		UserDTO user = new UserDTO();
		user.setId(rs.getInt("id_user"));
		user.setUsername(rs.getString("name_user"));
		user.setUserIdType(rs.getInt("id_tipo_user"));
		user.setName(rs.getString("name"));
		user.setLastName(rs.getString("lastName"));
		user.setSecondLastName(rs.getString("secondLastName"));
		user.setPhone(rs.getString("phone"));
		user.setEmail(rs.getString("email"));
		user.setContrasena(authenticated ? null : rs.getString("contrasena"));
		return user;
	}

	private void setUserParameters(PreparedStatement ps, UserDTO user, boolean isUpdate) throws SQLException {
		ps.setString(1, user.getUsername());
		ps.setString(2, passwordEncryptor.encryptPassword(user.getContrasena()));
		ps.setInt(3, user.getUserIdType());
		ps.setString(4, user.getName());
		ps.setString(5, user.getLastName());
		ps.setString(6, user.getSecondLastName());
		ps.setString(7, user.getPhone());
		ps.setString(8, user.getEmail());
		if (isUpdate) {
			ps.setInt(9, user.getId());
		}
	}
}
