package com.pinguela.rentexpres.desktop.util;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.*;
import com.pinguela.rentexpres.service.*;

public final class CatalogCache {

	private static List<ReservationStatusDTO> estadosReservation;
	private static List<RentalStatusDTO> estadosRental;
	private static List<VehicleStatusDTO> estadosVehicle;
	private static List<VehicleCategoryDTO> categorysVehicle;
	private static List<VehicleDTO> vehicles;
	private static List<ProvinceDTO> provinces;
	private static List<CityDTO> cities;

	private CatalogCache() {
	}

	public static List<ReservationStatusDTO> getEstadosReservation(ReservationStatusService s) throws RentexpresException {
		if (estadosReservation == null) {
			estadosReservation = s.findAll();
		}
		return estadosReservation;
	}

	public static List<RentalStatusDTO> getEstadosRental(RentalStatusService s) throws RentexpresException {
		if (estadosRental == null) {
			estadosRental = s.findAll();
		}
		return estadosRental;
	}

	public static List<VehicleStatusDTO> getEstadosVehicle(VehicleStatusService s) throws RentexpresException {
		if (estadosVehicle == null) {
			estadosVehicle = s.findAll();
		}
		return estadosVehicle;
	}

	public static List<VehicleCategoryDTO> getCategorysVehicle(VehicleCategoryService s)
			throws RentexpresException {
		if (categorysVehicle == null) {
			categorysVehicle = s.findAll();
		}
		return categorysVehicle;
	}

	public static List<VehicleDTO> getVehicles(VehicleService s) throws RentexpresException {
		if (vehicles == null) {
			vehicles = s.findAll();
		}
		return vehicles;
	}

	public static List<ProvinceDTO> getProvinces(ProvinceService s) throws RentexpresException {
		if (provinces == null) {
			provinces = s.findAll();
		}
		return provinces;
	}

	public static List<CityDTO> getCities(CityService s) throws RentexpresException {
		if (cities == null) {
			cities = s.findAll();
		}
		return cities;
	}
}
