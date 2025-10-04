package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.CustomerCreateDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.service.CustomerService;

public class ShowCustomerCreateAction extends AbstractCreateAction<CustomerDTO, CustomerCreateDialog> {

        private final CustomerService customerService;

        public ShowCustomerCreateAction(Frame frame, CustomerService customerService) {
                super(frame, null);
                this.customerService = customerService;
        }

        @Override
        protected CustomerCreateDialog createDialog() {
                return new CustomerCreateDialog(frame);
        }

        @Override
        protected void save(CustomerDTO dto) throws RentexpresException {
                customerService.create(dto);
        }
}
