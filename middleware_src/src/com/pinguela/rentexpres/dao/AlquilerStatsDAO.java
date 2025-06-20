package com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.AlquilerStatsDTO;

public interface AlquilerStatsDAO {
    List<AlquilerStatsDTO> getAlquilerStats(Connection connection) throws DataException;
}
