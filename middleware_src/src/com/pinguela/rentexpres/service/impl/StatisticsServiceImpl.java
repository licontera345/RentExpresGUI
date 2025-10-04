package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.RentalStatsDAO;
import com.pinguela.rentexpres.dao.ReservationStatsDAO;
import com.pinguela.rentexpres.dao.impl.RentalStatsDAOImpl;
import com.pinguela.rentexpres.dao.impl.ReservationStatsDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalStatsDTO;
import com.pinguela.rentexpres.model.ReservationStatsDTO;
import com.pinguela.rentexpres.service.StatisticsService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger logger = LogManager.getLogger(StatisticsServiceImpl.class);

    private final RentalStatsDAO rentalDAO = new RentalStatsDAOImpl();
    private final ReservationStatsDAO reservationDAO = new ReservationStatsDAOImpl();

    @Override
    public List<RentalStatsDTO> getRentalsMensuales() throws RentexpresException {
        Connection conn = null;
        List<RentalStatsDTO> list = null;
        try {
            conn = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(conn);
            list = rentalDAO.getRentalStats(conn);
            JDBCUtils.commitTransaction(conn);
            logger.info(LogUtils.buildMessage(StatisticsServiceImpl.class, "Estadísticas de rental obtenidas: {}"), list != null ? list.size() : 0);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(conn);
            logger.error(LogUtils.buildMessage(StatisticsServiceImpl.class, "Error en getRentalsMensuales"), e);
            throw new RentexpresException("Error obteniendo estadísticas de rental", e);
        } finally {
            JDBCUtils.close(conn);
        }
        return list;
    }

    @Override
    public List<ReservationStatsDTO> getReservationsMensuales() throws RentexpresException {
        Connection conn = null;
        List<ReservationStatsDTO> list = null;
        try {
            conn = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(conn);
            list = reservationDAO.getReservationStats(conn);
            JDBCUtils.commitTransaction(conn);
            logger.info(LogUtils.buildMessage(StatisticsServiceImpl.class, "Estadísticas de reservation obtenidas: {}"), list != null ? list.size() : 0);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(conn);
            logger.error(LogUtils.buildMessage(StatisticsServiceImpl.class, "Error en getReservationsMensuales"), e);
            throw new RentexpresException("Error obteniendo estadísticas de reservation", e);
        } finally {
            JDBCUtils.close(conn);
        }
        return list;
    }
}
