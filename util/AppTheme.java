package com.pinguela.rentexpres.desktop.util;

import java.awt.Color;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

/**
 * Centraliza la configuración de colores y estilo de la aplicación.
 */
public final class AppTheme {

    private AppTheme() {}

    private static boolean darkMode = false;

    /** Color de fondo utilizado en los paneles de filtros. */
    public static Color FILTER_BG = new Color(250, 250, 250);

    /** Colores para la barra superior y la navegación lateral */
    public static Color TOPBAR_BG = new Color(245, 245, 245);
    public static Color NAV_BG = new Color(240, 242, 245);
    public static Color NAV_BTN_BG = Color.WHITE;
    public static Color NAV_BTN_HOVER_BG = new Color(220, 224, 230);
    public static Color NAV_BTN_FG = new Color(45, 52, 70);

    /** Colores utilizados en el diálogo de inicio de sesión */
    public static Color LOGIN_GRADIENT_START = new Color(225, 242, 254);
    public static Color LOGIN_GRADIENT_END = Color.WHITE;

    /** Color de texto para etiquetas en paneles */
    public static Color LABEL_FG = new Color(70, 70, 70);

    /** Altura por defecto de las filas de las tablas */
    public static final int TABLE_ROW_HEIGHT = 28;

    /** Color de fondo usado en todos los diálogos. */
    public static Color DIALOG_BG = Color.WHITE;

    /** Color acento para botones primarios. */
    public static Color PRIMARY = new Color(33, 150, 243);

    /** Color para botones secundarios/cancelar. */
    public static Color CANCEL = new Color(238, 238, 238);
    public static Color CANCEL_FG = new Color(55, 71, 79);

    private static void setupColorsLight() {
        FILTER_BG = new Color(250, 250, 250);
        TOPBAR_BG = new Color(245, 245, 245);
        NAV_BG = new Color(240, 242, 245);
        NAV_BTN_BG = Color.WHITE;
        NAV_BTN_HOVER_BG = new Color(220, 224, 230);
        NAV_BTN_FG = new Color(45, 52, 70);
        LOGIN_GRADIENT_START = new Color(225, 242, 254);
        LOGIN_GRADIENT_END = Color.WHITE;
        LABEL_FG = new Color(70, 70, 70);
        DIALOG_BG = Color.WHITE;
        PRIMARY = new Color(33, 150, 243);
        CANCEL = new Color(238, 238, 238);
        CANCEL_FG = new Color(55, 71, 79);
    }

    private static void setupColorsDark() {
        FILTER_BG = new Color(60, 63, 65);
        TOPBAR_BG = new Color(43, 43, 43);
        NAV_BG = new Color(48, 48, 48);
        NAV_BTN_BG = new Color(60, 63, 65);
        NAV_BTN_HOVER_BG = new Color(77, 77, 77);
        NAV_BTN_FG = Color.WHITE;
        LOGIN_GRADIENT_START = new Color(60, 63, 65);
        LOGIN_GRADIENT_END = new Color(43, 43, 43);
        LABEL_FG = Color.WHITE;
        DIALOG_BG = new Color(60, 63, 65);
        PRIMARY = new Color(98, 0, 238);
        CANCEL = new Color(77, 77, 77);
        CANCEL_FG = Color.WHITE;
    }

    /** Configura FlatLaf y algunos colores por defecto. */
    public static void setup() {
        darkMode = false;
        FlatLightLaf.setup();
        setupColorsLight();
        applyUI();
    }

    /** Configura la aplicación para modo oscuro. */
    public static void setupDark() {
        darkMode = true;
        FlatDarkLaf.setup();
        setupColorsDark();
        applyUI();
    }

    private static void applyUI() {
        UIManager.put("Component.arc", 8);
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Table.alternateRowColor", darkMode ? NAV_BG : new Color(245, 249, 254));
        UIManager.put("Table.selectionBackground", darkMode ? new Color(96, 99, 102) : new Color(0xC7DEF8));
        UIManager.put("Table.selectionForeground", darkMode ? Color.WHITE : Color.BLACK);
        UIManager.put("Table.rowHeight", TABLE_ROW_HEIGHT);

        UIManager.put("Panel.background", DIALOG_BG);
        UIManager.put("Button.background", PRIMARY);
        UIManager.put("Button.foreground", NAV_BTN_FG);
    }

    /** Cambia entre modo claro y oscuro. */
    public static void toggleDarkMode() {
        if (darkMode) {
            setup();
        } else {
            setupDark();
        }
        FlatLaf.updateUI();
    }

    /** Indica si el modo oscuro está activo. */
    public static boolean isDark() {
        return darkMode;
    }
}

