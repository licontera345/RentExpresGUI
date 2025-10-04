package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.dialog.UserDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.UserEditDialog;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserService;

/** Controller to handle actions on a single user row. */
public class UserRowController {
    private final Frame frame;
    private final UserService userService;
    private final ActionCallback reload;

    public UserRowController(Frame frame, UserService userService, ActionCallback reload) {
        this.frame = frame;
        this.userService = userService;
        this.reload = reload;
    }

    public void showDetail(UserDTO dto) {
        if (dto != null) {
            new UserDetailDialog(frame, dto.getId()).setVisible(true);
        }
    }

    public void edit(UserDTO dto) {
        if (dto == null) return;
        UserEditDialog dlg = new UserEditDialog(frame, dto.getId());
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            try {
                userService.update(dlg.getUser());
                if (reload != null) reload.execute();
            } catch (Exception ex) {
                SwingUtils.showError(frame, ex.getMessage());
            }
        }
    }

    public void delete(UserDTO dto) {
        if (dto == null) return;
        int resp = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete al user " + dto.getName() + "?",
                "Delete User", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                userService.delete(dto, dto.getId());
                if (reload != null) reload.execute();
            } catch (Exception ex) {
                SwingUtils.showError(frame, ex.getMessage());
            }
        }
    }
}
