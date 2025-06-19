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

    private static UsuarioSearchController controller;
    private boolean initialized = false;

    public  UsuarioSearchView(UsuarioService service, Frame owner) throws RentexpresException {
        super(new UsuarioFilterPanel(), new UsuarioSearchActionsView(),
              new UsuarioTablePanel(service, owner, new ActionCallback() {
                  @Override
                  public void execute() {
                      controller.buscar();
                  }
              }));

        UsuarioFilterPanel filter = getFilter();
        UsuarioSearchActionsView actions = getActions();
        UsuarioTablePanel table = getTable();

        controller = new UsuarioSearchController(this, service, owner);

        filter.addPropertyChangeListener("filtrosCambio", e -> controller.goFirstPage());
    }

    public void initIfNeeded() {
        if (!initialized) {
            controller.init();
            initialized = true;
        }
    }

    // Los getters se heredan de StandardSearchView
}
