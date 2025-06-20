package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerStatsDTO;
import com.pinguela.rentexpres.model.ReservaStatsDTO;

public interface EstadisticaService {
    List<AlquilerStatsDTO> getAlquileresMensuales() throws RentexpresException;
    List<ReservaStatsDTO> getReservasMensuales() throws RentexpresException;
}
