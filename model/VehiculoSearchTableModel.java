package com.pinguela.rentexpres.desktop.model;

import java.util.List;

import com.pinguela.rentexpres.model.VehiculoDTO;

/**
 * Tabla para mostrar vehículos en búsquedas.
 */
public class VehiculoSearchTableModel extends AbstractSearchTableModel<VehiculoDTO> {
    private static final long serialVersionUID = 1L;

    private static final String[] DATA_COLUMNS = { "ID", "Placa", "Marca", "Modelo", "A\u00f1o",
            "Precio/D\u00eda", "Estado", "Categor\u00eda", "Acciones" };

    private static final Class<?>[] DATA_CLASSES = { Integer.class, String.class, String.class,
            String.class, Integer.class, Double.class, String.class, String.class, Object.class };

    public VehiculoSearchTableModel() {
        super(DATA_COLUMNS, DATA_CLASSES);
    }

    public VehiculoSearchTableModel(List<VehiculoDTO> data) {
        this();
        setVehiculos(data);
    }

    @Override
    protected Integer getIdOf(VehiculoDTO v) {
        return v.getId();
    }

    @Override
    protected Object getFieldAt(VehiculoDTO v, int col) {
        switch (col) {
        case 0:
            return v.getId();
        case 1:
            return v.getPlaca();
        case 2:
            return v.getMarca();
        case 3:
            return v.getModelo();
        case 4:
            return v.getAnioFabricacion();
        case 5:
            return v.getPrecioDia();
        case 6:
            return v.getNombreEstadoVehiculo();
        case 7:
            return v.getNombreCategoria();
        case 8:
            return null;
        default:
            return null;
        }
    }

    public void setVehiculos(List<VehiculoDTO> lista) {
        setData(lista);
    }

    public VehiculoDTO getVehiculoAt(int row) {
        return getItem(row);
    }
}
