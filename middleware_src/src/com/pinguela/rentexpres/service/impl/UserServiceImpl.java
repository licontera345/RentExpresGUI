package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.UserDAO;
import com.pinguela.rentexpres.dao.impl.UserDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UserCriteria;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.FileService;
import com.pinguela.rentexpres.service.UserService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class UserServiceImpl implements UserService {

	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	private UserDAO userDAO;

	public UserServiceImpl() {
		this.userDAO = new UserDAOImpl();
	}

	@Override
	public UserDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		UserDTO user = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			user = userDAO.findById(connection, id);

			if (user != null) {
				user.setContrasena(null);
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(UserServiceImpl.class, "findById de User completado. ID: {}"), id);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UserServiceImpl.class, "Error en findById de User: "), e);
			throw new RentexpresException("Error en findById de User", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return user;
	}

	@Override
	public List<UserDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<UserDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			lista = userDAO.findAll(connection);

			if (lista != null) {
				for (UserDTO u : lista) {
					u.setContrasena(null);
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(UserServiceImpl.class, "findAll de User completado. Cantidad: {}"), (lista != null ? lista.size() : 0));
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UserServiceImpl.class, "Error en findAll de User: "), e);
			throw new RentexpresException("Error en findAll de User", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}

	@Override
	public boolean create(UserDTO user) throws RentexpresException {
		Connection connection = null;
		boolean creado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			creado = userDAO.create(connection, user);
			if (creado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(UserServiceImpl.class, "User creado exitosamente. ID: {}"), user.getId());

				MailServiceImpl mailService = new MailServiceImpl();
				String asunto = "Bienvenido a RentExpress";
				String mensaje = "Estimado " + user.getName() + ", bienvenido a RentExpress.";
				mailService.enviar(user.getEmail(), asunto, mensaje);

				user.setContrasena(null);

				// Subir imágenes, si las tiene
                                if (user.getImagenes() != null && !user.getImagenes().isEmpty()) {
                                        FileService fileService = new FileServiceImpl();
                                        fileService.uploadUserImages(user.getImagenes(), user.getId());
                                }
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(UserServiceImpl.class, "No se pudo crear el User."));
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UserServiceImpl.class, "Error en create de User: "), e);
			throw new RentexpresException("Error en create de User", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return creado;
	}

	@Override
	public boolean update(UserDTO user) throws RentexpresException {
		Connection connection = null;
		boolean actualizado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			actualizado = userDAO.update(connection, user);
			if (actualizado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(UserServiceImpl.class, "User actualizado exitosamente. ID: {}"), user.getId());

				user.setContrasena(null);
                                if (user.getImagenes() != null && !user.getImagenes().isEmpty()) {
                                        FileService fileService = new FileServiceImpl();
                                        fileService.uploadUserImages(user.getImagenes(), user.getId());
                                }
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(UserServiceImpl.class, "No se pudo actualizar el User. ID: {}"), user.getId());
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UserServiceImpl.class, "Error en update de User: "), e);
			throw new RentexpresException("Error en update de User", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return actualizado;
	}

	@Override
	public boolean delete(UserDTO user, Integer id) throws RentexpresException {
		Connection connection = null;
		boolean eliminado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			eliminado = userDAO.delete(connection, user, id);
			if (eliminado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(UserServiceImpl.class, "User eliminado exitosamente. ID: {}"), id);
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(UserServiceImpl.class, "No se pudo delete el User. ID: {}"), id);
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UserServiceImpl.class, "Error en delete de User: "), e);
			throw new RentexpresException("Error en delete de User", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return eliminado;
	}

	@Override
	public UserDTO autenticar(String username, String contrasenaEnClaro) throws RentexpresException {
		Connection connection = null;
		UserDTO user = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			user = userDAO.autenticar(connection, username, contrasenaEnClaro);

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(UserServiceImpl.class, "Autenticación de User completada. User: {}"), username);

			if (user != null) {
				user.setContrasena(null);
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UserServiceImpl.class, "Error en autenticación de User: "), e);
			throw new RentexpresException("Error en autenticación de User", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return user;
	}

	@Override
        public Results<UserDTO> findByCriteria(UserCriteria criteria) throws RentexpresException {
		Connection connection = null;
		Results<UserDTO> results = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			results = userDAO.findByCriteria(connection, criteria);

			if (results != null && results.getResults() != null) {
				for (UserDTO u : results.getResults()) {
					u.setContrasena(null);
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(UserServiceImpl.class, "findByCriteria de User completado. Página {} (Tamaño: {}), Total registros: {}"),
					criteria.getPageNumber(), criteria.getPageSize(), results != null ? results.getTotalRecords() : 0);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UserServiceImpl.class, "Error en findByCriteria de User: "), e);
			throw new RentexpresException("Error en findByCriteria de User", e);
		} finally {
			JDBCUtils.close(connection);
		}
                return results;
        }

        @Override
        public List<String> getUserImages(Integer userId) throws RentexpresException {
                FileService fileService = new FileServiceImpl();
                try {
                        return fileService.getUserImagePaths(userId);
                } catch (Exception e) {
                        logger.error(LogUtils.buildMessage(UserServiceImpl.class, "Error al obtener imágenes del user {}"), userId, e);
                        throw new RentexpresException("Error al obtener imágenes del user", e);
                }
        }
}
