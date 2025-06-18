package com.pinguela.rentexpres.desktop.view;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.controller.AlquilerSearchController;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.AlquilerService;
import com.pinguela.rentexpres.service.EstadoAlquilerService;
import com.pinguela.rentexpres.service.VehiculoService;

public class AlquilerSearchView
                extends StandardSearchView<AlquilerFilterPanel, AlquilerSearchActionsView, AlquilerTablePanel> {
        private static final long serialVersionUID = 1L;

        private AlquilerSearchController controller;
        private boolean initialized = false;

        public AlquilerSearchView(AlquilerService alquilerSvc, EstadoAlquilerService estadoSvc, VehiculoService vehiculoSvc,
                        Frame owner) throws RentexpresException {

                super(new AlquilerFilterPanel(), new AlquilerSearchActionsView(),
                      new AlquilerTablePanel(owner, alquilerSvc));

                AlquilerFilterPanel filter = getFilter();
                AlquilerSearchActionsView actions = getActions();
                AlquilerTablePanel table = getTable();

                controller = new AlquilerSearchController(this, alquilerSvc, estadoSvc, owner);
                table.setReloadCallback(new AlquilerTablePanel.ReloadCallback() {
                        public void reload() {
                                controller.buscar();
                        }
                });

                actions.onLimpiar(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                        @Override
                        public void execute() {
                                filter.clear();
                                table.hideSelectColumn();
                                controller.goFirstPage();
                        }
                });
                filter.setToggleListener(new AlquilerFilterPanel.ToggleListener() {
                        public void onToggle() {
                                table.toggleSelectColumn();
                        }
                });
        }

        public void initIfNeeded() {
                if (!initialized) {
                        controller.goFirstPage();
                        initialized = true;
                }
        }

        /* ───────── getters se heredan de StandardSearchView ───────── */
}
