package com.pinguela.rentexpres.desktop.view;

/** Barra de acciones reutilizable para Usuarios. */
import com.pinguela.rentexpres.desktop.util.ActionCallback;

public class UsuarioSearchActionsView extends AbstractSearchActionsView {
        private static final long serialVersionUID = 1L;

        public UsuarioSearchActionsView() {
                super();
                remove(btnBuscar);
        }

        @Override
        public void onBuscar(ActionCallback r) {
                // Intentionally empty: this view does not have a search button
        }
}
