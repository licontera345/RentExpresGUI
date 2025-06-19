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
        wirePager();
    }

    private void wireActions() {
        UsuarioSearchActionsView actions = view.getActions();
        actions.onNuevo(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
            @Override
            public void execute() {
                new ShowUsuarioCreateAction(frame, service, new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                    @Override
                    public void execute() {
                        goFirstPage();
                    }
                }).actionPerformed(null);
            }
        });
        actions.onBuscar(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
            @Override
            public void execute() {
                goFirstPage();
            }
        });
        actions.onLimpiar(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
            @Override
            public void execute() {
                onLimpiar();
            }
        });
        actions.onBorrarSeleccionados(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
            @Override
            public void execute() {
                onDeleteSelected();
            }
        });
    }

    private void wirePager() {
        view.getPager().onFirst(new com.pinguela.rentexpres.desktop.util.PaginationPanel.OnPagerListener() {
            @Override
            public void onAction() {
                if (!loading) {
                    goFirstPage();
                }
            }
        });
        view.getPager().onPrev(new com.pinguela.rentexpres.desktop.util.PaginationPanel.OnPagerListener() {
            @Override
            public void onAction() {
                if (!loading && currentPage > 1) {
                    currentPage--;
                    buscar();
                }
            }
        });
        view.getPager().onNext(new com.pinguela.rentexpres.desktop.util.PaginationPanel.OnPagerListener() {
            @Override
            public void onAction() {
                if (!loading && currentPage < totalPages) {
                    currentPage++;
                    buscar();
                }
            }
        });
        view.getPager().onLast(new com.pinguela.rentexpres.desktop.util.PaginationPanel.OnPagerListener() {
            @Override
            public void onAction() {
                if (!loading && currentPage < totalPages) {
                    currentPage = totalPages;
                    buscar();
                }
            }
        });
    }

    private void onDeleteSelected() {
        List<UsuarioDTO> selected = model.getSelectedItems();
        if (selected.isEmpty()) {
            SwingUtils.showWarning(frame, "No hay usuarios seleccionados.");
            return;
        }
        int resp = javax.swing.JOptionPane.showConfirmDialog(frame,
                "¿Eliminar " + selected.size() + " usuario" + (selected.size() > 1 ? "s" : "") + " seleccionado" + (selected.size() > 1 ? "s" : "") + "?",
                "Confirmar borrado", javax.swing.JOptionPane.YES_NO_OPTION);
        if (resp != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
        StringBuilder errores = new StringBuilder();
        for (UsuarioDTO u : selected) {
            try {
                service.delete(u, u.getId());
            } catch (Exception ex) {
                errores.append(u.getId()).append(": ").append(ex.getMessage()).append("\n");
            }
        }
        goFirstPage();
        if (errores.length() > 0) {
            SwingUtils.showError(frame, "Errores al eliminar:\n" + errores.toString());
        } else {
            SwingUtils.showInfo(frame, "Usuarios eliminados correctamente.");
        }
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
