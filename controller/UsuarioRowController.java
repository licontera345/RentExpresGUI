package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.dialog.UsuarioDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.UsuarioEditDialog;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.UsuarioService;

/** Controller to handle actions on a single usuario row. */
public class UsuarioRowController {
    private final Frame frame;
    private final UsuarioService usuarioService;
    private final ActionCallback reload;

    public UsuarioRowController(Frame frame, UsuarioService usuarioService, ActionCallback reload) {
        this.frame = frame;
        this.usuarioService = usuarioService;
        this.reload = reload;
    }

    public void showDetail(UsuarioDTO dto) {
        if (dto != null) {
            new UsuarioDetailDialog(frame, dto.getId()).setVisible(true);
        }
    }

    public void edit(UsuarioDTO dto) {
        if (dto == null) return;
        UsuarioEditDialog dlg = new UsuarioEditDialog(frame, dto.getId());
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            try {
                usuarioService.update(dlg.getUsuario());
                if (reload != null) reload.execute();
            } catch (Exception ex) {
                SwingUtils.showError(frame, ex.getMessage());
            }
        }
    }

    public void delete(UsuarioDTO dto) {
        if (dto == null) return;
        int resp = JOptionPane.showConfirmDialog(frame,
                "¿Seguro que deseas eliminar al usuario " + dto.getNombre() + "?",
                "Eliminar Usuario", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                usuarioService.delete(dto, dto.getId());
                if (reload != null) reload.execute();
            } catch (Exception ex) {
                SwingUtils.showError(frame, ex.getMessage());
            }
        }
    }
}
