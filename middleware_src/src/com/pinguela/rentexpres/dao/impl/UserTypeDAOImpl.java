package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.UserTypeDAO;
import com.pinguela.rentexpres.model.UserTypeDTO;
import com.pinguela.rentexpres.util.JDBCUtils;
import com.pinguela.rentexpres.util.LogUtils;

public class UserTypeDAOImpl implements UserTypeDAO {

	private static final Logger logger = LogManager.getLogger(UserTypeDAOImpl.class);

	@Override
    public UserTypeDTO findById(Connection connection, Integer id) {
        UserTypeDTO tu = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("SELECT id_tipo_user, name_tipo FROM tipo_user WHERE id_tipo_user = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                tu = loadUserType(rs);
                logger.info(LogUtils.buildMessage(UserTypeDAOImpl.class, "UserType encontrado con ID: " + id));
            }
        } catch (SQLException e) {
            logger.error(LogUtils.buildMessage(UserTypeDAOImpl.class, "Error al buscar UserType por ID: " + e.getMessage()), e);
        } finally {
            JDBCUtils.close(ps, rs);
        }
        return tu;
    }

	@Override
	public List<UserTypeDTO> findAll(Connection connection) {
		List<UserTypeDTO> lista = new ArrayList<>();

		try (PreparedStatement ps = connection
				.prepareStatement("SELECT id_tipo_user, name_tipo FROM tipo_user")) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(loadUserType(rs));
				}
				logger.info(LogUtils.buildMessage(UserTypeDAOImpl.class, "Total UserTypes found: " + lista.size()));
			}
		} catch (SQLException e) {
			logger.error(LogUtils.buildMessage(UserTypeDAOImpl.class, "Error retrieving all UserTypes: " + e.getMessage()), e);
		} 

		return lista;
	}

	private UserTypeDTO loadUserType(ResultSet rs) throws SQLException {
		UserTypeDTO tu = new UserTypeDTO();
		tu.setId(rs.getInt("id_tipo_user"));
		tu.setNameTipo(rs.getString("name_tipo"));
		return tu;
	}
}
