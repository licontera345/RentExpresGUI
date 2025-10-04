package com.pinguela.rentexpres.desktop.model;

import java.util.List;
import java.util.Map;

import com.pinguela.rentexpres.model.ReservationDTO;

/**
 * Table model for reservations.
 */
public class ReservationSearchTableModel extends AbstractSearchTableModel<ReservationDTO> {
    private static final long serialVersionUID = 1L;

    private static final String[] DATA_COLUMNS = { "ID", "Veh\u00edculo", "LicensePlate", "Make",
            "Model", "Precio/D\u00eda", "Name", "Apellido", "Tel\u00e9fono", "Inicio",
            "Fin", "Estado", "Acciones" };

    private static final Class<?>[] DATA_CLASSES = { Integer.class, Integer.class, String.class,
            String.class, String.class, Double.class, String.class, String.class,
            String.class, String.class, String.class, String.class, Object.class };

    private final Map<Integer, String> estadoMap;

    public ReservationSearchTableModel(Map<Integer, String> estadoMap) {
        super(DATA_COLUMNS, DATA_CLASSES);
        this.estadoMap = estadoMap;
    }

    public ReservationSearchTableModel(List<ReservationDTO> data, Map<Integer, String> estadoMap) {
        this(estadoMap);
        setReservations(data);
    }

    @Override
    protected Integer getIdOf(ReservationDTO r) {
        return r.getId();
    }

    @Override
    protected Object getFieldAt(ReservationDTO r, int col) {
        switch (col) {
        case 0:
            return r.getId();
        case 1:
            return r.getVehicleId();
        case 2:
            return r.getLicensePlate();
        case 3:
            return r.getMake();
        case 4:
            return r.getModel();
        case 5:
            return r.getDailyPrice();
        case 6:
            return r.getName();
        case 7:
            return r.getLastName();
        case 8:
            return r.getPhone();
        case 9:
            return r.getStartDate();
        case 10:
            return r.getEndDate();
        case 11:
            return estadoMap != null ? estadoMap.getOrDefault(r.getReservationIdStatus(), r.getStatusName()) : r.getStatusName();
        case 12:
            return null;
        default:
            return null;
        }
    }

    public void setReservations(List<ReservationDTO> lista) {
        setData(lista);
    }

    public ReservationDTO getReservationAt(int row) {
        return getItem(row);
    }
}
