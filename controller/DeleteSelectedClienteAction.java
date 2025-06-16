package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.service.ClienteService;

public class DeleteSelectedClienteAction implements ActionListener {

	private final Supplier<List<Integer>> idsSupplier;
	private final Component parent;
	private final ClienteService service;
	private final Runnable onRefresh;

	public DeleteSelectedClienteAction(Supplier<List<Integer>> idsSupplier, Component parent, ClienteService service,
			Runnable onRefresh) {
		this.idsSupplier = Objects.requireNonNull(idsSupplier);
		this.parent = Objects.requireNonNull(parent);
		this.service = Objects.requireNonNull(service);
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Integer> ids = idsSupplier.get();
		if (ids == null || ids.isEmpty()) {
			SwingUtils.showWarning(parent, "Selecciona al menos un cliente.");
			return;
		}

		String msg = "¿Eliminar " + ids.size() + " cliente" + (ids.size() > 1 ? "s" : "") + " seleccionado"
				+ (ids.size() > 1 ? "s" : "") + "?";
		if (SwingUtils.showConfirm(parent, msg, "Confirmar borrado") != JOptionPane.YES_OPTION)
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
			onRefresh.run();

		if (noBorrados.isEmpty())
			SwingUtils.showInfo(parent, "Clientes eliminados correctamente.");
		else if (noBorrados.size() == ids.size())
			SwingUtils.showError(parent, "No se pudo eliminar ninguno.");
		else
			SwingUtils.showWarning(parent, "Algunos clientes no pudieron borrarse.");
	}
}
	