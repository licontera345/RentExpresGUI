package com.pinguela.rentexpres.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.CustomerDAO;
import com.pinguela.rentexpres.dao.impl.CustomerDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.model.CustomerCriteria;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.CustomerService;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);
    private CustomerDAO customerDAO;

    public CustomerServiceImpl() {
        this.customerDAO = new CustomerDAOImpl();
    }

    @Override
    public CustomerDTO findById(Integer id) throws RentexpresException {
        Connection connection = null;
        CustomerDTO customer = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            customer = customerDAO.findById(connection, id);
            JDBCUtils.commitTransaction(connection);
            logger.info(LogUtils.buildMessage(CustomerServiceImpl.class, "findById de Customer completado. ID: {}"), id);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(CustomerServiceImpl.class, "Error en findById de Customer: "), e);
            throw new RentexpresException("Error en findById de Customer", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return customer;
    }

    @Override
    public List<CustomerDTO> findAll() throws RentexpresException {
        Connection connection = null;
        List<CustomerDTO> lista = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            lista = customerDAO.findAll(connection);
            JDBCUtils.commitTransaction(connection);
            logger.info(LogUtils.buildMessage(CustomerServiceImpl.class, "findAll de Customer completado. Cantidad: {}"), (lista != null ? lista.size() : 0));
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(CustomerServiceImpl.class, "Error en findAll de Customer: "), e);
            throw new RentexpresException("Error en findAll de Customer", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return lista;
    }

    @Override
    public boolean create(CustomerDTO customer) throws RentexpresException {
        Connection connection = null;
        boolean creado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            creado = customerDAO.create(connection, customer);
            if (creado) {
                JDBCUtils.commitTransaction(connection);
                logger.info(LogUtils.buildMessage(CustomerServiceImpl.class, "Customer creado exitosamente. ID: {}"), customer.getId());
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn(LogUtils.buildMessage(CustomerServiceImpl.class, "No se pudo crear el Customer."));
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(CustomerServiceImpl.class, "Error en create de Customer: "), e);
            throw new RentexpresException("Error en create de Customer", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return creado;
    }

    @Override
    public boolean update(CustomerDTO customer) throws RentexpresException {
        Connection connection = null;
        boolean actualizado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            actualizado = customerDAO.update(connection, customer);
            if (actualizado) {
                JDBCUtils.commitTransaction(connection);
                logger.info(LogUtils.buildMessage(CustomerServiceImpl.class, "Customer actualizado exitosamente. ID: {}"), customer.getId());
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn(LogUtils.buildMessage(CustomerServiceImpl.class, "No se pudo actualizar el Customer. ID: {}"), customer.getId());
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(CustomerServiceImpl.class, "Error en update de Customer: "), e);
            throw new RentexpresException("Error en update de Customer", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return actualizado;
    }

    @Override
    public boolean delete(Integer id) throws RentexpresException {
        Connection connection = null;
        boolean eliminado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            eliminado = customerDAO.delete(connection, id);
            if (eliminado) {
                JDBCUtils.commitTransaction(connection);
                logger.info(LogUtils.buildMessage(CustomerServiceImpl.class, "Customer eliminado exitosamente. ID: {}"), id);
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn(LogUtils.buildMessage(CustomerServiceImpl.class, "No se pudo delete el Customer. ID: {}"), id);
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(CustomerServiceImpl.class, "Error en delete de Customer: "), e);
            throw new RentexpresException("Error en delete de Customer", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return eliminado;
    }

    @Override
    public Results<CustomerDTO> findByCriteria(CustomerCriteria criteria) throws RentexpresException {
        Results<CustomerDTO> results = null;
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);
            results = customerDAO.findByCriteria(connection, criteria);
            JDBCUtils.commitTransaction(connection);
            logger.info(LogUtils.buildMessage(CustomerServiceImpl.class, "findByCriteria de Customer completado: Página {} (Tamaño: {}), Total registros: {}"),
                    criteria.getPageNumber(), criteria.getPageSize(), results.getTotalRecords());
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error(LogUtils.buildMessage(CustomerServiceImpl.class, "Error en findByCriteria de Customer: "), e);
            throw new RentexpresException("Error en findByCriteria de Customer", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return results;
    }
}
