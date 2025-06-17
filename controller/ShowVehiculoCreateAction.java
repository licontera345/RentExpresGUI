package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.VehiculoCreateDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.CategoriaVehiculoService;
import com.pinguela.rentexpres.service.EstadoVehiculoService;
import com.pinguela.rentexpres.service.VehiculoService;

public class ShowVehiculoCreateAction extends AbstractCreateAction<VehiculoDTO, VehiculoCreateDialog> {
        private final VehiculoService vehiculoService;
        private final CategoriaVehiculoService categoriaService;
        private final EstadoVehiculoService estadoService;

        public ShowVehiculoCreateAction(Frame frame, VehiculoService vehiculoService,
                        CategoriaVehiculoService categoriaService, EstadoVehiculoService estadoService) {
                super(frame, null);
                this.vehiculoService = vehiculoService;
                this.categoriaService = categoriaService;
                this.estadoService = estadoService;
        }

        @Override
        protected VehiculoCreateDialog createDialog() {
                return new VehiculoCreateDialog(frame, categoriaService.findAll(), estadoService.findAll());
        }

        @Override
        protected void save(VehiculoDTO dto) throws RentexpresException {
                vehiculoService.create(dto, null);
        }
}
