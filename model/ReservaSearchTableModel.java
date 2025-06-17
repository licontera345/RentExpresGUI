package com.pinguela.rentexpres.desktop.model;

import java.util.List;
import java.util.Map;

import com.pinguela.rentexpres.model.ReservaDTO;

/**
 * Table model for reservas.
 */
public class ReservaSearchTableModel extends AbstractSearchTableModel<ReservaDTO> {
    private static final long serialVersionUID = 1L;

    private static final String[] DATA_COLUMNS = { "ID", "Veh\u00edculo", "Placa", "Marca",
            "Modelo", "Precio/D\u00eda", "Nombre", "Apellido", "Tel\u00e9fono", "Inicio",
            "Fin", "Estado", "Acciones" };

    private static final Class<?>[] DATA_CLASSES = { Integer.class, Integer.class, String.class,
            String.class, String.class, Double.class, String.class, String.class,
            String.class, String.class, String.class, String.class, Object.class };

    private final Map<Integer, String> estadoMap;

    public ReservaSearchTableModel(Map<Integer, String> estadoMap) {
        super(DATA_COLUMNS, DATA_CLASSES);
        this.estadoMap = estadoMap;
    }

    public ReservaSearchTableModel(List<ReservaDTO> data, Map<Integer, String> estadoMap) {
        this(estadoMap);
        setReservas(data);
    }

    @Override
    protected Integer getIdOf(ReservaDTO r) {
        return r.getId();
    }

    @Override
    protected Object getFieldAt(ReservaDTO r, int col) {
        switch (col) {
        case 0:
            return r.getId();
        case 1:
            return r.getIdVehiculo();
        case 2:
            return r.getPlaca();
        case 3:
            return r.getMarca();
        case 4:
            return r.getModelo();
        case 5:
            return r.getPrecioDia();
        case 6:
            return r.getNombre();
        case 7:
            return r.getApellido1();
        case 8:
            return r.getTelefono();
        case 9:
            return r.getFechaInicio();
        case 10:
            return r.getFechaFin();
        case 11:
            return estadoMap != null ? estadoMap.getOrDefault(r.getIdEstadoReserva(), r.getNombreEstado()) : r.getNombreEstado();
        case 12:
            return null;
        default:
            return null;
        }
    }

    public void setReservas(List<ReservaDTO> lista) {
        setData(lista);
    }

    public ReservaDTO getReservaAt(int row) {
        return getItem(row);
    }
}
