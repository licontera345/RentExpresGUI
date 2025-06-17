package com.pinguela.rentexpres.desktop.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class AppConfig {
    private static final Properties props = new Properties();

    static {
        // Try to load properties from classpath
        try (InputStream in = AppConfig.class.getResourceAsStream("/config/config.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            System.err.println("[AppConfig] No se pudo cargar config desde classpath: " + e.getMessage());
        }
        // Try to load properties from file system
        try (InputStream in = new FileInputStream("config/config.properties")) {
            props.load(in);
        } catch (IOException e) {
            // ignore if not found
        }
    }

    private AppConfig() {}

    public static Path getImageDir(String... more) {
        String base = props.getProperty("base.image.path", "imagenes");
        return Paths.get(base, more);
    }
}
