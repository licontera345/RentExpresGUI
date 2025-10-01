package com.pinguela.rentexpres.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.pinguela.rentexpres.config.ConfigManager;

/**
 * Clase de utilidades para manejo de conexiones JDBC y transacciones.
 */
public class JDBCUtils {

	private static final Logger logger = LogManager.getLogger(JDBCUtils.class);
	private static final ComboPooledDataSource cpds = new ComboPooledDataSource();

	static {
		try {
			// Cargar driver de la configuración
			String driverClass = ConfigManager.getStringValue("db.driver");
			Class.forName(driverClass);
                        logger.info(LogUtils.buildMessage(JDBCUtils.class,
                                        "Driver JDBC cargado correctamente: " + driverClass));

			// Configurar pool de conexiones
			cpds.setDriverClass(driverClass);
			cpds.setJdbcUrl(ConfigManager.getStringValue("db.url"));
			cpds.setUser(ConfigManager.getStringValue("db.user"));
			cpds.setPassword(ConfigManager.getStringValue("db.password"));

			// Parámetros iniciales del pool
			cpds.setMinPoolSize(5);
			cpds.setAcquireIncrement(3);
			cpds.setMaxPoolSize(20);
			cpds.setMaxIdleTime(300);
			cpds.setTestConnectionOnCheckout(true);

                        logger.info(LogUtils.buildMessage(JDBCUtils.class,
                                        "Pool de conexiones C3P0 inicializado correctamente."));

                } catch (Exception e) {
                        logger.fatal(LogUtils.buildMessage(JDBCUtils.class,
                                        "Error al configurar el pool de conexiones C3P0: " + e.getMessage()), e);
			throw new RuntimeException("Error en configuración de C3P0", e);
		}
	}

	/**
	 * Obtiene una conexión del pool de conexiones.
	 */
	public static Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}

	/**
	 * Inicia una transacción poniendo auto-commit a false.
	 */
	public static void beginTransaction(Connection connection) throws SQLException {
		if (connection != null) {
			connection.setAutoCommit(false);
                        logger.debug(LogUtils.buildMessage(JDBCUtils.class, "Transacción iniciada."));
		}
	}

	/**
	 * Hace commit de la transacción si la conexión no es nula.
	 */
	public static void commitTransaction(Connection connection) {
		if (connection != null) {
			try {
				connection.commit();
                                logger.debug(LogUtils.buildMessage(JDBCUtils.class,
                                                "Transacción confirmada (commit)."));
                        } catch (SQLException e) {
                                logger.error(LogUtils.buildMessage(JDBCUtils.class,
                                                "Error al confirmar la transacción."), e);
			}
		}
	}

	/**
	 * Hace rollback de la transacción si la conexión no es nula.
	 */
	public static void rollbackTransaction(Connection connection) {
		if (connection != null) {
			try {
				// Verifica si la conexión no está cerrada antes de hacer rollback
				if (!connection.isClosed()) {
					connection.rollback();
                                        logger.debug(LogUtils.buildMessage(JDBCUtils.class,
                                                        "Transacción revertida."));
                                } else {
                                        logger.warn(LogUtils.buildMessage(JDBCUtils.class,
                                                        "No se pudo revertir la transacción porque la conexión ya está cerrada."));
				}
                        } catch (SQLException e) {
                                logger.error(LogUtils.buildMessage(JDBCUtils.class,
                                                "Error al revertir la transacción."), e);
			}
		}
	}

	/**
	 * Cierra el PreparedStatement y el ResultSet si no son nulos.
	 */
	public static void close(PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
                } catch (SQLException e) {
                        logger.error(LogUtils.buildMessage(JDBCUtils.class,
                                        "Error cerrando ResultSet o PreparedStatement."), e);
		}
	}

	/**
	 * Cierra la conexión (devuelve al pool).
	 */
	public static void close(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
                                logger.debug(LogUtils.buildMessage(JDBCUtils.class,
                                                "Conexión cerrada (devuelta al pool)."));
			}
                } catch (SQLException e) {
                        logger.error(LogUtils.buildMessage(JDBCUtils.class,
                                        "Error cerrando la conexión."), e);
		}
	}
}
