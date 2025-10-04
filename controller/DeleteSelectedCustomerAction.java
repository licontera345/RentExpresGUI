package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.service.CustomerService;

public class DeleteSelectedCustomerAction implements ActionListener {

	private final Supplier<List<Integer>> idsSupplier;
	private final Component parent;
        private final CustomerService service;
        private final ActionCallback onRefresh;

        public DeleteSelectedCustomerAction(Supplier<List<Integer>> idsSupplier, Component parent, CustomerService service,
                        ActionCallback onRefresh) {
		this.idsSupplier = Objects.requireNonNull(idsSupplier);
		this.parent = Objects.requireNonNull(parent);
		this.service = Objects.requireNonNull(service);
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Integer> ids = idsSupplier.get();
		if (ids == null || ids.isEmpty()) {
			SwingUtils.showWarning(parent, "Selecciona al menos un customer.");
			return;
		}

		String msg = "Delete " + ids.size() + " customer" + (ids.size() > 1 ? "s" : "") + " selected"
				+ (ids.size() > 1 ? "s" : "") + "?";
		if (SwingUtils.showConfirm(parent, msg, "Confirm deletion") != JOptionPane.YES_OPTION)
			return;

                List<Integer> noBorrados = new ArrayList<>();
                for (Integer id : ids) {
                        try {
                                if (!service.delete(id)) {
                                        noBorrados.add(id);
                                }
                        } catch (Exception ex) {
                                noBorrados.add(id);
                        }
                }

                if (onRefresh != null)
                        onRefresh.execute();

		if (noBorrados.isEmpty())
			SwingUtils.showInfo(parent, "Customers eliminados correctamente.");
		else if (noBorrados.size() == ids.size())
			SwingUtils.showError(parent, "No se pudo delete ninguno.");
		else
			SwingUtils.showWarning(parent, "Algunos customers no pudieron borrarse.");
	}
}
	