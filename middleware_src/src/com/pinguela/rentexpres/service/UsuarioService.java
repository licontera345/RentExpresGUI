package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UsuarioCriteria;
import com.pinguela.rentexpres.model.UsuarioDTO;

public interface UsuarioService {

	public UsuarioDTO findById(Integer id) throws RentexpresException;

	public boolean create(UsuarioDTO usuario) throws RentexpresException;

	public boolean update(UsuarioDTO usuario) throws RentexpresException;

	public boolean delete(UsuarioDTO usuario, Integer id) throws RentexpresException;

	public UsuarioDTO autenticar(String nombreUsuario, String contrasenaEnClaro) throws RentexpresException;

	public List<UsuarioDTO> findAll() throws RentexpresException;

	public Results<UsuarioDTO> findByCriteria(UsuarioCriteria criteria) throws RentexpresException;
}
