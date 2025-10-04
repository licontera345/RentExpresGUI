package com.pinguela.rentexpres.desktop.model;

import java.util.List;

import com.pinguela.rentexpres.model.VehicleDTO;

/**
 * Tabla para mostrar vehicles en búsquedas.
 */
public class VehicleSearchTableModel extends AbstractSearchTableModel<VehicleDTO> {
    private static final long serialVersionUID = 1L;

    private static final String[] DATA_COLUMNS = { "ID", "LicensePlate", "Make", "Model", "A\u00f1o",
            "Precio/D\u00eda", "Estado", "Categor\u00eda", "Acciones" };

    private static final Class<?>[] DATA_CLASSES = { Integer.class, String.class, String.class,
            String.class, Integer.class, Double.class, String.class, String.class, Object.class };

    public VehicleSearchTableModel() {
        super(DATA_COLUMNS, DATA_CLASSES);
    }

    public VehicleSearchTableModel(List<VehicleDTO> data) {
        this();
        setVehicles(data);
    }

    @Override
    protected Integer getIdOf(VehicleDTO v) {
        return v.getId();
    }

    @Override
    protected Object getFieldAt(VehicleDTO v, int col) {
        switch (col) {
        case 0:
            return v.getId();
        case 1:
            return v.getLicensePlate();
        case 2:
            return v.getMake();
        case 3:
            return v.getModel();
        case 4:
            return v.getManufactureYear();
        case 5:
            return v.getDailyPrice();
        case 6:
            return v.getVehicleStatusName();
        case 7:
            return v.getCategoryName();
        case 8:
            return null;
        default:
            return null;
        }
    }

    public void setVehicles(List<VehicleDTO> lista) {
        setData(lista);
    }

    public VehicleDTO getVehicleAt(int row) {
        return getItem(row);
    }
}
