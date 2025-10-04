package com.pinguela.rentexpres.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileService {

    List<String> getImagePaths(Integer vehicleId);

    boolean deleteImage(String imagePath);

    String uploadImage(File imagen, Integer vehicleId) throws IOException;

    /** Obtiene las imágenes asociadas a un user. */
    List<String> getUserImagePaths(Integer userId);

    /** Sube imágenes para un user y las guarda en su carpeta correspondiente. */
    void uploadUserImages(List<File> imagenes, Integer userId);

}
 
