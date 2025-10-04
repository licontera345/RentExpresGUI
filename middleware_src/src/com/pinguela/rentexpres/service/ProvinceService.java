package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ProvinceDTO;

public interface ProvinceService {

	public ProvinceDTO findById(Integer id) throws RentexpresException;

	public List<ProvinceDTO> findAll() throws RentexpresException;

	public boolean create(ProvinceDTO province) throws RentexpresException;

	public boolean update(ProvinceDTO province) throws RentexpresException;

	public boolean delete(ProvinceDTO province) throws RentexpresException;

}
