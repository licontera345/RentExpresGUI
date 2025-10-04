package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pinguela.rentexpres.dao.ReservationStatusDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservationStatusDTO;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class ReservationStatusDAOImpl implements ReservationStatusDAO {
    private static final Logger logger = LogManager.getLogger(ReservationStatusDAOImpl.class);
    
    @Override
    public ReservationStatusDTO findById(Connection connection, Integer id) throws DataException {
        ReservationStatusDTO er = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT id_estado_reservation, name_estado FROM estado_reservation WHERE id_estado_reservation = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                er = new ReservationStatusDTO();
                er.setId(rs.getInt("id_estado_reservation"));
                er.setStatusName(rs.getString("name_estado"));
                logger.info(LogUtils.buildMessage(ReservationStatusDAOImpl.class, "ReservationStatus encontrado, id: " + id));
            }
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(ReservationStatusDAOImpl.class, "Error al buscar ReservationStatus por ID: " + id), e);
            throw new DataException("Error al buscar ReservationStatus por ID: " + id, e);
        } finally {
            JDBCUtils.close(ps, rs);
        }
        return er;
    }
    
    @Override
    public List<ReservationStatusDTO> findAll(Connection connection) throws DataException {
        List<ReservationStatusDTO> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT id_estado_reservation, name_estado FROM estado_reservation";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReservationStatusDTO er = new ReservationStatusDTO();
                er.setId(rs.getInt("id_estado_reservation"));
                er.setStatusName(rs.getString("name_estado"));
                lista.add(er);
            }
            logger.info(LogUtils.buildMessage(ReservationStatusDAOImpl.class, "Total de ReservationStatus encontrados: " + lista.size()));
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(ReservationStatusDAOImpl.class, "Error al obtener ReservationStatus"), e);
            throw new DataException("Error al obtener ReservationStatus", e);
        } finally {
            JDBCUtils.close(ps, rs);
        }
        return lista;
    }
}
