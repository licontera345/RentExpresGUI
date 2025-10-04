package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.RentalService;
import com.pinguela.rentexpres.desktop.util.SwingUtils;

public class DeleteSelectedRentalAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private static final String CONFIRM_TITLE = "Confirm deletion";
	private static final String NO_SELECTION_MSG = "No hay rentals selected.";
	private static final String SUCCESS_MSG = "Rental(es) eliminado(s) correctamente.";
	private static final String ERROR_MSG = "Error eliminando rental(es): ";
	private static final String PARTIAL_SUCCESS_MSG = "Algunos rentals no se pudieron delete.";

	private final Supplier<List<Integer>> idSupplier;
	private final Component parent;
        private final RentalService rentalService;
        private final ActionCallback onSuccess;

        public DeleteSelectedRentalAction(Supplier<List<Integer>> idSupplier, Component parent,
                        RentalService rentalService, ActionCallback onSuccess) {
		this.idSupplier = Objects.requireNonNull(idSupplier, "El proveedor de IDs no puede ser nulo");
		this.parent = Objects.requireNonNull(parent, "El componente padre no puede ser nulo");
		this.rentalService = Objects.requireNonNull(rentalService, "El servicio no puede ser nulo");
		this.onSuccess = Objects.requireNonNull(onSuccess, "La acción de éxito no puede ser nula");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Integer> ids = idSupplier.get();
		if (ids.isEmpty()) {
			SwingUtils.showInfo(parent, NO_SELECTION_MSG);
			return;
		}

		if (!confirmDeletion(ids.size())) {
			return;
		}

		performDeletion(ids);
	}

	private boolean confirmDeletion(int count) {
		String message = String.format("Are you sure you want to delete %d rental(es) selected(s)?", count);
		return JOptionPane.showConfirmDialog(parent, message, CONFIRM_TITLE,
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	private void performDeletion(List<Integer> ids) {
		try {
			DeletionResult result = deleteRentals(ids);

			if (result.successCount > 0) {
                                onSuccess.execute();
			}

			showResultMessage(result);
		} catch (RentexpresException ex) {
			SwingUtils.showError(parent, ERROR_MSG + ex.getMessage());
		}
	}

        private DeletionResult deleteRentals(List<Integer> ids) throws RentexpresException {
                List<Boolean> results = new ArrayList<>();
                for (Integer id : ids) {
                        try {
                                results.add(rentalService.delete(id));
                        } catch (RentexpresException e) {
                                results.add(false);
                        }
                }

                long successfulDeletions = 0;
                for (Boolean b : results) {
                        if (b.booleanValue()) {
                                successfulDeletions++;
                        }
                }
                return new DeletionResult(successfulDeletions, results.size());
        }

	private void showResultMessage(DeletionResult result) {
		if (result.totalCount == result.successCount) {
			SwingUtils.showInfo(parent, SUCCESS_MSG);
		} else if (result.successCount > 0) {
			SwingUtils.showWarning(parent, PARTIAL_SUCCESS_MSG);
		} else {
			SwingUtils.showError(parent, "No se pudo delete ningún rental.");
		}
	}

	private static class DeletionResult {
		final long successCount;
		final long totalCount;

		DeletionResult(long successCount, long totalCount) {
			this.successCount = successCount;
			this.totalCount = totalCount;
		}
	}
}