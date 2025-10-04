package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;


import com.pinguela.rentexpres.desktop.dialog.UserCreateDialog;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserService;

public class ShowUserCreateAction extends AbstractCreateAction<UserDTO, UserCreateDialog> {
    private static final long serialVersionUID = 1L;
    private final UserService userService;

    public ShowUserCreateAction(Frame parent, UserService userService, ActionCallback afterCreate) {
        super("Nuevo", parent, afterCreate);
        this.userService = userService;
    }

    @Override
    protected UserCreateDialog createDialog() {
        return new UserCreateDialog(frame);
    }

    @Override
    protected void save(UserDTO dto) throws RentexpresException {
        userService.create(dto);
    }
}
