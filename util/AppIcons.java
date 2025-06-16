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
	public static final ImageIcon ALQUILER = load("alquiler.png");
	public static final ImageIcon RESERVA = load("reserva.png");
	public static final ImageIcon CLIENTE = load("cliente.png");
	public static final ImageIcon USUARIO = load("usuario.png");
	public static final ImageIcon VEHICULO = load("vehiculo.png");
	public static final ImageIcon INICIO = load("inicio.png");

	private AppIcons() {
	}

	public static ImageIcon load(String file) {
		URL url = AppIcons.class.getResource(PATH + file);
		if (url == null) {
			System.err.println("[AppIcons] Recurso no encontrado: " + PATH + file);
			return null;
		}
		return new ImageIcon(url);
	}

	public static ImageIcon load(String file, int size) {
		ImageIcon base = load(file);
		if (base == null)
			return null;
		Image img = base.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}

	public static ImageIcon of(String file) {
		return load(file);
	}
}
