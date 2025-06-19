package com.pinguela.rentexpres.desktop.util;

import java.awt.Color;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * Centraliza la configuración de colores y estilo de la aplicación.
 */
public final class AppTheme {

    private AppTheme() {}

    /** Color de fondo utilizado en los paneles de filtros. */
    public static final Color FILTER_BG = new Color(250, 250, 250);

    /** Colores para la barra superior y la navegación lateral */
    public static final Color TOPBAR_BG = new Color(245, 245, 245);
    public static final Color NAV_BG = new Color(240, 242, 245);
    public static final Color NAV_BTN_BG = Color.WHITE;
    public static final Color NAV_BTN_HOVER_BG = new Color(220, 224, 230);
    public static final Color NAV_BTN_FG = new Color(45, 52, 70);

    /** Colores utilizados en el diálogo de inicio de sesión */
    public static final Color LOGIN_GRADIENT_START = new Color(240, 247, 255);
    public static final Color LOGIN_GRADIENT_END = Color.WHITE;

    /** Color de texto para etiquetas en paneles */
    public static final Color LABEL_FG = new Color(70, 70, 70);

    /** Altura por defecto de las filas de las tablas */
    public static final int TABLE_ROW_HEIGHT = 28;

    /** Color de fondo usado en todos los diálogos. */
    public static final Color DIALOG_BG = Color.WHITE;

    /** Color acento para botones primarios. */
    public static final Color PRIMARY = new Color(33, 150, 243);

    /** Configura FlatLaf y algunos colores por defecto. */
    public static void setup() {
        FlatLightLaf.setup();
        UIManager.put("Component.arc", 8);
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Table.alternateRowColor", new Color(245, 249, 254));
        UIManager.put("Table.selectionBackground", new Color(0xC7DEF8));
        UIManager.put("Table.selectionForeground", Color.BLACK);
        UIManager.put("Table.rowHeight", TABLE_ROW_HEIGHT);

        UIManager.put("Panel.background", DIALOG_BG);
        UIManager.put("Button.background", PRIMARY);
        UIManager.put("Button.foreground", Color.WHITE);
    }
}

