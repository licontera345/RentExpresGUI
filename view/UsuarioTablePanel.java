package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel;
import com.pinguela.rentexpres.desktop.renderer.UsuarioActionsCellEditor;
import com.pinguela.rentexpres.desktop.renderer.UsuarioActionsCellRenderer;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;

public class UsuarioTablePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final JTable table = new JTable();
    private final UsuarioService service;
    private final Frame owner;
    private ActionCallback reload;

    public UsuarioTablePanel(UsuarioService service, Frame owner, ActionCallback reload) {
        this.service = service;
        this.owner = owner;
        this.reload = reload;
        setLayout(new BorderLayout());
        table.setRowHeight(AppTheme.TABLE_ROW_HEIGHT);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setReloadCallback(ActionCallback reload) {
        this.reload = reload;
    }

    public JTable getTable() {
        return table;
    }


    public void setModel(UsuarioSearchTableModel m) {
        table.setModel(m);
        int[] widths = { 40, 120, 120, 120, 200, 120, 120, 150 };
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        try {
            table.getColumn("Acciones").setCellRenderer(new UsuarioActionsCellRenderer());

            Supplier<UsuarioDTO> supplier = new Supplier<UsuarioDTO>() {
                @Override
                public UsuarioDTO get() {
                    int row = table.getEditingRow();
                    if (row < 0) {
                        return null;
                    }
                    return ((UsuarioSearchTableModel) table.getModel()).getUsuarioAt(row);
                }
            };

            table.getColumn("Acciones")
                 .setCellEditor(new UsuarioActionsCellEditor(owner, service, reload, supplier));

        } catch (IllegalArgumentException ex) {
        }
    }

//    public void setSelectVisible(boolean visible) {
//        this.selectVisible = visible;
//        if (visible) {
//            table.setRowSelectionAllowed(true);
//            table.setColumnSelectionAllowed(false);
//        } else {
//            table.clearSelection();
//        }
//    }
}
