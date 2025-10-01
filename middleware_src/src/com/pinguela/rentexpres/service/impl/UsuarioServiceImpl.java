package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.UsuarioDAO;
import com.pinguela.rentexpres.dao.impl.UsuarioDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UsuarioCriteria;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.FileService;
import com.pinguela.rentexpres.service.UsuarioService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class UsuarioServiceImpl implements UsuarioService {

	private static final Logger logger = LogManager.getLogger(UsuarioServiceImpl.class);
	private UsuarioDAO usuarioDAO;

	public UsuarioServiceImpl() {
		this.usuarioDAO = new UsuarioDAOImpl();
	}

	@Override
	public UsuarioDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		UsuarioDTO usuario = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			usuario = usuarioDAO.findById(connection, id);

			if (usuario != null) {
				usuario.setContrasena(null);
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(UsuarioServiceImpl.class, "findById de Usuario completado. ID: {}"), id);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UsuarioServiceImpl.class, "Error en findById de Usuario: "), e);
			throw new RentexpresException("Error en findById de Usuario", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return usuario;
	}

	@Override
	public List<UsuarioDTO> findAll() throws RentexpresException {
		Connection connection = null;
		List<UsuarioDTO> lista = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			lista = usuarioDAO.findAll(connection);

			if (lista != null) {
				for (UsuarioDTO u : lista) {
					u.setContrasena(null);
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(UsuarioServiceImpl.class, "findAll de Usuario completado. Cantidad: {}"), (lista != null ? lista.size() : 0));
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UsuarioServiceImpl.class, "Error en findAll de Usuario: "), e);
			throw new RentexpresException("Error en findAll de Usuario", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return lista;
	}

	@Override
	public boolean create(UsuarioDTO usuario) throws RentexpresException {
		Connection connection = null;
		boolean creado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			creado = usuarioDAO.create(connection, usuario);
			if (creado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(UsuarioServiceImpl.class, "Usuario creado exitosamente. ID: {}"), usuario.getId());

				MailServiceImpl mailService = new MailServiceImpl();
				String asunto = "Bienvenido a RentExpress";
				String mensaje = "Estimado " + usuario.getNombre() + ", bienvenido a RentExpress.";
				mailService.enviar(usuario.getEmail(), asunto, mensaje);

				usuario.setContrasena(null);

				// Subir imágenes, si las tiene
                                if (usuario.getImagenes() != null && !usuario.getImagenes().isEmpty()) {
                                        FileService fileService = new FileServiceImpl();
                                        fileService.uploadUsuarioImages(usuario.getImagenes(), usuario.getId());
                                }
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(UsuarioServiceImpl.class, "No se pudo crear el Usuario."));
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UsuarioServiceImpl.class, "Error en create de Usuario: "), e);
			throw new RentexpresException("Error en create de Usuario", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return creado;
	}

	@Override
	public boolean update(UsuarioDTO usuario) throws RentexpresException {
		Connection connection = null;
		boolean actualizado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			actualizado = usuarioDAO.update(connection, usuario);
			if (actualizado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(UsuarioServiceImpl.class, "Usuario actualizado exitosamente. ID: {}"), usuario.getId());

				usuario.setContrasena(null);
                                if (usuario.getImagenes() != null && !usuario.getImagenes().isEmpty()) {
                                        FileService fileService = new FileServiceImpl();
                                        fileService.uploadUsuarioImages(usuario.getImagenes(), usuario.getId());
                                }
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(UsuarioServiceImpl.class, "No se pudo actualizar el Usuario. ID: {}"), usuario.getId());
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UsuarioServiceImpl.class, "Error en update de Usuario: "), e);
			throw new RentexpresException("Error en update de Usuario", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return actualizado;
	}

	@Override
	public boolean delete(UsuarioDTO usuario, Integer id) throws RentexpresException {
		Connection connection = null;
		boolean eliminado = false;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			eliminado = usuarioDAO.delete(connection, usuario, id);
			if (eliminado) {
				JDBCUtils.commitTransaction(connection);
				logger.info(LogUtils.buildMessage(UsuarioServiceImpl.class, "Usuario eliminado exitosamente. ID: {}"), id);
			} else {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(UsuarioServiceImpl.class, "No se pudo eliminar el Usuario. ID: {}"), id);
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UsuarioServiceImpl.class, "Error en delete de Usuario: "), e);
			throw new RentexpresException("Error en delete de Usuario", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return eliminado;
	}

	@Override
	public UsuarioDTO autenticar(String nombreUsuario, String contrasenaEnClaro) throws RentexpresException {
		Connection connection = null;
		UsuarioDTO usuario = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			usuario = usuarioDAO.autenticar(connection, nombreUsuario, contrasenaEnClaro);

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(UsuarioServiceImpl.class, "Autenticación de Usuario completada. Usuario: {}"), nombreUsuario);

			if (usuario != null) {
				usuario.setContrasena(null);
			}
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UsuarioServiceImpl.class, "Error en autenticación de Usuario: "), e);
			throw new RentexpresException("Error en autenticación de Usuario", e);
		} finally {
			JDBCUtils.close(connection);
		}
		return usuario;
	}

	@Override
        public Results<UsuarioDTO> findByCriteria(UsuarioCriteria criteria) throws RentexpresException {
		Connection connection = null;
		Results<UsuarioDTO> results = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			results = usuarioDAO.findByCriteria(connection, criteria);

			if (results != null && results.getResults() != null) {
				for (UsuarioDTO u : results.getResults()) {
					u.setContrasena(null);
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(UsuarioServiceImpl.class, "findByCriteria de Usuario completado. Página {} (Tamaño: {}), Total registros: {}"),
					criteria.getPageNumber(), criteria.getPageSize(), results != null ? results.getTotalRecords() : 0);
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(UsuarioServiceImpl.class, "Error en findByCriteria de Usuario: "), e);
			throw new RentexpresException("Error en findByCriteria de Usuario", e);
		} finally {
			JDBCUtils.close(connection);
		}
                return results;
        }

        @Override
        public List<String> getUsuarioImages(Integer idUsuario) throws RentexpresException {
                FileService fileService = new FileServiceImpl();
                try {
                        return fileService.getUsuarioImagePaths(idUsuario);
                } catch (Exception e) {
                        logger.error(LogUtils.buildMessage(UsuarioServiceImpl.class, "Error al obtener imágenes del usuario {}"), idUsuario, e);
                        throw new RentexpresException("Error al obtener imágenes del usuario", e);
                }
        }
}
