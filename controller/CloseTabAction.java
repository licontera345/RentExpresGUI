package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.util.function.Supplier;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

public class CloseTabAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private final Supplier<JTabbedPane> tabbedPaneSupplier;

    public CloseTabAction(Supplier<JTabbedPane> tabbedPaneSupplier) {
        super("Cerrar Pestaña");
        this.tabbedPaneSupplier = tabbedPaneSupplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTabbedPane tabs = tabbedPaneSupplier.get();
        int idx = tabs.getSelectedIndex();
        if (idx >= 0) {
            tabs.removeTabAt(idx);
        }
    }
}
