package com.pinguela.rentexpres.desktop.renderer;

import static com.pinguela.rentexpres.desktop.util.AppIcons.DELETE;
import static com.pinguela.rentexpres.desktop.util.AppIcons.EDIT;
import static com.pinguela.rentexpres.desktop.util.AppIcons.VIEW;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Generic renderer for action columns showing View, Edit and Delete buttons.
 * Subclasses can override only if they need custom styling.
 */
public class AbstractActionsCellRenderer extends JPanel implements TableCellRenderer {
    private static final long serialVersionUID = 1L;

    protected final JButton btnView = iconButton(VIEW, "Ver");
    protected final JButton btnEdit = iconButton(EDIT, "Editar");
    protected final JButton btnDel = iconButton(DELETE, "Borrar");

    public AbstractActionsCellRenderer() {
        super(new FlowLayout(FlowLayout.CENTER, 6, 0));
        setOpaque(true);
        add(btnView);
        add(btnEdit);
        add(btnDel);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this;
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
