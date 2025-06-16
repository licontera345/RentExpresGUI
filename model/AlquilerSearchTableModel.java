package com.pinguela.rentexpres.desktop.model;

import java.util.List;
import java.util.Map;

import com.pinguela.rentexpres.model.AlquilerDTO;

public class AlquilerSearchTableModel extends AbstractSearchTableModel<AlquilerDTO> {

	private static final long serialVersionUID = 1L;

	private static final String[] DATA_COLUMNS = { "ID", "Reserva", "Vehículo", "Marca", "Modelo", "Cliente", "Inicio",
			"Fin", "KM Inicio", "KM Fin", "Estado", "Acciones" };

	private static final Class<?>[] DATA_CLASSES = { Integer.class, Integer.class, String.class, String.class,
			String.class, String.class, String.class, String.class, Integer.class, Integer.class, String.class,
			Object.class };

	private final Map<Integer, String> estadoMap;

	public AlquilerSearchTableModel(Map<Integer, String> estadoMap) {
		super(DATA_COLUMNS, DATA_CLASSES);
		this.estadoMap = estadoMap;
	}

	@Override
	protected Integer getIdOf(AlquilerDTO a) {
		return a.getId();
	}

	@Override
	protected Object getFieldAt(AlquilerDTO a, int col) {
		Object value;
		switch (col) {
		case 0:
			value = a.getId();
			break;
		case 1:
			value = a.getIdReserva();
			break;
		case 2:
			value = a.getPlaca();
			break;
		case 3:
			value = a.getMarca();
			break;
		case 4:
			value = a.getModelo();
			break;
		case 5:
			value = a.getNombre();
			break;
		case 6:
			value = a.getFechaInicioEfectivo();
			break;
		case 7:
			value = a.getFechaFinEfectivo();
			break;
		case 8:
			value = a.getKmInicial();
			break;
		case 9:
			value = a.getKmFinal();
			break;
		case 10:
			value = estadoMap.getOrDefault(a.getIdEstadoAlquiler(), String.valueOf(a.getIdEstadoAlquiler()));
			break;
		case 11:
			value = null;
			break; 
		default:
			value = null;
			break;
		}
		return value;
	}

	public void setAlquileres(List<AlquilerDTO> lista) {
		setData(lista);
	}

	public AlquilerDTO getAlquilerAt(int row) {
		return getItem(row);
	}

	public AlquilerSearchTableModel(java.util.List<AlquilerDTO> data, java.util.Map<Integer, String> estadoMap) {
		this(estadoMap);
		setAlquileres(data);
	}
}
