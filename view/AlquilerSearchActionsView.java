package com.pinguela.rentexpres.desktop.view;

import javax.swing.JButton;

public class AlquilerSearchActionsView extends AbstractSearchActionsView {
	private static final long serialVersionUID = 1L;

	/* ───── interfaces clásicas ───── */
	public interface NuevoListener {
		void onNuevo();
	}

	public interface LimpiarListener {
		void onLimpiar();
	}

	public interface BorrarSeleccionadosListener {
		void onBorrarSeleccionados();
	}

	private NuevoListener nuevoListener;
	private LimpiarListener limpiarListener;
	private BorrarSeleccionadosListener borrarListener;

	public AlquilerSearchActionsView() {
		super();
		makeFlat(getBtnLimpiarFiltros());

		/* wire clásico */
		getBtnNuevo().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (nuevoListener != null)
					nuevoListener.onNuevo();
			}
		});
		getBtnLimpiarFiltros().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (limpiarListener != null)
					limpiarListener.onLimpiar();
			}
		});
		getBtnEliminar().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (borrarListener != null)
					borrarListener.onBorrarSeleccionados();
			}
		});
	}

	private void makeFlat(JButton... bs) {
		for (int i = 0; i < bs.length; i++) {
			JButton b = bs[i];
			b.setBorderPainted(false);
			b.setFocusPainted(false);
			b.setContentAreaFilled(false);
			if (b.getToolTipText() == null || b.getToolTipText().trim().isEmpty()) {
				b.setToolTipText(b.getText());
			}
		}
	}

	/* ───── setters para el controlador ───── */
	public void setNuevoListener(NuevoListener l) {
		this.nuevoListener = l;
	}

	public void setLimpiarListener(LimpiarListener l) {
		this.limpiarListener = l;
	}

	public void setBorrarSeleccionadosListener(BorrarSeleccionadosListener l) {
		this.borrarListener = l;
	}
}
