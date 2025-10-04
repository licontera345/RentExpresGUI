package com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalStatsDTO;
import com.pinguela.rentexpres.model.ReservationStatsDTO;

public interface StatisticsService {
    List<RentalStatsDTO> getRentalsMensuales() throws RentexpresException;
    List<ReservationStatsDTO> getReservationsMensuales() throws RentexpresException;
}
