package com.pinguela.rentexpres.desktop.util;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public final class AppIcons {

        private static final String PATH = "/icons/";

        public static final ImageIcon NEW = load("nuevo.png");
        public static final ImageIcon SEARCH = load("lupa.png");
        public static final ImageIcon EDIT = load("editar.png");
        public static final ImageIcon VIEW = load("ver.png");
        public static final ImageIcon DELETE = load("eliminar.png");
        public static final ImageIcon CLEAR = load("limpiar-filtros.png");
        public static final ImageIcon PLUS = load("signo-de-mas.png");
        public static final ImageIcon RENTAL = load("rental.png");
        public static final ImageIcon RESERVATION = load("reservation.png");
        public static final ImageIcon CUSTOMER = load("customer.png");
        public static final ImageIcon USER = load("user.png");
        public static final ImageIcon VEHICLE = load("vehicle.png");
        public static final ImageIcon HOME = load("inicio.png");

        private AppIcons() {
        }

        public static ImageIcon load(String file) {
                URL url = AppIcons.class.getResource(PATH + file);
                if (url == null) {
                        System.err.println("[AppIcons] Resource not found: " + PATH + file);
                        return null;
                }
                return new ImageIcon(url);
        }

        public static ImageIcon load(String file, int size) {
                ImageIcon base = load(file);
                if (base == null)
                        return null;
                Image scaled = base.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
        }
}
