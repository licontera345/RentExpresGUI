package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RentalStatsDTO;

public interface RentalStatsDAO {
    List<RentalStatsDTO> getRentalStats(Connection connection) throws DataException;
}
