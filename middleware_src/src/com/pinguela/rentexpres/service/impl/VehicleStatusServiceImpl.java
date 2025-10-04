package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.VehicleStatusDAO;
import com.pinguela.rentexpres.dao.impl.VehicleStatusDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.service.VehicleStatusService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class VehicleStatusServiceImpl implements VehicleStatusService {

    private static final Logger logger = LogManager.getLogger(VehicleStatusServiceImpl.class);
    private VehicleStatusDAO vehicleStatusDAO;

    public VehicleStatusServiceImpl() {
        this.vehicleStatusDAO = new VehicleStatusDAOImpl();
    }

    @Override
    public VehicleStatusDTO findById(Integer id) throws RentexpresException {
        Connection connection = null;
        VehicleStatusDTO estado = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            try {
				estado = vehicleStatusDAO.findById(connection, id);
			} catch (DataException e) {
				e.printStackTrace();
			}
            JDBCUtils.commitTransaction(connection);
            logger.info(LogUtils.buildMessage(VehicleStatusServiceImpl.class, "findById de VehicleStatus completado. ID: " + id));
        } catch (SQLException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(VehicleStatusServiceImpl.class, "Error en findById de VehicleStatus: "), e);
            throw new RentexpresException("Error en findById de VehicleStatus", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return estado;
    }

    @Override
    public List<VehicleStatusDTO> findAll() throws RentexpresException {
        Connection connection = null;
        List<VehicleStatusDTO> lista = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            try {
				lista = vehicleStatusDAO.findAll(connection);
			} catch (DataException e) {
				e.printStackTrace();
			}
            JDBCUtils.commitTransaction(connection);
            logger.info(LogUtils.buildMessage(VehicleStatusServiceImpl.class, "findAll de VehicleStatus completado. Cantidad: " + (lista != null ? lista.size() : 0)));
        } catch (SQLException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(VehicleStatusServiceImpl.class, "Error en findAll de VehicleStatus: "), e);
            throw new RentexpresException("Error en findAll de VehicleStatus", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return lista;
    }
}
