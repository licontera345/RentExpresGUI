package com.pinguela.rentexpres.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.config.ConfigManager;
import com.pinguela.rentexpres.service.FileService;
import com.pinguela.rentexpres.util.LogUtils;

public class FileServiceImpl implements FileService {

	private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);
	private static final String BASE_IMAGE_PATH = ConfigManager.getStringValue("base.image.path");

	@Override
	public String uploadImage(File imagen, Integer vehicleId) throws IOException {
		if (imagen == null || !imagen.exists()) {
			logger.warn(LogUtils.buildMessage(FileServiceImpl.class, "El archivo de imagen es nulo o no existe"));
			return null;
		}

		String carpetaImagenes = BASE_IMAGE_PATH + File.separator + "vehicles" + File.separator + vehicleId;
		Path directorioDestino = Paths.get(carpetaImagenes);

		if (!Files.exists(directorioDestino)) {
			Files.createDirectories(directorioDestino);
		}

		String nameArchivo = imagen.getName();
		if (!validarNameArchivo(nameArchivo)) {
			logger.warn(LogUtils.buildMessage(FileServiceImpl.class, "El archivo no cumple con el formato requerido: {}"), nameArchivo);
			return null;
		}

		String nameUnico = generarNameUnico(nameArchivo);
		Path destino = directorioDestino.resolve(nameUnico);
		Files.copy(imagen.toPath(), destino);

		String relativePath = "vehicles" + File.separator + vehicleId + File.separator + nameUnico;
		logger.info(LogUtils.buildMessage(FileServiceImpl.class, "Imagen guardada en: {}"), destino.toString());

		return relativePath;
	}

    @Override
    public List<String> getImagePaths(Integer vehicleId) {
		String carpetaImagenes = BASE_IMAGE_PATH + File.separator + "vehicles" + File.separator + vehicleId;
		File directorioImagenes = new File(carpetaImagenes);
		List<String> imagePaths = new ArrayList<>();

		if (directorioImagenes.exists() && directorioImagenes.isDirectory()) {
			File[] archivos = directorioImagenes.listFiles();
			if (archivos != null) {
				for (File archivo : archivos) {
					if (archivo.isFile()) {
						String name = archivo.getName().toLowerCase();
						if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")) {
							String relativePath = "vehicles" + File.separator + vehicleId + File.separator
									+ archivo.getName();
							imagePaths.add(relativePath);
						}
					}
				}
			}
		} else {
			logger.info(LogUtils.buildMessage(FileServiceImpl.class, "No se encontraron imágenes para el vehicle ID: {}"), vehicleId);
		}

                return imagePaths;
        }

        @Override
        public List<String> getUserImagePaths(Integer userId) {
                String carpetaImagenes = BASE_IMAGE_PATH + File.separator + "users" + File.separator + userId;
                File directorioImagenes = new File(carpetaImagenes);
                List<String> imagePaths = new ArrayList<>();

                if (directorioImagenes.exists() && directorioImagenes.isDirectory()) {
                        File[] archivos = directorioImagenes.listFiles();
                        if (archivos != null) {
                                for (File archivo : archivos) {
                                        if (archivo.isFile()) {
                                                String name = archivo.getName().toLowerCase();
                                                if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")) {
                                                        String relativePath = "users" + File.separator + userId + File.separator + archivo.getName();
                                                        imagePaths.add(relativePath);
                                                }
                                        }
                                }
                        }
                } else {
                        logger.info(LogUtils.buildMessage(FileServiceImpl.class, "No se encontraron imágenes para el user ID: {}"), userId);
                }

                return imagePaths;
        }

	@Override
	public boolean deleteImage(String imagePath) {
		if (imagePath == null || imagePath.isEmpty()) {
			return false;
		}

		File imageFile = new File(BASE_IMAGE_PATH + File.separator + imagePath);
		if (!imageFile.exists()) {
			return false;
		}

		return imageFile.delete();
	}

	private boolean validarNameArchivo(String nameArchivo) {
		String regex = "^[a-zA-Z][a-zA-Z0-9_-]*\\.(jpg|png|jpeg)$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(nameArchivo);
		return matcher.matches();
	}

	private String generarNameUnico(String nameArchivo) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		int dotIndex = nameArchivo.lastIndexOf('.');
		if (dotIndex > 0) {
			return nameArchivo.substring(0, dotIndex) + "_" + timestamp + nameArchivo.substring(dotIndex);
		}
		return nameArchivo + "_" + timestamp;
	}

    @Override
    public void uploadUserImages(List<File> imagenes, Integer userId) {
        if (imagenes == null || imagenes.isEmpty()) {
            return;
        }

        String carpetaImagenes = BASE_IMAGE_PATH + File.separator + "users" + File.separator + userId;
        Path directorioDestino = Paths.get(carpetaImagenes);
        try {
            if (!Files.exists(directorioDestino)) {
                Files.createDirectories(directorioDestino);
            }

            for (File imagen : imagenes) {
                if (imagen == null || !imagen.exists()) {
                    logger.warn(LogUtils.buildMessage(FileServiceImpl.class, "La imagen {} no existe"), imagen);
                    continue;
                }

                String nameArchivo = imagen.getName();
                if (!validarNameArchivo(nameArchivo)) {
                    logger.warn(LogUtils.buildMessage(FileServiceImpl.class, "Name de archivo inválido: {}"), nameArchivo);
                    continue;
                }

                String nameUnico = generarNameUnico(nameArchivo);
                Path destino = directorioDestino.resolve(nameUnico);
                Files.copy(imagen.toPath(), destino);
                logger.info(LogUtils.buildMessage(FileServiceImpl.class, "Imagen de user guardada en {}"), destino.toString());
            }

        } catch (IOException e) {
            logger.error(LogUtils.buildMessage(FileServiceImpl.class, "Error subiendo imágenes para el user {}"), userId, e);
        }

    }
}
