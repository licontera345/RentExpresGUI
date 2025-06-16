package com.pinguela.rentexpres.desktop.view;

public class ReservaSearchActionsView extends AbstractSearchActionsView {

	private static final long serialVersionUID = 1L;

	public ReservaSearchActionsView() {
		super();
		makeFlat(getBtnLimpiarFiltros());
	}

        private void makeFlat(javax.swing.JButton... bs) {
                for (javax.swing.JButton b : bs) {
			b.setBorderPainted(false);
			b.setFocusPainted(false);
			b.setContentAreaFilled(false);
			if (b.getToolTipText() == null || b.getToolTipText().trim().isEmpty()) {
				b.setToolTipText(b.getText());
			}
		}
	}

	protected void onNuevo() {
	}

	protected void onLimpiarFiltros() {
	}

	protected void onEliminarSeleccionados() {
	}
}
