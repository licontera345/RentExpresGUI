package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.ReservationStatsDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservationStatsDTO;
import com.pinguela.rentexpres.util.LogUtils;

public class ReservationStatsDAOImpl implements ReservationStatsDAO {

    private static final Logger logger = LogManager.getLogger(ReservationStatsDAOImpl.class);

    private static final String SQL =
        "SELECT YEAR(fecha_inicio) AS anio, MONTH(fecha_inicio) AS mes, COUNT(*) AS total " +
        "FROM reservation GROUP BY anio, mes ORDER BY anio, mes";

    @Override
    public List<ReservationStatsDTO> getReservationStats(Connection connection) throws DataException {
        List<ReservationStatsDTO> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ReservationStatsDTO dto = new ReservationStatsDTO();
                dto.setYear(rs.getInt("anio"));
                dto.setMonth(rs.getInt("mes"));
                dto.setTotalReservations(rs.getInt("total"));
                lista.add(dto);
            }
            logger.info(LogUtils.buildMessage(ReservationStatsDAOImpl.class, "Estadísticas de reservation cargadas: {}"), lista.size());
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(ReservationStatsDAOImpl.class, "Error obteniendo estadísticas de reservation"), e);
            throw new DataException("Error obteniendo estadísticas de reservation", e);
        }
        return lista;
    }
}
