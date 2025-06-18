package com.pinguela.rentexpres.desktop.util;

import java.awt.Color;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * Centraliza la configuración de colores y estilo de la aplicación.
 */
public final class AppTheme {

    private AppTheme() {}

    /** Configura FlatLaf y algunos colores por defecto. */
    public static void setup() {
        FlatLightLaf.setup();
        UIManager.put("Component.arc", 8);
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Table.alternateRowColor", new Color(245, 249, 254));
        UIManager.put("Table.selectionBackground", new Color(0xC7DEF8));
        UIManager.put("Table.selectionForeground", Color.BLACK);
    }
}

