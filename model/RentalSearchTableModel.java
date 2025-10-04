package com.pinguela.rentexpres.desktop.model;

import java.util.List;
import java.util.Map;

import com.pinguela.rentexpres.model.RentalDTO;

public class RentalSearchTableModel extends AbstractSearchTableModel<RentalDTO> {

	private static final long serialVersionUID = 1L;

        private static final String[] DATA_COLUMNS = { "ID", "Reservation", "Vehicle", "Make", "Model", "Customer", "Inicio",
                        "Fin", "KM Inicio", "KM Fin", "Coste", "Estado", "Acciones" };

        private static final Class<?>[] DATA_CLASSES = { Integer.class, Integer.class, String.class, String.class,
                        String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class,
                        String.class, Object.class };

	private final Map<Integer, String> estadoMap;

	public RentalSearchTableModel(Map<Integer, String> estadoMap) {
		super(DATA_COLUMNS, DATA_CLASSES);
		this.estadoMap = estadoMap;
	}

	@Override
	protected Integer getIdOf(RentalDTO a) {
		return a.getId();
	}

	@Override
	protected Object getFieldAt(RentalDTO a, int col) {
		Object value;
		switch (col) {
		case 0:
			value = a.getId();
			break;
		case 1:
			value = a.getReservationId();
			break;
		case 2:
			value = a.getLicensePlate();
			break;
		case 3:
			value = a.getMake();
			break;
		case 4:
			value = a.getModel();
			break;
		case 5:
			value = a.getName();
			break;
		case 6:
			value = a.getActualStartDate();
			break;
		case 7:
			value = a.getActualEndDate();
			break;
		case 8:
			value = a.getStartKm();
			break;
		case 9:
			value = a.getEndKm();
			break;
                case 10:
                        value = a.getTotalCost();
                        break;
                case 11:
                        value = estadoMap.getOrDefault(a.getRentalStatusId(), String.valueOf(a.getRentalStatusId()));
                        break;
                case 12:
                        value = null;
                        break;
		default:
			value = null;
			break;
		}
		return value;
	}

	public void setRentals(List<RentalDTO> lista) {
		setData(lista);
	}

	public RentalDTO getRentalAt(int row) {
		return getItem(row);
	}

	public RentalSearchTableModel(java.util.List<RentalDTO> data, java.util.Map<Integer, String> estadoMap) {
		this(estadoMap);
		setRentals(data);
	}
}
