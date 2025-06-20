package com.pinguela.rentexpres.desktop.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.pinguela.rentexpres.model.ReservaStatsDTO;

public class ReservaStatsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private static final String[] COLS = { "Año", "Mes", "Reservas" };
    private List<ReservaStatsDTO> data = new ArrayList<>();

    public void setData(List<ReservaStatsDTO> list) {
        data.clear();
        if (list != null)
            data.addAll(list);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return COLS.length;
    }

    @Override
    public String getColumnName(int col) {
        return COLS[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        ReservaStatsDTO dto = data.get(row);
        switch (col) {
            case 0:
                return dto.getYear();
            case 1:
                return dto.getMonth();
            case 2:
                return dto.getTotalReservas();
            default:
                return null;
        }
    }
}
