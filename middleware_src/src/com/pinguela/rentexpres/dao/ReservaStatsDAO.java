package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservaStatsDTO;

public interface ReservaStatsDAO {
    List<ReservaStatsDTO> getReservaStats(Connection connection) throws DataException;
}
