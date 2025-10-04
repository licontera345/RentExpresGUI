package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.model.UserSearchTableModel;
import com.pinguela.rentexpres.desktop.renderer.UserActionsCellEditor;
import com.pinguela.rentexpres.desktop.renderer.UserActionsCellRenderer;
import com.pinguela.rentexpres.desktop.util.AppTheme;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserService;

public class UserTablePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final JTable table = new JTable();
    private final UserService service;
    private final Frame owner;
    private ActionCallback reload;

    public UserTablePanel(UserService service, Frame owner, ActionCallback reload) {
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


    public void setModel(UserSearchTableModel m) {
        table.setModel(m);
        int[] widths = { 60, 150, 150, 150, 240, 150, 120, 150 };
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        table.getTableHeader().resizeAndRepaint();

        try {
            table.getColumn("Acciones").setCellRenderer(new UserActionsCellRenderer());

            Supplier<UserDTO> supplier = new Supplier<UserDTO>() {
                @Override
                public UserDTO get() {
                    int row = table.getEditingRow();
                    if (row < 0) {
                        return null;
                    }
                    return ((UserSearchTableModel) table.getModel()).getUserAt(row);
                }
            };

            table.getColumn("Acciones")
                 .setCellEditor(new UserActionsCellEditor(owner, service, reload, supplier));

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
