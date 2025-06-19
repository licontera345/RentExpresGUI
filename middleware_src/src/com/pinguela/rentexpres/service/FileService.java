package com.pinguela.rentexpres.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileService {

    List<String> getImagePaths(Integer idVehiculo);

    boolean deleteImage(String imagePath);

    String uploadImage(File imagen, Integer idVehiculo) throws IOException;

    /** Obtiene las imágenes asociadas a un usuario. */
    List<String> getUsuarioImagePaths(Integer idUsuario);

    /** Sube imágenes para un usuario y las guarda en su carpeta correspondiente. */
    void uploadUsuarioImages(List<File> imagenes, Integer idUsuario);

}
 
