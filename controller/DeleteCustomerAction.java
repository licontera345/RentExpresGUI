package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.desktop.util.LogUtils;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.service.CustomerService;

/** Borra UN customer (el selected en la fila). */
public class DeleteCustomerAction implements ActionListener {
    private static final Logger log = LogManager.getLogger(DeleteCustomerAction.class);

    private final Supplier<CustomerDTO> current;
    private final Component parent;
    private final CustomerService service;
    private final ActionCallback onRefresh;

    public DeleteCustomerAction(Supplier<CustomerDTO> current, Component parent,
                               CustomerService service, ActionCallback onRefresh) {
        this.current   = current;
        this.parent    = parent;
        this.service   = service;
        this.onRefresh = onRefresh;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CustomerDTO c = current.get();
        if (c == null) {
            SwingUtils.showWarning(parent,"Selecciona un customer para delete.");
            return;
        }
        int id  = c.getId();
        if (SwingUtils.showConfirm(parent,
                "Delete el customer con ID "+id+"?", "Confirm deletion")
            != JOptionPane.YES_OPTION) return;

        try {
            if (service.delete(id)) {
                SwingUtils.showInfo(parent,"Customer eliminado correctamente.");
                if (onRefresh!=null) onRefresh.execute();
            } else {
                SwingUtils.showError(parent,"No se pudo delete el customer.");
            }
        } catch (Exception ex) {
            log.error(LogUtils.buildMessage(DeleteCustomerAction.class,
                    "Error borrando customer {}"), id, ex);
            SwingUtils.showError(parent,"Error al borrar: "+ex.getMessage());
        }
    }
}
