package com.pinguela.rentexpres.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.VehicleDAO;
import com.pinguela.rentexpres.dao.impl.VehicleDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.FileService;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class VehicleServiceImpl implements VehicleService {

	private static final Logger logger = LogManager.getLogger(VehicleServiceImpl.class);
	private final VehicleDAO vehicleDAO;
	private final FileService fileService;

	public VehicleServiceImpl() {
		this.vehicleDAO = new VehicleDAOImpl();
		this.fileService = new FileServiceImpl();
	}

	@Override
	public VehicleDTO findById(Integer id) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			VehicleDTO vehicle = vehicleDAO.findById(connection, id);
			if (vehicle != null) {
				List<String> imagePaths = fileService.getImagePaths(id);
				if (!imagePaths.isEmpty()) {
					vehicle.setImagePath(imagePaths.get(0));
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(VehicleServiceImpl.class, "Vehicle encontrado con ID: {}"), id);
			return vehicle;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(VehicleServiceImpl.class, "Error al buscar vehicle por ID: {}"), id, e);
			throw new RentexpresException("Error al buscar vehicle por ID: " + id, e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public List<VehicleDTO> findAll() throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			List<VehicleDTO> vehicles = vehicleDAO.findAll(connection);
			for (VehicleDTO vehicle : vehicles) {
				List<String> imagePaths = fileService.getImagePaths(vehicle.getId());
				if (!imagePaths.isEmpty()) {
					vehicle.setImagePath(imagePaths.get(0));
				}
			}

			JDBCUtils.commitTransaction(connection);
			return vehicles;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			throw new RentexpresException("Error al obtener todos los vehicles", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public boolean create(VehicleDTO vehicle, File imagen) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			// 1. Crear el vehicle en la base de datos
			boolean creado = vehicleDAO.create(connection, vehicle);
			if (!creado) {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(VehicleServiceImpl.class, "No se pudo crear el vehicle en la base de datos"));
				return false;
			}

			// 2. Manejar la imagen si existe
			if (imagen != null) {
				try {
					String imagePath = fileService.uploadImage(imagen, vehicle.getId());
					if (imagePath != null) {
						// 3. Actualizar el vehicle con el path de la imagen
						vehicle.setImagePath(imagePath);
						boolean actualizado = vehicleDAO.update(connection, vehicle);
						if (!actualizado) {
							JDBCUtils.rollbackTransaction(connection);
							logger.warn(LogUtils.buildMessage(VehicleServiceImpl.class, "Vehicle creado pero no se pudo actualizar con la imagen"));
							return false;
						}
					}
				} catch (IOException e) {
					logger.error(LogUtils.buildMessage(VehicleServiceImpl.class, "Error al guardar la imagen del vehicle, pero se creó el vehicle"), e);
					// Continuamos sin hacer rollback porque el vehicle sí se creó
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(VehicleServiceImpl.class, "Vehicle creado exitosamente con ID: {}"), vehicle.getId());
			return true;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(VehicleServiceImpl.class, "Error al crear vehicle"), e);
			throw new RentexpresException("Error al crear vehicle", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public boolean update(VehicleDTO vehicle, File nuevaImagen) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			// 1. Manejar la imagen si se proporciona una nueva
			if (nuevaImagen != null) {
				try {
					// Delete imagen anterior si existe
					if (vehicle.getImagePath() != null) {
						fileService.deleteImage(vehicle.getImagePath());
					}

					// Subir nueva imagen
					String imagePath = fileService.uploadImage(nuevaImagen, vehicle.getId());
					vehicle.setImagePath(imagePath);
				} catch (IOException e) {
					logger.error(LogUtils.buildMessage(VehicleServiceImpl.class, "Error al actualizar la imagen del vehicle"), e);
					// Continuamos con la actualización sin la imagen
				}
			}

			// 2. Actualizar el vehicle en la base de datos
			boolean actualizado = vehicleDAO.update(connection, vehicle);
			if (!actualizado) {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(VehicleServiceImpl.class, "No se pudo actualizar el vehicle con ID: {}"), vehicle.getId());
				return false;
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(VehicleServiceImpl.class, "Vehicle actualizado exitosamente. ID: {}"), vehicle.getId());
			return true;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(VehicleServiceImpl.class, "Error al actualizar vehicle ID: {}"), vehicle.getId(), e);
			throw new RentexpresException("Error al actualizar vehicle", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public boolean delete(Integer id) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			// 1. Primero obtener el vehicle para manejar sus imágenes
			VehicleDTO vehicle = vehicleDAO.findById(connection, id);
			if (vehicle == null) {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(VehicleServiceImpl.class, "No se encontró el vehicle con ID: {} para delete"), id);
				return false;
			}

			// 2. Delete las imágenes asociadas si existen
			if (vehicle.getImagePath() != null) {
				fileService.deleteImage(vehicle.getImagePath());
			}

			// 3. Delete el vehicle de la base de datos
			boolean eliminado = vehicleDAO.delete(connection, id);
			if (!eliminado) {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(VehicleServiceImpl.class, "No se pudo delete el vehicle con ID: {}"), id);
				return false;
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(VehicleServiceImpl.class, "Vehicle eliminado exitosamente. ID: {}"), id);
			return true;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(VehicleServiceImpl.class, "Error al delete vehicle ID: {}"), id, e);
			throw new RentexpresException("Error al delete vehicle", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public Results<VehicleDTO> findByCriteria(VehicleCriteria criteria) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			// Validar y establecer valores por defecto para la paginación
			if (criteria == null) {
				criteria = new VehicleCriteria();
			}
			if (criteria.getPageNumber() == null || criteria.getPageNumber() < 1) {
				criteria.setPageNumber(1);
			}
			if (criteria.getPageSize() == null || criteria.getPageSize() < 1) {
				criteria.setPageSize(10);
			}

			// Buscar vehicles según criterios
			Results<VehicleDTO> results = vehicleDAO.findByCriteria(connection, criteria);

			// Para cada vehicle, obtener su imagen principal
			if (results != null && results.getResults() != null) {
				for (VehicleDTO vehicle : results.getResults()) {
					List<String> imagePaths = fileService.getImagePaths(vehicle.getId());
					if (!imagePaths.isEmpty()) {
						vehicle.setImagePath(imagePaths.get(0));
					}
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(VehicleServiceImpl.class, "Búsqueda por criterios completada. Encontrados {} vehicles"),
					(results != null && results.getResults() != null) ? results.getResults().size() : 0);
			return results;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(VehicleServiceImpl.class, "Error en búsqueda por criterios"), e);
			throw new RentexpresException("Error en búsqueda por criterios", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}

	@Override
	public List<String> getVehicleImages(Integer vehicleId) throws RentexpresException {
		try {
			return fileService.getImagePaths(vehicleId);
		} catch (Exception e) {
			logger.error(LogUtils.buildMessage(VehicleServiceImpl.class, "Error al obtener imágenes del vehicle ID: {}"), vehicleId, e);
			throw new RentexpresException("Error al obtener imágenes del vehicle", e);
		}
	}

	@Override
	public boolean updateVehicleImage(Integer vehicleId, File nuevaImagen) throws RentexpresException {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			JDBCUtils.beginTransaction(connection);

			// 1. Obtener el vehicle actual
			VehicleDTO vehicle = vehicleDAO.findById(connection, vehicleId);
			if (vehicle == null) {
				JDBCUtils.rollbackTransaction(connection);
				logger.warn(LogUtils.buildMessage(VehicleServiceImpl.class, "No se encontró el vehicle con ID: {} para actualizar imagen"), vehicleId);
				return false;
			}

			// 2. Manejar la nueva imagen
			if (nuevaImagen != null) {
				try {
					// Delete imagen anterior si existe
					if (vehicle.getImagePath() != null) {
						fileService.deleteImage(vehicle.getImagePath());
					}

					// Subir nueva imagen
					String imagePath = fileService.uploadImage(nuevaImagen, vehicleId);
					vehicle.setImagePath(imagePath);

					// Actualizar el vehicle con el nuevo path
					boolean actualizado = vehicleDAO.update(connection, vehicle);
					if (!actualizado) {
						JDBCUtils.rollbackTransaction(connection);
						logger.warn(LogUtils.buildMessage(VehicleServiceImpl.class, "No se pudo actualizar la imagen del vehicle ID: {}"), vehicleId);
						return false;
					}
				} catch (IOException e) {
					JDBCUtils.rollbackTransaction(connection);
					logger.error(LogUtils.buildMessage(VehicleServiceImpl.class, "Error al actualizar la imagen del vehicle ID: {}"), vehicleId, e);
					throw new RentexpresException("Error al actualizar la imagen del vehicle", e);
				}
			}

			JDBCUtils.commitTransaction(connection);
			logger.info(LogUtils.buildMessage(VehicleServiceImpl.class, "Imagen del vehicle actualizada exitosamente. ID: {}"), vehicleId);
			return true;
		} catch (SQLException | DataException e) {
			JDBCUtils.rollbackTransaction(connection);
			logger.error(LogUtils.buildMessage(VehicleServiceImpl.class, "Error al actualizar imagen del vehicle ID: {}"), vehicleId, e);
			throw new RentexpresException("Error al actualizar imagen del vehicle", e);
		} finally {
			JDBCUtils.close(connection);
		}
	}
}
