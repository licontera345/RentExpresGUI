package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;

public interface VehicleCategoryDAO {

	public VehicleCategoryDTO findById(Connection connection, Integer id)throws DataException;

	public List<VehicleCategoryDTO> findAll(Connection connection)throws DataException;

}
