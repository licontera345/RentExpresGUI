package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.ReservaStatsDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservaStatsDTO;

public class ReservaStatsDAOImpl implements ReservaStatsDAO {

    private static final Logger logger = LogManager.getLogger(ReservaStatsDAOImpl.class);

    private static final String SQL =
        "SELECT YEAR(fecha_inicio) AS anio, MONTH(fecha_inicio) AS mes, COUNT(*) AS total " +
        "FROM reserva GROUP BY anio, mes ORDER BY anio, mes";

    @Override
    public List<ReservaStatsDTO> getReservaStats(Connection connection) throws DataException {
        List<ReservaStatsDTO> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ReservaStatsDTO dto = new ReservaStatsDTO();
                dto.setYear(rs.getInt("anio"));
                dto.setMonth(rs.getInt("mes"));
                dto.setTotalReservas(rs.getInt("total"));
                lista.add(dto);
            }
            logger.info("Estadísticas de reserva cargadas: {}", lista.size());
        } catch (SQLException e) {
            logger.error("Error obteniendo estadísticas de reserva", e);
            throw new DataException("Error obteniendo estadísticas de reserva", e);
        }
        return lista;
    }
}
