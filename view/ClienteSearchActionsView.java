package com.pinguela.rentexpres.desktop.view;

public class ClienteSearchActionsView extends AbstractSearchActionsView {
	private static final long serialVersionUID = 1L;

	public ClienteSearchActionsView() {
		super();
		makeFlat(getBtnLimpiarFiltros());
	}

	private void makeFlat(javax.swing.JButton... bs) {
		for (var b : bs) {
			b.setBorderPainted(false);
			b.setFocusPainted(false);
			b.setContentAreaFilled(false);
			if (b.getToolTipText() == null || b.getToolTipText().isBlank())
				b.setToolTipText(b.getText());
		}
	}

	protected void onNuevo() {
	}

	protected void onLimpiarFiltros() {
	}

	protected void onEliminarSeleccionados() {
	}
}
