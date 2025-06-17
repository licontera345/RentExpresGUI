package com.pinguela.rentexpres.desktop.renderer;

import static com.pinguela.rentexpres.desktop.util.AppIcons.DELETE;
import static com.pinguela.rentexpres.desktop.util.AppIcons.EDIT;
import static com.pinguela.rentexpres.desktop.util.AppIcons.VIEW;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Base editor with View, Edit and Delete buttons. Subclasses just
 * configure listeners and retrieve the current row object.
 */
public abstract class AbstractActionsCellEditor extends AbstractCellEditor implements TableCellEditor {
    private static final long serialVersionUID = 1L;

    protected final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
    protected final JButton btnView = iconButton(VIEW, "Ver");
    protected final JButton btnEdit = iconButton(EDIT, "Editar");
    protected final JButton btnDel = iconButton(DELETE, "Borrar");

    protected AbstractActionsCellEditor() {
        panel.add(btnView);
        panel.add(btnEdit);
        panel.add(btnDel);
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    protected static JButton iconButton(ImageIcon ico, String tip) {
        JButton b = new JButton(ico);
        b.setToolTipText(tip);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        return b;
    }
}
