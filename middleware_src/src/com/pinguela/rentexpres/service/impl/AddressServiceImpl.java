package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.AddressDAO;
import com.pinguela.rentexpres.dao.impl.AddressDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AddressDTO;
import com.pinguela.rentexpres.service.AddressService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LogManager.getLogger(AddressServiceImpl.class);
    private AddressDAO addressDAO;

    public AddressServiceImpl() {
        this.addressDAO = new AddressDAOImpl();
    }

    @Override
    public AddressDTO findById(Integer id) throws RentexpresException {
        Connection connection = null;
        AddressDTO address = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            address = addressDAO.findById(connection, id);
            JDBCUtils.commitTransaction(connection);
            logger.info(LogUtils.buildMessage(AddressServiceImpl.class, "findById de Dirección completado. ID: {}"), id);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(AddressServiceImpl.class, "Error en findById de Dirección: "), e);
            throw new RentexpresException("Error en findById de Dirección", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return address;
    }

    @Override
    public boolean create(AddressDTO address) throws RentexpresException {
        Connection connection = null;
        boolean creado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            creado = addressDAO.create(connection, address);
            if (creado) {
                JDBCUtils.commitTransaction(connection);
                logger.info(LogUtils.buildMessage(AddressServiceImpl.class, "Dirección creada exitosamente. ID: {}"), address.getId());
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn(LogUtils.buildMessage(AddressServiceImpl.class, "No se pudo crear la Dirección."));
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(AddressServiceImpl.class, "Error en create de Dirección: "), e);
            throw new RentexpresException("Error en create de Dirección", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return creado;
    }

    @Override
    public boolean update(AddressDTO address) throws RentexpresException {
        Connection connection = null;
        boolean actualizado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            actualizado = addressDAO.update(connection, address);
            if (actualizado) {
                JDBCUtils.commitTransaction(connection);
                logger.info(LogUtils.buildMessage(AddressServiceImpl.class, "Dirección actualizada exitosamente. ID: {}"), address.getId());
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn(LogUtils.buildMessage(AddressServiceImpl.class, "No se pudo actualizar la Dirección. ID: {}"), address.getId());
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(AddressServiceImpl.class, "Error en update de Dirección: "), e);
            throw new RentexpresException("Error en update de Dirección", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return actualizado;
    }

    @Override
    public boolean delete(AddressDTO address) throws RentexpresException {
        Connection connection = null;
        boolean eliminado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            eliminado = addressDAO.delete(connection, address, address.getId());
            if (eliminado) {
                JDBCUtils.commitTransaction(connection);
                logger.info(LogUtils.buildMessage(AddressServiceImpl.class, "Dirección eliminada exitosamente. ID: {}"), address.getId());
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn(LogUtils.buildMessage(AddressServiceImpl.class, "No se pudo delete la Dirección. ID: {}"), address.getId());
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(AddressServiceImpl.class, "Error en delete de Dirección: "), e);
            throw new RentexpresException("Error en delete de Dirección", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return eliminado;
    }
}
