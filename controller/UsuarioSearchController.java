package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;

import com.pinguela.rentexpres.desktop.model.UsuarioSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.view.UsuarioFilterPanel;
import com.pinguela.rentexpres.desktop.view.UsuarioSearchActionsView;
import com.pinguela.rentexpres.desktop.view.UsuarioSearchView;
import com.pinguela.rentexpres.desktop.view.UsuarioTablePanel;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;

/**
 * Controlador sencillo para la búsqueda de usuarios.
 */
public class UsuarioSearchController {

    private final UsuarioSearchView view;
    private final UsuarioService service;
    private final Frame frame;
    private final UsuarioSearchTableModel model = new UsuarioSearchTableModel();

    public UsuarioSearchController(UsuarioSearchView view, UsuarioService service, Frame frame) {
        this.view = view;
        this.service = service;
        this.frame = frame;
        view.getTable().setModel(model);
        wireActions();
    }

    private void wireActions() {
        UsuarioSearchActionsView actions = view.getActions();
        actions.onNuevo(() -> {}); // reservado para futuras mejoras
        actions.onBuscar(() -> goFirstPage());
        actions.onLimpiar(() -> onLimpiar());
        actions.onBorrarSeleccionados(() -> {});
    }

    public void init() {
        goFirstPage();
    }

    public void goFirstPage() {
        buscar();
    }

    public void buscar() {
        try {
            UsuarioFilterPanel f = view.getFilter();
            List<UsuarioDTO> lista = service.findAll(); // sin filtrar, ejemplar
            model.setUsuarios(lista);
            view.getPager().setInfo(1, 1);
        } catch (Exception ex) {
            SwingUtils.showError(frame, "Error al buscar usuarios: " + ex.getMessage());
        }
    }

    public void onLimpiar() {
        // sin implementación específica, podría limpiarse el filtro aquí
        buscar();
    }
}
