package com.pinguela.rentexpres.service;

import java.io.File;
import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;

public interface VehicleService {

	public VehicleDTO findById(Integer id) throws RentexpresException;

	public List<VehicleDTO> findAll() throws RentexpresException;

	public boolean delete(Integer id) throws RentexpresException;

	public Results<VehicleDTO> findByCriteria(VehicleCriteria criteria) throws RentexpresException;

	boolean create(VehicleDTO vehicle, File imagen) throws RentexpresException;

	boolean update(VehicleDTO vehicle, File nuevaImagen) throws RentexpresException;

	List<String> getVehicleImages(Integer vehicleId) throws RentexpresException;

	boolean updateVehicleImage(Integer vehicleId, File nuevaImagen) throws RentexpresException;

}
