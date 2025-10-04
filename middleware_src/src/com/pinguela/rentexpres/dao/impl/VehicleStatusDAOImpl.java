package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.VehicleStatusDAO;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class VehicleStatusDAOImpl implements VehicleStatusDAO {

    private static final Logger logger = LogManager.getLogger(VehicleStatusDAOImpl.class);

    @Override
    public VehicleStatusDTO findById(Connection connection, Integer id) {
        VehicleStatusDTO ev = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("SELECT id_estado_vehicle, name_estado FROM estado_vehicle WHERE id_estado_vehicle = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                ev = loadVehicleStatus(rs);
                logger.info(LogUtils.buildMessage(VehicleStatusDAOImpl.class, "VehicleStatus encontrado con ID: " + id));
            }
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(VehicleStatusDAOImpl.class, "Error al buscar VehicleStatus por ID: " + e.getMessage()), e);
        } finally {
			JDBCUtils.close(ps, rs);
        }
        return ev;
    }

    @Override
    public List<VehicleStatusDTO> findAll(Connection connection) {
        List<VehicleStatusDTO> lista = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT id_estado_vehicle, name_estado FROM estado_vehicle")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(loadVehicleStatus(rs));
                }
                logger.info(LogUtils.buildMessage(VehicleStatusDAOImpl.class, "Estados de vehicle encontrados: " + lista.size()));
            }
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(VehicleStatusDAOImpl.class, "Error al obtener todos los VehicleStatus: " + e.getMessage()), e);
        }
        return lista;
    }

    private VehicleStatusDTO loadVehicleStatus(ResultSet rs) throws SQLException {
        VehicleStatusDTO ev = new VehicleStatusDTO();
        ev.setId(rs.getInt("id_estado_vehicle"));
        ev.setStatusName(rs.getString("name_estado"));
        return ev;
    }
}
