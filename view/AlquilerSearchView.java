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

        public AlquilerSearchView(AlquilerService alquilerSvc, EstadoAlquilerService estadoSvc, VehiculoService vehiculoSvc,
                        Frame owner) throws RentexpresException {

                AlquilerFilterPanel filter = new AlquilerFilterPanel();
                AlquilerSearchActionsView actions = new AlquilerSearchActionsView();
                AlquilerTablePanel table = new AlquilerTablePanel(owner, alquilerSvc);

                super(filter, actions, table);

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

        /* ───────── getters se heredan de StandardSearchView ───────── */
}
