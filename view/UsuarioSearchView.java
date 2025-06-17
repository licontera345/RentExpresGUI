package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.pinguela.rentexpres.desktop.controller.UsuarioSearchController;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.UsuarioService;

/**
 * Vista de búsqueda de usuarios con filtros, tabla de resultados y paginación.
 */
public class UsuarioSearchView extends JPanel {
    private static final long serialVersionUID = 1L;

    private final UsuarioFilterPanel filter;
    private final UsuarioSearchActionsView actions;
    private final UsuarioTablePanel table;
    private final PaginationPanel pager;
    private UsuarioSearchController controller;
    private boolean initialized = false;

    public UsuarioSearchView(UsuarioService service, Frame owner) throws RentexpresException {
        this.filter = new UsuarioFilterPanel();
        this.actions = new UsuarioSearchActionsView();
        this.table = new UsuarioTablePanel(service, owner, new ActionCallback() {
            @Override
            public void execute() {
                controller.buscar();
            }
        });
        this.pager = new PaginationPanel();

        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(actions);

        JPanel main = new JPanel(new BorderLayout());
        main.add(filter, BorderLayout.NORTH);
        main.add(new JScrollPane(table), BorderLayout.CENTER);
        main.add(pager, BorderLayout.SOUTH);

        setLayout(new BorderLayout(8, 8));
        add(bar, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);

        controller = new UsuarioSearchController(this, service, owner);

        filter.addPropertyChangeListener("filtrosCambio", e -> controller.goFirstPage());
        actions.onLimpiar(() -> controller.onLimpiar());
    }

    public void initIfNeeded() {
        if (!initialized) {
            controller.init();
            initialized = true;
        }
    }

    public UsuarioFilterPanel getFilter() {
        return filter;
    }

    public UsuarioSearchActionsView getActions() {
        return actions;
    }

    public UsuarioTablePanel getTable() {
        return table;
    }

    public PaginationPanel getPager() {
        return pager;
    }
}
