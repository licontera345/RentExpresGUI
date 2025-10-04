package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;

public interface VehicleCategoryService {
	public VehicleCategoryDTO findById(Integer id) throws RentexpresException;

	public List<VehicleCategoryDTO> findAll() throws RentexpresException;
}
