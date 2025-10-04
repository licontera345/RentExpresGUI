package com.pinguela.rentexpres.desktop.view;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.controller.UserSearchController;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.UserService;

/**
 * Vista de búsqueda de users con filtros, tabla de resultados y paginación.
 */
public class UserSearchView
        extends StandardSearchView<UserFilterPanel, UserSearchActionsView, UserTablePanel> {
    private static final long serialVersionUID = 1L;

    private final UserSearchController controller;
    private boolean initialized = false;

    public  UserSearchView(UserService service, Frame owner) throws RentexpresException {
        super(new UserFilterPanel(), new UserSearchActionsView(),
              new UserTablePanel(service, owner, null));

        UserFilterPanel filter = getFilter();
        UserTablePanel table = getTable();

        controller = new UserSearchController(this, service, owner);

        table.setReloadCallback(new ActionCallback() {
            @Override
            public void execute() {
                controller.buscar();
            }
        });

        filter.addPropertyChangeListener("filtrosCambio", new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent e) {
                controller.goFirstPage();
            }
        });
    }

    public void initIfNeeded() {
        if (!initialized) {
            controller.init();
            initialized = true;
        }
    }

    // Los getters se heredan de StandardSearchView
}
