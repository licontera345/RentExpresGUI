package com.pinguela.rentexpres.desktop.view;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.controller.UsuarioSearchController;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.UsuarioService;

/**
 * Vista de búsqueda de usuarios con filtros, tabla de resultados y paginación.
 */
public class UsuarioSearchView
        extends StandardSearchView<UsuarioFilterPanel, UsuarioSearchActionsView, UsuarioTablePanel> {
    private static final long serialVersionUID = 1L;

    private UsuarioSearchController controller;
    private boolean initialized = false;

    public UsuarioSearchView(UsuarioService service, Frame owner) throws RentexpresException {
        UsuarioFilterPanel filter = new UsuarioFilterPanel();
        UsuarioSearchActionsView actions = new UsuarioSearchActionsView();
        UsuarioTablePanel table = new UsuarioTablePanel(service, owner, new ActionCallback() {
            @Override
            public void execute() {
                controller.buscar();
            }
        });

        super(filter, actions, table);

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

    // Los getters se heredan de StandardSearchView
}
