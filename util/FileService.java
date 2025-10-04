package com.pinguela.rentexpres.desktop.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class FileService {

    private final Path carpetaBase;
    public FileService(Path carpetaBase) throws IOException {
        this.carpetaBase = carpetaBase;
        if (!Files.exists(this.carpetaBase)) {
            Files.createDirectories(this.carpetaBase);
        }
    }

    public String store(File origen) throws IOException {
        String fileName = origen.getName();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) {
            extension = fileName.substring(i);
        }
        String nuevoName = UUID.randomUUID().toString() + extension;
        Path destino = carpetaBase.resolve(nuevoName);
        Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return nuevoName;
    }

    public void delete(String nameArchivo) throws IOException {
        if (nameArchivo == null) return;
        Path ruta = carpetaBase.resolve(nameArchivo);
        if (Files.exists(ruta)) {
            Files.delete(ruta);
        }
    }
}
