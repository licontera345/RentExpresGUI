package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.VehicleCreateDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.VehicleStatusService;
import com.pinguela.rentexpres.service.VehicleService;

public class ShowVehicleCreateAction extends AbstractCreateAction<VehicleDTO, VehicleCreateDialog> {
        private final VehicleService vehicleService;
        private final VehicleCategoryService categoryService;
        private final VehicleStatusService estadoService;

        public ShowVehicleCreateAction(Frame frame, VehicleService vehicleService,
                        VehicleCategoryService categoryService, VehicleStatusService estadoService) {
                super(frame, null);
                this.vehicleService = vehicleService;
                this.categoryService = categoryService;
                this.estadoService = estadoService;
        }

        @Override
        protected VehicleCreateDialog createDialog() {
                return new VehicleCreateDialog(frame, categoryService.findAll(),
                                estadoService.findAll(), vehicleService);
        }

        @Override
        protected void save(VehicleDTO dto) throws RentexpresException {
                vehicleService.create(dto, null);
        }
}
