package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.pinguela.rentexpres.model.UserTypeDTO;

/** Renders UserTypeDTO showing only its nameTipo. */
public class UserTypeRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof UserTypeDTO) {
            UserTypeDTO t = (UserTypeDTO) value;
            setText(t.getNameTipo());
        }
        return this;
    }
}
