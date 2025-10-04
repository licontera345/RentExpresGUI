package com.pinguela.rentexpres.desktop.view;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.controller.ReservationSearchController;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.ReservationStatusService;
import com.pinguela.rentexpres.service.ReservationService;
import com.pinguela.rentexpres.service.VehicleService;

public class ReservationSearchView
                extends StandardSearchView<ReservationFilterPanel, ReservationSearchActionsView, ReservationTablePanel> {
        private static final long serialVersionUID = 1L;

        private static ReservationSearchController controller;

	private boolean initialized = false;

	public void initIfNeeded() {
		if (!initialized) {
			controller.init();
			initialized = true;
		}
	}

        public ReservationSearchView(ReservationService rs, ReservationStatusService es, VehicleService vs, Frame owner) throws RentexpresException {
        super(new ReservationFilterPanel(), new ReservationSearchActionsView(),
              new ReservationTablePanel(rs, owner, new ReservationTablePanel.ReloadCallback() {
                  @Override
                  public void reload() {
                      controller.buscar();
                  }
              }));

        ReservationFilterPanel filter = getFilter();
        ReservationSearchActionsView actions = getActions();
        ReservationTablePanel table = getTable();

                controller = new ReservationSearchController(this, rs, es, vs, owner);

                actions.onLimpiar(new ActionCallback() {
                        @Override
                        public void execute() {
                                filter.clear();
                                table.hideSelectColumn();
                                controller.goFirstPage();
                        }
                });

                filter.setToggleListener(new ActionCallback() {
                        @Override
                        public void execute() {
                                table.toggleSelectColumn();
                        }
                });
        }

        // Los getters se heredan de StandardSearchView
}
