package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CityDTO;

public interface CityService {

	public CityDTO findById(Integer id) throws RentexpresException;

	public List<CityDTO> findAll() throws RentexpresException;

	public boolean create(CityDTO city) throws RentexpresException;

	public boolean update(CityDTO city) throws RentexpresException;

	public boolean delete(CityDTO city) throws RentexpresException;

	List<CityDTO> findByProvinceId(Integer idProvince) throws RentexpresException;
}
