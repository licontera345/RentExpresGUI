package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.RentalStatusDAO;
import com.pinguela.rentexpres.model.RentalStatusDTO;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class RentalStatusDAOImpl implements RentalStatusDAO {

    private static final Logger logger = LogManager.getLogger(RentalStatusDAOImpl.class);


    @Override
    public RentalStatusDTO findById(Connection connection, Integer id) {
        RentalStatusDTO ea = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("SELECT id_estado_rental, name_estado FROM estado_rental WHERE id_estado_rental = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                ea = loadRentalStatus(rs);
                logger.info(LogUtils.buildMessage(RentalStatusDAOImpl.class, "RentalStatus encontrado con ID: " + id));
            }
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(RentalStatusDAOImpl.class, "Error al buscar RentalStatus por ID: " + e.getMessage()), e);
        } finally {
			JDBCUtils.close(ps, rs);
        }
        return ea;
    }
    @Override
    public List<RentalStatusDTO> findAll(Connection connection) {
        List<RentalStatusDTO> lista = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT id_estado_rental, name_estado FROM estado_rental")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(loadRentalStatus(rs));
                }
                logger.info(LogUtils.buildMessage(RentalStatusDAOImpl.class, "Total EstadosRental: " + lista.size()));
            }
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(RentalStatusDAOImpl.class, "Error al obtener todos los RentalStatus: " + e.getMessage()), e);
        }
        return lista;
    }

    private RentalStatusDTO loadRentalStatus(ResultSet rs) throws SQLException {
        RentalStatusDTO ea = new RentalStatusDTO();
        ea.setId(rs.getInt("id_estado_rental"));
        ea.setStatusName(rs.getString("name_estado"));

        return ea;
    }
}
