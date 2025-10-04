package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservationStatsDTO;

public interface ReservationStatsDAO {
    List<ReservationStatsDTO> getReservationStats(Connection connection) throws DataException;
}
