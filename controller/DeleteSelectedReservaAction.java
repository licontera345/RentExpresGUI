package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.ReservaService;

public class DeleteSelectedReservaAction implements ActionListener {

	private static final String CONFIRM_DELETE_TITLE = "Confirmar borrado";
	private static final String NO_SELECTION_MSG = "Selecciona al menos una reserva para borrar.";
	private static final String PARTIAL_SUCCESS_MSG = "Algunas reservas no se pudieron borrar.";
	private static final String SUCCESS_MSG = "Reservas eliminadas correctamente.";
	private static final String ERROR_MSG = "Error borrando reservas: ";

	private final Supplier<List<Integer>> idsSupplier;
	private final Component parent;
	private final ReservaService service;
	private final Runnable onRefresh;

	public DeleteSelectedReservaAction(Supplier<List<Integer>> idsSupplier, Component parent, ReservaService service,
			Runnable onRefresh) {
		this.idsSupplier = Objects.requireNonNull(idsSupplier, "El proveedor de IDs no puede ser nulo");
		this.parent = Objects.requireNonNull(parent, "El componente padre no puede ser nulo");
		this.service = Objects.requireNonNull(service, "El servicio no puede ser nulo");
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Integer> ids = idsSupplier.get();
		if (ids == null || ids.isEmpty()) {
			SwingUtils.showWarning(parent, NO_SELECTION_MSG);
			return;
		}

		if (!confirmDelete(ids.size())) {
			return;
		}

		performDelete(ids);
	}

	private boolean confirmDelete(int count) {
		String message = String.format("¿Eliminar %d reserva%s seleccionada%s?", count, count > 1 ? "s" : "",
				count > 1 ? "s" : "");
		return SwingUtils.showConfirm(parent, message, CONFIRM_DELETE_TITLE) == JOptionPane.YES_OPTION;
	}

	private void performDelete(List<Integer> ids) {
		try {
			DeleteResult result = deleteReservas(ids);

			if (onRefresh != null) {
				onRefresh.run();
			}

			showResultMessage(result);
		} catch (RentexpresException ex) {
			SwingUtils.showError(parent, ERROR_MSG + ex.getMessage());
		}
	}

	private DeleteResult deleteReservas(List<Integer> ids) throws RentexpresException {
		List<Boolean> results = ids.stream().map(id -> {
			try {
				return service.delete(id);
			} catch (RentexpresException e) {
				return false;
			}
		}).collect(Collectors.toList());

		long successfulDeletes = results.stream().filter(Boolean::booleanValue).count();
		return new DeleteResult(successfulDeletes, results.size());
	}

	private void showResultMessage(DeleteResult result) {
		if (result.totalCount == result.successCount) {
			SwingUtils.showInfo(parent, SUCCESS_MSG);
		} else if (result.successCount > 0) {
			SwingUtils.showWarning(parent, PARTIAL_SUCCESS_MSG);
		} else {
			SwingUtils.showError(parent, "No se pudo eliminar ninguna reserva.");
		}
	}

	private static class DeleteResult {
		final long successCount;
		final long totalCount;

		DeleteResult(long successCount, long totalCount) {
			this.successCount = successCount;
			this.totalCount = totalCount;
		}
	}
}