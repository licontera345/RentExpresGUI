package com.pinguela.rentexpres.desktop.view;

import javax.swing.JButton;

public class UsuarioSearchActionsView extends AbstractSearchActionsView {
	private static final long serialVersionUID = 1L;

	public UsuarioSearchActionsView() {
		super();
		makeFlat(getBtnLimpiarFiltros());
	}

	private void makeFlat(JButton... bs) {
		for (var b : bs) {
			b.setBorderPainted(false);
			b.setFocusPainted(false);
			b.setContentAreaFilled(false);
			if (b.getToolTipText() == null || b.getToolTipText().isBlank()) {
				b.setToolTipText(b.getText());
			}
		}
	}


	protected void onNuevo() {
		// Se implementa desde el controller
	}


	protected void onLimpiarFiltros() {
		// Se implementa desde el controller
	}


	protected void onEliminarSeleccionados() {
		// Se implementa desde el controller
	}
}
