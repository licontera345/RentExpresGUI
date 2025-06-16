package com.pinguela.rentexpres.desktop.view;

public class VehiculoSearchActionsView extends AbstractSearchActionsView {

	private static final long serialVersionUID = 1L;

	public VehiculoSearchActionsView() {
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
		// acción nueva de Vehiculo
	}

	protected void onLimpiarFiltros() {
		// acción limpiar filtros de Vehiculo
	}

	protected void onEliminarSeleccionados() {
		// acción eliminar seleccionados de Vehiculo
	}
}
