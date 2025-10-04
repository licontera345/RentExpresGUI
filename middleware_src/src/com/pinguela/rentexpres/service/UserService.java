package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UserCriteria;
import com.pinguela.rentexpres.model.UserDTO;

public interface UserService {

	public UserDTO findById(Integer id) throws RentexpresException;

	public boolean create(UserDTO user) throws RentexpresException;

	public boolean update(UserDTO user) throws RentexpresException;

	public boolean delete(UserDTO user, Integer id) throws RentexpresException;

	public UserDTO autenticar(String username, String contrasenaEnClaro) throws RentexpresException;

	public List<UserDTO> findAll() throws RentexpresException;

        public Results<UserDTO> findByCriteria(UserCriteria criteria) throws RentexpresException;

        /** Obtiene las rutas de las imágenes asociadas a un user. */
        public List<String> getUserImages(Integer userId) throws RentexpresException;
}
