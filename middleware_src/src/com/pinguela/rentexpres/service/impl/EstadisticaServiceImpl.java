package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.AlquilerStatsDAO;
import com.pinguela.rentexpres.dao.ReservaStatsDAO;
import com.pinguela.rentexpres.dao.impl.AlquilerStatsDAOImpl;
import com.pinguela.rentexpres.dao.impl.ReservaStatsDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerStatsDTO;
import com.pinguela.rentexpres.model.ReservaStatsDTO;
import com.pinguela.rentexpres.service.EstadisticaService;
import com.pinguela.rentexpres.util.JDBCUtils;

public class EstadisticaServiceImpl implements EstadisticaService {

    private static final Logger logger = LogManager.getLogger(EstadisticaServiceImpl.class);

    private final AlquilerStatsDAO alquilerDAO = new AlquilerStatsDAOImpl();
    private final ReservaStatsDAO reservaDAO = new ReservaStatsDAOImpl();

    @Override
    public List<AlquilerStatsDTO> getAlquileresMensuales() throws RentexpresException {
        Connection conn = null;
        List<AlquilerStatsDTO> list = null;
        try {
            conn = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(conn);
            list = alquilerDAO.getAlquilerStats(conn);
            JDBCUtils.commitTransaction(conn);
            logger.info("Estadísticas de alquiler obtenidas: {}", list != null ? list.size() : 0);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(conn);
            logger.error("Error en getAlquileresMensuales", e);
            throw new RentexpresException("Error obteniendo estadísticas de alquiler", e);
        } finally {
            JDBCUtils.close(conn);
        }
        return list;
    }

    @Override
    public List<ReservaStatsDTO> getReservasMensuales() throws RentexpresException {
        Connection conn = null;
        List<ReservaStatsDTO> list = null;
        try {
            conn = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(conn);
            list = reservaDAO.getReservaStats(conn);
            JDBCUtils.commitTransaction(conn);
            logger.info("Estadísticas de reserva obtenidas: {}", list != null ? list.size() : 0);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(conn);
            logger.error("Error en getReservasMensuales", e);
            throw new RentexpresException("Error obteniendo estadísticas de reserva", e);
        } finally {
            JDBCUtils.close(conn);
        }
        return list;
    }
}
