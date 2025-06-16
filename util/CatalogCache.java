package com.pinguela.rentexpres.desktop.util;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.*;
import com.pinguela.rentexpres.service.*;

public final class CatalogCache {

	private static List<EstadoReservaDTO> estadosReserva;
	private static List<EstadoAlquilerDTO> estadosAlquiler;
	private static List<EstadoVehiculoDTO> estadosVehiculo;
	private static List<CategoriaVehiculoDTO> categoriasVehiculo;
	private static List<VehiculoDTO> vehiculos;
	private static List<ProvinciaDTO> provincias;
	private static List<LocalidadDTO> localidades;

	private CatalogCache() {
	}

	public static List<EstadoReservaDTO> getEstadosReserva(EstadoReservaService s) throws RentexpresException {
		if (estadosReserva == null) {
			estadosReserva = s.findAll();
		}
		return estadosReserva;
	}

	public static List<EstadoAlquilerDTO> getEstadosAlquiler(EstadoAlquilerService s) throws RentexpresException {
		if (estadosAlquiler == null) {
			estadosAlquiler = s.findAll();
		}
		return estadosAlquiler;
	}

	public static List<EstadoVehiculoDTO> getEstadosVehiculo(EstadoVehiculoService s) throws RentexpresException {
		if (estadosVehiculo == null) {
			estadosVehiculo = s.findAll();
		}
		return estadosVehiculo;
	}

	public static List<CategoriaVehiculoDTO> getCategoriasVehiculo(CategoriaVehiculoService s)
			throws RentexpresException {
		if (categoriasVehiculo == null) {
			categoriasVehiculo = s.findAll();
		}
		return categoriasVehiculo;
	}

	public static List<VehiculoDTO> getVehiculos(VehiculoService s) throws RentexpresException {
		if (vehiculos == null) {
			vehiculos = s.findAll();
		}
		return vehiculos;
	}

	public static List<ProvinciaDTO> getProvincias(ProvinciaService s) throws RentexpresException {
		if (provincias == null) {
			provincias = s.findAll();
		}
		return provincias;
	}

	public static List<LocalidadDTO> getLocalidades(LocalidadService s) throws RentexpresException {
		if (localidades == null) {
			localidades = s.findAll();
		}
		return localidades;
	}
}
