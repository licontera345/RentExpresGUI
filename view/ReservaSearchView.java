package com.pinguela.rentexpres.desktop.view;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.controller.ReservaSearchController;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.EstadoReservaService;
import com.pinguela.rentexpres.service.ReservaService;
import com.pinguela.rentexpres.service.VehiculoService;

public class ReservaSearchView
                extends StandardSearchView<ReservaFilterPanel, ReservaSearchActionsView, ReservaTablePanel> {
        private static final long serialVersionUID = 1L;

        private static ReservaSearchController controller;

	private boolean initialized = false;

	public void initIfNeeded() {
		if (!initialized) {
			controller.init();
			initialized = true;
		}
	}

        public ReservaSearchView(ReservaService rs, EstadoReservaService es, VehiculoService vs, Frame owner) throws RentexpresException {
        super(new ReservaFilterPanel(), new ReservaSearchActionsView(),
              new ReservaTablePanel(rs, owner, new ReservaTablePanel.ReloadCallback() {
                  @Override
                  public void reload() {
                      controller.buscar();
                  }
              }));

        ReservaFilterPanel filter = getFilter();
        ReservaSearchActionsView actions = getActions();
        ReservaTablePanel table = getTable();

                controller = new ReservaSearchController(this, rs, es, vs, owner);

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
