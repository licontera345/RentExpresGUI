package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.RentalStatsDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RentalStatsDTO;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class RentalStatsDAOImpl implements RentalStatsDAO {

    private static final Logger logger = LogManager.getLogger(RentalStatsDAOImpl.class);

    private static final String SQL =
        "SELECT YEAR(fecha_inicio_efectivo) AS anio, MONTH(fecha_inicio_efectivo) AS mes, " +
        "COUNT(*) AS total, SUM(coste_total) AS ingresos FROM rental " +
        "GROUP BY anio, mes ORDER BY anio, mes";

    @Override
    public List<RentalStatsDTO> getRentalStats(Connection connection) throws DataException {
        List<RentalStatsDTO> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RentalStatsDTO dto = new RentalStatsDTO();
                dto.setYear(rs.getInt("anio"));
                dto.setMonth(rs.getInt("mes"));
                dto.setTotalRentals(rs.getInt("total"));
                dto.setTotalIngresos(rs.getDouble("ingresos"));
                lista.add(dto);
            }
            logger.info(LogUtils.buildMessage(RentalStatsDAOImpl.class, "Estadísticas de rental cargadas: {}"), lista.size());
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(RentalStatsDAOImpl.class, "Error obteniendo estadísticas de rental"), e);
            throw new DataException("Error obteniendo estadísticas de rental", e);
        }
        return lista;
    }
}
