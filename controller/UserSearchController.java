package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;

import com.pinguela.rentexpres.desktop.model.UserSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
import com.pinguela.rentexpres.desktop.view.UserFilterPanel;
import com.pinguela.rentexpres.desktop.view.UserSearchActionsView;
import com.pinguela.rentexpres.desktop.view.UserSearchView;
import com.pinguela.rentexpres.desktop.view.UserTablePanel;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.model.UserCriteria;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.UserService;

/**
 * Controlador sencillo para la búsqueda de users.
 */
public class UserSearchController {

    private final UserSearchView view;
    private final UserService service;
    private final Frame frame;
    private static final int PAGE_SIZE = 25;
    private final UserSearchTableModel model = new UserSearchTableModel();
    private int currentPage = 1;
    private int totalPages = 1;
    private boolean loading = false;

    public UserSearchController(UserSearchView view, UserService service, Frame frame) {
        this.view = view;
        this.service = service;
        this.frame = frame;
        view.getTable().setModel(model);
        wireActions();
        wirePager();
    }

    private void wireActions() {
        UserSearchActionsView actions = view.getActions();
        actions.onNuevo(new ActionCallback() {
            @Override
            public void execute() {
                new ShowUserCreateAction(frame, service, new ActionCallback() {
                    @Override
                    public void execute() {
                        goFirstPage();
                    }
                }).actionPerformed(null);
            }
        });
        actions.onBuscar(new ActionCallback() {
            @Override
            public void execute() {
                goFirstPage();
            }
        });
        actions.onLimpiar(new ActionCallback() {
            @Override
            public void execute() {
                onLimpiar();
            }
        });
        actions.onBorrarSeleccionados(new ActionCallback() {
            @Override
            public void execute() {
                onDeleteSelected();
            }
        });
    }

    private void wirePager() {
        view.getPager().onFirst(new PaginationPanel.OnPagerListener() {
            @Override
            public void onAction() {
                if (!loading) {
                    goFirstPage();
                }
            }
        });
        view.getPager().onPrev(new PaginationPanel.OnPagerListener() {
            @Override
            public void onAction() {
                if (!loading && currentPage > 1) {
                    currentPage--;
                    buscar();
                }
            }
        });
        view.getPager().onNext(new PaginationPanel.OnPagerListener() {
            @Override
            public void onAction() {
                if (!loading && currentPage < totalPages) {
                    currentPage++;
                    buscar();
                }
            }
        });
        view.getPager().onLast(new PaginationPanel.OnPagerListener() {
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
        List<UserDTO> selected = model.getSelectedItems();
        if (selected.isEmpty()) {
            SwingUtils.showWarning(frame, "No hay users selected.");
            return;
        }
        int resp = javax.swing.JOptionPane.showConfirmDialog(frame,
                "Delete " + selected.size() + " user" + (selected.size() > 1 ? "s" : "") + " selected" + (selected.size() > 1 ? "s" : "") + "?",
                "Confirm deletion", javax.swing.JOptionPane.YES_NO_OPTION);
        if (resp != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
        StringBuilder errores = new StringBuilder();
        for (UserDTO u : selected) {
            try {
                service.delete(u, u.getId());
            } catch (Exception ex) {
                errores.append(u.getId()).append(": ").append(ex.getMessage()).append("\n");
            }
        }
        goFirstPage();
        if (errores.length() > 0) {
            SwingUtils.showError(frame, "Errores al delete:\n" + errores.toString());
        } else {
            SwingUtils.showInfo(frame, "Users eliminados correctamente.");
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
            UserCriteria crit = buildCriteria();
            crit.setPageNumber(currentPage);
            crit.setPageSize(PAGE_SIZE);

            Results<UserDTO> res = service.findByCriteria(crit);
            totalPages = Math.max(1, (int) Math.ceil(res.getTotalRecords() / (double) PAGE_SIZE));

            model.setUsers(res.getResults());
            view.getPager().setInfo(currentPage, totalPages);
        } catch (Exception ex) {
            SwingUtils.showError(frame, "Error al buscar users: " + ex.getMessage());
        } finally {
            loading = false;
        }
    }

    public void onLimpiar() {
        view.getFilter().clear();
        goFirstPage();
    }

    private UserCriteria buildCriteria() {
        UserFilterPanel f = view.getFilter();
        UserCriteria c = new UserCriteria();

        if (!f.getUserLogin().isEmpty()) {
            c.setUsername(f.getUserLogin());
        }
        if (!f.getName().isEmpty()) {
            c.setName(f.getName());
        }
        if (!f.getLastName().isEmpty()) {
            c.setLastName(f.getLastName());
        }
        if (!f.getSecondLastName().isEmpty()) {
            c.setSecondLastName(f.getSecondLastName());
        }
        if (!f.getEmail().isEmpty()) {
            c.setEmail(f.getEmail());
        }

        return c;
    }
}
