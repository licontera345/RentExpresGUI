package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.ClienteCreateDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;

public class ShowClienteCreateAction extends AbstractCreateAction<ClienteDTO, ClienteCreateDialog> {

        private final ClienteService clienteService;

        public ShowClienteCreateAction(Frame frame, ClienteService clienteService) {
                super(frame, null);
                this.clienteService = clienteService;
        }

        @Override
        protected ClienteCreateDialog createDialog() {
                return new ClienteCreateDialog(frame);
        }

        @Override
        protected void save(ClienteDTO dto) throws RentexpresException {
                clienteService.create(dto);
        }
}
