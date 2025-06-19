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
import com.pinguela.rentexpres.model.UsuarioCriteria;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.UsuarioService;

/**
 * Controlador sencillo para la búsqueda de usuarios.
 */
public class UsuarioSearchController {

    private final UsuarioSearchView view;
    private final UsuarioService service;
    private final Frame frame;
    private static final int PAGE_SIZE = 25;
    private final UsuarioSearchTableModel model = new UsuarioSearchTableModel();
    private int currentPage = 1;
    private int totalPages = 1;
    private boolean loading = false;

    public UsuarioSearchController(UsuarioSearchView view, UsuarioService service, Frame frame) {
        this.view = view;
        this.service = service;
        this.frame = frame;
        view.getTable().setModel(model);
        wireActions();
    }

    private void wireActions() {
        UsuarioSearchActionsView actions = view.getActions();
        actions.onNuevo(() -> {
            new ShowUsuarioCreateAction(frame, service, () -> goFirstPage()).actionPerformed(null);
        });
        actions.onBuscar(() -> goFirstPage());
        actions.onLimpiar(() -> onLimpiar());
        actions.onBorrarSeleccionados(() -> {});
    }

    public void init() {
        goFirstPage();
    }

    public void goFirstPage() {
        currentPage = 1;
        buscar();
    }

    public void buscar() {
        if (loading) return;
        loading = true;
        try {
            UsuarioCriteria crit = buildCriteria();
            crit.setPageNumber(currentPage);
            crit.setPageSize(PAGE_SIZE);

            Results<UsuarioDTO> res = service.findByCriteria(crit);
            totalPages = Math.max(1, (int) Math.ceil(res.getTotalRecords() / (double) PAGE_SIZE));

            model.setUsuarios(res.getResults());
            view.getPager().setInfo(currentPage, totalPages);
        } catch (Exception ex) {
            SwingUtils.showError(frame, "Error al buscar usuarios: " + ex.getMessage());
        } finally {
            loading = false;
        }
    }

    public void onLimpiar() {
        view.getFilter().clear();
        goFirstPage();
    }

    private UsuarioCriteria buildCriteria() {
        UsuarioFilterPanel f = view.getFilter();
        UsuarioCriteria c = new UsuarioCriteria();

        if (!f.getUsuarioLogin().isEmpty()) {
            c.setNombreUsuario(f.getUsuarioLogin());
        }
        if (!f.getNombre().isEmpty()) {
            c.setNombre(f.getNombre());
        }
        if (!f.getApellido1().isEmpty()) {
            c.setApellido1(f.getApellido1());
        }
        if (!f.getApellido2().isEmpty()) {
            c.setApellido2(f.getApellido2());
        }
        if (!f.getEmail().isEmpty()) {
            c.setEmail(f.getEmail());
        }

        return c;
    }
}
