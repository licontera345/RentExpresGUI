package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.ReservationStatusDAO;
import com.pinguela.rentexpres.dao.impl.ReservationStatusDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationStatusDTO;
import com.pinguela.rentexpres.service.ReservationStatusService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class ReservationStatusServiceImpl implements ReservationStatusService {
    private static final Logger logger = LogManager.getLogger(ReservationStatusServiceImpl.class);
    private ReservationStatusDAO reservationStatusDAO;
    
    public ReservationStatusServiceImpl() {
        this.reservationStatusDAO = new ReservationStatusDAOImpl();
    }
    
    @Override
    public ReservationStatusDTO findById(Integer id) throws RentexpresException {
        Connection connection = null;
        ReservationStatusDTO er = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            er = reservationStatusDAO.findById(connection, id);
            JDBCUtils.commitTransaction(connection);
            logger.info(LogUtils.buildMessage(ReservationStatusServiceImpl.class, "findById de ReservationStatus completado. ID: " + id));
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(ReservationStatusServiceImpl.class, "Error en findById de ReservationStatus: "), e);
            throw new RentexpresException("Error en findById de ReservationStatus", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return er;
    }
    
    @Override
    public List<ReservationStatusDTO> findAll() throws RentexpresException {
        Connection connection = null;
        List<ReservationStatusDTO> lista = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            lista = reservationStatusDAO.findAll(connection);
            JDBCUtils.commitTransaction(connection);
            logger.info(LogUtils.buildMessage(ReservationStatusServiceImpl.class, "findAll de ReservationStatus completado. Cantidad: " + (lista != null ? lista.size() : 0)));
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(ReservationStatusServiceImpl.class, "Error en findAll de ReservationStatus: "), e);
            throw new RentexpresException("Error en findAll de ReservationStatus", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return lista;
    }
}
