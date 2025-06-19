package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.dialog.ClienteDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ClienteEditDialog;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;

/** Controller for actions on a single cliente row. */
public class ClienteRowController {
    private final Frame frame;
    private final ClienteService service;
    private final ActionCallback reload;

    public ClienteRowController(Frame frame, ClienteService service, ActionCallback reload) {
        this.frame = frame;
        this.service = service;
        this.reload = reload;
    }

    public void showDetail(ClienteDTO dto) {
        if (dto != null) {
            new ClienteDetailDialog(frame, dto).setVisible(true);
        }
    }

    public void edit(ClienteDTO dto) {
        if (dto == null) return;
        ClienteEditDialog dlg = new ClienteEditDialog(frame, dto);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            try {
                service.update(dlg.getCliente());
                if (reload != null) reload.execute();
            } catch (RentexpresException ex) {
                SwingUtils.showError(frame, ex.getMessage());
            }
        }
    }

    public void delete(ClienteDTO dto) {
        if (dto == null) return;
        if (SwingUtils.showConfirm(frame,
                "¿Eliminar cliente " + dto.getId() + "?", "Confirmar borrado") == JOptionPane.YES_OPTION) {
            try {
                service.delete(dto.getId());
                if (reload != null) reload.execute();
            } catch (RentexpresException ex) {
                SwingUtils.showError(frame, ex.getMessage());
            }
        }
    }
}
