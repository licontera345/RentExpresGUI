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
import com.pinguela.rentexpres.dao.UsuarioDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.model.UsuarioCriteria;
import com.pinguela.rentexpres.util.JDBCUtils;

public class UsuarioDAOImpl implements UsuarioDAO {

	private static final Logger logger = LogManager.getLogger(UsuarioDAOImpl.class);
	private static final StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

	private static final String USUARIO_SELECT_BASE = "SELECT id_usuario, nombre_usuario, contrasena, id_tipo_usuario, nombre, apellido1, apellido2, telefono, email FROM usuario";

	@Override
	public UsuarioDTO findById(Connection connection, Integer id) throws DataException {
		if (id == null) {
			logger.warn("findById called with null id.");
			return null;
		}
		String sql = USUARIO_SELECT_BASE + " WHERE id_usuario = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				logger.info("Usuario encontrado con id: {}", id);
				return loadUsuario(rs, false);
			}
		} catch (SQLException e) {
			logger.error("Error al buscar Usuario por ID: {}", id, e);
			throw new DataException("Error al buscar Usuario por ID: " + id, e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return null;
	}

	@Override
	public boolean create(Connection connection, UsuarioDTO usuario) throws DataException {
		if (usuario == null) {
			logger.warn("create called with null Usuario.");
			return false;
		}
		String sql = "INSERT INTO usuario (nombre_usuario, contrasena, id_tipo_usuario, nombre, apellido1, apellido2, telefono, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setUsuarioParameters(ps, usuario, false);
			if (ps.executeUpdate() > 0) {

				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						usuario.setId(generatedKeys.getInt(1));
					}
				}
				logger.info("Usuario creado exitosamente, nombre_usuario: {}", usuario.getNombreUsuario());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al crear Usuario: {}", usuario.getNombreUsuario(), e);
			throw new DataException("Error al crear Usuario: " + usuario.getNombreUsuario(), e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean update(Connection connection, UsuarioDTO usuario) throws DataException {
		if (usuario == null || usuario.getId() == null) {
			logger.warn("update called with null Usuario or id.");
			return false;
		}
                StringBuilder sql = new StringBuilder("UPDATE usuario SET nombre_usuario = ?,");
                boolean updatePassword = usuario.getContrasena() != null && !usuario.getContrasena().isEmpty();
                if (updatePassword) {
                        sql.append(" contrasena = ?,");
                }
                sql.append(" id_tipo_usuario = ?, nombre = ?, apellido1 = ?, apellido2 = ?, telefono = ?, email = ? WHERE id_usuario = ?");
                PreparedStatement ps = null;
                try {
                        ps = connection.prepareStatement(sql.toString());
                        int idx = 1;
                        ps.setString(idx++, usuario.getNombreUsuario());
                        if (updatePassword) {
                                ps.setString(idx++, passwordEncryptor.encryptPassword(usuario.getContrasena()));
                        }
                        ps.setInt(idx++, usuario.getIdTipoUsuario());
                        ps.setString(idx++, usuario.getNombre());
                        ps.setString(idx++, usuario.getApellido1());
                        ps.setString(idx++, usuario.getApellido2());
                        ps.setString(idx++, usuario.getTelefono());
                        ps.setString(idx++, usuario.getEmail());
                        ps.setInt(idx++, usuario.getId());
			if (ps.executeUpdate() > 0) {
				logger.info("Usuario actualizado exitosamente, id: {}", usuario.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al actualizar Usuario: {}", usuario.getId(), e);
			throw new DataException("Error al actualizar Usuario: " + usuario.getId(), e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public boolean delete(Connection connection, UsuarioDTO usuario, Integer id) throws DataException {
		if (usuario == null || usuario.getId() == null) {
			logger.warn("delete called with null Usuario or id.");
			return false;
		}
		String sql = "DELETE FROM usuario WHERE id_usuario = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, usuario.getId());
			if (ps.executeUpdate() > 0) {
				logger.info("Usuario eliminado, id: {}", usuario.getId());
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error al eliminar Usuario: {}", usuario.getId(), e);
			throw new DataException("Error al eliminar Usuario: " + usuario.getId(), e);
		} finally {
			JDBCUtils.close(ps, null);
		}
		return false;
	}

	@Override
	public UsuarioDTO autenticar(Connection connection, String nombreUsuario, String contrasenaEnClaro)
			throws DataException {
		if (nombreUsuario == null || contrasenaEnClaro == null) {
			logger.warn("autenticar called with null parameters.");
			return null;
		}
		String sql = USUARIO_SELECT_BASE + " WHERE nombre_usuario = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, nombreUsuario);
			rs = ps.executeQuery();
			if (rs.next() && passwordEncryptor.checkPassword(contrasenaEnClaro, rs.getString("contrasena"))) {
				logger.info("Usuario autenticado: {}", nombreUsuario);
				return loadUsuario(rs, true);
			}
		} catch (SQLException e) {
			logger.error("Error al autenticar Usuario: {}", nombreUsuario, e);
			throw new DataException("Error al autenticar Usuario: " + nombreUsuario, e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return null;
	}

	@Override
	public List<UsuarioDTO> findAll(Connection connection) throws DataException {
		List<UsuarioDTO> usuarios = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(USUARIO_SELECT_BASE);
			rs = ps.executeQuery();
			while (rs.next()) {
				usuarios.add(loadUsuario(rs, false));
			}
		} catch (SQLException e) {
			logger.error("Error al obtener todos los Usuarios", e);
			throw new DataException("Error al obtener todos los Usuarios", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return usuarios;
	}

	@Override
	public Results<UsuarioDTO> findByCriteria(Connection connection, UsuarioCriteria criteria) throws DataException {
		Results<UsuarioDTO> results = new Results<>();
		List<UsuarioDTO> listaCompleta = new ArrayList<>();

		StringBuilder sql = new StringBuilder(USUARIO_SELECT_BASE);
		sql.append(" WHERE 1=1 ");
		if (criteria.getNombreUsuario() != null && !criteria.getNombreUsuario().isEmpty()) {
			sql.append(" AND nombre_usuario LIKE ? ");
		}
		if (criteria.getNombre() != null && !criteria.getNombre().isEmpty()) {
			sql.append(" AND nombre LIKE ? ");
		}
		if (criteria.getApellido1() != null && !criteria.getApellido1().isEmpty()) {
			sql.append(" AND apellido1 LIKE ? ");
		}
		if (criteria.getApellido2() != null && !criteria.getApellido2().isEmpty()) {
			sql.append(" AND apellido2 LIKE ? ");
		}
		if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
			sql.append(" AND email LIKE ? ");
		}
		if (criteria.getTelefono() != null && !criteria.getTelefono().isEmpty()) {
			sql.append(" AND telefono LIKE ? ");
		}
		sql.append(" ORDER BY id_usuario ");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql.toString());
			int index = 1;
			if (criteria.getNombreUsuario() != null && !criteria.getNombreUsuario().isEmpty()) {
				ps.setString(index++, "%" + criteria.getNombreUsuario() + "%");
			}
			if (criteria.getNombre() != null && !criteria.getNombre().isEmpty()) {
				ps.setString(index++, "%" + criteria.getNombre() + "%");
			}
			if (criteria.getApellido1() != null && !criteria.getApellido1().isEmpty()) {
				ps.setString(index++, "%" + criteria.getApellido1() + "%");
			}
			if (criteria.getApellido2() != null && !criteria.getApellido2().isEmpty()) {
				ps.setString(index++, "%" + criteria.getApellido2() + "%");
			}
			if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
				ps.setString(index++, "%" + criteria.getEmail() + "%");
			}
			if (criteria.getTelefono() != null && !criteria.getTelefono().isEmpty()) {
				ps.setString(index++, "%" + criteria.getTelefono() + "%");
			}

			rs = ps.executeQuery();
			while (rs.next()) {
				listaCompleta.add(loadUsuario(rs, false));
			}

			int page = criteria.getPageNumber() <= 0 ? 1 : criteria.getPageNumber();
			int size = criteria.getPageSize() <= 0 ? 25 : criteria.getPageSize();

			int totalRecords = listaCompleta.size();
			int offset = (page - 1) * size;
			int toIndex = Math.min(offset + size, totalRecords);
			List<UsuarioDTO> paginatedList = new ArrayList<>();
			if (offset < totalRecords && offset >= 0) {
				paginatedList = listaCompleta.subList(offset, toIndex);
			}

			results.setResults(paginatedList);
			results.setPageNumber(page);
			results.setPageSize(size);
			results.setTotalRecords(totalRecords);

			logger.info("findByCriteria de Usuario completado: Página {} (Tamaño: {}), Total registros: {}", page, size,
					totalRecords);

		} catch (SQLException e) {
			logger.error("Error en findByCriteria de Usuario", e);
			throw new DataException("Error en findByCriteria de Usuario", e);
		} finally {
			JDBCUtils.close(ps, rs);
		}
		return results;
	}

	private UsuarioDTO loadUsuario(ResultSet rs, boolean authenticated) throws SQLException {
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setId(rs.getInt("id_usuario"));
		usuario.setNombreUsuario(rs.getString("nombre_usuario"));
		usuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
		usuario.setNombre(rs.getString("nombre"));
		usuario.setApellido1(rs.getString("apellido1"));
		usuario.setApellido2(rs.getString("apellido2"));
		usuario.setTelefono(rs.getString("telefono"));
		usuario.setEmail(rs.getString("email"));
		usuario.setContrasena(authenticated ? null : rs.getString("contrasena"));
		return usuario;
	}

	private void setUsuarioParameters(PreparedStatement ps, UsuarioDTO usuario, boolean isUpdate) throws SQLException {
		ps.setString(1, usuario.getNombreUsuario());
		ps.setString(2, passwordEncryptor.encryptPassword(usuario.getContrasena()));
		ps.setInt(3, usuario.getIdTipoUsuario());
		ps.setString(4, usuario.getNombre());
		ps.setString(5, usuario.getApellido1());
		ps.setString(6, usuario.getApellido2());
		ps.setString(7, usuario.getTelefono());
		ps.setString(8, usuario.getEmail());
		if (isUpdate) {
			ps.setInt(9, usuario.getId());
		}
	}
}
