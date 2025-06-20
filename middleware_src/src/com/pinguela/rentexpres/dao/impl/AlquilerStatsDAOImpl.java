package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.AlquilerStatsDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.AlquilerStatsDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class AlquilerStatsDAOImpl implements AlquilerStatsDAO {

    private static final Logger logger = LogManager.getLogger(AlquilerStatsDAOImpl.class);

    private static final String SQL =
        "SELECT YEAR(fecha_inicio_efectivo) AS anio, MONTH(fecha_inicio_efectivo) AS mes, " +
        "COUNT(*) AS total, SUM(coste_total) AS ingresos FROM alquiler " +
        "GROUP BY anio, mes ORDER BY anio, mes";

    @Override
    public List<AlquilerStatsDTO> getAlquilerStats(Connection connection) throws DataException {
        List<AlquilerStatsDTO> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AlquilerStatsDTO dto = new AlquilerStatsDTO();
                dto.setYear(rs.getInt("anio"));
                dto.setMonth(rs.getInt("mes"));
                dto.setTotalAlquileres(rs.getInt("total"));
                dto.setTotalIngresos(rs.getDouble("ingresos"));
                lista.add(dto);
            }
            logger.info("Estadísticas de alquiler cargadas: {}", lista.size());
        } catch (SQLException e) {
            logger.error("Error obteniendo estadísticas de alquiler", e);
            throw new DataException("Error obteniendo estadísticas de alquiler", e);
        }
        return lista;
    }
}
