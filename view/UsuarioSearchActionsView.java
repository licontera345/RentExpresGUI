package com.pinguela.rentexpres.desktop.view;

/** Barra de acciones reutilizable para Usuarios. */
import com.pinguela.rentexpres.desktop.util.ActionCallback;

public class UsuarioSearchActionsView extends AbstractSearchActionsView {
    private static final long serialVersionUID = 1L;

    public UsuarioSearchActionsView() {
        super();
    }

    /**
     * Delegates to the parent implementation. This method exists explicitly
     * to avoid build issues when older compiled classes are present.
     */
    @Override
    public void onBuscar(ActionCallback r) {
        super.onBuscar(r);
    }
}

