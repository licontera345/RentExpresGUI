package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;

/** Borra UN cliente (el seleccionado en la fila). */
public class DeleteClienteAction implements ActionListener {
    private static final Logger log = LogManager.getLogger(DeleteClienteAction.class);

    private final Supplier<ClienteDTO> current;
    private final Component parent;
    private final ClienteService service;
    private final Runnable onRefresh;

    public DeleteClienteAction(Supplier<ClienteDTO> current, Component parent,
                               ClienteService service, Runnable onRefresh) {
        this.current   = current;
        this.parent    = parent;
        this.service   = service;
        this.onRefresh = onRefresh;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ClienteDTO c = current.get();
        if (c == null) {
            SwingUtils.showWarning(parent,"Selecciona un cliente para eliminar.");
            return;
        }
        int id  = c.getId();
        if (SwingUtils.showConfirm(parent,
                "¿Eliminar el cliente con ID "+id+"?", "Confirmar borrado")
            != JOptionPane.YES_OPTION) return;

        try {
            if (service.delete(id)) {
                SwingUtils.showInfo(parent,"Cliente eliminado correctamente.");
                if (onRefresh!=null) onRefresh.run();
            } else {
                SwingUtils.showError(parent,"No se pudo eliminar el cliente.");
            }
        } catch (Exception ex) {
            log.error("Error borrando cliente {}", id, ex);
            SwingUtils.showError(parent,"Error al borrar: "+ex.getMessage());
        }
    }
}
