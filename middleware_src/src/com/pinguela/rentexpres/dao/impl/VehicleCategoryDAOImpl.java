package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.VehicleCategoryDAO;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class VehicleCategoryDAOImpl implements VehicleCategoryDAO {

    private static final Logger logger = LogManager.getLogger(VehicleCategoryDAOImpl.class);

    @Override
    public VehicleCategoryDTO findById(Connection connection, Integer id) {
        VehicleCategoryDTO cv = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_category, name_category FROM category_vehicle WHERE id_category = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                cv = loadVehicleCategory(rs);
                logger.info(LogUtils.buildMessage(VehicleCategoryDAOImpl.class, "VehicleCategory encontrada con ID: {}"), id);
            }
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(VehicleCategoryDAOImpl.class, "Error al buscar VehicleCategory por ID: {}"), e.getMessage(), e);
        } finally {
			JDBCUtils.close(ps, rs);
        }
        return cv;
    }

    @Override
    public List<VehicleCategoryDTO> findAll(Connection connection) {
        List<VehicleCategoryDTO> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_category, name_category FROM category_vehicle";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(loadVehicleCategory(rs));
            }
            logger.info(LogUtils.buildMessage(VehicleCategoryDAOImpl.class, "Total categorys de vehicle: {}"), lista.size());
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(VehicleCategoryDAOImpl.class, "Error al obtener todas las VehicleCategory: {}"), e.getMessage(), e);
        } finally {
            JDBCUtils.close(ps, rs);
        }
        return lista;
    }

    private VehicleCategoryDTO loadVehicleCategory(ResultSet rs) throws SQLException {
        VehicleCategoryDTO cv = new VehicleCategoryDTO();
        cv.setId(rs.getInt("id_category"));
        cv.setCategoryName(rs.getString("name_category"));
        return cv;
    }
}
