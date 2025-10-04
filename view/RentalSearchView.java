package com.pinguela.rentexpres.desktop.view;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.controller.RentalSearchController;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.RentalService;
import com.pinguela.rentexpres.service.RentalStatusService;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

public class RentalSearchView
                extends StandardSearchView<RentalFilterPanel, RentalSearchActionsView, RentalTablePanel> {
        private static final long serialVersionUID = 1L;

        private RentalSearchController controller;
        private boolean initialized = false;

        public RentalSearchView(RentalService rentalSvc, RentalStatusService estadoSvc, VehicleService vehicleSvc,
                        Frame owner) throws RentexpresException {

                super(new RentalFilterPanel(), new RentalSearchActionsView(),
                      new RentalTablePanel(owner, rentalSvc));

                RentalFilterPanel filter = getFilter();
                RentalSearchActionsView actions = getActions();
                RentalTablePanel table = getTable();

                controller = new RentalSearchController(this, rentalSvc, estadoSvc, owner);
                table.setReloadCallback(new RentalTablePanel.ReloadCallback() {
                        public void reload() {
                                controller.buscar();
                        }
                });

                actions.onLimpiar(new ActionCallback() {
                        @Override
                        public void execute() {
                                filter.clear();
                                table.hideSelectColumn();
                                controller.goFirstPage();
                        }
                });
                filter.setToggleListener(new RentalFilterPanel.ToggleListener() {
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
